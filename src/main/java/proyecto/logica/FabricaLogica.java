package proyecto.logica;

public class FabricaLogica {
    public static IDeposito getControladorDeposito(){
        return ControladorDeposito.getInstancia();
    }
}
