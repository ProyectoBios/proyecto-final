package proyecto.logica;
import proyecto.datatypes.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface IDeposito {
    //region Productos
    public int altaDeProducto(DTEspecificacionProducto ep) throws Exception;
    public void bajaProducto(DTEspecificacionProducto ep) throws Exception;
    public void modificarProducto(DTEspecificacionProducto ep) throws Exception;
    public DTEspecificacionProducto buscarProducto(int codigo) throws Exception;
    public ArrayList<DTLote> buscarStock(DTEspecificacionProducto ep) throws Exception;
    public ArrayList<DTEspecificacionProducto> listarProductos() throws Exception;
    public ArrayList<DTLote> stockProducto(DTEspecificacionProducto producto) throws Exception;
    //endregion
    //region Rack
    public DTRack buscarRack(String letra) throws Exception;
    public void altaRack(DTRack rack) throws Exception;
    public boolean esUbicacionVacia(DTUbicacion ubicacion) throws Exception;
    public void bajaRack(DTRack rack) throws Exception;
    public ArrayList<DTRack> listarRack() throws Exception;
    //endregion
    //region Lote
    public int altaLote(DTLote lote) throws Exception;
    public ArrayList<DTLote> obtenerLotesVencidos() throws Exception;
    public void bajaLote(DTLote lote) throws  Exception;
    public DTLote buscarLote(int id) throws Exception;
    //endregion

}
