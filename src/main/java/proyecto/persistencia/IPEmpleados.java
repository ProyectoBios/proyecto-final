package proyecto.persistencia;

import proyecto.entidades.Empleado;
import proyecto.entidades.Repartidor;
import proyecto.entidades.Vehiculo;

import java.util.ArrayList;

public interface IPEmpleados {
    //region Empleados
    Empleado buscarEmpleado(String ci) throws Exception;
    Repartidor buscarRepartidor(String ci) throws Exception;
    ArrayList<Empleado> listarEmpleadosXRol(String rol) throws Exception;
    //endregion

    //region Vehiculos
    Vehiculo buscarVehiculo(String matricula) throws Exception;
    ArrayList<Vehiculo> listarVehiculos() throws Exception;
    //endregion
}
