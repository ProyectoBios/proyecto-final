package proyecto.controladores;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import proyecto.entidades.Empleado;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

public class AutorizacionInterceptor extends HandlerInterceptorAdapter {

    private static ArrayList<String> paginasGerente = new ArrayList<>(Arrays.asList("/", "/Bienvenida", "/ABMProducto", "/AltaRack", "/EstadoDeRack", "/AltaLote", "/MoverLote", "/BajaLoteXVencimiento", "/GenerarViaje", "/MantenimientoEmpleados", "/VerLote", "/ABVehiculo"));
    private static ArrayList<String> paginasFuncionario = new ArrayList<>(Arrays.asList("/", "/Bienvenida", "/VerLote", "/MoverLote", "/EstadoDeRack", "/GenerarViaje", "/RealizarPicking", "/PreparacionPedidos", "/ListadoDePedidos"));
    private static ArrayList<String> paginasOperador = new ArrayList<>(Arrays.asList("/", "/Bienvenida", "/AltaOrdenDePedido", "/EstadoDePedido"));
    private static ArrayList<String> paginasRepartidor = new ArrayList<>(Arrays.asList("/", "/Bienvenida", "/EntregaDePedidos"));


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!request.getRequestURI().equals("/Centenario/")) {
            Empleado e = (Empleado) request.getSession().getAttribute("usuarioLogueado");
            if (e == null) {
                response.sendRedirect(request.getContextPath() + "/");
                return false;
            }

            switch(e.getRol()){
                case "gerente":
                    if(!paginasGerente.contains(request.getRequestURI().split(request.getContextPath())[1])) {
                        response.sendRedirect(request.getContextPath() + "/Bienvenida");
                        return false;
                    }
                case "funcionario":
                    if(!paginasFuncionario.contains(request.getRequestURI().split(request.getContextPath())[1])) {
                        response.sendRedirect(request.getContextPath() + "/Bienvenida");
                        return false;
                    }
                case "operador":
                    if(!paginasOperador.contains(request.getRequestURI().split(request.getContextPath())[1])) {
                        response.sendRedirect(request.getContextPath() + "/Bienvenida");
                        return false;
                    }
                case "repartidor":
                    if(!paginasRepartidor.contains(request.getRequestURI().split(request.getContextPath())[1])) {
                        response.sendRedirect(request.getContextPath() + "/Bienvenida");
                        return false;
                    }
                default:
                    break;
            }

        }
        return true;
    }
}


