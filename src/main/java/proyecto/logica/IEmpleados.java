package proyecto.logica;

import proyecto.entidades.Empleado;
import proyecto.entidades.ExcepcionFrigorifico;
import proyecto.entidades.Vehiculo;

import java.util.ArrayList;

public interface IEmpleados {
    //region Empleados
    Empleado buscarEmpleado(String ci) throws Exception;
    void altaEmpleado(Empleado e) throws Exception;
    void bajaEmpleado(Empleado e) throws Exception;
    void modificarEmpleado(Empleado e) throws Exception;
    ArrayList<Empleado> listarEmpleadosXRol(String rol) throws Exception;
    //endregion

    //region Vehiculos
    Vehiculo buscarVehiculo(String matricula) throws Exception;
    ArrayList<Vehiculo> listarVehiculos() throws Exception;
    //endregion
}
