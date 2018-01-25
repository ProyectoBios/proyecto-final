package proyecto.persistencia;
import proyecto.datatypes.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

class PControladorDeposito implements IPDeposito{

    private static PControladorDeposito instancia = null;

    public static PControladorDeposito getInstancia(){
        if(instancia == null){
            instancia = new PControladorDeposito();
        }

        return instancia;
    }

    private PControladorDeposito(){}

    //region Productos
    @Override
    public int altaDeProducto(DTEspecificacionProducto ep) throws Exception{

        try(Connection con = Conexion.AbrirConexion();
            CallableStatement consulta = con.prepareCall("{ CALL AltaEspProducto(?,?,?,?,?,?) }")) {
            consulta.setString(1, ep.getNombre());
            consulta.setInt(2, ep.getMinStock());
            consulta.setInt(3, ep.getStockCritico());
            consulta.setInt(4, ep.getMaxStock());
            consulta.setDouble(5, ep.getHistoricoPrecios().get(0).getPrecio());
            consulta.registerOutParameter(6, Types.INTEGER);

            int filasAfectadas = consulta.executeUpdate();

            if (filasAfectadas != 1) {
                throw new ExcepcionFrigorifico("Ocurrió un error al agregar el Producto");
            }

            return consulta.getInt(6);

        }catch (SQLException ex) {
                throw ex;

            }
        catch (Exception ex){
            throw ex;
        }

    }

