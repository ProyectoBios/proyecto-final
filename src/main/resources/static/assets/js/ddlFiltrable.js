function desplegarDDL() {
    if(document.getElementById("desplegable").classList.toggle("show")){
        document.getElementById("btnSeleccionar").className="dropbtn flechaArriba";
    }else{
        document.getElementById("btnSeleccionar").className="dropbtn flechaAbajo";
    } /* acá debería ir una función para el "onclick" para que se muestre u oculte con el click */

}

function filtrarDDL() {
    var input, filter, ul, li, a, i;
    input = document.getElementById("entrada");
    filter = input.value.toUpperCase();
    div = document.getElementById("desplegable");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
        if (a[i].innerHTML.toUpperCase().indexOf(filter) > -1) {
            a[i].style.display = "";
        } else {
            a[i].style.display = "none";
        }
    }
}

function seleccionarValor(codigo, nombre) {
    document.getElementById("idDropdown").value = codigo;
    document.getElementById("btnSeleccionar").innerHTML = nombre;
}

$(document).click(function () {
    document.getElementById("desplegable").classList.remove("show");
    document.getElementById("btnSeleccionar").className="dropbtn flechaAbajo";

})

$("#btnSeleccionar").click(function (e) {
    e.stopPropagation();
})

$("#entrada").click(function (e) {
    e.stopPropagation();
})