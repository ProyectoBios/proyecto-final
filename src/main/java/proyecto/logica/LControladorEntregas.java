package proyecto.logica;

import proyecto.entidades.OrdenPedido;
import proyecto.entidades.Viaje;
import proyecto.persistencia.FabricaPersistencia;

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
}