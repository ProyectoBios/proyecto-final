package proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import proyecto.entidades.Empleado;
import proyecto.entidades.ExcepcionFrigorifico;
import proyecto.logica.FabricaLogica;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorEmpleado {
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(value="/", method = RequestMethod.POST, params = "action=Ingresar")
    public String login(@RequestParam(value="cedula") String cedula, @RequestParam(value = "contrasenia")String contrasenia, ModelMap modelMap, HttpSession session, HttpServletResponse response){
        try{
            if(cedula.equals("") || contrasenia.equals("")){
                throw new ExcepcionFrigorifico("Credenciales inválidas, inténtelo denuevo.");
            }

            Empleado e = FabricaLogica.getControladorEmpleados().buscarEmpleado(cedula);
            if(e==null){
                throw new ExcepcionFrigorifico("Usuario y/o contraseña inválidos.");
            }else if(contrasenia.equals(e.getContrasenia())){
                session.setAttribute("usuarioLogueado", e);
                response.sendRedirect("/Bienvenida");
                return "index";
            }else{
                throw new ExcepcionFrigorifico("Usuario y/o contraseña inválidos.");
            }
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "index";
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "Ocurrió un error al procesar la autenticación.");
            return "index";
        }
    }

    @RequestMapping(value="/Bienvenida", method = RequestMethod.GET)
    public String bienvenida(HttpSession session, ModelMap modelMap){
        if(session.getAttribute("mensaje")!=null){
            modelMap.addAttribute("mensaje", session.getAttribute("mensaje"));
            session.removeAttribute("mensaje");
        }

        return "Bienvenida";
    }
}
