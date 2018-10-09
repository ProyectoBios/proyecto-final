package proyecto.persistencia;

import proyecto.entidades.Empleado;
import proyecto.entidades.ExcepcionFrigorifico;
import proyecto.entidades.Repartidor;
import proyecto.entidades.Vehiculo;

import java.sql.CallableStatement;
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
    public Empleado buscarEmpleado(String ci, boolean soloActivos) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Empleado WHERE ci = ?")){

            consulta.setString(1, ci);

            ResultSet resultado = consulta.executeQuery();

            Empleado empleado = null;
            if(resultado.next()) {
                if(!(resultado.getBoolean("eliminado") && soloActivos)) {
                    empleado = new Empleado(resultado.getString("ci"), resultado.getString("nombre"), resultado.getString("contrasenia"), resultado.getDate("fechaNac"), resultado.getDate("fechaContratacion"), resultado.getString("telefono"), resultado.getString("rol"));
                }
            }

            return empleado;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public Repartidor buscarRepartidor(String ci, boolean soloActivos) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT Empleado.*, Repartidor.vencLibreta FROM Empleado INNER JOIN Repartidor ON Empleado.ci = Repartidor.ci WHERE Empleado.ci = ?")){

            consulta.setString(1, ci);

            ResultSet resultado = consulta.executeQuery();

            Repartidor repartidor = null;
            if(resultado.next()) {
                if(!(resultado.getBoolean("eliminado") && soloActivos)) {
                    repartidor = new Repartidor(resultado.getString("ci"), resultado.getString("nombre"), resultado.getString("contrasenia"), resultado.getDate("fechaNac"), resultado.getDate("fechaContratacion"), resultado.getString("telefono"), resultado.getString("rol"), resultado.getDate("vencLibreta"));
                }
            }

            return repartidor;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<Empleado> listarEmpleadosXRol(String rol, boolean soloActivos) throws Exception {
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
                    if(!(resultado.getBoolean("eliminado") && soloActivos)) {
                        if (resultado.getString("rol").equals("repartidor")) {
                            empleado = new Repartidor(resultado.getString("ci"), resultado.getString("nombre"), resultado.getString("contrasenia"), resultado.getDate("fechaNac"), resultado.getDate("fechaContratacion"), resultado.getString("telefono"), resultado.getString("rol"), resultado.getDate("vencLibreta"));
                        } else {
                            empleado = new Empleado(resultado.getString("ci"), resultado.getString("nombre"), resultado.getString("contrasenia"), resultado.getDate("fechaNac"), resultado.getDate("fechaContratacion"), resultado.getString("telefono"), resultado.getString("rol"));
                        }
                        empleados.add(empleado);
                    }
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
                repartidor = new Repartidor(resultado.getString("ci"), resultado.getString("nombre"), resultado.getString("contrasenia"), resultado.getDate("fechaNac"), resultado.getDate("fechaContratacion"), resultado.getString("telefono"), resultado.getString("rol"), resultado.getDate("vencLibreta"));
                repartidores.add(repartidor);
            }

            return repartidores;
        }catch (Exception ex){
            throw ex;
        }
    }

    public void altaEmpleado(Empleado e) throws Exception{
        try(Connection con = Conexion.AbrirConexion();
            CallableStatement statement = con.prepareCall("{CALL AltaEmpleado(?, ?, ?, ?, ?, ?, ?)}")){

            if(e instanceof Repartidor){
                altaRepartidor((Repartidor)e, con);
            }else{
                statement.setString(1, e.getCi());
                statement.setString(2, e.getNombre());
                statement.setString(3, e.getContrasenia());
                statement.setDate(4, new java.sql.Date(e.getFechaDeNacimiento().getTime()));
                statement.setDate(5, new java.sql.Date(e.getFechaContratacion().getTime()));
                statement.setString(6, e.getTelefono());
                statement.setString(7, e.getRol());

                int filasAfectadas = statement.executeUpdate();
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    private void altaRepartidor(Repartidor r, Connection con) throws Exception{
        try(CallableStatement statement = con.prepareCall("{CALL AltaRepartidor(?, ?, ?, ?, ?, ?, ?)}")){
            statement.setString(1, r.getCi());
            statement.setString(2, r.getNombre());
            statement.setString(3, r.getContrasenia());
            statement.setDate(4, new java.sql.Date(r.getFechaDeNacimiento().getTime()));
            statement.setDate(5, new java.sql.Date(r.getFechaContratacion().getTime()));
            statement.setDate(6, new java.sql.Date(r.getVencLibreta().getTime()));
            statement.setString(7, r.getTelefono());

            statement.executeUpdate();
        }catch (Exception ex){
            throw ex;
        }

    }

    @Override
    public void bajaEmpleado(Empleado e) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            CallableStatement statement = con.prepareCall("{CALL BajaEmpleado(?)}")){
            statement.setString(1, e.getCi());

            statement.executeUpdate();
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public void modificarEmpleado(Empleado e) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement statement = con.prepareStatement("UPDATE Empleado SET nombre = ?, contrasenia=?, fechaNac = ?, fechaContratacion=?, telefono = ? WHERE ci = ?")){
            if(e instanceof Repartidor){
                modificarRepartidor((Repartidor)e, con);
            }else{
                statement.setString(1, e.getNombre());
                statement.setString(2, e.getContrasenia());
                statement.setDate(3, new java.sql.Date(e.getFechaDeNacimiento().getTime()));
                statement.setDate(4, new java.sql.Date(e.getFechaContratacion().getTime()));
                statement.setString(5, e.getTelefono());
                statement.setString(6, e.getCi());

                statement.executeUpdate();
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    private void modificarRepartidor(Repartidor e, Connection con) throws Exception{
        try(CallableStatement statement = con.prepareCall("{CALL ModificarRepartidor(?, ?, ?, ?, ?, ?, ?)}")){
            statement.setString(2, e.getNombre());
            statement.setString(3, e.getContrasenia());
            statement.setDate(4, new java.sql.Date(e.getFechaDeNacimiento().getTime()));
            statement.setDate(5, new java.sql.Date(e.getFechaContratacion().getTime()));
            statement.setDate(6, new java.sql.Date(e.getVencLibreta().getTime()));
            statement.setString(7, e.getTelefono());
            statement.setString(1, e.getCi());

            statement.executeUpdate();
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public Vehiculo buscarVehiculo(String matricula, boolean soloActivos) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
        PreparedStatement consulta = con.prepareStatement("SELECT * FROM Vehiculo WHERE matricula = ?")){

            consulta.setString(1, matricula);

            ResultSet resultado = consulta.executeQuery();

            Vehiculo v = null;
            if(resultado.next()) {
                if(!(resultado.getBoolean("eliminado") && soloActivos)) {
                    v = new Vehiculo(matricula, resultado.getString("marca"), resultado.getString("modelo"), resultado.getInt("cargaMax"));
                }
            }

            return v;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public void altaVehiculo(Vehiculo v) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement statement = con.prepareStatement("INSERT INTO Vehiculo VALUES (?, ?, ?, ?, 0)")) {

            statement.setString(1, v.getMatricula());
            statement.setString(2, v.getMarca());
            statement.setString(3, v.getModelo());
            statement.setInt(4, v.getCargaMax());

            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas != 1){
                throw new ExcepcionFrigorifico("Hubo un error al agregar el vehículo.");
            }

        }catch (Exception ex){
            throw ex;
        }

    }

    @Override
    public void bajaVehiculo(Vehiculo v) throws Exception {
        try (Connection con = Conexion.AbrirConexion();
             CallableStatement statement = con.prepareCall("{CALL BajaVehiculo(?)}")){
                statement.setString(1, v.getMatricula());

                statement.executeUpdate();

        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public ArrayList<Vehiculo> listarVehiculos(boolean soloActivos) throws Exception {
        try(Connection con = Conexion.AbrirConexion();
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Vehiculo")){

            ResultSet resultado = consulta.executeQuery();

            Vehiculo v = null;
            ArrayList<Vehiculo> vehiculos = new ArrayList<>();
            while(resultado.next()) {
                if(!(resultado.getBoolean("eliminado") && soloActivos)) {
                    v = new Vehiculo(resultado.getString("matricula"), resultado.getString("marca"), resultado.getString("modelo"), resultado.getInt("cargaMax"));
                    vehiculos.add(v);
                }
            }

            return vehiculos;
        }catch (Exception ex){
            throw ex;
        }
    }
}
