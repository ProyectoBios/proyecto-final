package proyecto.persistencia;

import proyecto.datatypes.*;

public interface IPDeposito {
    public int altaDeProducto(DTEspecificacionProducto ep)throws Exception;
    public void bajaProducto(DTEspecificacionProducto ep)throws Exception;
    public void modificarProducto(DTEspecificacionProducto ep)throws Exception;
    public DTEspecificacionProducto buscarProducto(int codigo)throws Exception;
}