package proyecto.controladores;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import proyecto.entidades.Empleado;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

public class AutorizacionInterceptor extends HandlerInterceptorAdapter {

    private static ArrayList<String> paginasGerente = new ArrayList<>(Arrays.asList("/", "/Bienvenida", "/AltaRack", "/EstadoDeRack", "/AltaLote", "/MoverLote", "/BajaLoteXVencimiento", "/GenerarViaje", "/MantenimientoEmpleados"));
    private static ArrayList<String> paginasFuncionario = new ArrayList<>();
    private static ArrayList<String> paginasOperador = new ArrayList<>();
    private static ArrayList<String> paginasRepartidor = new ArrayList<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //if(!request.getRequestURI().equals("/Centenario")){
        if(!request.getRequestURI().equals("/")) {
            Empleado e = (Empleado) request.getSession().getAttribute("usuarioLogueado");
            if (e == null) {
                response.sendRedirect("/");
                return false;
            }

            switch(e.getRol()){
                case "gerente":
                    if(!paginasGerente.contains(request.getRequestURI()))
                    break;
                default:
                    break;
            }

            //TODO: Si hay un usuario logueado, chequear si tiene permiso para acceder a la URI del request.
        }
        return true;
    }
}
