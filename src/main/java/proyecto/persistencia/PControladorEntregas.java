package proyecto.persistencia;

import jdk.nashorn.internal.codegen.CompilerConstants;
import proyecto.entidades.OrdenPedido;
import proyecto.entidades.Viaje;

import java.sql.*;

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
}
