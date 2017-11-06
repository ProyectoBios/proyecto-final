package proyecto.logica;
import proyecto.datatypes.*;

public interface IDeposito {
    public int altaDeProducto(DTEspecificacionProducto ep) throws Exception; //
    public void bajaProducto(DTEspecificacionProducto ep) throws Exception;
    public void modificarProducto(DTEspecificacionProducto ep) throws Exception;
    public DTEspecificacionProducto buscarProducto(int codigo) throws Exception;

}
