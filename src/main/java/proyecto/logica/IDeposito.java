package proyecto.logica;
import proyecto.entidades.*;
import proyecto.entidades.EspecificacionProducto;

import java.util.ArrayList;

public interface IDeposito {
    //region Productos
    public int altaDeProducto(EspecificacionProducto ep) throws Exception;
    public void bajaProducto(EspecificacionProducto ep) throws Exception;
    public void modificarProducto(EspecificacionProducto ep) throws Exception;
    public EspecificacionProducto buscarProducto(int codigo, boolean soloActivos) throws Exception;
    public ArrayList<Lote> buscarStock(EspecificacionProducto ep) throws Exception;
    public ArrayList<EspecificacionProducto> listarProductos(boolean soloActivos) throws Exception;
    public ArrayList<EspecificacionProducto> buscarProductosXNombre(String nombre) throws Exception;
    //endregion
    //region Rack
    public Rack buscarRack(String letra) throws Exception;
    public void altaRack(Rack rack) throws Exception;
    public boolean esUbicacionVacia(Ubicacion ubicacion) throws Exception;
    public void bajaRack(Rack rack) throws Exception;
    public ArrayList<Rack> listarRacks() throws Exception;
    ArrayList<ArrayList<Lote>> obtenerRack(Rack rack) throws Exception;
    //endregion
    //region Lote
    public int altaLote(Lote lote) throws Exception;
    public ArrayList<Lote> obtenerLotesVencidos() throws Exception;
    public void bajaLote(Lote lote) throws  Exception;
    public Lote buscarLote(int id) throws Exception;
    void moverLote(int idLote, String ubicacion) throws Exception;
    public void deshacerBajaLogicaLote(Lote lote) throws Exception;
    public void actualizarStock(Lote lote, int cant) throws Exception;
    byte[] generarCodigoQRLote(int codigo) throws Exception;
    //endregion

}
