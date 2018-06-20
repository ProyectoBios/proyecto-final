package proyecto.logica;

import proyecto.entidades.OrdenPedido;
import proyecto.entidades.Repartidor;
import proyecto.entidades.Viaje;
import proyecto.persistencia.FabricaPersistencia;

import java.util.ArrayList;

public class LControladorEntregas implements IEntregas{
    private static LControladorEntregas instancia = null;

    public static LControladorEntregas getInstancia(){
        if(instancia==null){
            instancia = new LControladorEntregas();
        }
        return instancia;
    }

    private LControladorEntregas(){}

    @Override
    public void generarViaje(Viaje viaje) throws Exception {
        FabricaPersistencia.getControladorEntregas().generarViaje(viaje);
        for(OrdenPedido p : viaje.getPedidos()) {
            FabricaPersistencia.getControladorPedidos().modificarEstadoDePedido(p, "en distribucion");
        }
    }

    @Override
    public ArrayList<Viaje> listarViajesPendientesXRepartidor(Repartidor repartidor) throws Exception {
        return FabricaPersistencia.getControladorEntregas().listarViajesPendientesXRepartidor(repartidor);
    }

    @Override
    public void entregaFallidaPedido(OrdenPedido pedido, String detalleCancelacion) throws Exception {
        FabricaPersistencia.getControladorEntregas().entregaFallidaPedido(pedido, detalleCancelacion);
    }

    @Override
    public void finalizarViaje(Viaje viaje) throws Exception {
        FabricaPersistencia.getControladorEntregas().finalizarViaje(viaje);
    }

    @Override
    public void entregarPedido(OrdenPedido pedido) throws Exception {
        FabricaPersistencia.getControladorEntregas().entregarPedido(pedido);
    }
}
