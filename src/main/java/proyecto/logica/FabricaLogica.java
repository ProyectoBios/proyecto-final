package proyecto.logica;

public class FabricaLogica {
    public static IDeposito getControladorDeposito(){
        return LControladorDeposito.getInstancia();
    }

    public static IPedidos getControladorPedidos(){
        return LControladorPedidos.getInstancia();
    }
}
