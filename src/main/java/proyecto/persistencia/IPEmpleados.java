package proyecto.persistencia;

import proyecto.entidades.Empleado;
import proyecto.entidades.ExcepcionFrigorifico;
import proyecto.entidades.Repartidor;
import proyecto.entidades.Vehiculo;

import java.util.ArrayList;

public interface IPEmpleados {
    //region Empleados
    Empleado buscarEmpleado(String ci, boolean soloActivos) throws Exception;
    Repartidor buscarRepartidor(String ci, boolean soloActivos) throws Exception;
    void altaEmpleado(Empleado e) throws Exception;
    void bajaEmpleado(Empleado e) throws Exception;
    void modificarEmpleado(Empleado e) throws Exception;
    ArrayList<Empleado> listarEmpleadosXRol(String rol, boolean soloActivos) throws Exception;
    //endregion

    //region Vehiculos
    Vehiculo buscarVehiculo(String matricula, boolean soloActivos) throws Exception;
    void altaVehiculo(Vehiculo v) throws Exception;
    void bajaVehiculo(Vehiculo v) throws Exception;
    ArrayList<Vehiculo> listarVehiculos(boolean soloActivos) throws Exception;
    //endregion
}
