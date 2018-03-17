package proyecto.logica;

import org.springframework.stereotype.Controller;
import proyecto.datatypes.*;
import proyecto.persistencia.FabricaPersistencia;

import java.util.ArrayList;

class ControladorPedidos implements IPedidos {
    private static ControladorPedidos instancia = null;

    private ControladorPedidos(){}

    public static ControladorPedidos getInstancia(){
        if(instancia == null){
            instancia = new ControladorPedidos();
        }

        return instancia;
    }

    //region Clientes
    private void validarCliente(DTCliente cliente) throws ExcepcionFrigorifico{
        if (cliente == null){
            throw new ExcepcionFrigorifico("¡ERROR! El cliente no puede ser nulo.");
        }

        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()){
            throw new ExcepcionFrigorifico("¡ERROR! El nombre no puede quedar vacio.");
        }

        if (cliente.getNombre().length() > 30){
            throw new ExcepcionFrigorifico("¡ERROR! El nombre no puede tener mas de 30 caracteres.");
        }

        if(cliente.getTelefono() == null || cliente.getTelefono().isEmpty()){
            throw new ExcepcionFrigorifico("¡ERROR! El telefono no puede quedar vacio.");
        }

        if(cliente.getTelefono().length() > 10){
            throw new ExcepcionFrigorifico("¡ERROR! El telefono no puede tener mas de 10 digitos");
        }

        try{
            Long.parseLong(cliente.getTelefono());
        }catch (Exception ex){
            throw new ExcepcionFrigorifico("¡ERROR! El telefono debe ser numérico.");
        }

        if(cliente.getCorreo() == null || cliente.getCorreo().isEmpty()){
            throw new ExcepcionFrigorifico("¡ERROR! El correo no puede quedar vacio.");
        }

