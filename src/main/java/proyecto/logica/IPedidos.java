package proyecto.logica;

import proyecto.datatypes.DTCliente;
import proyecto.datatypes.DTOrdenPedido;
import sun.reflect.annotation.ExceptionProxy;

import java.util.ArrayList;

public interface IPedidos {
    //region Clientes
    public ArrayList<DTCliente> buscarClientes(String nombre) throws Exception;
    public void altaCliente(DTCliente cliente) throws Exception;
    //endregion

    //region OrdenDePedido
    public int altaOrdenDePedido(DTOrdenPedido ordenPedido) throws Exception;
    //endregion
}
