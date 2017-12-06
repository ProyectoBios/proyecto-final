package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import proyecto.datatypes.DTEspecificacionProducto;
import proyecto.datatypes.DTPrecio;

import java.util.ArrayList;

@Controller
public class ControladorDeposito {
    @RequestMapping("/")
    public String prueba(){
        return "index";
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.GET)
    public String AbmProducto(ModelMap modelMap){
        modelMap.addAttribute("producto", new DTEspecificacionProducto());
        return "ABMProducto";
    }

}
