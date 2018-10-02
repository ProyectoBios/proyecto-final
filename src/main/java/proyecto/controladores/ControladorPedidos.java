package proyecto.controladores;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import proyecto.entidades.*;
import proyecto.logica.FabricaLogica;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
            OrdenPedido ordenPedido = null;
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

                ArrayList<Cliente> clientes = FabricaLogica.getControladorPedidos().buscarClientes(nombreCliente);
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
            Cliente cliente = FabricaLogica.getControladorPedidos().buscarCliente(nombreCliente);
            ArrayList<OrdenPedido> ordenes = FabricaLogica.getControladorPedidos().buscarOrdenesXCliente(cliente);
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

            OrdenPedido ordenPedido = FabricaLogica.getControladorPedidos().buscarOrdenPedido(id);
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

            OrdenPedido ordenPedido = FabricaLogica.getControladorPedidos().buscarOrdenPedido(id);
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
            ArrayList<Cliente> clientes = null;
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
            Cliente cliente = FabricaLogica.getControladorPedidos().buscarCliente(nombreCliente);

            session.setAttribute("cliente", cliente);
            OrdenPedido orden = new OrdenPedido();
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
            modelMap.addAttribute("cliente", new Cliente());
            return "AltaOrdenDePedido";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al dar de alta el cliente.");
            return "AltaOrdenDePedido";
        }
    }

    @RequestMapping(value="/AltaOrdenDePedido", method = RequestMethod.POST, params = "action=Agregar Cliente")
    public String altaCliente(@ModelAttribute Cliente cliente, BindingResult bindingResult, ModelMap modelMap, HttpSession session){
        try{
            FabricaLogica.getControladorPedidos().altaCliente(cliente);

            session.setAttribute("cliente", cliente);
            OrdenPedido orden = new OrdenPedido();
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
                modelMap.addAttribute("mensajeStock", "La cantidad de unidades no puede quedar vacía");
                return "AltaOrdenDePedido";
            }

            int id;
            try{
                id = Integer.parseInt(idProducto);
            }catch (Exception ex){
                modelMap.addAttribute("tablaProducto", true);
                modelMap.addAttribute("mensajeStock", "ERROR! Debe seleccionar un producto");
                return "AltaOrdenDePedido";
            }
            EspecificacionProducto producto = FabricaLogica.getControladorDeposito().buscarProducto(id);

            int cantidadUnidades;
            try{
                cantidadUnidades = Integer.parseInt(cantUnidades);
            }catch (Exception ex){
                modelMap.addAttribute("tablaProducto", true);
                throw new ExcepcionFrigorifico("ERROR! Ingrese una cantidad correcta");
            }

            String resultado = FabricaLogica.getControladorPedidos().agregarLineaDePedido((OrdenPedido)session.getAttribute("orden"), producto, cantidadUnidades);

            modelMap.addAttribute("mensajeStock", resultado);

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
            FabricaLogica.getControladorPedidos().eliminarLinea((OrdenPedido)session.getAttribute("orden"), nro);

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
            OrdenPedido orden = (OrdenPedido)session.getAttribute("orden");

            orden.setDireccionEnvio(direccion);
            orden.setContacto(contacto);
            orden.setOperador((Empleado)session.getAttribute("usuarioLogueado"));

            int id = FabricaLogica.getControladorPedidos().altaOrdenDePedido(orden);

            session.removeAttribute("orden");
            session.removeAttribute("cliente");
            session.removeAttribute("productos");

            modelMap.addAttribute("tablaBusquedaCliente", true);
            modelMap.addAttribute("mensaje", "Alta de pedido exitosa. ID: " + String.valueOf(id));
            return "AltaOrdenDePedido";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("tablaProducto", true);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaOrdenDePedido";
        }catch (Exception ex){
            modelMap.addAttribute("tablaProducto", true);
            modelMap.addAttribute("mensaje", ex.getMessage()); //"Ocurrió un error al dar de alta el pedido."
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
                modelMap.addAttribute("pedidosSeleccionados", new String[0]);
            }else{
                modelMap.addAttribute("tablaPicking", true);
            }

            return "realizarPicking";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cargar la pagina.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/RealizarPicking", method = RequestMethod.POST, params = "action=Seleccionar")
    public synchronized String seleccionarPedidosPicking(@RequestParam(value="pedidos", required = false)int[] pedidos, ModelMap modelMap, HttpSession session, HttpServletResponse response, HttpServletRequest request){
        ArrayList<OrdenPedido> ordenesPedido = new ArrayList<>();
        try {
            if(pedidos.length == 0 || pedidos.length > 5){
                throw new ExcepcionFrigorifico("Debe seleccionar al menos 1 pedido y no más de 5.");
            }

            for(int i=0; i<pedidos.length; i++){
                OrdenPedido orden = FabricaLogica.getControladorPedidos().buscarOrdenPedido(pedidos[i]);
                if(orden.getEstado().equals("en preparacion")){
                    throw new ExcepcionFrigorifico("El pedido con ID: " + orden.getId() + " ya está siendo preparado por otro empleado.");
                }
                FabricaLogica.getControladorPedidos().modificarEstadoDePedido(orden, "en preparacion");
                ordenesPedido.add(orden);
            }
            session.setAttribute("pedidosPicking", ordenesPedido);

            session.setAttribute("listaPicking", FabricaLogica.getControladorPedidos().obtenerPicking(ordenesPedido));
            modelMap.addAttribute("pedidosSeleccionados", new String[0]);

            modelMap.addAttribute("tablaPicking", true);
            return "realizarPicking";
        }catch (ExcepcionFrigorifico ex){
            try{
                for(OrdenPedido o : ordenesPedido){
                    FabricaLogica.getControladorPedidos().modificarEstadoDePedido(o, "pendiente");
                }
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("pendiente"));
            }catch (Exception e){}

            modelMap.addAttribute("tablaPedidos", true);
            modelMap.addAttribute("mensaje", ex.getMessage());
            modelMap.addAttribute("pedidosSeleccionados", pedidos);
            return "realizarPicking";
        }catch (Exception ex){
            try{
                for(OrdenPedido o : ordenesPedido){
                    FabricaLogica.getControladorPedidos().modificarEstadoDePedido(o, "pendiente");
                }
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("pendiente"));
            }catch (Exception e){}

            modelMap.addAttribute("tablaPedidos", true);
            modelMap.addAttribute("mensaje", "Ocurrió un error al seleccionar los pedidos para el picking.");
            modelMap.addAttribute("pedidosSeleccionados", pedidos);

            return "realizarPicking";
        }
    }

    @RequestMapping(value="/RealizarPicking", method = RequestMethod.POST, params="action=Cancelar Picking")
    public String cancelarPicking(ModelMap modelMap, HttpSession session){
        try{
            if(session.getAttribute("pedidosPicking") != null){
                ArrayList<OrdenPedido> ordenes = (ArrayList<OrdenPedido>) session.getAttribute("pedidosPicking");
                for (OrdenPedido ordenPedido : ordenes) {
                    FabricaLogica.getControladorPedidos().modificarEstadoDePedido(ordenPedido, "pendiente");
                }
                session.removeAttribute("pedidosPicking");
            }
            modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("pendiente"));

            if(session.getAttribute("listaPicking") != null){
                ArrayList<Picking> picking = (ArrayList<Picking>)session.getAttribute("listaPicking");
                for(Picking p : picking){
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
            modelMap.addAttribute("tablaPedidos", true);
            modelMap.addAttribute("mensaje", "Picking cancelado con exito.");
            return "realizarPicking";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cancelar el picking.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/PreparacionPedidos", method = RequestMethod.POST, params="action=Finalizar")
    public String finalizarPicking(ModelMap modelMap, HttpSession session){
        try{
            if(session.getAttribute("pedidosPicking") != null){
                return "PreparacionPedidos";
            }else{
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("pendiente"));
                throw new ExcepcionFrigorifico("Ocurrió un error al finalizar el picking.");
            }
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al finalizar el picking.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/PreparacionPedidos", method = RequestMethod.POST, params="action=Seleccionar")
    public String seleccionarPedidoPreparacion(@RequestParam(value="idPedido", required = true) String  idPedido, ModelMap modelMap, HttpSession session){
        try{
            int id = Integer.parseInt(idPedido);
            for(OrdenPedido orden : (ArrayList<OrdenPedido>)session.getAttribute("pedidosPicking")){
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
            modelMap.addAttribute("mensaje", "Ocurrió un error al seleccionar el pedido a preparar.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/PreparacionPedidos", method = RequestMethod.POST, params="action=Cancelar Preparacion")
    public String cancelarPreparacionPedidos(ModelMap modelMap, HttpSession session){
        try{
            if(session.getAttribute("pedidosPicking") != null){
                ArrayList<OrdenPedido> ordenes = (ArrayList<OrdenPedido>) session.getAttribute("pedidosPicking");
                for (OrdenPedido ordenPedido : ordenes) {
                    FabricaLogica.getControladorPedidos().modificarEstadoDePedido(ordenPedido, "pendiente");
                }
                session.removeAttribute("pedidosPicking");
            }

            if(session.getAttribute("listaPicking") != null){
                ArrayList<Picking> picking = (ArrayList<Picking>)session.getAttribute("listaPicking");
                for(Picking p : picking){
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
            modelMap.addAttribute("mensaje", "Preparación de pedidos interrumpida con éxito.");
            return "index";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "realizarPicking";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cancelar la preparacion de los pedidos.");
            return "realizarPicking";
        }
    }

    @RequestMapping(value="/PreparacionPedidos", method = RequestMethod.POST, params="action=Listo")
    public String confirmarPreparacion(@RequestParam(value="idPedido", required = true) String  idPedido, ModelMap modelMap, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        try{
            int id = Integer.parseInt(idPedido);
            OrdenPedido ordenPedido = null;
            for(OrdenPedido orden : (ArrayList<OrdenPedido>)session.getAttribute("pedidosPicking")){
                if(orden.getId() == id){
                    ordenPedido=orden;
                    break;
                }
            }

            ((ArrayList<OrdenPedido>)session.getAttribute("pedidosPicking")).remove(ordenPedido);
            FabricaLogica.getControladorPedidos().modificarEstadoDePedido(ordenPedido, "preparado");
            ordenPedido.setFuncionario((Empleado)session.getAttribute("usuarioLogueado"));
            FabricaLogica.getControladorPedidos().prepararPedido(ordenPedido);

            for(LineaPedido linea : ordenPedido.getLineas()){
                for(Picking p : (ArrayList<Picking>)session.getAttribute("listaPicking")){
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

            if(((ArrayList<OrdenPedido>) session.getAttribute("pedidosPicking")).size() == 0){
                session.removeAttribute("pedidosPicking");
                session.removeAttribute("listaPicking");
                session.setAttribute("mensaje", "Pedidos preparados con éxito.");
                response.sendRedirect(request.getContextPath() + "/Bienvenida");
                return "Bienvenida";
            }else{
                modelMap.addAttribute("mensaje", "Pedido preparado con éxito.");
                return "PreparacionPedidos";
            }

        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "PreparacionPedidos";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error finalizar el pedido.");
            return "PreparacionPedidos";
        }
    }

    @RequestMapping(value="/ListadoDePedidos", method = RequestMethod.GET)
    public String getListadoDePedidos(ModelMap modelMap, HttpSession session){
        try{
            ArrayList<OrdenPedido> pedidos = FabricaLogica.getControladorPedidos().listarPedidos();
            session.setAttribute("ListadoDePedidos", pedidos);
            session.setAttribute("clientes", FabricaLogica.getControladorPedidos().buscarClientes(""));
            session.setAttribute("operadores", FabricaLogica.getControladorEmpleados().listarEmpleadosXRol("operador"));
            session.setAttribute("funcionarios", FabricaLogica.getControladorEmpleados().listarEmpleadosXRol("funcionario"));
            session.setAttribute("repartidores", FabricaLogica.getControladorEmpleados().listarEmpleadosXRol("repartidor"));
            modelMap.addAttribute("listadoPedidos", pedidos);
            return "ListadoDePedidos";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ListadoDePedidos";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al cargar la pagina.");
            return "ListadoDePedidos";
        }
    }

    @RequestMapping(value="/ListadoDePedidos", method = RequestMethod.POST, params="action=Filtrar")
    public String filtrarPedidos(
            @RequestParam String cliente,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaIni,
            @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            @RequestParam String importeMin,
            @RequestParam String importeMax,
            @RequestParam String estado,
            @RequestParam String opTel,
            @RequestParam String preparador,
            ModelMap modelMap, HttpSession session) {
        try{
            ArrayList<OrdenPedido> resultado = new ArrayList<>((ArrayList<OrdenPedido>)session.getAttribute("ListadoDePedidos"));
            ArrayList<OrdenPedido> remover = new ArrayList<>();

            if(!cliente.equals("")){
                for(OrdenPedido p : resultado){
                    if(!p.getCliente().getNombre().equals(cliente)){
                        remover.add(p);
                    }
                }
                resultado.removeAll(remover);
                remover.clear();
            }

            if(fechaIni != null){
                for(OrdenPedido p : resultado){
                    if(p.getFecha().before(fechaIni)){
                        remover.add(p);
                    }
                }

                resultado.removeAll(remover);
                remover.clear();
            }

            if(fechaFin != null){
                for(OrdenPedido p : resultado){
                    if(p.getFecha().after(fechaFin)){
                        remover.add(p);
                    }
                }
                resultado.removeAll(remover);
                remover.clear();
            }

            if(!importeMin.equals("")){
                double min = 0;
                try{
                    min = Double.parseDouble(importeMin);
                }catch (Exception ex){
                    throw new ExcepcionFrigorifico("¡ERROR! Importe minimo inválido.");
                }

                for(OrdenPedido p : resultado){
                    if(p.getTotal() < min){
                        remover.add(p);
                    }
                }
                resultado.removeAll(remover);
                remover.clear();
            }

            if(!importeMax.equals("")){
                double max = 0;
                try{
                    max = Double.parseDouble(importeMax);
                }catch (Exception ex){
                    throw new ExcepcionFrigorifico("¡ERROR! Importe máximo inválido.");
                }

                for(OrdenPedido p : resultado){
                    if(p.getTotal() > max){
                        remover.add(p);
                    }
                }
                resultado.removeAll(remover);
                remover.clear();
            }

            if(!estado.equals("")){
                for(OrdenPedido p : resultado){
                    if(!p.getEstado().equals(estado)){
                        remover.add(p);
                    }
                }
                resultado.removeAll(remover);
                remover.clear();
            }

            if(!opTel.equals("")){
                for(OrdenPedido p : resultado){
                    if(!p.getOperador().getCi().equals(opTel)){
                        remover.add(p);
                    }
                }
                resultado.removeAll(remover);
                remover.clear();
            }

            if(!preparador.equals("")){
                for(OrdenPedido p : resultado){
                    if(p.getFuncionario()== null || !p.getFuncionario().getCi().equals(preparador)){
                        remover.add(p);
                    }
                }
                resultado.removeAll(remover);
                remover.clear();
            }

            modelMap.addAttribute("listadoPedidos", resultado);
            return "ListadoDePedidos";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ListadoDePedidos";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al filtrar los pedidos.");
            return "ListadoDePedidos";
        }
    }

    //endregion

}
