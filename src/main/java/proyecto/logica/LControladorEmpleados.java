package proyecto.logica;

import proyecto.entidades.Empleado;
import proyecto.entidades.Vehiculo;
import proyecto.persistencia.FabricaPersistencia;

import java.util.ArrayList;

public class LControladorEmpleados implements IEmpleados{
    private static LControladorEmpleados instancia = null;

    public static LControladorEmpleados getInstancia(){
        if(instancia == null){
            instancia = new LControladorEmpleados();
        }
        return instancia;
    }

    private LControladorEmpleados(){}

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
        FabricaPersistencia.getControladorEmpleados().altaEmpleado(e);
    }

    @Override
    public void bajaEmpleado(Empleado e) throws Exception {
        FabricaPersistencia.getControladorEmpleados().bajaEmpleado(e);
    }

    @Override
    public void modificarEmpleado(Empleado e) throws Exception {
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
