package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControladorDeposito {
    @RequestMapping("/")
    public String prueba(){
        return "index";
    }

}
