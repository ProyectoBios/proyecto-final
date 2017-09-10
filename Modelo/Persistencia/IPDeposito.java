package persistencia;

import datatypes.*;

public interface IPDeposito {
    public boolean altaDeProducto(DTEspecificacionProducto ep);
    public boolean bajaProducto(DTEspecificacionProducto ep);
    public boolean modificarProducto(DTEspecificacionProducto ep);
    public DTEspecificacionProducto buscarProducto(int codigo);
}
