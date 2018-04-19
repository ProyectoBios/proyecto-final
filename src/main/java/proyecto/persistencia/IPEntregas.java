package proyecto.persistencia;

import proyecto.entidades.Repartidor;
import proyecto.entidades.Viaje;

import java.util.ArrayList;

public interface IPEntregas {
    void generarViaje(Viaje viaje) throws Exception;
    ArrayList<Viaje> listarViajesPendientes (Repartidor repartidor) throws Exception;
}
