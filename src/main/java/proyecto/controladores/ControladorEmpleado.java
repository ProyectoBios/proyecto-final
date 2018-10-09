package proyecto.controladores;

import org.jboss.logging.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import proyecto.entidades.Empleado;
import proyecto.entidades.ExcepcionFrigorifico;
import proyecto.entidades.Repartidor;
import proyecto.entidades.Vehiculo;
import proyecto.logica.FabricaLogica;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Controller
public class ControladorEmpleado {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpSession session, ModelMap modelMap) {
        try {
            if (session.getAttribute("usuarioLogueado") != null) {
                session.removeAttribute("usuarioLogueado");
            }
        } catch (Exception ex) {
            modelMap.addAttribute("mensaje", "hubo un error al cargar la página");
        }
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params = "action=Ingresar")
    public String login(@RequestParam(value = "cedula") String cedula, @RequestParam(value = "contrasenia") String contrasenia, ModelMap modelMap, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (cedula.equals("") || contrasenia.equals("")) {
                throw new ExcepcionFrigorifico("Credenciales inválidas, inténtelo denuevo.");
            }

            if(FabricaLogica.getControladorEmpleados().iniciarSesion(cedula, contrasenia)){
                session.setAttribute("usuarioLogueado", FabricaLogica.getControladorEmpleados().buscarEmpleado(cedula));
                response.sendRedirect(request.getContextPath() + "/Bienvenida");
                return "index";
            }else{
                throw new ExcepcionFrigorifico("Usuario y/o contraseña inválidos");
            }
        } catch (ExcepcionFrigorifico ex) {
            modelMap.addAttribute("mensaje", ex.getMessage());
            return "index";
        } catch (Exception ex) {
            modelMap.addAttribute("mensaje", "Ocurrió un error al procesar la autenticación.");
            return "index";
        }
    }

    @RequestMapping(value = "/Bienvenida", method = RequestMethod.GET)
    public String bienvenida(HttpSession session, ModelMap modelMap) {
        if (session.getAttribute("mensaje") != null) {
            modelMap.addAttribute("mensaje", session.getAttribute("mensaje"));
            session.removeAttribute("mensaje");
        }

        return "Bienvenida";
    }

    @RequestMapping(value = "/MantenimientoEmpleados", method = RequestMethod.GET)
    public String getABMEmpleados(ModelMap modelMap) {
        try {
            ABMBotonesPorDefecto(modelMap);
            modelMap.addAttribute("empleado", new Empleado());
            //modelMap.addAttribute("vencLibreta", new Date());
        } /*catch (ExcepcionFrigorifico ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        } */catch (Exception ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al cargar el formulario")));
        }
        return "ABMEmpleado";
    }

    @RequestMapping(value = "/MantenimientoEmpleados", method = RequestMethod.POST, params = "action=Buscar")
    public String buscarEmpleado(@ModelAttribute Empleado empleado, BindingResult bindingResult, ModelMap modelMap) {
        try {
            if (bindingResult.getFieldError("Ci")!= null){
                throw new ExcepcionFrigorifico(bindingResult.getFieldError("Ci").getDefaultMessage().split(":")[1]);
            }
            Empleado e = FabricaLogica.getControladorEmpleados().buscarEmpleado(empleado.getCi());
            if (e != null) {
                ABMBotonesEncontrado(modelMap);
                modelMap.addAttribute("empleado", e);
                if (e instanceof Repartidor) {
                    modelMap.addAttribute("vencLibreta", ((Repartidor) e).getVencLibreta());
                }

                modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Empleado encontrado con éxito")));
            } else {
                ABMBotonesNoEncontrado(modelMap);
                modelMap.addAttribute("empleado", empleado);

                modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Empleado no encontrado")));
            }

        } catch (ExcepcionFrigorifico ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            ABMBotonesPorDefecto(modelMap);
        } catch (Exception ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al dar de alta el empleado")));
            ABMBotonesPorDefecto(modelMap);
        }
        return "ABMEmpleado";
    }

    @RequestMapping(value = "/MantenimientoEmpleados", method = RequestMethod.POST, params = "action=Limpiar")
    public String limpiarABMEmpleado(@ModelAttribute Empleado empleado, BindingResult bindingResult, ModelMap modelMap) {
        try {
            ABMBotonesPorDefecto(modelMap);
            modelMap.addAttribute("empleado", new Empleado());
            modelMap.addAttribute("vencLibreta", new Date());
        } /*catch (ExcepcionFrigorifico ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        }*/ catch (Exception ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al cargar el formulario")));
        }
        return "ABMEmpleado";
    }

    @RequestMapping(value = "/MantenimientoEmpleados", method = RequestMethod.POST, params = "action=Agregar")
    public String agregarEmpleado(@RequestParam(value="confirmarPass") String confirmarPass, @RequestParam(value = "vencLibreta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date vencLibreta, @ModelAttribute Empleado empleado, BindingResult bindingResult, ModelMap modelMap) {
        try {
            if (bindingResult.hasErrors()) {
                modelMap.addAttribute(empleado);
                modelMap.addAttribute("vencLibreta", vencLibreta);
                modelMap.addAttribute("mensajes", cargarErrores(bindingResult));
                ABMBotonesNoEncontrado(modelMap);
                return "ABMEmpleado";
            }

            if(!confirmarPass.equals(empleado.getContrasenia())){
                modelMap.addAttribute(empleado);
                modelMap.addAttribute("vencLibreta", vencLibreta);
                throw new ExcepcionFrigorifico("Las contraseñas no coinciden.");
            }

            /*if (empleado.getCi().isEmpty()){ //Debería ir solo en la entidad en sí.
                modelMap.addAttribute(empleado);
                throw new ExcepcionFrigorifico("La cédula no puede quedar vacía");
            }*/

            if (empleado.getRol().equals("repartidor")) {
                empleado = new Repartidor(empleado.getCi(), empleado.getNombre(), empleado.getContrasenia(), empleado.getFechaDeNacimiento(), empleado.getFechaContratacion(), empleado.getTelefono(), empleado.getRol(), vencLibreta);
            }

            FabricaLogica.getControladorEmpleados().altaEmpleado(empleado);

            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Alta de empleado con éxito.")));
            modelMap.addAttribute("empleado", new Empleado());
            ABMBotonesPorDefecto(modelMap);
        } catch (ExcepcionFrigorifico ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            ABMBotonesNoEncontrado(modelMap);
        } catch (Exception ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al dar de alta el empleado")));
            ABMBotonesNoEncontrado(modelMap);
        }

        return "ABMEmpleado";
    }

    @RequestMapping(value = "/MantenimientoEmpleados", method = RequestMethod.POST, params = "action=Eliminar")
    public String bajaEmpleado(@ModelAttribute Empleado empleado, BindingResult bindingResult, ModelMap modelMap) {
        try {
            FabricaLogica.getControladorEmpleados().bajaEmpleado(empleado);

            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Baja de empleado con éxito.")));
            modelMap.addAttribute("empleado", new Empleado());
            ABMBotonesPorDefecto(modelMap);
        } catch (ExcepcionFrigorifico ex) {
            ABMBotonesEncontrado(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        } catch (Exception ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al dar de baja el empleado")));
            ABMBotonesEncontrado(modelMap);
        }

        return "ABMEmpleado";
    }

    @RequestMapping(value = "/MantenimientoEmpleados", method = RequestMethod.POST, params = "action=Listar")
    public String listarEmpleados(ModelMap modelMap) {
        try {
            modelMap.addAttribute("empleados", FabricaLogica.getControladorEmpleados().listarEmpleados());
            modelMap.addAttribute("tablaListado", "true");
            modelMap.addAttribute("empleado", new Empleado());
            ABMBotonesPorDefecto(modelMap);
        } catch (ExcepcionFrigorifico ex) {
            ABMBotonesPorDefecto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        } catch (Exception ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al listar los empleados.")));
            ABMBotonesPorDefecto(modelMap);
        }

        return "ABMEmpleado";
    }

    @RequestMapping(value = "/MantenimientoEmpleados/{cedula}", method = RequestMethod.GET)
    public String buscarEmpleadoXCedula(@PathVariable String cedula, ModelMap modelMap) {
        try {
            Empleado e = FabricaLogica.getControladorEmpleados().buscarEmpleado(cedula);
            modelMap.addAttribute("empleado", e);
            if(e instanceof Repartidor){
                modelMap.addAttribute("vencLibreta", ((Repartidor) e).getVencLibreta());
            }
            ABMBotonesEncontrado(modelMap);
        } catch (ExcepcionFrigorifico ex) {
            ABMBotonesPorDefecto(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        } catch (Exception ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al dar de baja el empleado")));
            ABMBotonesPorDefecto(modelMap);
        }

        return "ABMEmpleado";
    }

    @RequestMapping(value = "/MantenimientoEmpleados", method = RequestMethod.POST, params = "action=Modificar")
    public String modificarEmpleado(@RequestParam(value="confirmarPass") String confirmarPass, @RequestParam(value = "vencLibreta") @DateTimeFormat(pattern = "yyyy-MM-dd") Date vencLibreta, @ModelAttribute Empleado empleado, BindingResult bindingResult, ModelMap modelMap) {
        try {
            if (bindingResult.hasErrors()) {
                modelMap.addAttribute(empleado);
                modelMap.addAttribute("vencLibreta", vencLibreta);
                modelMap.addAttribute("mensajes", cargarErrores(bindingResult));
                ABMBotonesEncontrado(modelMap);
                return "ABMEmpleado";
            }

            if(!confirmarPass.equals(empleado.getContrasenia())){
                modelMap.addAttribute(empleado);
                modelMap.addAttribute("vencLibreta", vencLibreta);
                throw new ExcepcionFrigorifico("Las contraseñas no coinciden.");
            }

            if (empleado.getRol().equals("repartidor")) {
                empleado = new Repartidor(empleado.getCi(), empleado.getNombre(), empleado.getContrasenia(), empleado.getFechaDeNacimiento(), empleado.getFechaContratacion(), empleado.getTelefono(), empleado.getRol(), vencLibreta);
            }

            FabricaLogica.getControladorEmpleados().modificarEmpleado(empleado);

            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Empleado modificado con éxito.")));
            modelMap.addAttribute("empleado", new Empleado());
            ABMBotonesPorDefecto(modelMap);
        } catch (ExcepcionFrigorifico ex) {
            ABMBotonesEncontrado(modelMap);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        } catch (Exception ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al modificar el empleado")));
            ABMBotonesEncontrado(modelMap);
        }

        return "ABMEmpleado";
    }

    @RequestMapping(value = "/MantenimientoVehiculos", method = RequestMethod.GET)
    public String getABVehiculos(ModelMap modelMap) {
        try {
            ABMBotonesPorDefecto(modelMap);
            modelMap.addAttribute("vehiculo", new Vehiculo());

        } catch (ExcepcionFrigorifico ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        } catch (Exception ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al cargar el formulario")));
        }
        return "ABVehiculo";
    }

    @RequestMapping(value = "/MantenimientoVehiculos", method = RequestMethod.POST, params = "action=Buscar")
    public String buscarVehiculo(@ModelAttribute Vehiculo vehiculo, BindingResult bindingResult, ModelMap modelMap){
        try {
            if (bindingResult.getFieldError("Matricula")!= null){
                throw new ExcepcionFrigorifico(bindingResult.getFieldError("Matricula").getDefaultMessage().split(":")[1]);
            }

            Vehiculo v = FabricaLogica.getControladorEmpleados().buscarVehiculo(vehiculo.getMatricula());
            if (v != null) {
                ABMBotonesEncontrado(modelMap);
                modelMap.addAttribute("vehiculo", v);
                modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Vehículo encontrado con éxito")));
            } else {
                ABMBotonesNoEncontrado(modelMap);
                modelMap.addAttribute("vehiculo", vehiculo);
                modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Vehículo no encontrado")));
            }

        } catch (ExcepcionFrigorifico ex){
            ABMBotonesNoEncontrado(modelMap);
            modelMap.addAttribute("vehiculo", vehiculo);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        } catch (Exception ex){
            modelMap.addAttribute("vehiculo", vehiculo);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("¡ERROR! Ocurrió un error al buscar el vehículo")));
        }
        return "ABVehiculo";
    }

    @RequestMapping(value = "/MantenimientoVehiculos", method = RequestMethod.POST, params = "action=Limpiar")
    public String limpiarVehiculo(ModelMap modelMap){
        try {
            ABMBotonesPorDefecto(modelMap);
            modelMap.addAttribute("vehiculo", new Vehiculo());

        } catch (ExcepcionFrigorifico ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
        } catch (Exception ex) {
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al cargar el formulario")));
        }

        return "ABVehiculo";
    }

    @RequestMapping(value = "/MantenimientoVehiculos", method = RequestMethod.POST, params = "action=Agregar")
    public String agregarVehiculo(@ModelAttribute Vehiculo vehiculo, BindingResult bindingResult, ModelMap modelMap) {
        try {
            if (bindingResult.hasErrors()) {
                modelMap.addAttribute("vehiculo", vehiculo);
                modelMap.addAttribute("mensajes", cargarErrores(bindingResult));
                ABMBotonesNoEncontrado(modelMap);
                return "ABVehiculo";
            }
            FabricaLogica.getControladorEmpleados().altaVehiculo(vehiculo);

            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Alta de vehículo con éxito.")));
            modelMap.addAttribute("vehiculo", new Vehiculo());
            ABMBotonesPorDefecto(modelMap);

        } catch (ExcepcionFrigorifico ex) {
            modelMap.addAttribute("vehiculo", vehiculo);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            ABMBotonesNoEncontrado(modelMap);
        } catch (Exception ex) {
            modelMap.addAttribute("vehiculo", vehiculo);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al agregar el vehículo")));
            ABMBotonesNoEncontrado(modelMap);
        }
        return "ABVehiculo";
    }

    @RequestMapping(value = "/MantenimientoVehiculos", method = RequestMethod.POST, params = "action=Eliminar")
    public String bajaVehiculo(@ModelAttribute Vehiculo vehiculo, BindingResult bindingResult, ModelMap modelMap) {
        try {
            FabricaLogica.getControladorEmpleados().bajaVehiculo(vehiculo);

            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("Baja de vehículo con éxito.")));
            modelMap.addAttribute("vehiculo", new Vehiculo());
            ABMBotonesPorDefecto(modelMap);
        } catch (ExcepcionFrigorifico ex) {
            modelMap.addAttribute("vehiculo", vehiculo);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList(ex.getMessage())));
            ABMBotonesPorDefecto(modelMap);
        } catch (Exception ex) {
            modelMap.addAttribute("vehiculo", vehiculo);
            modelMap.addAttribute("mensajes", new ArrayList<String>(Arrays.asList("ERROR! Ocurrió un error al dar de baja el vehículo")));
            ABMBotonesPorDefecto(modelMap);
        }
        return "ABVehiculo";
    }

    private void ABMBotonesPorDefecto(ModelMap modelMap) {
        modelMap.addAttribute("botonAgregar", "false");
        modelMap.addAttribute("botonModificar", "false");
        modelMap.addAttribute("botonEliminar", "false");
        modelMap.addAttribute("botonBuscar", "true");
        modelMap.addAttribute("ciSoloLectura", "false");
        modelMap.addAttribute("matriculaSoloLectura", "false");
    }

    private void ABMBotonesEncontrado(ModelMap modelMap) {
        modelMap.addAttribute("botonAgregar", "false");
        modelMap.addAttribute("botonModificar", "true");
        modelMap.addAttribute("botonEliminar", "true");
        modelMap.addAttribute("botonBuscar", "true");
        modelMap.addAttribute("ciSoloLectura", "true");
        modelMap.addAttribute("matriculaSoloLectura", "true");
        modelMap.addAttribute("btnNoAllowed", "not-allowed");
    }

    private void ABMBotonesNoEncontrado(ModelMap modelMap) {
        modelMap.addAttribute("botonAgregar", "true");
        modelMap.addAttribute("botonModificar", "false");
        modelMap.addAttribute("botonEliminar", "false");
        modelMap.addAttribute("botonBuscar", "false");
        modelMap.addAttribute("ciSoloLectura", "true");
        modelMap.addAttribute("matriculaSoloLectura", "true");
        modelMap.addAttribute("btnNoAllowed", "not-allowed");
    }

    private ArrayList<String> cargarErrores(BindingResult bindingResult) {
        ArrayList<String> mensajes = new ArrayList<>();
        for (Object obj : bindingResult.getAllErrors()) {
            if (obj instanceof FieldError) {
                if(((FieldError) obj).getCode().equals("methodInvocation")) {
                    mensajes.add(((FieldError) obj).getDefaultMessage().split(": ")[1]);
                }else{
                    mensajes.add("El campo " + ((FieldError) obj).getField() + " no puede quedar vacio");
                }
            }
        }
        return mensajes;
    }

}
