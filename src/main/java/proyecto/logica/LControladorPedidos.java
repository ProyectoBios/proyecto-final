package proyecto.logica;

import proyecto.controladores.ControladorDeposito;
import proyecto.entidades.*;
import proyecto.entidades.EspecificacionProducto;
import proyecto.persistencia.FabricaPersistencia;

import java.util.ArrayList;
import java.util.HashMap;

class LControladorPedidos implements IPedidos {
    private static LControladorPedidos instancia = null;

    private LControladorPedidos(){}

    public static LControladorPedidos getInstancia(){
        if(instancia == null){
            instancia = new LControladorPedidos();
        }

        return instancia;
    }

    //region Clientes
    private void validarCliente(Cliente cliente) throws ExcepcionFrigorifico{
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
    public ArrayList<Cliente> buscarClientes(String nombre) throws Exception{
        return FabricaPersistencia.getControladorPedidos().buscarClientes(nombre);
    }

    @Override
    public void altaCliente(Cliente cliente) throws Exception {
        validarCliente(cliente);
        FabricaPersistencia.getControladorPedidos().altaCliente(cliente);
    }

    @Override
    public Cliente buscarCliente(String nombre) throws Exception{
        return FabricaPersistencia.getControladorPedidos().buscarCliente(nombre);
    }

    //endregion

    //region OrdenDePedido
    public void validarLineaDePedido(LineaPedido linea) throws ExcepcionFrigorifico{
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

        LControladorDeposito.getInstancia().ValidarEspecificacionProducto(linea.getProducto());
    }

    public void validarOrdenDePedido(OrdenPedido orden) throws ExcepcionFrigorifico{
        if(orden == null){
            throw new ExcepcionFrigorifico("¡ERROR! La orden no puede ser nula.");
        }

        if(orden.getEstado() == null || orden.getEstado().isEmpty()){
            throw new ExcepcionFrigorifico("¡ERROR! El estado no puede quedar vacio.");
        }

        validarEstadoPedido(orden.getEstado());

        if(orden.getDireccionEnvio() == null || orden.getDireccionEnvio().isEmpty()){
            throw new ExcepcionFrigorifico("¡ERROR! La direccion de envio no puede quedar vacia.");
        }

        if(orden.getDireccionEnvio().length() > 40){
            throw new ExcepcionFrigorifico("¡ERROR! La direccion de envio no puede tener mas de 40 caracteres.");
        }

        if(orden.getContacto() == null ||orden.getContacto().isEmpty()){
            throw new ExcepcionFrigorifico("¡ERROR! El nombre de contacto no puede quedar vacio");
        }

        if(orden.getContacto().length() > 40){
            throw new ExcepcionFrigorifico("¡ERROR! El nombre de contacto no puede tener mas de 40 caracteres");
        }

        validarCliente(orden.getCliente());

        if (orden.getLineas().size() == 0){
            throw new ExcepcionFrigorifico("¡ERROR! Debe agregar productos al pedido.");
        }

        if(orden.getSubtotal() <= 0){
            throw new ExcepcionFrigorifico("¡ERROR! El subtotal no puede ser menor o igual que 0");
        }

        for(LineaPedido linea : orden.getLineas()){
            validarLineaDePedido(linea);
        }
    }

    public void validarEstadoPedido(String estado) throws ExcepcionFrigorifico{
        if(!(estado.equals("pendiente") || estado.equals("en preparacion") || estado.equals("preparado") || estado.equals("en distribucion") || estado.equals("cancelado") || estado.equals("entregado") || estado.equals("entrega fallida"))){
            throw new ExcepcionFrigorifico("¡ERROR! El estado no es valido.");
        }
    }

    @Override
    public OrdenPedido buscarOrdenPedido(int idOrden) throws Exception {
        return FabricaPersistencia.getControladorPedidos().buscarOrdenPedido(idOrden);
    }

    @Override
    public int altaOrdenDePedido(OrdenPedido ordenPedido) throws Exception{
        validarOrdenDePedido(ordenPedido);
        return FabricaPersistencia.getControladorPedidos().altaOrdenDePedidio(ordenPedido);
    }

    @Override
    public ArrayList<OrdenPedido> buscarOrdenesXCliente(Cliente cliente) throws Exception {
        return FabricaPersistencia.getControladorPedidos().buscarOrdenesXCliente(cliente);
    }

    @Override
    public void cancelarPedido(OrdenPedido orden) throws Exception {
        if(!orden.getEstado().equals("pendiente")){
            throw new ExcepcionFrigorifico("¡ERROR! Solo se pueden cancelar pedidos cuyo estado sea pendiente");
        }

        modificarEstadoDePedido(orden, "cancelado");
    }

    public String agregarLineaDePedido(OrdenPedido orden, EspecificacionProducto producto, int cant) throws Exception{
        Boolean encontrado = false;
        LineaPedido linea = null;
        for(LineaPedido l : orden.getLineas()){
            if(l.getProducto().getCodigo() == producto.getCodigo()){
                encontrado = true;
                linea = l;
                break;
            }
        }

        if(encontrado){
            linea.setCantidad(linea.getCantidad() + cant);
            linea.setImporte(linea.getImporte() + producto.getPrecioActual()*cant);
            orden.setSubtotal(orden.getSubtotal() + producto.getPrecioActual()*cant);
        }else {
            int numero = orden.getLineas().size() + 1;
            linea = new LineaPedido(numero, cant, cant * producto.getPrecioActual(), producto);

            orden.getLineas().add(linea);
            orden.setSubtotal(orden.getSubtotal() + linea.getImporte());
        }

        ArrayList<Lote> stock = LControladorDeposito.getInstancia().buscarStock(producto);
        int cantidad = 0;
        for(Lote l : stock){
            cantidad+=l.getCantUnidades();
        }

        if(cantidad < linea.getCantidad()){
            return "AVISO: No hay suficiente stock actualmente para satisfacer " + linea.getCantidad() + " unidades de " + linea.getProducto().getNombre() + ". Stock actual: " + cantidad + " unidades.";
        }else{
            return "";
        }
    }

    @Override
    public void eliminarLinea(OrdenPedido orden, int numero) throws Exception {
        LineaPedido linea = orden.getLineas().get(numero-1);
        orden.getLineas().remove(numero-1);

        orden.setSubtotal(orden.getSubtotal() - linea.getImporte());

        //ajuste de indices
        for(int i = numero-1; i<orden.getLineas().size(); i++){
            orden.getLineas().get(i).setNumero(orden.getLineas().get(i).getNumero()-1);
        }
    }

    @Override
    public ArrayList<OrdenPedido> listarPedidosXEstado(String estado) throws Exception {
        validarEstadoPedido(estado);
        return FabricaPersistencia.getControladorPedidos().listarPedidosXEstado(estado);
    }

    @Override
    public ArrayList<Picking> obtenerPicking(ArrayList<OrdenPedido> ordenes) throws Exception {
        HashMap<Integer, Picking> pickingHashMap = new HashMap<Integer, Picking>();
        HashMap<Integer, ArrayList<Lote>> stocks = new HashMap<Integer, ArrayList<Lote>>();

        for(OrdenPedido orden : ordenes){
            for(LineaPedido linea : orden.getLineas()){
                if(!stocks.containsKey(linea.getProducto().getCodigo())){
                    stocks.put(linea.getProducto().getCodigo(), FabricaLogica.getControladorDeposito().buscarStock(linea.getProducto()));
                }

                Picking p;
                if(!pickingHashMap.containsKey(linea.getProducto().getCodigo())){
                    p = new Picking(linea.getProducto(), linea.getCantidad(), new ArrayList<Lote>());
                    pickingHashMap.put(linea.getProducto().getCodigo(), p);
                }else{
                    p = pickingHashMap.get(linea.getProducto().getCodigo());
                    p.setCantidad(p.getCantidad() + linea.getCantidad());
                }

                while(p.getCantidad() > p.getCantidadUnidadesTotal()){
                    if(stocks.get(p.getProducto().getCodigo()).size() == 0){
                        throw new ExcepcionFrigorifico("ERROR! No hay suficiente stock de: " + p.getProducto().getNombre() + " para satisfacer el pedido con ID: " + orden.getId());
                    }
                    p.getLotes().add(stocks.get(p.getProducto().getCodigo()).get(0));
                    stocks.get(p.getProducto().getCodigo()).remove(0);
                }
            }
        }

        ArrayList<Picking> picking = new ArrayList<>(pickingHashMap.values());

        for(Picking p : picking){
            for(int i=0; i<p.getLotes().size(); i++){
                if(i!=p.getLotes().size()-1) {
                    FabricaPersistencia.getControladorDeposito().bajaLogicaLote(p.getLotes().get(i)); //Baja logica del lote a remover.
                }else{
                    if(p.getCantidad()==p.getCantidadUnidadesTotal()){
                        FabricaPersistencia.getControladorDeposito().bajaLogicaLote(p.getLotes().get(i));
                    }else{
                        FabricaPersistencia.getControladorDeposito().actualizarStock(p.getLotes().get(i), (p.getCantidadUnidadesTotal() - p.getCantidad()) - p.getLotes().get(i).getCantUnidades()); //actualizo de forma temporal el stock en el último lote de la cola
                    }
                }
            }
        }

        return picking;
    }

    @Override
    public void modificarEstadoDePedido(OrdenPedido ordenPedido, String estado) throws Exception {
        validarEstadoPedido(estado);
        ordenPedido.setEstado(estado);
        FabricaPersistencia.getControladorPedidos().modificarEstadoDePedido(ordenPedido, estado);
    }

    @Override
    public ArrayList<OrdenPedido> listarPedidos() throws Exception {
        return FabricaPersistencia.getControladorPedidos().listarPedidos();
    }

    @Override
    public void prepararPedido(OrdenPedido orden) throws Exception{
        FabricaPersistencia.getControladorPedidos().prepararPedido(orden);
    }

    //endregion


}
