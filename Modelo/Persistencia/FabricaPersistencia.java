package persistencia;

import datatypes.*;
import logica.IDeposito;

public class FabricaPersistencia {
    public static IPDeposito getControladorDeposito(){
        return PControladorDeposito.getInstancia();

}}
