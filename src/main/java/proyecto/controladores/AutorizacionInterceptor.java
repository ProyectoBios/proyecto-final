package proyecto.controladores;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import proyecto.entidades.Empleado;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;

public class AutorizacionInterceptor extends HandlerInterceptorAdapter {

    private static ArrayList<String> paginasGerente = new ArrayList<>(Arrays.asList("/", "/Bienvenida", "/ABMProducto", "/AltaRack", "/EstadoDeRack", "/AltaLote", "/MoverLote", "/BajaLoteXVencimiento", "/GenerarViaje", "/MantenimientoEmpleados"));
    private static ArrayList<String> paginasFuncionario = new ArrayList<>();
    private static ArrayList<String> paginasOperador = new ArrayList<>();
    private static ArrayList<String> paginasRepartidor = new ArrayList<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //if(!request.getRequestURI().equals("/Centenario")){
        if(!request.getRequestURI().equals("/Centenario/")) {
            Empleado e = (Empleado) request.getSession().getAttribute("usuarioLogueado");
            if (e == null) {
                response.sendRedirect(request.getContextPath() + "/");
                return false;
            }
            /*String uri = request.getRequestURI();
            String rol = e.getRol();

            if (rol.equals("gerente") && autorizarGerente(uri)){
                return true;
            }else if (rol == "funcionario" && autorizarFuncionario(uri)){
                return true;
            }else if (rol == "operador" && autorizarOperador(uri)){
                return true;
            }else if (rol == "repartidor" && autorizarRepartidor(uri)){
                return true;
            }else {
                //response.sendRedirect("/Bienvenida");
                return false;
            }*/

            switch(e.getRol()){
                case "gerente":
                    if(!paginasGerente.contains(request.getRequestURI().split(request.getContextPath())[1])) {
                        response.sendRedirect(request.getContextPath() + "/Bienvenida");
                        return false;
                    }
                default:
                    break;
            }

        }
        return true;
    }

    public boolean autorizarGerente(String URI){
        if (URI == "/AltaLote" || URI =="/BajaLoteXVencimiento" || URI == "/ListadoDePedidos"
                || URI == "/GenerarViaje" || URI == "/ABMEmpleado" || URI.equals("/ABMProducto") || URI == "/AltaRack"
                || URI == "/ABVehiculo" || URI == "/EstadoDeRack" || URI == "/MoverLote" || URI == "/VerLote"){
            return true;
        }else{
            return false;
        }
    }
    public boolean autorizarFuncionario(String URI){
        if (URI == "/GenerarViaje" || URI =="/EstadoDeRack" || URI == "/RealizarPicking"
                || URI == "/PreparacionPedidos" || URI == "/AltaRack"
                || URI == "/MoverLote" || URI == "/VerLote"){
            return true;
        }else{
            return false;
        }
    }
    public boolean autorizarOperador(String URI){
        if (URI == "/AltaOrdenDePedido" || URI =="/EstadoDePedido"){
            return true;
        }else{
            return false;
        }
    }
    public boolean autorizarRepartidor(String URI){
        if (URI == "/EntregaDePedidos"){
            return true;
        }else{
            return false;
        }
    }
}


