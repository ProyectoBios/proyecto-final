package proyecto.logica;

import proyecto.entidades.OrdenPedido;
import proyecto.entidades.Repartidor;
import proyecto.entidades.Viaje;

import java.util.ArrayList;

public interface IEntregas {
    void generarViaje(Viaje viaje) throws Exception;
    ArrayList<Viaje> listarViajesPendientesXRepartidor(Repartidor repartidor) throws Exception;
    void entregaFallidaPedido(OrdenPedido pedido, String detalleCancelacion) throws Exception;
    void finalizarViaje(Viaje viaje) throws Exception;
    void entregarPedido(OrdenPedido pedido) throws Exception;
}