    @Override
    public void bajaProducto(DTEspecificacionProducto ep) throws Exception{

        try (Connection con = Conexion.AbrirConexion();
             CallableStatement consulta = con.prepareCall("{ CALL BajaEspProducto(?) }")){

            consulta.setInt(1, ep.getCodigo());

            int filasAfectadas = consulta.executeUpdate();

            if (filasAfectadas < 1) {
                throw new Exception("No se pudo eliminar el Producto");
            }

        } catch (SQLException ex) {
            throw ex;

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void modificarProducto(DTEspecificacionProducto ep) throws Exception{
        try (Connection con = Conexion.AbrirConexion();
             CallableStatement consulta = con.prepareCall("{ CALL ModificarProducto(?,?,?,?,?,?) }")){

            consulta.setInt(1, ep.getCodigo());
            consulta.setString(2, ep.getNombre());
            consulta.setInt(3, ep.getMinStock());
            consulta.setInt(4, ep.getStockCritico());
            consulta.setInt(5, ep.getMaxStock());
            consulta.setDouble(6, ep.getHistoricoPrecios().get(0).getPrecio());

            int filasAfectadas = consulta.executeUpdate();

            /*if (filasAfectadas < 1) {
                throw new Exception("No se pudo modificar el Producto");
            }*/

        } catch (SQLException ex) {
            throw ex;

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public DTEspecificacionProducto buscarProducto(int codigo) throws Exception{
        DTEspecificacionProducto productoEncontrado = null;

        try (Connection con = Conexion.AbrirConexion();
             PreparedStatement consulta = con.prepareStatement("SELECT * FROM EspecificacionProducto WHERE ID = ? AND Eliminado = 0;");){

            consulta.setInt(1, codigo);

            ResultSet resultadoConsulta = consulta.executeQuery();

            if (resultadoConsulta.next()) {
                productoEncontrado = new DTEspecificacionProducto(resultadoConsulta.getInt("ID"), resultadoConsulta.getString("nombre"), resultadoConsulta.getInt("minStock"), resultadoConsulta.getInt("stockCritico"), resultadoConsulta.getInt("maxStock"), buscarHistorico(codigo));
            }

        } catch (SQLException ex) {
            throw ex;

        } catch (Exception ex) {
            throw ex;
        }
        return productoEncontrado;
    }

    //devuelve el histórico de precio del producto especificado
    //ordenados por fecha inicial decreciente, el precio actual siempre está en la primera posicion.
    public ArrayList<DTPrecio> buscarHistorico(int codigoProducto) throws Exception{
        ArrayList<DTPrecio> historico = new ArrayList<DTPrecio>();

        try (Connection con = Conexion.AbrirConexion();
             PreparedStatement consulta = con.prepareStatement("SELECT * FROM PrecioProducto WHERE IDproducto = ? ORDER BY fechaIni DESC;");){

            consulta.setInt(1, codigoProducto);

            ResultSet resultadoConsulta = consulta.executeQuery();
            DTPrecio precio = null;
            while (resultadoConsulta.next()) {
                precio = new DTPrecio(resultadoConsulta.getDouble("precio"), resultadoConsulta.getTimestamp("fechaIni"), resultadoConsulta.getTimestamp("fechaFin"));
                historico.add(precio);
            }

            return historico;

        } catch (SQLException ex) {
            throw ex;

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public ArrayList<DTLote> buscarStock(DTEspecificacionProducto ep) throws Exception {
        ArrayList<DTLote> stock = new ArrayList<DTLote>();

        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Lote WHERE IDProducto = ? AND fechaVencimiento > NOW() ORDER BY fechaVencimiento ASC")){

            consulta.setInt(1, ep.getCodigo());

            ResultSet resultadoConsulta = consulta.executeQuery();
            DTLote lote = null;
            while(resultadoConsulta.next()){
                lote = new DTLote(resultadoConsulta.getInt("idLote"), resultadoConsulta.getTimestamp("fechaIngreso"), resultadoConsulta.getTimestamp("fechaVencimiento"), resultadoConsulta.getInt("cantUnidades"), ep, new DTUbicacion(resultadoConsulta.getInt("fila"), resultadoConsulta.getInt("columna"), buscarRack(resultadoConsulta.getString("letraRack"))));
                stock.add(lote);
            }

            return stock;

        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<DTEspecificacionProducto> listarProductos() throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM EspecificacionProducto")){

            ResultSet resultadoConsulta = consulta.executeQuery();
            ArrayList<DTEspecificacionProducto> productos = new ArrayList<DTEspecificacionProducto>();
            DTEspecificacionProducto prod = null;
            while(resultadoConsulta.next()){
                prod = new DTEspecificacionProducto(resultadoConsulta.getInt("ID"), resultadoConsulta.getString("nombre"), resultadoConsulta.getInt("minStock"), resultadoConsulta.getInt("stockCritico"), resultadoConsulta.getInt("maxStock"), buscarHistorico(resultadoConsulta.getInt("ID")));
                productos.add(prod);
            }
            return productos;
        }catch(Exception ex){
            throw ex;
        }
    }

    //endregion

    //region Rack

    @Override
    public DTRack buscarRack(String letra) throws Exception {
        DTRack rackEncontrado = null;

        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Rack WHERE letra = ?;");){

            consulta.setString(1, letra);

            ResultSet resultadoConsulta = consulta.executeQuery();

            if (resultadoConsulta.next()){
                rackEncontrado = new DTRack(resultadoConsulta.getString("letra"), resultadoConsulta.getInt("dimAlto"), resultadoConsulta.getInt("dimAncho"));
            }
        }catch(Exception ex){
            throw ex;
        }
        return rackEncontrado;
    }

    @Override
    public void altaRack(DTRack rack) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            CallableStatement consulta = con.prepareCall("{ CALL AltaRack(?,?,?) }");){
            consulta.setString(1, rack.getLetra());
            consulta.setInt(2, rack.getDimAlto());
            consulta.setInt(3, rack.getDimAncho());

            int filasAfectadas = consulta.executeUpdate();

            if(filasAfectadas != 1){
                throw new ExcepcionFrigorifico("¡ERROR! Ocurrió un error al dar de alta el rack");
            }
        }catch(Exception ex){
            throw ex;
        }

    }

    @Override
    public void bajaRack(DTRack rack) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            CallableStatement consulta = con.prepareCall("{ CALL BajaRack(?) }");){

            consulta.setString(1, rack.getLetra());

            int filasAfectadas = consulta.executeUpdate();

            if(filasAfectadas != 1){
                throw new ExcepcionFrigorifico("¡ERROR! Ocurrio un error al dar de baja el rack");
            }

        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<DTRack> listarRacks() throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Rack")){

            ResultSet resultadoConsulta = consulta.executeQuery();

            ArrayList<DTRack> racks = new ArrayList<DTRack>();
            DTRack rack = null;
            while(resultadoConsulta.next()){
                rack = new DTRack(resultadoConsulta.getString("letra"), resultadoConsulta.getInt("dimAlto"), resultadoConsulta.getInt("dimAncho"));
                racks.add(rack);
            }
            return racks;
        }catch (Exception ex){
            throw ex;
        }
    }