        if(cliente.getCorreo().length() > 40){
            throw new ExcepcionFrigorifico("¡ERROR! El correo no puede tener mas de 40 caracteres");
        }
    }

    @Override
    public ArrayList<DTCliente> buscarClientes(String nombre) throws Exception{
        return FabricaPersistencia.getControladorPedidos().buscarClientes(nombre);
    }

    @Override
    public void altaCliente(DTCliente cliente) throws Exception {
        validarCliente(cliente);
        FabricaPersistencia.getControladorPedidos().altaCliente(cliente);
    }

    @Override
    public DTCliente buscarCliente(String nombre) throws Exception{
        return FabricaPersistencia.getControladorPedidos().buscarCliente(nombre);
    }

    //endregion

    //region OrdenDePedido
    public void validarLineaDePedido(DTLineaPedido linea) throws ExcepcionFrigorifico{
        if(linea == null){
            throw new ExcepcionFrigorifico("¡ERROR! La linea no puede ser nula.");
        }

        if(linea.getCantidad() <= 0){
            throw new ExcepcionFrigorifico("¡ERROR! La cantidad no puede ser menor o igual que 0");
        }

        if(linea.getImporte() <= 0){
            throw new ExcepcionFrigorifico("¡ERROR! El importe no puede ser menor o igual que 0");
        }

        if(linea.getNumero() <= 0){
            throw new ExcepcionFrigorifico("¡ERROR! El numero de linea no puede ser menor o igual que 0");
        }

        ControladorDeposito.getInstancia().ValidarEspecificacionProducto(linea.getProducto());
    }

    public void validarOrdenDePedido(DTOrdenPedido orden) throws ExcepcionFrigorifico{
        if(orden == null){
            throw new ExcepcionFrigorifico("¡ERROR! La orden no puede ser nula.");
        }

        if(orden.getEstado() == null || orden.getEstado().isEmpty()){
            throw new ExcepcionFrigorifico("¡ERROR! El estado no puede quedar vacio.");
        }

        if(!(orden.getEstado() == "Pendiente" || orden.getEstado() == "En preparacion" || orden.getEstado() == "Preparado" || orden.getEstado() == "En distribucion" || orden.getEstado() == "Entregado" ||orden.getEstado() == "Entrega fallida")){
            throw new ExcepcionFrigorifico("¡ERROR! El estado no es valido.");
        }

        if(orden.getDireccionEnvio() == null || orden.getDireccionEnvio().isEmpty()){
            throw new ExcepcionFrigorifico("¡ERROR! La direccion de envio no puede quedar vacia.");
        }

        if(orden.getDireccionEnvio().length() > 40){
            throw new ExcepcionFrigorifico("¡ERROR! La direccion de envio no puede tener mas de 40 caracteres.");
        }

        if(orden.getContacto() == null ||orden.getEstado().isEmpty()){
            throw new ExcepcionFrigorifico("¡ERROR! El estado no puede quedar vacio");
        }

        if(orden.getContacto().length() > 40){
            throw new ExcepcionFrigorifico("¡ERROR! El estado no puede tener mas de 40 caracteres");
        }

        if(orden.getSubtotal() <= 0){
            throw new ExcepcionFrigorifico("¡ERROR! El subtotal no puede ser menor o igual que 0");
        }

        validarCliente(orden.getCliente());
        for(DTLineaPedido linea : orden.getLineas()){
            validarLineaDePedido(linea);
        }
    }

    @Override
    public DTOrdenPedido buscarOrdenPedido(int idOrden) throws Exception {
        return FabricaPersistencia.getControladorPedidos().buscarOrdenPedido(idOrden);
    }

    @Override
    public int altaOrdenDePedido(DTOrdenPedido ordenPedido) throws Exception{
        return FabricaPersistencia.getControladorPedidos().altaOrdenDePedidio(ordenPedido);
    }

    @Override
    public ArrayList<DTOrdenPedido> buscarOrdenesXCliente(DTCliente cliente) throws Exception {
        return FabricaPersistencia.getControladorPedidos().buscarOrdenesXCliente(cliente);
    }

    @Override
    public void cancelarPedido(DTOrdenPedido orden) throws Exception {
        if(!orden.getEstado().equals("pendiente")){
            throw new ExcepcionFrigorifico("¡ERROR! Solo se pueden cancelar pedidos cuyo estado sea pendiente");
        }

        FabricaPersistencia.getControladorPedidos().cancelarPedido(orden);
    }

    public void agregarLineaDePedido(DTOrdenPedido orden, DTEspecificacionProducto producto, int cantidad){
        Boolean encontrado = false;
        DTLineaPedido linea = null;
        for(DTLineaPedido l : orden.getLineas()){
            if(l.getProducto().getCodigo() == producto.getCodigo()){
                encontrado = true;
                linea = l;
                break;
            }
        }

        if(encontrado){
            linea.setCantidad(linea.getCantidad() + cantidad);
            linea.setImporte(linea.getImporte() + producto.getPrecioActual()*cantidad);
            orden.setSubtotal(orden.getSubtotal() + producto.getPrecioActual()*cantidad);
        }else {
            int numero = orden.getLineas().size() + 1;
            DTLineaPedido lineaPedido = new DTLineaPedido(numero, cantidad, cantidad * producto.getPrecioActual(), producto);

            orden.getLineas().add(lineaPedido);
            orden.setSubtotal(orden.getSubtotal() + lineaPedido.getImporte());
        }
    }

    @Override
    public void eliminarLinea(DTOrdenPedido orden, int numero) throws Exception {
        DTLineaPedido linea = orden.getLineas().get(numero-1);
        orden.getLineas().remove(numero-1);

        orden.setSubtotal(orden.getSubtotal() - linea.getImporte());

        //ajuste de indices
        for(int i = numero-1; i<orden.getLineas().size(); i++){
            orden.getLineas().get(i).setNumero(orden.getLineas().get(i).getNumero()-1);
        }
    }

    //endregion
}
