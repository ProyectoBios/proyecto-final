package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import proyecto.entidades.*;
import proyecto.logica.FabricaLogica;

import javax.jws.WebParam;
import java.util.ArrayList;

@Controller
public class ControladorEntregas {

    @RequestMapping(value="/GenerarViaje", method = RequestMethod.GET)
    public String getGenerarViaje(ModelMap modelMap){
        try{
            modelMap.addAttribute("repartidores", FabricaLogica.getControladorEmpleados().listarEmpleadosXRol("repartidor"));
            modelMap.addAttribute("vehiculos", FabricaLogica.getControladorEmpleados().listarVehiculos());
            modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("preparado"));
            return "GenerarViaje";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al cargar la pagina");
            return "GenerarViaje";
        }
    }

    @RequestMapping(value="/GenerarViaje", method = RequestMethod.POST, params = "action=Generar viaje")
    public String generarViaje(@RequestParam int[] pedidos, @RequestParam String vehiculo, @RequestParam String repartidor, ModelMap modelMap){
        try{
            modelMap.addAttribute("repartidores", FabricaLogica.getControladorEmpleados().listarEmpleadosXRol("repartidor"));
            modelMap.addAttribute("vehiculos", FabricaLogica.getControladorEmpleados().listarVehiculos());

            if(pedidos.length == 0){
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("preparado"));
                throw new ExcepcionFrigorifico("Debe seleccionar al menos un pedido");
            }

            ArrayList<OrdenPedido> listaPedidos = new ArrayList<>();
            for(int i = 0; i<pedidos.length; i++){
                OrdenPedido p = FabricaLogica.getControladorPedidos().buscarOrdenPedido(pedidos[i]);
                listaPedidos.add(p);
            }

            if(repartidor.equals("")){
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("preparado"));
                throw new ExcepcionFrigorifico("Debe seleccionar un repartidor");
            }
            Empleado rep = FabricaLogica.getControladorEmpleados().buscarEmpleado(repartidor);

            if(vehiculo.equals("")){
                modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("preparado"));
                throw new ExcepcionFrigorifico("Debe seleccionar un vehiculo");
            }
            Vehiculo v = FabricaLogica.getControladorEmpleados().buscarVehiculo(vehiculo);

            Viaje viaje = new Viaje(0, (Repartidor)rep, v, listaPedidos);

            FabricaLogica.getControladorEntregas().generarViaje(viaje);

            modelMap.addAttribute("mensaje", "Viaje generado con éxito.");
            modelMap.addAttribute("pedidos", FabricaLogica.getControladorPedidos().listarPedidosXEstado("preparado"));
            return "GenerarViaje";
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "GenerarViaje";
        }catch(Exception ex){
            modelMap.addAttribute("mensaje", "¡ERROR! Ocurrio un error al cargar la pagina");
            return "GenerarViaje";
        }
    }
}