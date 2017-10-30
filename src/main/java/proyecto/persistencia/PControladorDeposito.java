package proyecto.persistencia;
import proyecto.datatypes.*;

import java.sql.*;
import java.util.ArrayList;

public class PControladorDeposito implements IPDeposito{

    private static PControladorDeposito instancia = null;

    public static PControladorDeposito getInstancia(){
        if(instancia == null){
            instancia = new PControladorDeposito();
        }

        return instancia;
    }

    private PControladorDeposito(){}

    @Override
    public void altaDeProducto(DTEspecificacionProducto ep) throws Exception{

        try(Connection con = Conexion.AbrirConexion();
            CallableStatement consulta = con.prepareCall("{ CALL AltaEspProducto(?,?,?,?,?) }")) {
            consulta.setInt(1, ep.getCodigo());
            consulta.setString(2, ep.getNombre());
            consulta.setInt(3, ep.getMinStock());
            consulta.setInt(4, ep.getStockCritico());
            consulta.setInt(5, ep.getMaxStock());

            int filasAfectadas = consulta.executeUpdate();

            if (filasAfectadas != 1) {
                throw new Exception("Ocurrió un error al agregar el Producto");
            }

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
             CallableStatement consulta = con.prepareCall("{ CALL ModificarProducto(?,?,?,?,?) }")){

            consulta.setInt(1, ep.getCodigo());
            consulta.setString(2, ep.getNombre());
            consulta.setInt(3, ep.getMinStock());
            consulta.setInt(4, ep.getStockCritico());
            consulta.setInt(5, ep.getMaxStock());

            int filasAfectadas = consulta.executeUpdate();

            if (filasAfectadas < 1) {
                throw new Exception("No se pudo modificar el Producto");
            }

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
                productoEncontrado = new DTEspecificacionProducto(resultadoConsulta.getInt("ID"), resultadoConsulta.getString("nombre"), resultadoConsulta.getInt("minStock"), resultadoConsulta.getInt("stockCritico"), resultadoConsulta.getInt("maxStock"), new ArrayList<DTPrecio>());
            }

        } catch (SQLException ex) {
            throw ex;

        } catch (Exception ex) {
            throw ex;
        }
        return productoEncontrado;
    }
}
