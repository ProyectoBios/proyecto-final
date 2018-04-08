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
        if(empleado.getRol().equals("repartidor")){
            empleado = FabricaPersistencia.getControladorEmpleados().buscarRepartidor(ci);
        }
        return empleado;
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
    public ArrayList<Vehiculo> listarVehiculos() throws Exception {
        return FabricaPersistencia.getControladorEmpleados().listarVehiculos();
    }
}
