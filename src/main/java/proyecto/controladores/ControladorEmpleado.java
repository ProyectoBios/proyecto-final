package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import proyecto.entidades.Empleado;
import proyecto.entidades.ExcepcionFrigorifico;
import proyecto.logica.FabricaLogica;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorEmpleado {
    @RequestMapping(value = "/Login", method = RequestMethod.GET)
    public String getLogin(ModelMap modelMap){
        return "login";
    }

    @RequestMapping(value="/Login", method = RequestMethod.POST, params = "action=Ingresar")
    public String login(@RequestParam(value="cedula") String cedula, @RequestParam(value = "contrasenia")String contrasenia, ModelMap modelMap, HttpSession session){
        try{
            if(cedula.equals("") || contrasenia.equals("")){
                throw new ExcepcionFrigorifico("Credenciales inválidas, inténtelo denuevo.");
            }

            Empleado e = FabricaLogica.getControladorEmpleados().buscarEmpleado(cedula);
            if(e==null){
                throw new ExcepcionFrigorifico("Usuario y/o contraseña inválidos.");
            }else if(contrasenia.equals("1234")){//TODO: Reemplazar por e.getContrasenia()
                session.setAttribute("usuarioLogueado", e);
                return "index";
            }else{
                throw new ExcepcionFrigorifico("Usuario y/o contraseña inválidos.");
            }


        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "login";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al procesar la autenticación.");
            return "login";
        }
    }
}
