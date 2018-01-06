package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import proyecto.datatypes.DTEspecificacionProducto;

import proyecto.datatypes.DTRack;
import proyecto.datatypes.ExcepcionFrigorifico;
import proyecto.logica.*;

@Controller
public class ControladorDeposito {
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    //region ABMProducto
    @RequestMapping(value="/ABMProducto", method = RequestMethod.GET)
    public String GetAbmProducto(ModelMap modelMap){
        modelMap.addAttribute("producto", new DTEspecificacionProducto());
        botonesPorDefectoProducto(modelMap);

        return "ABMProducto";
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Buscar")
    public String buscarProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            producto = FabricaLogica.getControladorDeposito().buscarProducto(producto.getCodigo());

            if(producto==null){
                producto = new DTEspecificacionProducto();
                modelMap.addAttribute("producto", producto);
                botonesAltaProducto(modelMap);

            }else{
                modelMap.addAttribute("producto", producto);
                botonesBMProducto(modelMap);
            }

            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            producto = new DTEspecificacionProducto();
            modelMap.addAttribute("producto", producto);
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }
        catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Agregar")
    public String agregarABMProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            int codigo = FabricaLogica.getControladorDeposito().altaDeProducto(producto);

            modelMap.addAttribute("producto", new DTEspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensaje", "Alta exitosa. ID:  " + codigo + ".");
            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesAltaProducto(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesAltaProducto(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Eliminar")
    public String eliminarABMProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            FabricaLogica.getControladorDeposito().bajaProducto(producto);

            modelMap.addAttribute("producto", new DTEspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensaje", "Baja exitosa!");
            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Modificar")
    public String modificarABMProducto(@ModelAttribute DTEspecificacionProducto producto, BindingResult bindingResult, ModelMap modelMap){
        try {
            FabricaLogica.getControladorDeposito().modificarProducto(producto);

            modelMap.addAttribute("producto", new DTEspecificacionProducto());
            botonesPorDefectoProducto(modelMap);
            modelMap.addAttribute("mensaje", "Modificacion exitosa!");
            return "ABMProducto";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "ABMProducto";
        }catch(Exception ex){
            modelMap.addAttribute("producto", producto);
            botonesBMProducto(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR!");
            return "ABMProducto";
        }
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.POST, params="action=Limpiar")
    public String limpiarABMProducto(ModelMap modelMap){
        modelMap.addAttribute("producto", new DTEspecificacionProducto());
        botonesPorDefectoProducto(modelMap);

        return "ABMProducto";
    }

    public void botonesPorDefectoProducto(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "false");
        modelMap.addAttribute("eliminarHabilitado", "false");
        modelMap.addAttribute("modificarHabilitado", "false");
        modelMap.addAttribute("buscarHabilitado", "true");
        modelMap.addAttribute("codigoBloqueado", "false");
    }

    public void botonesAltaProducto(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "true");
        modelMap.addAttribute("eliminarHabilitado", "false");
        modelMap.addAttribute("modificarHabilitado", "false");
        modelMap.addAttribute("buscarHabilitado", "false");
        modelMap.addAttribute("codigoBloqueado", "true");
    }

    public void botonesBMProducto(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "false");
        modelMap.addAttribute("eliminarHabilitado", "true");
        modelMap.addAttribute("modificarHabilitado", "true");
        modelMap.addAttribute("buscarHabilitado", "false");
        modelMap.addAttribute("codigoBloqueado", "true");
    }
    //endregion

    //region AltaRack
    @RequestMapping(value="/AltaRack", method = RequestMethod.GET)
    public String GetAltaRack(ModelMap modelMap){
        modelMap.addAttribute("rack", new DTRack());
        botonesPorDefectoRack(modelMap);

        return "AltaRack";
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Agregar")
    public String agregarAltaRack(@ModelAttribute DTRack rack, BindingResult bindingResult, ModelMap modelMap){
        try {
            FabricaLogica.getControladorDeposito().altaRack(rack);

            modelMap.addAttribute("rack", new DTRack());
            botonesPorDefectoRack(modelMap);
            modelMap.addAttribute("mensaje", "¡Alta exitosa!");
            return "AltaRack";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("rack", rack);
            botonesAltaRack(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaRack";
        }catch(Exception ex){
            modelMap.addAttribute("rack", rack);
            botonesAltaRack(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR! No se pudo dar de alta el rack.");
            return "AltaRack";
        }
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Buscar")
    public String buscarAltaRack(@ModelAttribute DTRack rack, BindingResult bindingResult, ModelMap modelMap){
        String letra = rack.getLetra();
        try{
            rack = FabricaLogica.getControladorDeposito().buscarRack(letra);

            if(rack == null){
                rack = new DTRack();
                rack.setLetra(letra);
                modelMap.addAttribute("rack", rack);
                botonesAltaRack(modelMap);
            }else{
                modelMap.addAttribute("rack", rack);
                botonesPorDefectoRack(modelMap);
            }

            return "AltaRack";
        }catch(ExcepcionFrigorifico ex){
            modelMap.addAttribute("rack", new DTRack());
            botonesPorDefectoRack(modelMap);
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "AltaRack";
        }catch(Exception ex){
            modelMap.addAttribute("rack", new DTRack());
            botonesPorDefectoRack(modelMap);
            modelMap.addAttribute("mensaje", "¡ERROR! No se pudo buscar el rack");
            return "AltaRack";
        }
    }

    public void botonesPorDefectoRack(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "false");
        modelMap.addAttribute("buscarHabilitado", "true");
        modelMap.addAttribute("letraBloqueado", "false");
    }

    public void botonesAltaRack(ModelMap modelMap){
        modelMap.addAttribute("agregarHabilitado", "true");
        modelMap.addAttribute("buscarHabilitado", "false");
        modelMap.addAttribute("letraBloqueado", "true");
    }

    @RequestMapping(value="/AltaRack", method = RequestMethod.POST, params="action=Limpiar")
    public String limpiarAltaRack(ModelMap modelMap){
        modelMap.addAttribute("rack", new DTRack());
        botonesPorDefectoRack(modelMap);

        return "AltaRack";
    }
    //endregion

}
