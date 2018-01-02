package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import proyecto.datatypes.DTEspecificacionProducto;
import proyecto.datatypes.DTPrecio;

import java.util.ArrayList;
import java.util.Date;

@Controller
public class ControladorDeposito {
    @RequestMapping("/")
    public String prueba(){
        return "ABMProducto";
    }

    @RequestMapping(value="/ABMProducto", method = RequestMethod.GET)
    public String AbmProducto(ModelMap modelMap){
        ArrayList<DTPrecio> historico = new ArrayList<DTPrecio>();
        historico.add(new DTPrecio(10.5, new Date()));
        modelMap.addAttribute("producto", new DTEspecificacionProducto(1, "Mortadela", 100, 10, 500, historico));
        return "ABMProducto";
    }

}
