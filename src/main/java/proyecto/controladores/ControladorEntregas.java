package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import proyecto.entidades.*;
import proyecto.logica.FabricaLogica;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class ControladorEntregas {

    @RequestMapping(value="/GenerarViaje", method = RequestMethod.GET)
    public String getGenerarViaje(ModelMap modelMap){
        try{
            modelMap.addAttribute("repartidores", FabricaLogica.getControladorEmpleados().listarEmpleadosXRol("repartidor", true));
            modelMap.addAttribute("vehiculos", FabricaLogica.getControladorEmpleados().listarVehiculos(true));
            ArrayList<OrdenPedido> pedidos = FabricaLogica.getControladorPedidos().listarPedidosXEstado("preparado");

            if (pedidos.size() > 0){
                modelMap.addAttribute("pedidos", pedidos);
                modelMap.addAttribute("tablaPedidos", true);
            } else {
                modelMap.addAttribute("mensaje", "No hay pedidos pendientes para su distribución");
            }


            return "GenerarViaje";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al cargar la pagina");
            return "GenerarViaje";
        }
    }

    @RequestMapping(value="/GenerarViaje", method = RequestMethod.POST, params = "action=Generar viaje")
    public String generarViaje(@RequestParam(value = "pedidos", required = false) int[] pedidos, @RequestParam String vehiculo, @RequestParam String repartidor, ModelMap modelMap){
        try{
            modelMap.addAttribute("repartidores", FabricaLogica.getControladorEmpleados().listarEmpleadosXRol("repartidor", true));
            modelMap.addAttribute("vehiculos", FabricaLogica.getControladorEmpleados().listarVehiculos(true));

            if(repartidor.equals("")){
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("preparado"));
                modelMap.addAttribute("tablaPedidos", true);
                throw new ExcepcionFrigorifico("Debe seleccionar un repartidor");
            }
            Empleado rep = FabricaLogica.getControladorEmpleados().buscarEmpleado(repartidor, true);

            if(vehiculo.equals("")){
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("preparado"));
                modelMap.addAttribute("tablaPedidos", true);
                throw new ExcepcionFrigorifico("Debe seleccionar un vehículo");
            }
            Vehiculo v = FabricaLogica.getControladorEmpleados().buscarVehiculo(vehiculo, true);

            if(pedidos == null){
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("preparado"));
                modelMap.addAttribute("tablaPedidos", true);
                throw new ExcepcionFrigorifico("Debe seleccionar al menos un pedido");
            }

            ArrayList<OrdenPedido> listaPedidos = new ArrayList<>();
            for(int i = 0; i<pedidos.length; i++){
                OrdenPedido p = FabricaLogica.getControladorPedidos().buscarOrdenPedido(pedidos[i]);
                listaPedidos.add(p);
            }

            Viaje viaje = new Viaje(0, (Repartidor)rep, v, listaPedidos, new Date());

            FabricaLogica.getControladorEntregas().generarViaje(viaje);

            modelMap.addAttribute("mensaje", "Viaje generado con éxito.");
            ArrayList<OrdenPedido> listaDePedidos = FabricaLogica.getControladorPedidos().listarPedidosXEstado("preparado");
            if (listaDePedidos.size() != 0){
                modelMap.addAttribute("pedidos", listaDePedidos);
                modelMap.addAttribute("tablaPedidos", true);
            }
            return "GenerarViaje";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "GenerarViaje";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrió un error al cargar la página");
            return "GenerarViaje";
        }
    }

    @RequestMapping(value = "/EntregaDePedidos", method = RequestMethod.GET)
    public String getEntregaDePedidos (HttpSession session, ModelMap modelMap){
        try {
            Repartidor repartidor = (Repartidor) session.getAttribute("usuarioLogueado");

            ArrayList<Viaje> viajes = FabricaLogica.getControladorEntregas().listarViajesPendientesXRepartidor(repartidor);

            if (viajes.size() > 0) {

                session.removeAttribute("viaje");
                session.setAttribute("viajes", viajes);
                return "EntregaDePedidos";
            }else {
                session.removeAttribute("viaje");
                modelMap.addAttribute("mensaje", "No tiene viajes pendientes");
                return "EntregaDePedidos";
            }

        } catch (Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Hubo un error al cargar la página");
            return "EntregaDePedidos";
        }
    }

    @RequestMapping(value = "/EntregaDePedidos", method = RequestMethod.POST, params="action=Seleccionar Viaje")
    public String seleccionarViaje (@RequestParam(value = "viajes", required = false) int idViaje, ModelMap modelMap, HttpSession session){
        try {

            if (idViaje == -1) {
                throw new ExcepcionFrigorifico("Debe seleccionar un viaje de la lista.");
            }

            ArrayList<Viaje> viajes = (ArrayList<Viaje>) session.getAttribute("viajes");

            Viaje viaje = null;

            for(Viaje v : viajes){
                if (v.getId() == idViaje){
                    viaje = v;
                    break;
                }
            }

            session.setAttribute("viaje", viaje);
            modelMap.addAttribute("mostrarDdlPedidos", "true");
        } catch (ExcepcionFrigorifico exF){
            modelMap.addAttribute("mensaje", exF.getMessage());
        } catch (Exception ex){
            modelMap.addAttribute("mensaje", "ERROR!, ocurrió un error al mostrar los pedidos del viaje");
        }

        return "EntregaDePedidos";
    }

    @RequestMapping(value = "/EntregaDePedidos", method = RequestMethod.POST, params="action=Seleccionar Pedido")
    public String seleccionarPedido (@RequestParam(value = "pedidos", required = false) int idPedido, ModelMap modelMap, HttpSession session){
        try {
            if(idPedido == -1){
                throw new ExcepcionFrigorifico("Debe seleccionar un pedido de la lista.");
            }


            Viaje v = (Viaje)session.getAttribute("viaje");
            OrdenPedido pedido = null;

            for(OrdenPedido p : v.getPedidos()){
                if(p.getId()==idPedido){
                    pedido=p;
                    break;
                }
            }

            session.setAttribute("pedido", pedido);
            modelMap.addAttribute("tablaPedido", "true");
            modelMap.addAttribute("mostrarDdlPedidos", "true");

        } catch (ExcepcionFrigorifico exF){
            modelMap.addAttribute("mensaje", exF.getMessage());
        } catch (Exception ex){
            modelMap.addAttribute("mensaje", "ERROR!, ocurrió un error al mostrar el pedido seleccionado.");
        }

        return "EntregaDePedidos";
    }

    @RequestMapping(value = "/EntregaDePedidos", method = RequestMethod.POST, params="action=Entregado")
    public String entregarPedido (ModelMap modelMap, HttpSession session){

        try {

            OrdenPedido pedido = (OrdenPedido)session.getAttribute("pedido");
            FabricaLogica.getControladorPedidos().modificarEstadoDePedido(pedido, "entregado");
            pedido.setRepartidor((Empleado)session.getAttribute("usuarioLogueado"));
            FabricaLogica.getControladorEntregas().entregarPedido(pedido);

            session.removeAttribute("Pedido");
            modelMap.addAttribute("mensaje", "Pedido entregado con éxito.");

            ((Viaje)session.getAttribute("viaje")).getPedidos().remove(pedido);

            Viaje viaje = ((Viaje)session.getAttribute("viaje"));

            if (viaje.getPedidos().size() == 0){
                FabricaLogica.getControladorEntregas().finalizarViaje(viaje);
                session.removeAttribute("viaje");
                ((ArrayList<Viaje>)session.getAttribute("viajes")).remove(viaje);
            } else {
                modelMap.addAttribute("mostrarDdlPedidos", "true");
            }

        } catch (ExcepcionFrigorifico exF){
            modelMap.addAttribute("mensaje", exF.getMessage());
        } catch (Exception ex){
            modelMap.addAttribute("mensaje", "ERROR!, ocurrió un error al mostrar el pedido seleccionado.");
        }

        return "EntregaDePedidos";
    }

    @RequestMapping(value = "/EntregaDePedidos", method = RequestMethod.POST, params="action=Cancelado")
    public String entregaFallidaPedido (@RequestParam(value = "Descripcion Entrega", required = false) String detalleEntrega, ModelMap modelMap, HttpSession session){

        try {
                if (detalleEntrega.isEmpty()){
                    modelMap.addAttribute("tablaPedido", "true");
                    modelMap.addAttribute("mostrarDdlPedidos", "true");
                    throw new ExcepcionFrigorifico("Debe especificar los detalles de la cancelación.");
                }

            OrdenPedido pedido = (OrdenPedido)session.getAttribute("pedido");
            FabricaLogica.getControladorEntregas().entregaFallidaPedido(pedido, detalleEntrega);

            session.removeAttribute("pedido");
            modelMap.addAttribute("mensaje", "Pedido cancelado con éxito.");

            ((Viaje)session.getAttribute("viaje")).getPedidos().remove(pedido);

            Viaje viaje = ((Viaje)session.getAttribute("viaje"));

            if (viaje.getPedidos().size() == 0){
                FabricaLogica.getControladorEntregas().finalizarViaje(viaje);
                session.removeAttribute("viaje");
                ((ArrayList<Viaje>)session.getAttribute("viajes")).remove(viaje);
            } else {
                modelMap.addAttribute("mostrarDdlPedidos", "true");
            }

        } catch (ExcepcionFrigorifico exF){
            modelMap.addAttribute("mensaje", exF.getMessage());
        } catch (Exception ex){
            modelMap.addAttribute("mensaje", "ERROR!, ocurrió un error al cancelar el pedido.");
        }

        return "EntregaDePedidos";
    }
}
