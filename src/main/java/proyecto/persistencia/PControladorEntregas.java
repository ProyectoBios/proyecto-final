package proyecto.persistencia;

import jdk.nashorn.internal.codegen.CompilerConstants;
import proyecto.entidades.OrdenPedido;
import proyecto.entidades.Repartidor;
import proyecto.entidades.Vehiculo;
import proyecto.entidades.Viaje;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PControladorEntregas implements IPEntregas{
    private static PControladorEntregas instacia = null;

    public static PControladorEntregas getInstacia(){
        if(instacia==null){
            instacia=new PControladorEntregas();
        }
        return instacia;
    }

    private PControladorEntregas(){}

    @Override
    public void generarViaje(Viaje viaje) throws Exception {
        Connection con = null;
        CallableStatement statement = null;
        try {
            con = Conexion.AbrirConexion();
            con.setAutoCommit(false);

            statement = con.prepareCall("{CALL AltaViaje(?,?,?)}");
            statement.setString(1, viaje.getRepartidor().getCi());
            statement.setString(2, viaje.getVehiculo().getMatricula());
            statement.registerOutParameter(3, Types.INTEGER);

            statement.executeUpdate();

            int id = statement.getInt(3);
            viaje.setId(id);

            for(OrdenPedido p : viaje.getPedidos()){
                agregarPedidoViaje(viaje, p, con);
            }

            con.commit();
        }catch (Exception ex){
            if(con!=null){
                con.rollback();
            }
            throw ex;
        }finally {
            if(statement!=null){
                statement.close();
            }
            if(con!=null){
                con.close();
            }
        }
    }

    public void agregarPedidoViaje(Viaje viaje, OrdenPedido p, Connection con) throws Exception{
        try(PreparedStatement statement = con.prepareStatement("INSERT INTO PedidosViaje VALUES(?, ?)")){
            statement.setInt(1, viaje.getId());
            statement.setInt(2, p.getId());

            statement.executeUpdate();
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<Viaje> listarViajesPendientesXRepartidor(Repartidor repartidor) throws Exception{
        try (Connection con = Conexion.AbrirConexion();
             CallableStatement consulta = con.prepareCall("{CALL ListarIdViajeYVehiculoXRepartidor(?)}")){

            consulta.setString(1, repartidor.getCi());

            ArrayList<Viaje> listaViajes = new ArrayList<>();
            Viaje viaje = null;

            Vehiculo vehiculo = null;

            ArrayList<OrdenPedido> ordenes = null;
            OrdenPedido pedido = null;

            ResultSet resultadoConsulta = consulta.executeQuery();

            while (resultadoConsulta.next()){

                vehiculo = FabricaPersistencia.getControladorEmpleados().buscarVehiculo(resultadoConsulta.getString("matriculaVehiculo"));
                ordenes = obtenerPedidosXViaje(resultadoConsulta.getInt("id"));
                viaje = new Viaje(resultadoConsulta.getInt("id"), repartidor, vehiculo, ordenes, resultadoConsulta.getTimestamp("fechaHora"));
                listaViajes.add(viaje);
            }

            return listaViajes;

        } catch (Exception ex){
            throw ex;
        }
    }

    public ArrayList<OrdenPedido> obtenerPedidosXViaje (int idViaje) throws Exception{

        try (Connection con = Conexion.AbrirConexion();
             PreparedStatement consulta = con.prepareStatement("SELECT idPedido FROM PedidosViaje WHERE idViaje = ?")){

            consulta.setInt(1, idViaje);

            ResultSet resultadoConsulta = consulta.executeQuery();

            ArrayList<OrdenPedido> pedidos = new ArrayList<>();

            while (resultadoConsulta.next()){
                OrdenPedido pedido = FabricaPersistencia.getControladorPedidos().buscarOrdenPedido(resultadoConsulta.getInt("idPedido"));
                if (pedido.getEstado().contains("en distribucion")){
                    pedidos.add(pedido);
                }
            }
            return pedidos;

        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public void entregaFallidaPedido(OrdenPedido pedido, String detalleCancelacion) throws Exception {
        try (Connection con = Conexion.AbrirConexion();
             PreparedStatement statement = con.prepareStatement("UPDATE OrdenPedido SET estado = ?, descripcionEntrega = ? WHERE idOrden = ?")){

            statement.setString(1, "entrega fallida");
            statement.setString(2, detalleCancelacion);
            statement.setInt(3, pedido.getId());

            statement.executeUpdate();

        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public void finalizarViaje(Viaje viaje) throws Exception {
        try (Connection con = Conexion.AbrirConexion();
             PreparedStatement statement = con.prepareStatement("UPDATE Viaje SET finalizado = ? WHERE id = ?")){

            statement.setInt(1, 1);
            statement.setInt(2, viaje.getId());

            statement.executeUpdate();

        }catch (Exception ex){
            throw ex;
        }
    }
}
