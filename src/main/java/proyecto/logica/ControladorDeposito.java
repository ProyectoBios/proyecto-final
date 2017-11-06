package proyecto.logica;
import proyecto.datatypes.*;

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
    public int altaDeProducto(DTEspecificacionProducto ep) throws Exception{
        return 1;
    }

    @Override
    public void bajaProducto(DTEspecificacionProducto ep) throws Exception{

    }

    @Override
    public void modificarProducto(DTEspecificacionProducto ep) throws Exception{

    }

    @Override
    public DTEspecificacionProducto buscarProducto(int codigo) throws Exception{
        return null;
    }
}
