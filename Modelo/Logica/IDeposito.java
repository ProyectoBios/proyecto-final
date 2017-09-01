package logica;
import datatypes.*;

public interface IDeposito {
    public boolean altaDeProducto(DTEspecificacionProducto ep);
    public boolean bajaProducto(DTEspecificacionProducto ep);
    public boolean modificarProducto(DTEspecificacionProducto ep);
    public DTEspecificacionProducto buscarProducto(int codigo);

}
