package proyecto.controladores;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import proyecto.entidades.Empleado;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutorizacionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!request.getRequestURI().equals("/")) {
            Empleado e = (Empleado) request.getSession().getAttribute("usuarioLogueado");
            if (e == null) {
                response.sendRedirect("/");
                return false;
            }

            //TODO: Si hay un usuario logueado, chequear si tiene permiso para acceder a la URI del request.
        }
        return true;
    }
}
