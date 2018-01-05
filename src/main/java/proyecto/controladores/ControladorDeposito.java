package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import proyecto.datatypes.DTEspecificacionProducto;

import proyecto.datatypes.ExcepcionFrigorifico;
import proyecto.logica.*;

@Controller
public class ControladorDeposito {
    @RequestMapping("/")
    public String prueba(){
        return "ABMProducto";
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.GET)
    public String GetAbmProducto(ModelMap modelMap){
        modelMap.addAttribute("producto", new DTEspecificacionProducto());
        botonesPorDefecto(modelMap);

        return "ABMProducto";
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Buscar")
    public String buscarProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            producto = FabricaLogica.getControladorDeposito().buscarProducto(producto.getCodigo());

            if(producto==null){
                producto = new DTEspecificacionProducto();
                modelMap.addAttribute("producto", producto);
                botonesAlta(modelMap);

            }else{
                modelMap.addAttribute("producto", producto);
                botonesBM(modelMap);
            }

            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            producto = new DTEspecificacionProducto();
            modelMap.addAttribute("producto", producto);
            botonesPorDefecto(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }
        catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesPorDefecto(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Agregar")
    public String agregarABMProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            int codigo = FabricaLogica.getControladorDeposito().altaDeProducto(producto);

            modelMap.addAttribute("producto", new DTEspecificacionProducto());
            botonesPorDefecto(modelMap);
            modelMap.addAttribute("mensaje", "Alta exitosa. ID:  " + codigo + ".");
            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesAlta(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesAlta(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Eliminar")
    public String eliminarABMProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            FabricaLogica.getControladorDeposito().bajaProducto(producto);

            modelMap.addAttribute("producto", new DTEspecificacionProducto());
            botonesPorDefecto(modelMap);
            modelMap.addAttribute("mensaje", "Baja exitosa!");
            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesBM(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesBM(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Modificar")
    public String modificarABMProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            FabricaLogica.getControladorDeposito().modificarProducto(producto);

            modelMap.addAttribute("producto", new DTEspecificacionProducto());
            botonesPorDefecto(modelMap);
            modelMap.addAttribute("mensaje", "Modificacion exitosa!");
            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesBM(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesBM(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Limpiar")
    public String limpiarABMProducto(ModelMap modelMap){
        modelMap.addAttribute("producto", new DTEspecificacionProducto());
        modelMap.addAttribute("agregarHabilitado", "false");
        modelMap.addAttribute("eliminarHabilitado", "false");
        modelMap.addAttribute("modificarHabilitado", "false");
        modelMap.addAttribute("buscarHabilitado", "true");
        modelMap.addAttribute("codigoBloqueado", "false");

        return "ABMProducto";
    }

    public void botonesPorDefecto(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "false");
        modelMap.addAttribute("eliminarHabilitado", "false");
        modelMap.addAttribute("modificarHabilitado", "false");
        modelMap.addAttribute("buscarHabilitado", "true");
        modelMap.addAttribute("codigoBloqueado", "false");
    }

    public void botonesAlta(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "true");
        modelMap.addAttribute("eliminarHabilitado", "false");
        modelMap.addAttribute("modificarHabilitado", "false");
        modelMap.addAttribute("buscarHabilitado", "false");
        modelMap.addAttribute("codigoBloqueado", "true");
    }

    public void botonesBM(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "false");
        modelMap.addAttribute("eliminarHabilitado", "true");
        modelMap.addAttribute("modificarHabilitado", "true");
        modelMap.addAttribute("buscarHabilitado", "false");
        modelMap.addAttribute("codigoBloqueado", "true");
    }


}
