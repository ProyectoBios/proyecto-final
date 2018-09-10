(function($){
    $.fn.smpSortableTable = function (data, max, lang) {
      // Victor Rivas <vrivas@ujaen.es>: 30-jul-2018
      // If lang is not defined, then lang is en
      lang=lang||"en";
      lang=lang.toLowerCase();
      var local=function(word) {
          var dict={
            "en" : {
                "of"          : "Of"
                , "next"      : "[Next]"
                , "previous"  : "[Previous]"
                , "first"     : "[First]"
                , "last"      : "[Last]"
                , "nothing"   : "Nothing to Display"
              }
              , "es" :{
                "of"          : "De"
                , "next"      : "[Siguiente]"
                , "previous"  : "[Anterior]"
                , "first"     : "[Primero]"
                , "last"      : "[&Uacute;ltimo]"
                , "nothing"   : "Nada que mostrar"
              }
              , "pt": {
                "of"          : "Do"
                , "next"      : "[Pr&oacute;ximo]"
                , "previous"  : "[Anterior]"
                , "first"     : "[Primeiro]"
                , "last"      : "[&Uacute;ltimo]"
                , "nothing"   : "Nada a exibir"
              }
              , "symbols": {
                "of"          : "/"
                , "next"      : "&#9654;"
                , "previous"  : "&#9664;"
                , "first"     : "|&#9664;"
                , "last"      : "&#9654;|"
                , "nothing"   : "&#8709;"
              }
          } ;
          dict["en-us"]=dict["en-uk"]=dict["en"];
          dict["es-es"]=dict["es"];
          dict["pt-br"]=dict["pt-pt"]=dict["pt"];

          // If lang is defined but not included in dict, then lang is en
          lang=( typeof dict[lang]==='undefined')?"en":lang;

          // If the word is not in our little dictionary
          return ( typeof dict[lang][word.toLowerCase()]==='undefined' ) ? "unknown" : dict[lang][word.toLowerCase()] ;
        };


        var generateData = function($table) {
            var keys = [] ;
            var data = [] ;

            $table.find('thead th').each(function(i,v){
                keys.push($(v).attr('id')) ;
            }) ;

            $table.find('tbody tr').each(function(i,v){
                var $tmp = {} ;
                $.each(keys, function(i,v2){
                    var sort = $($(v).children('td')[i]).data('smp-sort');
                    if(!sort) $tmp[v2] = $(v).children('td')[i].innerHTML;
                    else $tmp[v2] = { "text": $(v).children('td')[i].innerText, "sort": sort } ;
                }) ;
                data.push($tmp) ;
            }) ;

            return data ;
        };


        var renderTable = function (start, end, max, data) {
            var returnHTML = '';
            for (var i = start; i < Math.min(end, max); i++) {
                returnHTML += '<tr>';
                for (var key in data[i]) {
                    if(data[i].hasOwnProperty(key)) {
                        if (typeof data[i][key] !== 'object')
                            returnHTML += '<td>' + (data[i][key] ? data[i][key] : 'N/A') + '</td>';
                        else returnHTML += '<td>' + (data[i][key].text ? data[i][key].text : 'N/A') + '</td>';
                    }
                }
                returnHTML += '</tr>';
            }
            return returnHTML;
        };

        var sortFns = function (key) {
            return {
                desc: function (a, b) {
                    if (typeof a[key] !== 'object')
                        return a[key] > b[key] ? -1 :
                            (a[key] < b[key] ? 1  : 0);
                    else return a[key].sort > b[key].sort ? -1 :
                        (a[key].sort < b[key].sort ? 1  : 0);
                },
                asc: function (a, b) {
                    if (typeof a[key] !== 'object')
                        return a[key] < b[key] ? -1 :
                            (a[key] > b[key] ? 1  : 0);
                    else return a[key].sort < b[key].sort ? -1 :
                        (a[key].sort > b[key].sort ? 1  : 0);
                }
            }
        };
        /* SETUP */
        var $table = $(this);
        var tableName = $table.attr('id');
        var index = 0;
        max = max < 1 ? 10 : max ;
        data = !data ? generateData($table) : data ;
        $table.addClass('smpSortableTable--processed') ;
        $table.find('tbody').html(renderTable(0, data.length, max, data));
        $table.find('th:not(.smp-not-sortable)').addClass('smpSortableTable--sortable ' + tableName + '--sortable');
        $table.after(
            '<div class="smpSortableTable--nav" id="' + tableName + '--nav">'
            +'<a class="smpSortableTable--nav-links smpSortableTable--first smpSortableTable--disabled" id="' + tableName + '--first">'
            + local("first")
            +'</a>'
            +'<a class="smpSortableTable--nav-links smpSortableTable--prev smpSortableTable--disabled" id="' + tableName + '--prev">'
            + local("previous")
            +'</a>'
            +'<span class="smpSortableTable--counter" id="' + tableName + '--counter"></span>'
            + '<a class="smpSortableTable--nav-links smpSortableTable--next" id="' + tableName + '--next">'
            + local("next")+'</a>'
            +'<a class="smpSortableTable--nav-links smpSortableTable--last" id="' + tableName + '--last">'
            + local("last")+'</a>'
            +'</div>'
        );
        $.each($table.find('th'), function (i, v) {
            var id = $(v).attr('id');
            $(v).attr('id', tableName + '_' + id);
        });

        /* Init counter */
        if (data.length) {
            $('#' + tableName + '--counter').text(
                '1 - ' + Math.min(data.length, max) + ' '+local("of").toLowerCase()+' ' + data.length
            );
        } else {
            $('#' + tableName + '--counter').text(local('nothing'));
            $('#' + tableName + '--next').addClass('smpSortableTable--disabled');
            $('#' + tableName + '--last').addClass('smpSortableTable--disabled');
            $table.find('th').removeClass('smpSortableTable--sortable');
        }
        if (data.length <= max) {
            $('#' + tableName + '--next').addClass('smpSortableTable--disabled');
            $('#' + tableName + '--last').addClass('smpSortableTable--disabled');
        }

        /* Init next/prev */
        if (data.length > max) {
            $('#' + tableName + '--next').click(function () {
                if (!$(this).hasClass('smpSortableTable--disabled')) {
                    var start = index += max;
                    var end = start + max;
                    var size = data.length;

                    $table.find('tbody').html(
                        renderTable(start, size, end, data)
                    );

                    $('#' + tableName + '--counter').text(
                        (start + 1) + ' - ' + Math.min(size, end) + ' '+local("of").toLowerCase()+' ' + size
                    );

                    $('#' + tableName + '--prev').removeClass('smpSortableTable--disabled');
                    $('#' + tableName + '--first').removeClass('smpSortableTable--disabled');
                    if (end >= size) {
                      $('#' + tableName + '--next').addClass('smpSortableTable--disabled');
                      $('#' + tableName + '--last').addClass('smpSortableTable--disabled');
                    }
                }
            });
            $('#' + tableName + '--last').click(function () {
                if (!$(this).hasClass('smpSortableTable--disabled')) {
                    var size = data.length;
                    index=Math.trunc(size/max)*max-max;
                    var start = index += max;
                    // In case size%max=0, start becames greater than size and has to be fixed.
                    start-=(start<size)?0:max;
                    var end = start + max;

                    $table.find('tbody').html(
                        renderTable(start, size, end, data)
                    );

                    $('#' + tableName + '--counter').text(
                        (start + 1) + ' - ' + Math.min(size, end) + ' '+local("of").toLowerCase()+' ' + size
                    );

                    $('#' + tableName + '--prev').removeClass('smpSortableTable--disabled');
                    $('#' + tableName + '--first').removeClass('smpSortableTable--disabled');
                    if (end >= size) {
                      $('#' + tableName + '--next').addClass('smpSortableTable--disabled');
                      $('#' + tableName + '--last').addClass('smpSortableTable--disabled');
                    }
                }
            });
            $('#' + tableName + '--prev').click(function () {
                if (!$(this).hasClass('smpSortableTable--disabled')) {
                    var start = index -= max;
                    var end = start + max;
                    var size = data.length;

                    $table.find('tbody').html(
                        renderTable(start, size, end, data)
                    );

                    $('#' + tableName + '--counter').text(
                        (start + 1) + ' - ' + Math.min(size, end) + ' '+local("of").toLowerCase()+' ' + size
                    );

                    $('#' + tableName + '--next').removeClass('smpSortableTable--disabled');
                    $('#' + tableName + '--last').removeClass('smpSortableTable--disabled');
                    if (!start) {
                      $('#' + tableName + '--prev').addClass('smpSortableTable--disabled');
                      $('#' + tableName + '--first').addClass('smpSortableTable--disabled');
                    }
                }
            });
            $('#' + tableName + '--first').click(function () {
                if (!$(this).hasClass('smpSortableTable--disabled')) {
                    index=max;
                    var start = index -= max;
                    var end = start + max;
                    var size = data.length;

                    $table.find('tbody').html(
                        renderTable(start, size, end, data)
                    );

                    $('#' + tableName + '--counter').text(
                        (start + 1) + ' - ' + Math.min(size, end) + ' '+local("of").toLowerCase()+' ' + size
                    );

                    $('#' + tableName + '--next').removeClass('smpSortableTable--disabled');
                    $('#' + tableName + '--last').removeClass('smpSortableTable--disabled');
                    if (!start) {
                      $('#' + tableName + '--prev').addClass('smpSortableTable--disabled');
                      $('#' + tableName + '--first').addClass('smpSortableTable--disabled');
                    }
                }
            });
        }

        /* Init sorting*/
        $('.' + tableName + '--sortable').click(function () {
            var direction = $(this).hasClass('asc') ? 'desc' : 'asc';
            var colName = $(this).attr('id').replace(tableName + '_', '');
            $('.' + tableName + '--sortable').removeClass('desc asc');
            index = 0;
            data.sort(sortFns(colName)[direction]);
            $table.find('tbody').html(
                renderTable(0, data.length, max, data)
            );
            $(this).addClass(direction);
            $('#' + tableName + '--prev').addClass('smpSortableTable--disabled');
            $('#' + tableName + '--first').addClass('smpSortableTable--disabled');
            if (data.length > max) {
                $('#' + tableName + '--next').removeClass('smpSortableTable--disabled');
                $('#' + tableName + '--last').removeClass('smpSortableTable--disabled');
            }
            $('#' + tableName + '--counter').text('1 - ' + Math.min(data.length, max) + ' '+local("of").toLowerCase()+' ' + data.length);
        });
    };
})(jQuery) ;
