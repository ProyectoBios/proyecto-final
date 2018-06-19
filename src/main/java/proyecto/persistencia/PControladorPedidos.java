package proyecto.persistencia;

import proyecto.entidades.Cliente;
import proyecto.entidades.LineaPedido;
import proyecto.entidades.OrdenPedido;
import proyecto.entidades.ExcepcionFrigorifico;

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
    public Cliente buscarCliente(String nombre) throws Exception{
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Cliente WHERE nombre = ?")){

            consulta.setString(1, nombre);

            ResultSet resultadoConsulta = consulta.executeQuery();

            Cliente cliente = null;

            if(resultadoConsulta.next()){
                cliente = new Cliente(resultadoConsulta.getString("nombre"), resultadoConsulta.getString("telefono"), resultadoConsulta.getString("correo"));
            }

            return cliente;
        }catch(Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<Cliente> buscarClientes(String nombre) throws Exception{
        try(Connection con = Conexion.AbrirConexion();
            CallableStatement consulta = con.prepareCall("{ CALL BuscarClientes(?)}")){

            consulta.setString(1, nombre);

            ResultSet resultadoConsulta = consulta.executeQuery();

            ArrayList<Cliente> clientes = new ArrayList<>();
            Cliente cliente = null;

            while(resultadoConsulta.next()){
                cliente = new Cliente(resultadoConsulta.getString("nombre"), resultadoConsulta.getString("telefono"), resultadoConsulta.getString("correo"));
                clientes.add(cliente);
            }

            return clientes;
        }catch(Exception ex){
            throw ex;
        }
    }

    @Override
    public void altaCliente(Cliente cliente) throws Exception {
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
    public OrdenPedido buscarOrdenPedido(int idOrden) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM OrdenPedido WHERE idOrden = ?")){

            consulta.setInt(1, idOrden);

            ResultSet resultado = consulta.executeQuery();

            OrdenPedido orden = null;

            if(resultado.next()){
                orden = new OrdenPedido(resultado.getInt("idOrden"), resultado.getTimestamp("fecha"), resultado.getString("estado"), resultado.getString("descripcionEntrega"), resultado.getTimestamp("ultimaActEst"), resultado.getString("direccionEnvio"), resultado.getString("contacto"), resultado.getDouble("subtotal"), buscarCliente(resultado.getString("nombreCliente")), PControladorEmpleados.getInstancia().buscarEmpleado(resultado.getString("operador")), PControladorEmpleados.getInstancia().buscarEmpleado(resultado.getString("funcionario")), buscarLineasXOrden(resultado.getInt("idOrden")));
            }

            return orden;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public int altaOrdenDePedidio(OrdenPedido orden) throws Exception {
        Connection con = null;
        CallableStatement consulta = null;

        try{
            con = Conexion.AbrirConexion();
            con.setAutoCommit(false);

            consulta = con.prepareCall("{ CALL AltaOrdenDePedido(?, ?, ?, ? ,? ,? ,? , ?, ?)}");

            consulta.setString(1, orden.getEstado());
            consulta.setString(2, orden.getDireccionEnvio());
            consulta.setString(3, orden.getContacto());
            consulta.setDouble(4, orden.getSubtotal());
            consulta.setDouble(5, orden.getImpuestos());
            consulta.setDouble(6, orden.getTotal());
            consulta.setString(7, orden.getCliente().getNombre());
            consulta.setString(8, orden.getOperador().getCi());
            consulta.registerOutParameter(9, Types.INTEGER);

            consulta.executeUpdate();

            int id = consulta.getInt(9);
            orden.setId(id);

            for(LineaPedido linea : orden.getLineas()){
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

    private void altaLineaDePedido(OrdenPedido orden, LineaPedido linea, Connection con) throws Exception{
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

    @Override
    public ArrayList<OrdenPedido> buscarOrdenesXCliente(Cliente cliente) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM OrdenPedido WHERE nombreCliente = ?")){

            consulta.setString(1, cliente.getNombre());

            ResultSet resultado = consulta.executeQuery();
            ArrayList<OrdenPedido> ordenes = new ArrayList<>();
            OrdenPedido orden = null;

            while(resultado.next()){
                orden = new OrdenPedido(resultado.getInt("idOrden"), resultado.getTimestamp("fecha"), resultado.getString("estado"), resultado.getString("descripcionEntrega"), resultado.getTimestamp("ultimaActEst"), resultado.getString("direccionEnvio"), resultado.getString("contacto"), resultado.getDouble("subtotal"), cliente, PControladorEmpleados.getInstancia().buscarEmpleado(resultado.getString("operador")), PControladorEmpleados.getInstancia().buscarEmpleado(resultado.getString("funcionario")),buscarLineasXOrden(resultado.getInt("idOrden")));
                ordenes.add(orden);
            }

            return ordenes;
        }catch (Exception ex){
            throw ex;
        }
    }

    private ArrayList<LineaPedido> buscarLineasXOrden(int idOrden) throws Exception{
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM LineaPedido WHERE idOrden = ?")){

            consulta.setInt(1, idOrden);

            ResultSet resultado = consulta.executeQuery();

            ArrayList<LineaPedido> lineas = new ArrayList<>();
            LineaPedido linea = null;

            while(resultado.next()){
                linea = new LineaPedido(resultado.getInt("numero"), resultado.getInt("cantidad"), resultado.getDouble("importe"), PControladorDeposito.getInstancia().buscarProducto(resultado.getInt("idProducto")));
                lineas.add(linea);
            }

            return lineas;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public void modificarEstadoDePedido(OrdenPedido orden, String estado) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
        PreparedStatement consulta = con.prepareStatement("UPDATE OrdenPedido SET estado = ?, ultimaActEst = NOW() WHERE idOrden = ?")){

            consulta.setString(1, estado);
            consulta.setInt(2, orden.getId());

            int filasAfectadas = consulta.executeUpdate();

            if(filasAfectadas != 1){
                throw new ExcepcionFrigorifico("¡ERROR! No se pudo modificar el estado de el pedido");
            }

        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public void prepararPedido(OrdenPedido orden) throws Exception{
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("UPDATE OrdenPedido SET funcionario = ? WHERE idOrden = ?")){

            consulta.setString(1, orden.getFuncionario().getCi());
            consulta.setInt(2, orden.getId());

            int filasAfectadas = consulta.executeUpdate();

            if(filasAfectadas != 1){
                throw new ExcepcionFrigorifico("¡ERROR! No se pudo asignar el preparador del pedido");
            }

        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<OrdenPedido> listarPedidosXEstado(String estado) throws Exception{
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM OrdenPedido WHERE estado = ?")){

            preparedStatement.setString(1, estado);

            ResultSet resultado = preparedStatement.executeQuery();
            ArrayList<OrdenPedido> pedidos = new ArrayList<>();
            OrdenPedido pedido = null;

            while(resultado.next()){
                pedido = new OrdenPedido(resultado.getInt("idOrden"), resultado.getTimestamp("fecha"), resultado.getString("estado"), resultado.getString("descripcionEntrega"), resultado.getTimestamp("ultimaActEst"), resultado.getString("direccionEnvio"), resultado.getString("contacto"), resultado.getDouble("subtotal"), buscarCliente(resultado.getString("nombreCliente")), PControladorEmpleados.getInstancia().buscarEmpleado(resultado.getString("operador")), PControladorEmpleados.getInstancia().buscarEmpleado(resultado.getString("funcionario")),buscarLineasXOrden(resultado.getInt("idOrden")));
                pedidos.add(pedido);
            }

            return pedidos;
        }catch(Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<OrdenPedido> listarPedidos() throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM OrdenPedido")){

            ResultSet resultado = preparedStatement.executeQuery();
            ArrayList<OrdenPedido> pedidos = new ArrayList<>();
            OrdenPedido pedido = null;

            while(resultado.next()){
                pedido = new OrdenPedido(resultado.getInt("idOrden"), resultado.getTimestamp("fecha"), resultado.getString("estado"), resultado.getString("descripcionEntrega"), resultado.getTimestamp("ultimaActEst"), resultado.getString("direccionEnvio"), resultado.getString("contacto"), resultado.getDouble("subtotal"), buscarCliente(resultado.getString("nombreCliente")), PControladorEmpleados.getInstancia().buscarEmpleado(resultado.getString("operador")), PControladorEmpleados.getInstancia().buscarEmpleado(resultado.getString("funcionario")), buscarLineasXOrden(resultado.getInt("idOrden")));
                pedidos.add(pedido);
            }

            return pedidos;
        }catch(Exception ex){
            throw ex;
        }
    }

    //endregion
}
