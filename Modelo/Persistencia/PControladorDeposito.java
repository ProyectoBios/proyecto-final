package persistencia;
import datatypes.*;

public class PControladorDeposito implements IPDeposito{

    private static PControladorDeposito instancia = null;

    public static PControladorDeposito getInstancia(){
        if(instancia == null){
            instancia = new PControladorDeposito();
        }

        return instancia;
    }

    private PControladorDeposito(){}

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
