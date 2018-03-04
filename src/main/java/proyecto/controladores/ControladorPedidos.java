package proyecto.controladores;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import proyecto.datatypes.DTCliente;
import proyecto.datatypes.DTEspecificacionProducto;
import proyecto.datatypes.DTOrdenPedido;
import proyecto.datatypes.ExcepcionFrigorifico;
import proyecto.logica.FabricaLogica;
import proyecto.persistencia.FabricaPersistencia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class ControladorPedidos {

    /*@RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Agregar")
    public String AltaOrdenDePedido() {
        return "/AltaOrdenDePedido";
    }*/

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
                if(nombreCliente==""){
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


}
