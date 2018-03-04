package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import proyecto.datatypes.DTEspecificacionProducto;

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
        modelMap.addAttribute("tablaCliente", false);
        return "EstadoDePedido";
    }


}
