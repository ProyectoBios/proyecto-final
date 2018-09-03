package proyecto.controladores;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import proyecto.entidades.Empleado;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

public class AutorizacionInterceptor extends HandlerInterceptorAdapter {

    private static ArrayList<String> paginasGerente = new ArrayList<>(Arrays.asList("", "Bienvenida", "ABMProducto", "MantenimientoVehiculos", "AltaRack", "EstadoDeRack", "AltaLote", "MoverLote", "BajaLoteXVencimiento", "GenerarViaje", "MantenimientoEmpleados", "VerLote", "ABVehiculo", "ListadoDePedidos", "EstadoDePedido"));
    private static ArrayList<String> paginasFuncionario = new ArrayList<>(Arrays.asList("", "Bienvenida", "VerLote", "MoverLote", "EstadoDeRack","RealizarPicking", "PreparacionPedidos"));
    private static ArrayList<String> paginasOperador = new ArrayList<>(Arrays.asList("", "Bienvenida", "AltaOrdenDePedido", "EstadoDePedido"));
    private static ArrayList<String> paginasRepartidor = new ArrayList<>(Arrays.asList("", "Bienvenida", "EntregaDePedidos"));


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean autorizado = true;
        if(!request.getRequestURI().equals("/Centenario/") && !request.getRequestURI().contains("jsessionid")) {
            Empleado e = (Empleado) request.getSession().getAttribute("usuarioLogueado");
            if (e == null) {
                response.sendRedirect(request.getContextPath() + "/");
                autorizado = false;
            }else {
                String pagina = request.getRequestURI().split(request.getContextPath()+"/")[1].split("/")[0];
                switch (e.getRol()) {
                    case "gerente":
                        if (!paginasGerente.contains(pagina)) {
                            response.sendRedirect(request.getContextPath() + "/Bienvenida");
                            autorizado = false;
                        }
                        break;
                    case "funcionario":
                        if (!paginasFuncionario.contains(pagina)) {
                            response.sendRedirect(request.getContextPath() + "/Bienvenida");
                            autorizado = false;
                        }
                        break;
                    case "operador":
                        if (!paginasOperador.contains(pagina)) {
                            response.sendRedirect(request.getContextPath() + "/Bienvenida");
                            autorizado = false;
                        }
                        break;
                    case "repartidor":
                        if (!paginasRepartidor.contains(pagina)) {
                            response.sendRedirect(request.getContextPath() + "/Bienvenida");
                            autorizado = false;
                        }
                        break;
                    default:
                        break;
                }
            }

        }
        return autorizado;
    }
}


