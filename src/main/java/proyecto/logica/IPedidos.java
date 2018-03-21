package proyecto.logica;

import proyecto.datatypes.DTCliente;
import proyecto.datatypes.DTEspecificacionProducto;
import proyecto.datatypes.DTOrdenPedido;
import proyecto.datatypes.ExcepcionFrigorifico;

import java.util.ArrayList;

public interface IPedidos {
    //region Clientes
    public ArrayList<DTCliente> buscarClientes(String nombre) throws Exception;
    public void altaCliente(DTCliente cliente) throws Exception;
    public DTCliente buscarCliente(String nombre) throws  Exception;
    //endregion

    //region OrdenDePedido
    public DTOrdenPedido buscarOrdenPedido(int idOrden) throws Exception;
    public int altaOrdenDePedido(DTOrdenPedido ordenPedido) throws Exception;
    public ArrayList<DTOrdenPedido> buscarOrdenesXCliente(DTCliente cliente) throws Exception;
    public void cancelarPedido(DTOrdenPedido orden) throws Exception;
    void agregarLineaDePedido(DTOrdenPedido orden, DTEspecificacionProducto producto, int cantidad) throws Exception;
    void eliminarLinea(DTOrdenPedido orden, int numero) throws Exception;
    public ArrayList<DTOrdenPedido> listarPedidosXEstado(String estado) throws Exception;
    //endregion
}
