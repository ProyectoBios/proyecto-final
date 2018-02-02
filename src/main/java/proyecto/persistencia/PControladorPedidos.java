package proyecto.persistencia;

import proyecto.datatypes.DTCliente;
import proyecto.datatypes.DTLineaPedido;
import proyecto.datatypes.DTOrdenPedido;
import proyecto.datatypes.ExcepcionFrigorifico;

import java.sql.*;
import java.util.ArrayList;

class PControladorPedidos implements IPPedidos{
    private static PControladorPedidos instancia = null;

    private PControladorPedidos() {}

    public static IPPedidos getInstancia(){
        if(instancia==null){
            instancia = new PControladorPedidos();
        }

        return instancia;
    }

    //region Clientes

    @Override
    public ArrayList<DTCliente> buscarClientes(String nombre) throws Exception{
        try(Connection con = Conexion.AbrirConexion();
            CallableStatement consulta = con.prepareCall("{ CALL BuscarClientes(?)}")){

            consulta.setString(1, nombre);

            ResultSet resultadoConsulta = consulta.executeQuery();

            ArrayList<DTCliente> clientes = new ArrayList<>();
            DTCliente cliente = null;

            while(resultadoConsulta.next()){
                cliente = new DTCliente(resultadoConsulta.getString("nombre"), resultadoConsulta.getString("telefono"), resultadoConsulta.getString("correo"));
                clientes.add(cliente);
            }

            return clientes;
        }catch(Exception ex){
            throw ex;
        }
    }

    @Override
    public void altaCliente(DTCliente cliente) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("INSERT INTO Cliente VALUES(?,?,?)")){
            consulta.setString(1, cliente.getNombre());
            consulta.setString(2, cliente.getTelefono());
            consulta.setString(3, cliente.getCorreo());

            int filasAfectadas = consulta.executeUpdate();
            if (filasAfectadas != 1){
                throw new ExcepcionFrigorifico("¡ERROR! Ocurrio un error al dar de alta el cliente.");
            }

        }catch (Exception ex){
            throw ex;
        }
    }

    //endregion

    //region OrdenDePedido

    @Override
    public int altaOrdenDePedidio(DTOrdenPedido orden) throws Exception {
        Connection con = null;
        CallableStatement consulta = null;

        try{
            con = Conexion.AbrirConexion();
            con.setAutoCommit(false);

            consulta = con.prepareCall("{ CALL AltaOrdenDePedido(?, ?, ?, ? ,? ,? ,? ,?)}");

            consulta.setString(1, orden.getEstado());
            consulta.setString(2, orden.getDireccionEnvio());
            consulta.setString(3, orden.getContacto());
            consulta.setDouble(4, orden.getSubtotal());
            consulta.setDouble(5, orden.getImpuestos());
            consulta.setDouble(6, orden.getTotal());
            consulta.setString(7, orden.getCliente().getNombre());
            consulta.registerOutParameter(8, Types.INTEGER);

            consulta.executeUpdate();

            int id = consulta.getInt(8);
            orden.setId(id);

            for(DTLineaPedido linea : orden.getLineas()){
                altaLineaDePedido(orden, linea, con);
            }

            con.commit();
            return id;
        }catch(Exception ex){
            if(con != null){
                con.rollback();
            }
            throw ex;
        }finally {
            if(consulta != null){
                consulta.close();
            }

            if(con != null){
                con.close();
            }
        }
    }

    private void altaLineaDePedido(DTOrdenPedido orden, DTLineaPedido linea, Connection con) throws Exception{
        CallableStatement statement = null;

        try{
            statement = con.prepareCall("{ CALL AltaLineaPedido(?, ?, ?, ?, ?) }");

            statement.setInt(1, orden.getId());
            statement.setInt(2, linea.getNumero());
            statement.setInt(3, linea.getCantidad());
            statement.setDouble(4, linea.getImporte());
            statement.setInt(5, linea.getProducto().getCodigo());

            statement.executeUpdate();

        }catch (Exception ex){
            throw ex;
        }finally {
            if(statement != null){
                statement.close();
            }
        }
    }

    //endregion
}
