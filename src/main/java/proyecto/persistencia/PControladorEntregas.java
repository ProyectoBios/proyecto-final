package proyecto.persistencia;

import jdk.nashorn.internal.codegen.CompilerConstants;
import proyecto.entidades.OrdenPedido;
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

    public ArrayList<Viaje> listarViajesPendientes (String ciRepartidor) throws Exception{
        try (Connection con = Conexion.AbrirConexion();
             CallableStatement consulta = con.prepareCall("{CALL ListarIdViajeYVehiculoXRepartidor(?)}")){

            consulta.setString(1, ciRepartidor);

            ArrayList<Viaje> listaViajes = new ArrayList<>();
            Viaje viaje = new Viaje();

            Vehiculo vehiculo = new Vehiculo();

            ArrayList<OrdenPedido> ordenes = new ArrayList<>();
            OrdenPedido pedido = new OrdenPedido();

            ResultSet resultadoConsulta = consulta.executeQuery();

            while (resultadoConsulta.next()){
            //TODO-am: Traerme los viajes del repartidor y su vehículo.
            //TODO-am: Luego para cada viaje, recorrer los id de Pedidos y traérmelos para crear la lista de pedidos
            //TODO-am: Agregar el vehículo y la lista de pedidos al viaje. Retornar la lista de Viajes.

            }


        } catch (Exception ex){
            throw ex;
        }

        return null;
    }

    public ArrayList<Integer> obtenerIdPedidosXViaje (int idViaje) throws Exception{

        try (Connection con = Conexion.AbrirConexion();
             PreparedStatement consulta = con.prepareStatement("SELECT idPedidos FROM PedidosViaje WHERE idViaje = ?")){

            consulta.setInt(1, idViaje);

            ResultSet resultadoConsulta = consulta.executeQuery();

            ArrayList<Integer> idPedidos = new ArrayList<>();

            while (resultadoConsulta.next()){
                int idPedido = resultadoConsulta.getInt("idOrden");
                idPedidos.add(idPedido);
            }

            return idPedidos;

        }catch (Exception ex){
            throw ex;
        }
    }
}