    //endregion

    //region Lote

    @Override
    public int altaLote(DTLote lote) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            CallableStatement consulta = con.prepareCall("{ CALL AltaLote(?, ?, ?, ?, ?, ?, ?)}")){

            consulta.setDate(1, new java.sql.Date(lote.getFechaVencimiento().getTime()));
            consulta.setInt(2, lote.getCantUnidades());
            consulta.setInt(3, lote.getProducto().getCodigo());
            consulta.setString(4, lote.getUbicacion().getRack().getLetra());
            consulta.setInt(5, lote.getUbicacion().getFila());
            consulta.setInt(6, lote.getUbicacion().getColumna());
            consulta.registerOutParameter(7, Types.INTEGER);

            int filasAfectadas = consulta.executeUpdate();

            if(filasAfectadas != 1){
                throw new ExcepcionFrigorifico("¡ERROR! Ocurrió un error al dar de alta el lote.");
            }

            return consulta.getInt(7);

        }catch(Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<DTLote> obtenerLotesVencidos() throws Exception {
        ArrayList<DTLote> lotes = new ArrayList<DTLote>();

        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Lote WHERE fechaVencimiento < NOW()")){

            ResultSet resultadoConsulta = consulta.executeQuery();
            DTLote lote = null;
            while(resultadoConsulta.next()){
                lote = new DTLote(resultadoConsulta.getInt("idLote"), resultadoConsulta.getTimestamp("fechaIngreso"), resultadoConsulta.getTimestamp("fechaVencimiento"), resultadoConsulta.getInt("cantUnidades"), buscarProducto(resultadoConsulta.getInt("IDProducto")), new DTUbicacion(resultadoConsulta.getInt("fila"), resultadoConsulta.getInt("columna"), buscarRack(resultadoConsulta.getString("letraRack"))));
                lotes.add(lote);
            }

            return lotes;
        }catch(Exception ex){
            throw ex;
        }
    }

    @Override
    public void bajaLote(DTLote lote) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            CallableStatement consulta = con.prepareCall("{ CALL BajaLote(?) }")){

            consulta.setInt(1, lote.getId());

            int filasAfectadas = consulta.executeUpdate();
            if(filasAfectadas!=1){
                throw new ExcepcionFrigorifico("¡ERROR! No se pudo dar de baja el lote");
            }
        }catch(Exception ex){
            throw ex;
        }
    }

    @Override
    public DTLote buscarLote(int id) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Lote WHERE idLote = ?")){

            consulta.setInt(1, id);

            ResultSet resultadoConsulta = consulta.executeQuery();
            DTLote lote = null;
            if(resultadoConsulta.next()){
                lote = new DTLote(resultadoConsulta.getInt("idLote"), resultadoConsulta.getTimestamp("fechaIngreso"), resultadoConsulta.getTimestamp("fechaVencimiento"), resultadoConsulta.getInt("cantUnidades"), buscarProducto(resultadoConsulta.getInt("IDProducto")), new DTUbicacion(resultadoConsulta.getInt("fila"), resultadoConsulta.getInt("columna"), buscarRack(resultadoConsulta.getString("letraRack"))));
            }
            return lote;
        }catch(Exception ex){
            throw ex;
        }
    }

    @Override
    public DTLote obtenerUbicacion(DTUbicacion ubicacion) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Lote WHERE letraRack = ? AND fila = ? AND columna = ?")){

            consulta.setString(1, ubicacion.getRack().getLetra());
            consulta.setInt(2, ubicacion.getFila());
            consulta.setInt(3, ubicacion.getColumna());

            ResultSet resultadoConsulta = consulta.executeQuery();
            DTLote lote = null;
            if(resultadoConsulta.next()){
                lote = new DTLote(resultadoConsulta.getInt("idLote"), resultadoConsulta.getTimestamp("fechaIngreso"), resultadoConsulta.getTimestamp("fechaVencimiento"), resultadoConsulta.getInt("cantUnidades"), buscarProducto(resultadoConsulta.getInt("IDProducto")), new DTUbicacion(resultadoConsulta.getInt("fila"), resultadoConsulta.getInt("columna"), buscarRack(resultadoConsulta.getString("letraRack"))));
            }

            return lote;

        }catch (Exception ex){
            throw ex;
        }
    }

    //endregion
}
