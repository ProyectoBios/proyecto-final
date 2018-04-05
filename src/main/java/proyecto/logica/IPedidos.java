package proyecto.logica;

import proyecto.entidades.*;
import proyecto.entidades.EspecificacionProducto;

import java.util.ArrayList;

public interface IPedidos {
    //region Clientes
    public ArrayList<Cliente> buscarClientes(String nombre) throws Exception;
    public void altaCliente(Cliente cliente) throws Exception;
    public Cliente buscarCliente(String nombre) throws  Exception;
    //endregion

    //region OrdenDePedido
    public OrdenPedido buscarOrdenPedido(int idOrden) throws Exception;
    public int altaOrdenDePedido(OrdenPedido ordenPedido) throws Exception;
    public ArrayList<OrdenPedido> buscarOrdenesXCliente(Cliente cliente) throws Exception;
    public void cancelarPedido(OrdenPedido orden) throws Exception;
    void agregarLineaDePedido(OrdenPedido orden, EspecificacionProducto producto, int cantidad) throws Exception;
    void eliminarLinea(OrdenPedido orden, int numero) throws Exception;
    public ArrayList<OrdenPedido> listarPedidosXEstado(String estado) throws Exception;
    public ArrayList<Picking> obtenerPicking(ArrayList<OrdenPedido> ordenes) throws Exception;
    public void modificarEstadoDePedido(OrdenPedido ordenPedido, String estado) throws Exception;
    public ArrayList<OrdenPedido> listarPedidos() throws Exception;
    //endregion
}
