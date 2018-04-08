package proyecto.persistencia;


public class FabricaPersistencia {
    public static IPDeposito getControladorDeposito(){
        return PControladorDeposito.getInstancia();

    }

    public  static IPPedidos getControladorPedidos(){
        return PControladorPedidos.getInstancia();
    }

    public static IPEmpleados getControladorEmpleados(){
        return PControladorEmpleados.getInstancia();
    }

    public static IPEntregas getControladorEntregas() {return PControladorEntregas.getInstacia();}
}
