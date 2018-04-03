package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import proyecto.datatypes.*;
import proyecto.logica.FabricaLogica;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class ControladorPedidos {

    @RequestMapping(value="/EstadoDePedido", method = RequestMethod.GET)
    public String getEstadoDePedido(ModelMap modelMap){
        modelMap.addAttribute("tablaClientes", false);
        return "EstadoDePedido";
    }

    @RequestMapping(value="/EstadoDePedido", method = RequestMethod.POST, params="action=Buscar")
    public String buscarPedidoOClientes(@RequestParam(value="idPedido", required = false) String idPedido, @RequestParam(value="nombreCliente", required = false) String nombreCliente, ModelMap modelMap){
        try{
            DTOrdenPedido ordenPedido = null;
            if(idPedido!="") {
                int id;
                try {
                    id = Integer.valueOf(idPedido);
                } catch (Exception ex) {
                    throw new ExcepcionFrigorifico("¡ERROR! El id del pedido debe ser numérico.");
                }

                ordenPedido = FabricaLogica.getControladorPedidos().buscarOrdenPedido(id);
            }

            if(ordenPedido==null){
                if(nombreCliente.isEmpty()){
                    throw new ExcepcionFrigorifico("No hay coincidencias con los parámetros de búsqueda.");
                }

                ArrayList<DTCliente> clientes = FabricaLogica.getControladorPedidos().buscarClientes(nombreCliente);
                modelMap.addAttribute("clientes", clientes);
                modelMap.addAttribute("tablaClientes", true);
                modelMap.addAttribute("tablaPedidos", false);
                modelMap.addAttribute("detallePedido", false);
            }else{
                modelMap.addAttribute("ordenPedido", ordenPedido);
                modelMap.addAttribute("tablaClientes", false);
                modelMap.addAttribute("tablaPedidos", false);
                modelMap.addAttribute("detallePedido", true);
            }

            return "EstadoDePedido";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "EstadoDePedido";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al buscar.");
            return "EstadoDePedido";
        }
    }

    @RequestMapping(value="/EstadoDePedido", method = RequestMethod.POST, params="action=Seleccionar")
    public String seleccionarCliente(@RequestParam(value="nombre", required = true) String nombreCliente, ModelMap modelMap){
        try{
            DTCliente cliente = FabricaLogica.getControladorPedidos().buscarCliente(nombreCliente);
            ArrayList<DTOrdenPedido> ordenes = FabricaLogica.getControladorPedidos().buscarOrdenesXCliente(cliente);
            modelMap.addAttribute("tablaPedidos", true);
            modelMap.addAttribute("ordenes", ordenes);
            return "EstadoDePedido";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "EstadoDePedido";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al buscar.");
            return "EstadoDePedido";
        }
    }

    @RequestMapping(value="/EstadoDePedido", method = RequestMethod.POST, params="action=Ver")
    public String seleccionarPedido(@RequestParam(value="idPedido", required = true) String idPedido, ModelMap modelMap){
        try{
            int id;
            try{
                id = Integer.valueOf(idPedido);
            }catch (Exception ex){
                throw new ExcepcionFrigorifico("¡ERROR! Ocurrio un error al recibir el id del pedido.");
            }

            DTOrdenPedido ordenPedido = FabricaLogica.getControladorPedidos().buscarOrdenPedido(id);
            modelMap.addAttribute("ordenPedido", ordenPedido);
            modelMap.addAttribute("detallePedido", true);
            return "EstadoDePedido";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "EstadoDePedido";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al buscar.");
            return "EstadoDePedido";
        }
    }

    @RequestMapping(value="/EstadoDePedido", method=RequestMethod.POST, params="action=Cancelar Pedido")
    public String cancelarPedido(@RequestParam(value="idPedido", required = true) String idPedido, ModelMap modelMap){
        try{
            int id;
            try{
                id = Integer.valueOf(idPedido);
            }catch (Exception ex){
                throw new ExcepcionFrigorifico("¡ERROR! Ocurrio un error al recibir el id del pedido.");
            }

            DTOrdenPedido ordenPedido = FabricaLogica.getControladorPedidos().buscarOrdenPedido(id);
            FabricaLogica.getControladorPedidos().cancelarPedido(ordenPedido);
            modelMap.addAttribute("mensaje", "Pedido número " + id + " cancelado con éxito.");
            return "EstadoDePedido";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "EstadoDePedido";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al buscar.");
            return "EstadoDePedido";
        }
    }

    @RequestMapping(value="/EstadoDePedido", method=RequestMethod.POST, params="action=Limpiar")
    public String limpiarEstadoDePedido(ModelMap modelMap){
        try{
            return "EstadoDePedido";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al buscar.");
            return "EstadoDePedido";
        }
    }

    @RequestMapping(value="/AltaOrdenDePedido", method=RequestMethod.GET)
    public String getAltaOrdenDePedido(ModelMap modelMap, HttpSession session){
        session.removeAttribute("cliente");
        session.removeAttribute("orden");
        session.removeAttribute("productos");
        modelMap.addAttribute("tablaProducto", false);
        modelMap.addAttribute("tablaBusquedaCliente", true);

        return "AltaOrdenDePedido";
    }

    @RequestMapping(value="/AltaOrdenDePedido", method = RequestMethod.POST, params="action=Buscar")
    public String buscarClientes(@RequestParam(value="nombreCliente", required = false) String nombreCliente, ModelMap modelMap){
        try{
            ArrayList<DTCliente> clientes = null;
            if(!nombreCliente.equals("")) {
                clientes = FabricaLogica.getControladorPedidos().buscarClientes(nombreCliente);
            }else{
                modelMap.addAttribute("tablaBusquedaCliente", true);
                throw new ExcepcionFrigorifico("El nombre del cliente no puede quedar vacío");
            }
            modelMap.addAttribute("tablaClientes", true);
            modelMap.addAttribute("tablaProducto", false);

            modelMap.addAttribute("clientes", clientes);
            return "AltaOrdenDePedido";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaOrdenDePedido";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al buscar el cliente.");
            return "AltaOrdenDePedido";
        }
    }

    @RequestMapping(value="/AltaOrdenDePedido", method = RequestMethod.POST, params="action=Seleccionar")
    public String seleccionarClienteOrdenPedido(@RequestParam(value="nombre", required = true) String nombreCliente, ModelMap modelMap, HttpSession session){
        try{
            DTCliente cliente = FabricaLogica.getControladorPedidos().buscarCliente(nombreCliente);

            session.setAttribute("cliente", cliente);
            DTOrdenPedido orden = new DTOrdenPedido();
            orden.setEstado("pendiente");
            orden.setCliente(cliente);
            session.setAttribute("orden", orden);
            modelMap.addAttribute("tablaProducto", true);
            session.setAttribute("productos", FabricaLogica.getControladorDeposito().listarProductos());
            return "AltaOrdenDePedido";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaOrdenDePedido";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al buscar.");
            return "AltaOrdenDePedido";
        }
    }

    @RequestMapping(value="/AltaOrdenDePedido", method = RequestMethod.POST, params = "action=Agregar nuevo cliente")
    public String habilitarAltaDeCliente(ModelMap modelMap){
        try {
            modelMap.addAttribute("tablaAltaCliente", true);
            modelMap.addAttribute("tablaProducto", false);
            modelMap.addAttribute("cliente", new DTCliente());
            return "AltaOrdenDePedido";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al dar de alta el cliente.");
            return "AltaOrdenDePedido";
        }
    }

    @RequestMapping(value="/AltaOrdenDePedido", method = RequestMethod.POST, params = "action=Agregar Cliente")
    public String altaCliente(@ModelAttribute DTCliente cliente, BindingResult bindingResult, ModelMap modelMap, HttpSession session){
        try{
            FabricaLogica.getControladorPedidos().altaCliente(cliente);

            session.setAttribute("cliente", cliente);
            DTOrdenPedido orden = new DTOrdenPedido();
            orden.setEstado("pendiente");
            orden.setCliente(cliente);
            session.setAttribute("orden", orden);

            modelMap.addAttribute("tablaProducto", true);
            session.setAttribute("productos", FabricaLogica.getControladorDeposito().listarProductos());
            modelMap.addAttribute("mensaje", "Cliente agregado con éxito.");
            return "AltaOrdenDePedido";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaOrdenDePedido";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al dar de alta el cliente.");
            return "AltaOrdenDePedido";
        }
    }

    @RequestMapping(value="/AltaOrdenDePedido", method = RequestMethod.POST, params = "action=Agregar")
    public String agregarProductoALinea(@RequestParam(value="producto") String idProducto, @RequestParam(value="cantUnidades") String cantUnidades, ModelMap modelMap, HttpSession session){
        try{
            if(cantUnidades.equals("")){
                modelMap.addAttribute("tablaProducto", true);
                throw new ExcepcionFrigorifico("La cantidad de unidades no puede quedar vacía");
            }

            int id;
            try{
                id = Integer.parseInt(idProducto);
            }catch (Exception ex){
                modelMap.addAttribute("tablaProducto", true);
                throw new ExcepcionFrigorifico("ERROR! Debe seleccionar un producto");
            }
            DTEspecificacionProducto producto = FabricaLogica.getControladorDeposito().buscarProducto(id);

            int cantidadUnidades;
            try{
                cantidadUnidades = Integer.parseInt(cantUnidades);
            }catch (Exception ex){
                modelMap.addAttribute("tablaProducto", true);
                throw new ExcepcionFrigorifico("ERROR! Ingrese una cantidad correcta");
            }

            FabricaLogica.getControladorPedidos().agregarLineaDePedido((DTOrdenPedido)session.getAttribute("orden"), producto, cantidadUnidades);

            DTLineaPedido linea = null;
            for(DTLineaPedido l : ((DTOrdenPedido)session.getAttribute("orden")).getLineas()){
                if(l.getProducto().getCodigo() == producto.getCodigo()){
                    linea = l;
                    break;
                }
            }

            ArrayList<DTLote> stock = FabricaLogica.getControladorDeposito().buscarStock(producto);
            int cantidad = 0;
            for(DTLote l : stock){
                cantidad+=l.getCantUnidades();
            }

            if(cantidad < linea.getCantidad()){
                modelMap.addAttribute("mensajeStock", "AVISO: No hay suficiente stock actualmente para satisfacer " + linea.getCantidad() + " unidades de " + linea.getProducto().getNombre() + ". Stock actual: " + cantidad + " unidades.");
            }

            modelMap.addAttribute("tablaProducto", true);

            return "AltaOrdenDePedido";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaOrdenDePedido";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al dar de alta el cliente.");
            return "AltaOrdenDePedido";
        }
    }

    @RequestMapping(value="/AltaOrdenDePedido", method = RequestMethod.POST, params = "action=Eliminar")
    public String eliminarLinea(@RequestParam(value="numeroLinea") String numero, ModelMap modelMap, HttpSession session){
        try{
            int nro = Integer.parseInt(numero);
            FabricaLogica.getControladorPedidos().eliminarLinea((DTOrdenPedido)session.getAttribute("orden"), nro);

            modelMap.addAttribute("tablaProducto", true);

            return "AltaOrdenDePedido";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaOrdenDePedido";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al dar de alta el cliente.");
            return "AltaOrdenDePedido";
        }
    }

    @RequestMapping(value="/AltaOrdenDePedido", method = RequestMethod.POST, params = "action=Finalizar")
    public String confirmarOrdenPedido(@RequestParam(value="contacto") String contacto, @RequestParam(value="direccionEnvio") String direccion, ModelMap modelMap, HttpSession session){
        try{
            if(contacto.equals("")){
                modelMap.addAttribute("tablaProducto", true);
                throw new ExcepcionFrigorifico("¡ERROR! Especifique un nombre de contacto.");
            }

            if(direccion.equals("")){
                modelMap.addAttribute("tablaProducto", true);
                throw new ExcepcionFrigorifico("¡ERROR! Especifique una direccion de envio.");
            }

            DTOrdenPedido orden = (DTOrdenPedido)session.getAttribute("orden");

            orden.setContacto(contacto);
            orden.setDireccionEnvio(direccion);

            int id = FabricaLogica.getControladorPedidos().altaOrdenDePedido(orden);

            session.removeAttribute("orden");
            session.removeAttribute("cliente");
            session.removeAttribute("productos");

            modelMap.addAttribute("tablaBusquedaCliente", true);
            modelMap.addAttribute("mensaje", "Alta de pedido exitosa. ID: " + String.valueOf(id));
            return "AltaOrdenDePedido";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaOrdenDePedido";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al dar de alta el cliente.");
            return "AltaOrdenDePedido";
        }
    }

    @RequestMapping(value="/AltaOrdenDePedido", method = RequestMethod.POST, params = "action=Limpiar")
    public String limpiarOrdenPedido(ModelMap modelMap, HttpSession session){
        session.removeAttribute("cliente");
        session.removeAttribute("orden");
        session.removeAttribute("productos");
        modelMap.addAttribute("tablaProducto", false);
        modelMap.addAttribute("tablaBusquedaCliente", true);

        return "AltaOrdenDePedido";
    }

    //region Picking

    @RequestMapping(value="/RealizarPicking", method = RequestMethod.GET)
    public String getRealizarPicking(ModelMap modelMap, HttpSession session){
        try {
            if(session.getAttribute("listaPicking") == null){
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("pendiente"));
                modelMap.addAttribute("tablaPedidos", true);
            }else{
                modelMap.addAttribute("tablaPicking", true);
            }

            return "realizarPicking";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cargar el formulario.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/RealizarPicking", method = RequestMethod.POST, params = "action=Seleccionar")
    public String seleccionarPedidosPicking(@RequestParam(value="pedidos", required = false)int[] pedidos, ModelMap modelMap, HttpSession session){
        try {
            ArrayList<DTOrdenPedido> ordenesPedido = new ArrayList<>();
            for(int i=0; i<pedidos.length; i++){
                DTOrdenPedido orden = FabricaLogica.getControladorPedidos().buscarOrdenPedido(pedidos[i]);
                FabricaLogica.getControladorPedidos().modificarEstadoDePedido(orden, "en preparacion");
                ordenesPedido.add(orden);
            }
            session.setAttribute("pedidosPicking", ordenesPedido);

            session.setAttribute("listaPicking", FabricaLogica.getControladorPedidos().obtenerPicking(ordenesPedido));

            modelMap.addAttribute("tablaPicking", true);
            return "realizarPicking";
        }catch (ExcepcionFrigorifico ex){
            try{
                for(int i=0; i<pedidos.length; i++){
                    DTOrdenPedido orden = FabricaLogica.getControladorPedidos().buscarOrdenPedido(pedidos[i]);
                    FabricaLogica.getControladorPedidos().modificarEstadoDePedido(orden, "pendiente");
                }
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("pendiente"));
            }catch (Exception e){}

            modelMap.addAttribute("tablaPedidos", true);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }catch (Exception ex){
            try{
                for(int i=0; i<pedidos.length; i++){
                    DTOrdenPedido orden = FabricaLogica.getControladorPedidos().buscarOrdenPedido(pedidos[i]);
                    FabricaLogica.getControladorPedidos().modificarEstadoDePedido(orden, "pendiente");
                }
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("pendiente"));
            }catch (Exception e){}

            modelMap.addAttribute("tablaPedidos", true);
            modelMap.addAttribute("mensaje", "Ocurrió un error al cargar el formulario.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/RealizarPicking", method = RequestMethod.POST, params="action=Cancelar Picking")
    public String cancelarPicking(ModelMap modelMap, HttpSession session){
        try{
            if(session.getAttribute("pedidosPicking") != null){
                ArrayList<DTOrdenPedido> ordenes = (ArrayList<DTOrdenPedido>) session.getAttribute("pedidosPicking");
                for (DTOrdenPedido ordenPedido : ordenes) {
                    FabricaLogica.getControladorPedidos().modificarEstadoDePedido(ordenPedido, "pendiente");
                }
                session.removeAttribute("pedidosPicking");
            }

            if(session.getAttribute("listaPicking") != null){
                ArrayList<DTPicking> picking = (ArrayList<DTPicking>)session.getAttribute("listaPicking");
                for(DTPicking p : picking){
                    for(int i=0; i<p.getLotes().size(); i++){
                        if(i!=p.getLotes().size()-1) {
                            FabricaLogica.getControladorDeposito().deshacerBajaLogicaLote(p.getLotes().get(i)); //deshacer la baja logica temporal sobre el lote
                        }else{
                            if(p.getCantidad()==p.getCantidadUnidadesTotal()){
                                FabricaLogica.getControladorDeposito().deshacerBajaLogicaLote(p.getLotes().get(i));
                            }else {
                                FabricaLogica.getControladorDeposito().actualizarStock(p.getLotes().get(i), p.getLotes().get(i).getCantUnidades() - (p.getCantidadUnidadesTotal() - p.getCantidad())); //devolver el stock al lugar de origen
                            }
                        }
                    }
                }
                session.removeAttribute("listaPicking");
            }
            modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("pendiente"));
            modelMap.addAttribute("tablaPedidos", true);
            modelMap.addAttribute("mensaje", "Picking cancelado con exito.");
            return "realizarPicking";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cargar el formulario.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/PreparacionPedidos", method = RequestMethod.POST, params="action=Finalizar")
    public String finalizarPicking(ModelMap modelMap, HttpSession session){
        try{
            if(session.getAttribute("pedidosPicking") != null){
                return "PreparacionPedidos";
            }else{
                throw new ExcepcionFrigorifico("");
            }
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cargar el formulario.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/PreparacionPedidos", method = RequestMethod.POST, params="action=Seleccionar")
    public String seleccionarPedidoPreparacion(@RequestParam(value="idPedido", required = true) String  idPedido, ModelMap modelMap, HttpSession session){
        try{
            int id = Integer.parseInt(idPedido);
            for(DTOrdenPedido orden : (ArrayList<DTOrdenPedido>)session.getAttribute("pedidosPicking")){
                if(orden.getId() == id){
                    modelMap.addAttribute("ordenPedido", orden);
                    break;
                }
            }
            modelMap.addAttribute("detallePedido", true);
            return "PreparacionPedidos";
        }/*catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }*/catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cargar el formulario.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/PreparacionPedidos", method = RequestMethod.POST, params="action=Cancelar Preparacion")
    public String cancelarPreparacionPedidos(ModelMap modelMap, HttpSession session){
        try{
            if(session.getAttribute("pedidosPicking") != null){
                ArrayList<DTOrdenPedido> ordenes = (ArrayList<DTOrdenPedido>) session.getAttribute("pedidosPicking");
                for (DTOrdenPedido ordenPedido : ordenes) {
                    FabricaLogica.getControladorPedidos().modificarEstadoDePedido(ordenPedido, "pendiente");
                }
                session.removeAttribute("pedidosPicking");
            }

            if(session.getAttribute("listaPicking") != null){
                ArrayList<DTPicking> picking = (ArrayList<DTPicking>)session.getAttribute("listaPicking");
                for(DTPicking p : picking){
                    for(int i=0; i<p.getLotes().size(); i++){
                        if(i!=p.getLotes().size()-1) {
                            FabricaLogica.getControladorDeposito().deshacerBajaLogicaLote(p.getLotes().get(i)); //deshacer la baja logica temporal sobre el lote
                        }else{
                            if(p.getCantidad()==p.getCantidadUnidadesTotal()){
                                FabricaLogica.getControladorDeposito().deshacerBajaLogicaLote(p.getLotes().get(i));
                            }else {
                                FabricaLogica.getControladorDeposito().actualizarStock(p.getLotes().get(i), p.getLotes().get(i).getCantUnidades() - (p.getCantidadUnidadesTotal() - p.getCantidad())); //devolver el stock al lugar de origen
                            }                        }
                    }
                }
                session.removeAttribute("listaPicking");
            }

            return "index";
        }/*catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }*/catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cargar el formulario.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/PreparacionPedidos", method = RequestMethod.POST, params="action=Listo")
    public String pedidoListo(@RequestParam(value="idPedido", required = true) String  idPedido, ModelMap modelMap, HttpSession session){
        try{
            int id = Integer.parseInt(idPedido);
            DTOrdenPedido ordenPedido = null;
            for(DTOrdenPedido orden : (ArrayList<DTOrdenPedido>)session.getAttribute("pedidosPicking")){
                if(orden.getId() == id){
                    ordenPedido=orden;
                    break;
                }
            }

            ((ArrayList<DTOrdenPedido>)session.getAttribute("pedidosPicking")).remove(ordenPedido);
            FabricaLogica.getControladorPedidos().modificarEstadoDePedido(ordenPedido, "preparado");

            for(DTLineaPedido linea : ordenPedido.getLineas()){
                for(DTPicking p : (ArrayList<DTPicking>)session.getAttribute("listaPicking")){
                    if(p.getProducto().getCodigo() == linea.getProducto().getCodigo()){
                        int cantidadARestar = linea.getCantidad();
                        p.setCantidad(p.getCantidad() - cantidadARestar);
                        while((p.getLotes().size() > 0) && (p.getLotes().get(0).getCantUnidades() <= cantidadARestar)){
                            cantidadARestar = cantidadARestar - p.getLotes().get(0).getCantUnidades();
                            FabricaLogica.getControladorDeposito().bajaLote(p.getLotes().get(0));
                            p.getLotes().remove(0);
                        }

                        if(p.getLotes().size() > 0){
                            p.getLotes().get(0).setCantUnidades(p.getLotes().get(0).getCantUnidades() - cantidadARestar);
                        }

                        break;
                    }
                }
            }

            if(((ArrayList<DTOrdenPedido>) session.getAttribute("pedidosPicking")).size() == 0){
                session.removeAttribute("pedidosPicking");
                modelMap.addAttribute("mensaje", "Pedidos preparados con éxito.");
                return "index";
            }else{
                modelMap.addAttribute("mensaje", "Pedido preparado con éxito.");
                return "PreparacionPedidos";
            }

        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cargar el formulario.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/ListadoDePedidos", method = RequestMethod.GET)
    public String getListadoDePedidos(ModelMap modelMap, HttpSession session){
        try{
            ArrayList<DTOrdenPedido> pedidos = FabricaLogica.getControladorPedidos().listarPedidos();
            session.setAttribute("ListadoDePedidos", pedidos);
            modelMap.addAttribute("listadoPedidos", pedidos);
            return "ListadoDePedidos";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ListadoDePedidos";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cargar el formulario.");
            return "ListadoDePedidos";
        }
    }

    //endregion

}
