package proyecto.logica;

import proyecto.entidades.Empleado;
import proyecto.entidades.ExcepcionFrigorifico;
import proyecto.entidades.Vehiculo;
import proyecto.persistencia.FabricaPersistencia;

import java.util.ArrayList;
import java.security.MessageDigest;

public class LControladorEmpleados implements IEmpleados{
    private static LControladorEmpleados instancia = null;

    public static LControladorEmpleados getInstancia(){
        if(instancia == null){
            instancia = new LControladorEmpleados();
        }
        return instancia;
    }

    private LControladorEmpleados(){}

    //SHA 256
    private static String getSha256(String value) throws ExcepcionFrigorifico{
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes("UTF-8"));
            return bytesToHex(md.digest());
        } catch(Exception ex){
            throw new ExcepcionFrigorifico("Error encriptando la contraseña.");
        }
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    @Override
    public boolean iniciarSesion(String ci, String pass) throws Exception {
        Empleado e = buscarEmpleado(ci);
        if(e == null){
            return false;
        }

        String passHasheada = getSha256(pass);

        return (e.getContrasenia().equals(passHasheada));
    }

    @Override
    public Empleado buscarEmpleado(String ci) throws Exception {
        Empleado empleado = FabricaPersistencia.getControladorEmpleados().buscarEmpleado(ci);
        if(empleado != null && empleado.getRol().equals("repartidor")){
            empleado = FabricaPersistencia.getControladorEmpleados().buscarRepartidor(ci);
        }
        return empleado;
    }

    @Override
    public void altaEmpleado(Empleado e) throws Exception {
        e.setContrasenia(getSha256(e.getContrasenia()));
        FabricaPersistencia.getControladorEmpleados().altaEmpleado(e);
    }

    @Override
    public void bajaEmpleado(Empleado e) throws Exception {
        FabricaPersistencia.getControladorEmpleados().bajaEmpleado(e);
    }

    @Override
    public void modificarEmpleado(Empleado e) throws Exception {
        String pass = buscarEmpleado(e.getCi()).getContrasenia();
        if(!e.getContrasenia().equals(pass)){ //Si intenta modificar la password del empleado la hasheo
            e.setContrasenia(getSha256(e.getContrasenia()));
        }

        FabricaPersistencia.getControladorEmpleados().modificarEmpleado(e);
    }

    @Override
    public ArrayList<Empleado> listarEmpleadosXRol(String rol) throws Exception {
        return FabricaPersistencia.getControladorEmpleados().listarEmpleadosXRol(rol);
    }

    @Override
    public Vehiculo buscarVehiculo(String matricula) throws Exception {
        return FabricaPersistencia.getControladorEmpleados().buscarVehiculo(matricula);
    }

    @Override
    public void altaVehiculo(Vehiculo v) throws Exception {
        FabricaPersistencia.getControladorEmpleados().altaVehiculo(v);
    }

    @Override
    public void bajaVehiculo(Vehiculo v) throws Exception {
        FabricaPersistencia.getControladorEmpleados().bajaVehiculo(v);
    }

    @Override
    public ArrayList<Vehiculo> listarVehiculos() throws Exception {
        return FabricaPersistencia.getControladorEmpleados().listarVehiculos();
    }
}
