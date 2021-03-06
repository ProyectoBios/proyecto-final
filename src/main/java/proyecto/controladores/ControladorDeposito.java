package proyecto.controladores;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import proyecto.entidades.EspecificacionProducto;
import proyecto.entidades.Lote;
import proyecto.entidades.Rack;
import proyecto.entidades.ExcepcionFrigorifico;
import proyecto.logica.FabricaLogica;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Controller
public class ControladorDeposito {

    //region ABMProducto

    @RequestMapping(value="/ABMProducto", method = RequestMethod.GET)
    public String GetAbmProducto(ModelMap modelMap){
        try {
            modelMap.addAttribute("producto", new EspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
        } catch (Exception ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Hubo un error al cargar la página")));
            return "ABMProducto";
        }
        return "ABMProducto";
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Buscar")
    public String buscarProducto(@ModelAttribute EspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
/*                modelMap.addAttribute("producto", new EspecificacionProducto());
                modelMap.addAttribute("mensajes", cargarErrores(bindingResult));
                return "ABMProducto";
            }*/

            if(producto.getCodigo() == 0){ //si el código es 0, el gerente quiere dar de alta
                producto = new EspecificacionProducto();
                modelMap.addAttribute("producto", producto);
                botonesAltaProducto(modelMap);
            }else if (producto.getCodigo() < 0){ //si es menor que 0, cometió un error o quiere buscar por nombre
                if(!producto.getNombre().isEmpty()){
                    ArrayList<EspecificacionProducto> productos = FabricaLogica.getControladorDeposito().buscarProductosXNombre(producto.getNombre());
                    if(productos.size() == 0){
                        throw new ExcepcionFrigorifico("No existen productos que satisfagan los parámetros de búsqueda.");
                    }else{
                        modelMap.addAttribute("producto", producto);
                        modelMap.addAttribute("productos", productos);
                        modelMap.addAttribute("tablaListaProductos", "true");
                        botonesPorDefectoProducto(modelMap);
                    }
                }else{
                    throw new ExcepcionFrigorifico("Debe introducir un código positivo o un nombre para efectuar la búsqueda (0 para agregar un nuevo producto)");
                }
            }else{ //si es mayor que 0, busco por ID y si no existe, busco por nombre (si hay)
                EspecificacionProducto prod = FabricaLogica.getControladorDeposito().buscarProducto(producto.getCodigo(), true);
                if(prod != null){
                    modelMap.addAttribute("producto", prod);
                    botonesBMProducto(modelMap);
                }else if(prod == null && !producto.getNombre().isEmpty()){
                    ArrayList<EspecificacionProducto> productos = FabricaLogica.getControladorDeposito().buscarProductosXNombre(producto.getNombre());
                    if(productos.size() == 0){
                        throw new ExcepcionFrigorifico("No existen productos que satisfazcan los parámetros de búsqueda.");
                    }else{
                        modelMap.addAttribute("producto", producto);
                        modelMap.addAttribute("productos", productos);
                        modelMap.addAttribute("tablaListaProductos", "true");
                        botonesPorDefectoProducto(modelMap);
                    }
                }else{
                    throw new ExcepcionFrigorifico("No existen productos que satisfazcan los parámetros de búsqueda.");
                }
            }
            return "ABMProducto";

        }catch(ExcepcionFrigorifico ex){
            producto = new EspecificacionProducto();
            modelMap.addAttribute("producto", producto);
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            return "ABMProducto";
        }
        catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("¡ERROR! Ocurrió un error al procesar la página")));
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto/{codigo}", method = RequestMethod.GET)
    public String buscarProductoXid(@PathVariable String codigo, ModelMap modelMap){
        try {
            int cod;
            try{
                cod = Integer.parseInt(codigo);
            }catch (Exception ex){
                throw new ExcepcionFrigorifico("Código de producto inválido.");
            }

            EspecificacionProducto producto = FabricaLogica.getControladorDeposito().buscarProducto(cod, true);


            if(producto!=null) {
                modelMap.addAttribute("producto", producto);
                botonesBMProducto(modelMap);
            }else{
                //TODO Response send redirect abmproducto
            }

            return "ABMProducto";


        }catch(ExcepcionFrigorifico ex){
            EspecificacionProducto producto = new EspecificacionProducto();
            modelMap.addAttribute("producto", producto);
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            return "ABMProducto";
        }
        catch(Exception ex){
            modelMap.addAttribute("producto", new EspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al cargar el producto")));
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Agregar")
    public String agregarABMProducto(@ModelAttribute EspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            if (bindingResult.hasErrors()) {
                modelMap.addAttribute("producto", producto);
                modelMap.addAttribute("mensajes", cargarErrores(bindingResult));
                botonesAltaProducto(modelMap);
                return "ABMProducto";
            }

            int codigo = FabricaLogica.getControladorDeposito().altaDeProducto(producto);

            modelMap.addAttribute("producto", new EspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Alta exitosa. ID:  " + codigo + ".")));
            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesAltaProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesAltaProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Ocurrió un error al dar de alta el producto.")));

            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Eliminar")
    public String eliminarABMProducto(@ModelAttribute EspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            FabricaLogica.getControladorDeposito().bajaProducto(producto);

            modelMap.addAttribute("producto", new EspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("¡Baja exitosa!")));

            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Ocurrió un error al dar de baja el producto.")));

            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Modificar")
    public String modificarABMProducto(@ModelAttribute EspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            if (bindingResult.hasErrors()) {
                modelMap.addAttribute("producto", producto);
                modelMap.addAttribute("mensajes", cargarErrores(bindingResult));
                botonesBMProducto(modelMap);
                return "ABMProducto";
            }

            FabricaLogica.getControladorDeposito().modificarProducto(producto);

            modelMap.addAttribute("producto", new EspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("¡Modificacion exitosa!")));

            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Ocurrio un error al modificar el producto.")));

            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Limpiar")
    public String limpiarABMProducto(ModelMap modelMap){
        modelMap.addAttribute("producto", new EspecificacionProducto());
        botonesPorDefectoProducto(modelMap);

        return "ABMProducto";
    }

    public void botonesPorDefectoProducto(ModelMap modelMap){
        modelMap.addAttribute("botonAgregar", "false");
        modelMap.addAttribute("botonEliminar", "false");
        modelMap.addAttribute("botonModificar", "false");
        modelMap.addAttribute("botonBuscar", "true");
        modelMap.addAttribute("codigoBloqueado", "false");
    }

    public void botonesAltaProducto(ModelMap modelMap){
        modelMap.addAttribute("botonAgregar", "true");
        modelMap.addAttribute("botonEliminar", "false");
        modelMap.addAttribute("botonModificar", "false");
        modelMap.addAttribute("botonBuscar", "false");
        modelMap.addAttribute("codigoBloqueado", "true");
        modelMap.addAttribute("btnNoAllowed", "not-allowed");
    }

    public void botonesBMProducto(ModelMap modelMap){
        modelMap.addAttribute("botonAgregar", "false");
        modelMap.addAttribute("botonEliminar", "true");
        modelMap.addAttribute("botonModificar", "true");
        modelMap.addAttribute("botonBuscar", "false");
        modelMap.addAttribute("codigoBloqueado", "true");
        modelMap.addAttribute("btnNoAllowed", "not-allowed");
    }

    //endregion

    //region AltaRack
    @RequestMapping(value="/AltaRack", method = RequestMethod.GET)
    public String GetAltaRack(ModelMap modelMap){
        modelMap.addAttribute("rack", new Rack());
        botonesPorDefectoRack(modelMap);

        return "AltaRack";
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Agregar")
    public String agregarAltaRack(@ModelAttribute Rack rack, BindingResult bindingResult, ModelMap modelMap){
        try {
            if (bindingResult.hasErrors()) {
                modelMap.addAttribute("rack", rack);
                modelMap.addAttribute("mensajes", cargarErrores(bindingResult));
                botonesAltaRack(modelMap);
                return "AltaRack";
            }

            FabricaLogica.getControladorDeposito().altaRack(rack);

            modelMap.addAttribute("rack", new Rack());
            botonesPorDefectoRack(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("¡Alta exitosa!")));
            return "AltaRack";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("rack", rack);
            botonesAltaRack(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            return "AltaRack";
        }catch(Exception ex){
            modelMap.addAttribute("rack", rack);
            botonesAltaRack(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("¡Error! No se pudo dar de alta el rack.")));
            return "AltaRack";
        }
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Buscar")
    public String buscarAltaRack(@ModelAttribute Rack rack, BindingResult bindingResult, ModelMap modelMap){
        String letra = rack.getLetra();
        try{
            rack = FabricaLogica.getControladorDeposito().buscarRack(letra);

            if(rack == null){
                rack = new Rack();
                rack.setLetra(letra);
                modelMap.addAttribute("rack", rack);
                botonesAltaRack(modelMap);
            }else{
                modelMap.addAttribute("rack", rack);
                botonesBajaRack(modelMap);
            }

            return "AltaRack";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("rack", new Rack());
            botonesPorDefectoRack(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            return "AltaRack";
        }catch(Exception ex){
            modelMap.addAttribute("rack", new Rack());
            botonesPorDefectoRack(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("¡Error! No se pudo encontrar el rack")));
            return "AltaRack";
        }
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Eliminar")
    public String bajaRack(@ModelAttribute Rack rack, BindingResult bindingResult, ModelMap modelMap){
        try{
            FabricaLogica.getControladorDeposito().bajaRack(rack);

            modelMap.addAttribute("rack", new Rack());
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Baja exitosa")));
            botonesPorDefectoRack(modelMap);
            return "AltaRack";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("rack", rack);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            botonesBajaRack(modelMap);
            return "AltaRack";
        }catch(Exception ex){
            modelMap.addAttribute("rack", rack);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("¡Error! No se pudo dar de baja el rack")));
            botonesBajaRack(modelMap);
            return "AltaRack";
        }
    }

    @RequestMapping(value = {"/EstadoDeRack", "/MoverLote"}, method = RequestMethod.GET)
    public String getListarRacks(ModelMap modelMap, HttpSession session) throws Exception {
        try{
            //Obtengo la url del Request.
            UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
            String requestedValue = builder.buildAndExpand().getPath();

            ArrayList<Rack> listaRacks = FabricaLogica.getControladorDeposito().listarRacks();
            if(listaRacks.size() == 0) {
                modelMap.addAttribute("mensaje", "No se encontraron Racks");
            }
            session.setAttribute("racks", listaRacks);
            if(requestedValue.contains("MoverLote")){
                return "MoverLote";
            }else if(requestedValue.contains("EstadoDeRack")){
                return "EstadoDeRack";
            }else{
                return "";
            }
        }catch(Exception ex) {
            throw ex;
        }
    }

    public void botonesPorDefectoRack(ModelMap modelMap){
        modelMap.addAttribute("botonAgregar", "false");
        modelMap.addAttribute("botonBuscar", "true");
        modelMap.addAttribute("botonEliminar", "false");
        modelMap.addAttribute("letraBloqueado", "false");
    }

    public void botonesAltaRack(ModelMap modelMap){
        modelMap.addAttribute("botonAgregar", "true");
        modelMap.addAttribute("botonBuscar", "false");
        modelMap.addAttribute("botonEliminar", "false");
        modelMap.addAttribute("letraBloqueado", "true");
        modelMap.addAttribute("btnNoAllowed", "not-allowed");
    }

    public void botonesBajaRack(ModelMap modelMap){
        modelMap.addAttribute("botonAgregar", "false");
        modelMap.addAttribute("botonBuscar", "false");
        modelMap.addAttribute("botonEliminar", "true");
        modelMap.addAttribute("letraBloqueado", "true");
        modelMap.addAttribute("btnNoAllowed", "not-allowed");
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Limpiar")
    public String limpiarAltaRack(ModelMap modelMap){
        modelMap.addAttribute("rack", new Rack());
        botonesPorDefectoRack(modelMap);

        return "AltaRack";
    }
    //endregion

    //region Lotes

    @RequestMapping(value="/BajaLoteXVencimiento", method = RequestMethod.GET)
    public String getBajaLoteXVencimiento(ModelMap modelMap){
        try{
            ArrayList<Lote> lotes = FabricaLogica.getControladorDeposito().obtenerLotesVencidos();
            modelMap.addAttribute("lotes", lotes);

            return "BajaLoteXVencimiento";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "BajaLoteXVencimiento";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al obtener los lotes vencidos");
            return "BajaLoteXVencimiento";
        }
    }

    @RequestMapping(value="/BajaLoteXVencimiento", method = RequestMethod.POST)
    public String eliminarLote(@RequestParam(value="IdLote", required = false) String idLote, ModelMap modelMap){
        try{
            Lote lote = FabricaLogica.getControladorDeposito().buscarLote(Integer.valueOf(idLote));
            FabricaLogica.getControladorDeposito().bajaLote(lote);
            modelMap.addAttribute("lotes", FabricaLogica.getControladorDeposito().obtenerLotesVencidos());
            modelMap.addAttribute("mensaje", "Baja de lote exitosa.");
            return "BajaLoteXVencimiento";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "BajaLoteXVencimiento";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al eliminar el lote.");
            return "BajaLoteXVencimiento";
        }

    }

    @RequestMapping(value="/AltaLote", method = RequestMethod.GET)
    public String getAltaLote(ModelMap modelMap){
        ArrayList<EspecificacionProducto> prods = new ArrayList<>();
        ArrayList<Rack> racks = new ArrayList<Rack>();
        try {
            prods = FabricaLogica.getControladorDeposito().listarProductos(true);
            racks = FabricaLogica.getControladorDeposito().listarRacks();
            modelMap.addAttribute("lote", new Lote());
            modelMap.addAttribute("productos",prods);
            modelMap.addAttribute("racks", racks);
            return "AltaLote";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("lote", new Lote());
            modelMap.addAttribute("productos", prods);
            modelMap.addAttribute("racks", racks);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaLote";
        }catch (Exception ex){
            modelMap.addAttribute("lote", new Lote());
            modelMap.addAttribute("productos", prods);
            modelMap.addAttribute("racks", racks);
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al obtener el formulario.");
            return "AltaLote";
        }
    }

    @RequestMapping(value="/AltaLote", method = RequestMethod.POST,  params="action=Agregar")
    public <T> T altaLote(@ModelAttribute @Valid Lote lote, BindingResult bindingResult, ModelMap modelMap){
        ArrayList<EspecificacionProducto> prods = new ArrayList<>();
        ArrayList<Rack> racks = new ArrayList<>();
        try{
            prods = FabricaLogica.getControladorDeposito().listarProductos(true);
            racks = FabricaLogica.getControladorDeposito().listarRacks();
            if(lote.getProducto().getCodigo() == -1){
                throw new ExcepcionFrigorifico("Debe seleccionar un producto.");
            }
            if(lote.getUbicacion().getRack() == null){
                throw new ExcepcionFrigorifico("Debe seleccionar un rack.");
            }

            int codigo = FabricaLogica.getControladorDeposito().altaLote(lote);

            byte[] pdfBytes = FabricaLogica.getControladorDeposito().generarCodigoQRLote(codigo);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String nombreArchivo = "Lote" + codigo + ".pdf";
            //headers.setContentDispositionFormData("inline", nombreArchivo);
            headers.add("Content-Disposition", "inline;filename=" + nombreArchivo);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);
            return (T)response;

        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("lote", lote);
            modelMap.addAttribute("productos", prods);
            modelMap.addAttribute("racks", racks);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return (T)"AltaLote";
        }catch(Exception ex){
            modelMap.addAttribute("lote", lote);
            modelMap.addAttribute("productos", prods);
            modelMap.addAttribute("racks", racks);
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrió un error al dar de alta el lote.");
            return (T)"AltaLote";
        }
    }

    @RequestMapping(value="/MoverLote", method = RequestMethod.POST, params = "action=Mover")
    public String moverLote(@RequestParam(value="idLoteHidden") String idLote,
                            @RequestParam(value="idUbicacionHidden") String ubicacion, ModelMap modelMap) throws Exception{
        try {
            int loteId = 0;
            try {
                loteId = Integer.valueOf(idLote);
            }catch(Exception ex){
                throw new ExcepcionFrigorifico("Debe seleccionar el lote a mover");
            }
            if (loteId <= 0){
                modelMap.addAttribute("mensaje", "Debe seleccionar el lote a mover");
                return "MoverLote";
            } else if (ubicacion.isEmpty()){
                modelMap.addAttribute("mensaje2", "Debe seleccionar la ubicación de destino");
                return "MoverLote";
            }

            FabricaLogica.getControladorDeposito().moverLote(loteId, ubicacion);
            modelMap.addAttribute("mensaje2", "Lote movido con éxito.");

            return "MoverLote";

        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje2", ex.getMessage());
            return "MoverLote";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje2", "¡ERROR! Ocurrió un error al mover el lote.");
            return "MoverLote";
        }

    }

    @RequestMapping(value = {"/EstadoDeRack", "/MoverLote"}, method = RequestMethod.POST, params = "action=Seleccionar")
    public String listarLotesXRack(@RequestParam(value="letraRacks", required = false) String letraRack,
                                   ModelMap modelMap, HttpSession session) throws Exception {
        try{
            //Obtengo la url del Request.
            UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
            String requestedValue = builder.buildAndExpand().getPath();

            if(letraRack.isEmpty()) {
                modelMap.addAttribute("mensaje", "Debe seleccionar un rack de la lista");
            } else {
                ArrayList<ArrayList<Lote>> lotes = FabricaLogica.getControladorDeposito().obtenerRack(FabricaLogica.getControladorDeposito().buscarRack(letraRack));

                if(requestedValue.contains("MoverLote")) {
                    boolean loteEncontrado = false;

                    for (ArrayList<Lote> lote : lotes){
                        for (Lote l : lote ){
                            if (l.getCantUnidades() > 0){
                                loteEncontrado = true;
                                session.setAttribute("lotes", lotes);
                                modelMap.addAttribute("tablaRackOrigen", true);
                                break;
                            }
                        }
                        if(loteEncontrado) {
                            break;
                        }
                    }
                    if (!loteEncontrado) {
                        modelMap.addAttribute("mensaje", "No hay lotes disponibles para el rack seleccionado" );
                    }
                    return "MoverLote";

                } else if(requestedValue.contains("EstadoDeRack")){
                    modelMap.addAttribute("tablaRack", true);
                    session.setAttribute("lotes", lotes);
                    return "EstadoDeRack";
                }
            }
            return redireccionar(requestedValue);
        }catch (Exception ex){
            throw ex;
        }
    }

    private String redireccionar(String url) {
        try {
            if (url.contains("MoverLote")) {
                return "MoverLote";
            } else {
                return "EstadoDeRack";
            }
        } catch (Exception ex){
            throw ex;
        }
    }

    @RequestMapping(value = "/MoverLote", method = RequestMethod.POST, params = "action=Seleccionar Destino")
    public String listarLotesXRackDestino(@RequestParam(value="letraRacks2", required = false) String letraRack,
                                   @RequestParam(value = "idLoteHidden", required = false) String idLoteOculto,
                                   ModelMap modelMap, HttpSession session) throws Exception {
        try {
            if(letraRack.isEmpty()) {
                modelMap.addAttribute("mensaje2", "Debe seleccionar un rack de la lista");
            } else if (idLoteOculto.isEmpty()){
                modelMap.addAttribute("mensaje2", "Primero debe seleccionar el lote a mover");
            }else {
                ArrayList<ArrayList<Lote>> lotes = FabricaLogica.getControladorDeposito().obtenerRack(FabricaLogica.getControladorDeposito().buscarRack(letraRack));
                Lote loteAMover = FabricaLogica.getControladorDeposito().buscarLote(Integer.parseInt(idLoteOculto));
                LocalDate fecha = loteAMover.getFechaVencimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                modelMap.addAttribute("mensaje", String.format("Lote a mover: #ID %d,  ubicación: %s,  producto: %s,  cantidad: %d,  vencimiento: %d/%d/%d", loteAMover.getId(), loteAMover.getUbicacion().getUbicacionString(), loteAMover.getProducto().getNombre(), loteAMover.getCantUnidades(), fecha.getDayOfMonth(), fecha.getMonthValue(), fecha.getYear()));

                boolean loteVacio = false;

                for (ArrayList<Lote> lote : lotes){
                    for (Lote l : lote ){
                        if (l.getCantUnidades() == 0){
                            loteVacio = true;
                            session.setAttribute("lotes", lotes);
                            modelMap.addAttribute("tablaRackDestino", true);
                            break;
                        }
                    }
                }
                if (!loteVacio) {
                    modelMap.addAttribute("mensaje2", "No hay ubicaciones disponibles para el rack seleccionado" );
                }
            }

        } catch (Exception ex){
            throw ex;
        }

        return "MoverLote";
    }


    @RequestMapping(value="/AltaLote", method = RequestMethod.POST, params="action=Limpiar")
    public String limpiarAltaLote(ModelMap modelMap){
        try {
            modelMap.addAttribute("lote", new Lote());
            modelMap.addAttribute("productos", FabricaLogica.getControladorDeposito().listarProductos(true));
            modelMap.addAttribute("racks", FabricaLogica.getControladorDeposito().listarRacks());
            return "AltaLote";
        }catch (ExcepcionFrigorifico ex){
            return "error";
        }catch (Exception ex){
            return "error";
        }
    }


    @InitBinder
    public void initDateBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        binder.registerCustomEditor(Date.class, "fechaVencimiento", new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @RequestMapping(value="/MoverLote", method = RequestMethod.POST, params = "action=Cancelar")
    public String cancelarMoverLote(ModelMap modelMap, HttpSession session){
        try{
            ArrayList<Rack> listaRacks = FabricaLogica.getControladorDeposito().listarRacks();
            if(listaRacks.size() == 0) {
                modelMap.addAttribute("mensaje", "No se encontraron Racks");
            }
            session.setAttribute("racks", listaRacks);
            return "MoverLote";
        }
        catch (ExcepcionFrigorifico ex){
            return "error";
        }catch (Exception ex){
            return "error";
        }
    }

    @RequestMapping(value="/VerLote", method = RequestMethod.GET)
    public String getVerLote(HttpServletRequest request, HttpServletResponse response){
        try {
            response.sendRedirect(request.getContextPath() + "/Bienvenida");
        }catch (Exception ex) {
            return "Bienvenida";
        }
        return "Bienvenida";
    }

    @RequestMapping(value="/VerLote/{idLote}", method = RequestMethod.GET)
    public String getVerLoteId(@PathVariable String idLote, ModelMap modelmap, HttpServletRequest request, HttpServletResponse response){
        int id;
        try{
            try{
                id = Integer.parseInt(idLote);
            }catch(Exception ex){
                throw new ExcepcionFrigorifico("Id de lote no válido");
            }

            Lote l = FabricaLogica.getControladorDeposito().buscarLote(id);
            if(l==null){
                response.sendRedirect(request.getContextPath() + "/Bienvenida");
                return "Bienvenida";
            }else{
                modelmap.addAttribute("lote", l);
                return "VerLote";
            }
        }catch (Exception ex){
            return "error";
        }
    }

    @RequestMapping(value="/VerLote", method = RequestMethod.POST, params="action=Ver Lote")
    public String verLote(@RequestParam(value = "idLote", required = false) String idLote,  ModelMap modelMap){
        try {
            int id = 0;
            Lote lote;

            if (!idLote.isEmpty()){
                try{
                    id = Integer.parseInt(idLote);
                }catch(Exception ex){
                    throw new ExcepcionFrigorifico("Error al procesar el id del lote");
                }

                lote = FabricaLogica.getControladorDeposito().buscarLote(id);

                if (lote != null) {
                    modelMap.addAttribute("lote", lote);
                    modelMap.addAttribute("tablaLote", "true");
                } else{
                    modelMap.addAttribute("mensaje", "No existe un lote con el id: " + idLote);
                    return "VerLote";
                }
            } else{
                modelMap.addAttribute("mensaje", "debe ingresar el id del lote");
            }

        } catch (Exception ex){
            modelMap.addAttribute("mensaje", "Hubo un error al cargar los datos del lote");
        }
        return "VerLote";
    }

    //endregion

    @RequestMapping(value="/VerLote", method = RequestMethod.POST, params="action=Ver QR")
    public ResponseEntity verQR(@RequestParam(value = "idLote", required = false) String idLote,  ModelMap modelMap){
        try {
            int id = 0;
            Lote lote;

            if (!idLote.isEmpty()){
                try{
                    id = Integer.parseInt(idLote);
                }catch(Exception ex){
                    throw new ExcepcionFrigorifico("Error al procesar el id del lote");
                }

                byte[] pdfBytes = FabricaLogica.getControladorDeposito().generarCodigoQRLote(id);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/pdf"));
                String nombreArchivo = "Lote" + id + ".pdf";
                //headers.setContentDispositionFormData("inline", nombreArchivo);
                headers.add("Content-Disposition", "inline;filename=" + nombreArchivo);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
                ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);
                return response;


            } else{
                modelMap.addAttribute("mensaje", "debe ingresar el id del lote");
            }

        } catch (Exception ex){
            modelMap.addAttribute("mensaje", "Hubo un error al cargar los datos del lote");
        }
        return null;
    }

    private ArrayList<String> cargarErrores(BindingResult bindingResult) {
        ArrayList<String> mensajes = new ArrayList<>();
        for (Object obj : bindingResult.getAllErrors()) {
            if (obj instanceof FieldError) {
                if(((FieldError) obj).getCode().equals("methodInvocation")) {
                    mensajes.add(((FieldError) obj).getDefaultMessage().split(": ")[1]);
                }else{
                    mensajes.add("El campo " + ((FieldError) obj).getField() + " no puede quedar vacio");
                }
            }
        }
        return mensajes;
    }
}
