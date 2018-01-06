package proyecto.persistencia;

import proyecto.datatypes.*;

public interface IPDeposito {
    //Productos
    public int altaDeProducto(DTEspecificacionProducto ep)throws Exception;
    public void bajaProducto(DTEspecificacionProducto ep)throws Exception;
    public void modificarProducto(DTEspecificacionProducto ep)throws Exception;
    public DTEspecificacionProducto buscarProducto(int codigo)throws Exception;
    //Fin Productos
    //Rack
    public DTRack buscarRack(String letra) throws Exception;
    public void altaRack(DTRack rack) throws Exception;
    //Fin Rack
}
