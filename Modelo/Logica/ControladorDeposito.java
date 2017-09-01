package logica;
import datatypes.*;

class ControladorDeposito implements  IDeposito {
    private static ControladorDeposito instancia = null;

    public static ControladorDeposito getInstancia(){
        if(instancia == null){
            instancia = new ControladorDeposito();
        }

        return instancia;
    }

    private ControladorDeposito(){}

    @Override
    public boolean altaDeProducto(DTEspecificacionProducto ep) {
        return false;
    }

    @Override
    public boolean bajaProducto(DTEspecificacionProducto ep) {
        return false;
    }

    @Override
    public boolean modificarProducto(DTEspecificacionProducto ep) {
        return false;
    }

    @Override
    public DTEspecificacionProducto buscarProducto(int codigo) {
        return null;
    }
}
