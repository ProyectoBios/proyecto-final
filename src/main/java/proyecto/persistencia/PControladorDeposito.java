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

    //endregion
}
