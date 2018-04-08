package proyecto.logica;

public class FabricaLogica {
    public static IDeposito getControladorDeposito(){
        return LControladorDeposito.getInstancia();
    }

    public static IPedidos getControladorPedidos(){
        return LControladorPedidos.getInstancia();
    }

    public static IEmpleados getControladorEmpleados(){return LControladorEmpleados.getInstancia();}

    public static IEntregas getControladorEntregas(){return LControladorEntregas.getInstancia();}
}
