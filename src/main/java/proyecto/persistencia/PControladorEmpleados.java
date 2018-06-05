package proyecto.persistencia;

import proyecto.entidades.Empleado;
import proyecto.entidades.Repartidor;
import proyecto.entidades.Vehiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class PControladorEmpleados implements IPEmpleados{
    private static PControladorEmpleados instancia = null;

    public static PControladorEmpleados getInstancia(){
        if(instancia == null){
            instancia = new PControladorEmpleados();
        }
        return instancia;
    }

    private PControladorEmpleados(){}

    @Override
    public Empleado buscarEmpleado(String ci) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Empleado WHERE ci = ?")){

            consulta.setString(1, ci);

            ResultSet resultado = consulta.executeQuery();

            Empleado empleado = null;
            if(resultado.next()) {
                empleado = new Empleado(resultado.getString("ci"), resultado.getString("nombre"), resultado.getDate("fechaNac"), resultado.getDate("fechaContratacion"), resultado.getString("telefono"), resultado.getString("rol"));
            }

            return empleado;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public Repartidor buscarRepartidor(String ci) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT Empleado.*, Repartidor.vencLibreta FROM Empleado INNER JOIN Repartidor ON Empleado.ci = Repartidor.ci WHERE Empleado.ci = ?")){

            consulta.setString(1, ci);

            ResultSet resultado = consulta.executeQuery();

            Repartidor repartidor = null;
            if(resultado.next()) {
                repartidor = new Repartidor(resultado.getString("ci"), resultado.getString("nombre"), resultado.getDate("fechaNac"), resultado.getDate("fechaContratacion"), resultado.getString("telefono"), resultado.getString("rol"), resultado.getDate("vencLibreta"));
            }

            return repartidor;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<Empleado> listarEmpleadosXRol(String rol) throws Exception {
        if(rol.equals("repartidor")){
            return listarRepartidores();
        }else {
            try (Connection con = Conexion.AbrirConexion();
                 PreparedStatement consulta = con.prepareStatement("SELECT * FROM Empleado WHERE rol = ?")) {

                consulta.setString(1, rol);

                ResultSet resultado = consulta.executeQuery();

                ArrayList<Empleado> empleados = new ArrayList<>();
                Empleado empleado = null;
                while (resultado.next()) {
                    if (resultado.getString("rol").equals("repartidor")) {
                        empleado = new Repartidor(resultado.getString("ci"), resultado.getString("nombre"), resultado.getDate("fechaNac"), resultado.getDate("fechaContratacion"), resultado.getString("telefono"), resultado.getString("rol"), resultado.getDate("vencLibreta"));
                    } else {
                        empleado = new Empleado(resultado.getString("ci"), resultado.getString("nombre"), resultado.getDate("fechaNac"), resultado.getDate("fechaContratacion"), resultado.getString("telefono"), resultado.getString("rol"));
                    }
                    empleados.add(empleado);
                }

                return empleados;
            } catch (Exception ex) {
                throw ex;
            }
        }
    }

    public ArrayList<Empleado> listarRepartidores() throws Exception{
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT Empleado.*, Repartidor.vencLibreta FROM Empleado INNER JOIN Repartidor ON Empleado.ci = Repartidor.ci")){


            ResultSet resultado = consulta.executeQuery();

            Empleado repartidor = null;
            ArrayList<Empleado> repartidores = new ArrayList<>();
            while(resultado.next()) {
                repartidor = new Repartidor(resultado.getString("ci"), resultado.getString("nombre"), resultado.getDate("fechaNac"), resultado.getDate("fechaContratacion"), resultado.getString("telefono"), resultado.getString("rol"), resultado.getDate("vencLibreta"));
                repartidores.add(repartidor);
            }

            return repartidores;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public Vehiculo buscarVehiculo(String matricula) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
        PreparedStatement consulta = con.prepareStatement("SELECT * FROM Vehiculo WHERE matricula = ?")){

            consulta.setString(1, matricula);

            ResultSet resultado = consulta.executeQuery();

            Vehiculo v = null;
            if(resultado.next()) {
                v = new Vehiculo(matricula, resultado.getString("marca"), resultado.getString("modelo"), resultado.getInt("cargaMax"));
            }

            return v;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<Vehiculo> listarVehiculos() throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Vehiculo")){

            ResultSet resultado = consulta.executeQuery();

            Vehiculo v = null;
            ArrayList<Vehiculo> vehiculos = new ArrayList<>();
            while(resultado.next()) {
                v = new Vehiculo(resultado.getString("matricula"), resultado.getString("marca"), resultado.getString("modelo"), resultado.getInt("cargaMax"));
                vehiculos.add(v);
            }

            return vehiculos;
        }catch (Exception ex){
            throw ex;
        }
    }
}
