package proyecto.controladores;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import proyecto.entidades.Empleado;
import proyecto.entidades.ExcepcionFrigorifico;
import proyecto.entidades.Repartidor;
import proyecto.logica.FabricaLogica;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class ControladorEmpleado {
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index(HttpSession session, ModelMap modelMap){
        try {
            if (session.getAttribute("usuarioLogueado") != null){
                session.removeAttribute("usuarioLogueado");
            }
        }catch (Exception ex){
            modelMap.addAttribute("mensaje", "hubo un error al cargar la página");
        }
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

    @RequestMapping(value="/MantenimientoEmpleados", method = RequestMethod.GET)
    public String getABMEmpleados(ModelMap modelMap){
        try {
            ABMEmpleadoBotonesPorDefecto(modelMap);
            modelMap.addAttribute("empleado", new Empleado());
            //modelMap.addAttribute("vencLibreta", new Date());
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        }catch (Exception ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al cargar el formulario")));
        }
        return "ABMEmpleado";
    }

    @RequestMapping(value="/MantenimientoEmpleados", method = RequestMethod.POST, params="action=Buscar")
    public String buscarEmpleado(@ModelAttribute Empleado empleado, BindingResult bindingResult, ModelMap modelMap){
        try{
            if(bindingResult.hasErrors()){
                modelMap.addAttribute(new Empleado());
                modelMap.addAttribute("vencLibreta", new Date());
                modelMap.addAttribute("mensajes", cargarErrores(bindingResult));
                return "ABMEmpleado";
            }

            Empleado e = FabricaLogica.getControladorEmpleados().buscarEmpleado(empleado.getCi());
            if(e != null){
                ABMEmpleadoBotonesEmpleadoEncontrado(modelMap);
                modelMap.addAttribute("empleado", e);
                if(e instanceof Repartidor){
                    modelMap.addAttribute("vencLibreta", ((Repartidor) e).getVencLibreta());
                }

                modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Empleado encontrado con éxito")));
            }else{
                ABMEmpleadoBotonesEmpleadoNoEncontrado(modelMap);
                modelMap.addAttribute("empleado", empleado);

                modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Empleado no encontrado")));
            }

        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        }catch (Exception ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al dar de alta el empleado")));
        }
        return "ABMEmpleado";
    }

    @RequestMapping(value="/MantenimientoEmpleados", method = RequestMethod.POST, params="action=Limpiar")
    public String limpiarABMEmpleado(@ModelAttribute Empleado empleado, BindingResult bindingResult, ModelMap modelMap){
        try {
            ABMEmpleadoBotonesPorDefecto(modelMap);
            modelMap.addAttribute("empleado", new Empleado());
            modelMap.addAttribute("vencLibreta", new Date());
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        }catch (Exception ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al cargar el formulario")));
        }
        return "ABMEmpleado";
    }

    private void ABMEmpleadoBotonesPorDefecto(ModelMap modelMap){
        modelMap.addAttribute("botonAgregar", "false");
        modelMap.addAttribute("botonModificar", "false");
        modelMap.addAttribute("botonEliminar", "false");
        modelMap.addAttribute("botonBuscar", "true");
        modelMap.addAttribute("ciSoloLectura", "false");
    }

    private void ABMEmpleadoBotonesEmpleadoEncontrado(ModelMap modelMap){
        modelMap.addAttribute("botonAgregar", "false");
        modelMap.addAttribute("botonModificar", "true");
        modelMap.addAttribute("botonEliminar", "true");
        modelMap.addAttribute("botonBuscar", "true");
        modelMap.addAttribute("ciSoloLectura", "true");
    }

    private void ABMEmpleadoBotonesEmpleadoNoEncontrado(ModelMap modelMap){
        modelMap.addAttribute("botonAgregar", "true");
        modelMap.addAttribute("botonModificar", "false");
        modelMap.addAttribute("botonEliminar", "false");
        modelMap.addAttribute("botonBuscar", "false");
        modelMap.addAttribute("ciSoloLectura", "true");
    }

    @RequestMapping(value="/MantenimientoEmpleados", method = RequestMethod.POST, params="action=Agregar")
    public String agregarEmpleado(@RequestParam(value="vencLibreta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date vencLibreta, @ModelAttribute Empleado empleado, BindingResult bindingResult, ModelMap modelMap){
        try{
            if(bindingResult.hasErrors()){
                modelMap.addAttribute(empleado);
                modelMap.addAttribute("vencLibreta", vencLibreta);
                modelMap.addAttribute("mensajes", cargarErrores(bindingResult));
                ABMEmpleadoBotonesEmpleadoNoEncontrado(modelMap);
                return "ABMEmpleado";
            }
            if(empleado.getRol().equals("repartidor")) {
                empleado = new Repartidor(empleado.getCi(), empleado.getNombre(), empleado.getContrasenia(), empleado.getFechaDeNacimiento(), empleado.getFechaContratacion(), empleado.getTelefono(), empleado.getRol(), vencLibreta);
            }

            FabricaLogica.getControladorEmpleados().altaEmpleado(empleado);

            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Alta de empleado con éxito.")));
            modelMap.addAttribute("empleado", new Empleado());
            ABMEmpleadoBotonesPorDefecto(modelMap);
        }catch (ExcepcionFrigorifico ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        }catch (Exception ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al dar de alta el empleado")));
        }

        return "ABMEmpleado";
    }

    @RequestMapping(value="/MantenimientoEmpleados", method = RequestMethod.POST, params="action=Eliminar")
    public String bajaEmpleado(@ModelAttribute Empleado empleado, BindingResult bindingResult, ModelMap modelMap){
        try{
            FabricaLogica.getControladorEmpleados().bajaEmpleado(empleado);

            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Baja de empleado con éxito.")));
            modelMap.addAttribute("empleado", new Empleado());
            ABMEmpleadoBotonesPorDefecto(modelMap);
        }catch (ExcepcionFrigorifico ex){
            ABMEmpleadoBotonesPorDefecto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        }catch (Exception ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al dar de baja el empleado")));
            ABMEmpleadoBotonesPorDefecto(modelMap);
        }

        return "ABMEmpleado";
    }

    @RequestMapping(value="/MantenimientoEmpleados", method = RequestMethod.POST, params="action=Modificar")
    public String modificarEmpleado(@RequestParam(value="vencLibreta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date vencLibreta, @ModelAttribute Empleado empleado, BindingResult bindingResult, ModelMap modelMap){
        try{
            if(bindingResult.hasErrors()){
                modelMap.addAttribute(empleado);
                modelMap.addAttribute("vencLibreta", vencLibreta);
                modelMap.addAttribute("mensajes", cargarErrores(bindingResult));
                ABMEmpleadoBotonesEmpleadoEncontrado(modelMap);
                return "ABMEmpleado";
            }

            if(empleado.getRol().equals("repartidor")) {
                empleado = new Repartidor(empleado.getCi(), empleado.getNombre(), empleado.getContrasenia(), empleado.getFechaDeNacimiento(), empleado.getFechaContratacion(), empleado.getTelefono(), empleado.getRol(), vencLibreta);
            }

            FabricaLogica.getControladorEmpleados().modificarEmpleado(empleado);

            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Empleado modificado con éxito.")));
            modelMap.addAttribute("empleado", new Empleado());
            ABMEmpleadoBotonesPorDefecto(modelMap);
        }catch (ExcepcionFrigorifico ex){
            ABMEmpleadoBotonesPorDefecto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        }catch (Exception ex){
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al modificar el empleado")));
            ABMEmpleadoBotonesPorDefecto(modelMap);
        }

        return "ABMEmpleado";
    }



    private ArrayList<String> cargarErrores(BindingResult bindingResult){
        ArrayList<String> mensajes = new ArrayList<>();
        for(Object obj : bindingResult.getAllErrors()){
            if(obj instanceof FieldError){
                mensajes.add(((FieldError)obj).getDefaultMessage().split(": ")[1]);
            }
        }
        return mensajes;
    }
}
