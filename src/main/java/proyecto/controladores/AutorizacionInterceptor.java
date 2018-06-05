package proyecto.controladores;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import proyecto.entidades.Empleado;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutorizacionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!request.getRequestURI().contains("/login")) {
            Empleado e = (Empleado) request.getSession().getAttribute("UsuarioLogueado");
            if (e == null) {
                response.sendRedirect("/login");
                return false;
            }
        }
        return true;
    }
}
