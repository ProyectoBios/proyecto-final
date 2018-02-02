package proyecto.persistencia;

import proyecto.datatypes.DTCliente;
import proyecto.datatypes.DTOrdenPedido;
import sun.reflect.annotation.ExceptionProxy;

import java.util.ArrayList;

public interface IPPedidos {
    //region Clientes
    public ArrayList<DTCliente> buscarClientes(String nombre) throws Exception;
    public void altaCliente(DTCliente cliente) throws Exception;
    //endregion

    //region OrdenDePedido
    public int altaOrdenDePedidio(DTOrdenPedido orden) throws Exception;
    //endregion
}
