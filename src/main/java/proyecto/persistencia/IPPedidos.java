package proyecto.persistencia;

import proyecto.entidades.DTCliente;
import proyecto.entidades.DTOrdenPedido;

import java.util.ArrayList;

public interface IPPedidos {
    //region Clientes
    public DTCliente buscarCliente(String nombre) throws Exception;
    public ArrayList<DTCliente> buscarClientes(String nombre) throws Exception;
    public void altaCliente(DTCliente cliente) throws Exception;
    //endregion

    //region OrdenDePedido
    public DTOrdenPedido buscarOrdenPedido(int idOrden) throws Exception;
    public int altaOrdenDePedidio(DTOrdenPedido orden) throws Exception;
    public ArrayList<DTOrdenPedido> buscarOrdenesXCliente(DTCliente cliente) throws Exception;
    public void modificarEstadoDePedido(DTOrdenPedido orden, String estado) throws Exception;
    public ArrayList<DTOrdenPedido> listarPedidosXEstado(String estado) throws Exception;
    public ArrayList<DTOrdenPedido> listarPedidos() throws Exception;

    //endregion
}
