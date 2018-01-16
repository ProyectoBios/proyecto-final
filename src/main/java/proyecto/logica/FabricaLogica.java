package proyecto.logica;

import javax.naming.ldap.Control;

public class FabricaLogica {
    public static IDeposito getControladorDeposito(){
        return ControladorDeposito.getInstancia();
    }

    public static IPedidos getControladorPedidos(){
        return ControladorPedidos.getInstancia();
    }
}
