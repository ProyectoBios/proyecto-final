package proyecto.persistencia;

import proyecto.datatypes.*;

public interface IPDeposito {
    //region Productos
    public int altaDeProducto(DTEspecificacionProducto ep)throws Exception;
    public void bajaProducto(DTEspecificacionProducto ep)throws Exception;
    public void modificarProducto(DTEspecificacionProducto ep)throws Exception;
    public DTEspecificacionProducto buscarProducto(int codigo)throws Exception;
    //endregion
    //region Rack
    public DTRack buscarRack(String letra) throws Exception;
    public void altaRack(DTRack rack) throws Exception;
    public void bajaRack(DTRack rack) throws Exception;
    //endregion
    //region Lote
    public int altaLote(DTLote lote) throws Exception;
    //endregion
}
