package proyecto.persistencia;

import proyecto.entidades.Cliente;
import proyecto.entidades.OrdenPedido;

import java.util.ArrayList;

public interface IPPedidos {
    //region Clientes
    public Cliente buscarCliente(String nombre) throws Exception;
    public ArrayList<Cliente> buscarClientes(String nombre) throws Exception;
    public void altaCliente(Cliente cliente) throws Exception;
    //endregion

    //region OrdenDePedido
    public OrdenPedido buscarOrdenPedido(int idOrden) throws Exception;
    public int altaOrdenDePedidio(OrdenPedido orden) throws Exception;
    public ArrayList<OrdenPedido> buscarOrdenesXCliente(Cliente cliente) throws Exception;
    public void modificarEstadoDePedido(OrdenPedido orden, String estado) throws Exception;
    public ArrayList<OrdenPedido> listarPedidosXEstado(String estado) throws Exception;
    public ArrayList<OrdenPedido> listarPedidos() throws Exception;
    void prepararPedido(OrdenPedido orden) throws Exception;

    //endregion
}
