package proyecto.logica;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_EXCLUSIONPeer;
import proyecto.datatypes.*;
import proyecto.persistencia.*;

import java.util.ArrayList;

class ControladorDeposito implements  IDeposito {
    private static ControladorDeposito instancia = null;

    public static ControladorDeposito getInstancia(){
        if(instancia == null){
            instancia = new ControladorDeposito();
        }

        return instancia;
    }

    private ControladorDeposito(){}

    //region Productos

    private void ValidarEspecificacionProducto(DTEspecificacionProducto ep) throws ExcepcionFrigorifico{
        if(ep == null){
            throw new ExcepcionFrigorifico("¡ERROR! Producto nulo");
        }

        if(ep.getCodigo() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El codigo no puede ser menor que 0");
        }

        if(ep.getMinStock() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El stock minimo no puede ser menor que 0");
        }

        if(ep.getMaxStock() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El stock maximo no puede ser menor que 0");
        }

        if(ep.getStockCritico() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El stock critico no puede ser menor que 0");
        }

        if(ep.getNombre().length() > 40){
            throw new ExcepcionFrigorifico("¡ERROR! El nombre no puede tener más de 40 caracteres");
        }

        for(DTPrecio p : ep.getHistoricoPrecios()){
            ValidarPrecio(p);
        }
    }

    private void ValidarPrecio(DTPrecio p) throws ExcepcionFrigorifico {
        if(p==null){
            throw new ExcepcionFrigorifico("¡ERROR! Precio nulo");
        }

        if(p.getPrecio() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El precio no puede ser menor que 0");
        }
    }

    @Override
    public int altaDeProducto(DTEspecificacionProducto ep) throws Exception{
        ValidarEspecificacionProducto(ep);
        return FabricaPersistencia.getControladorDeposito().altaDeProducto(ep);
    }

    @Override
    public void bajaProducto(DTEspecificacionProducto ep) throws Exception{
        FabricaPersistencia.getControladorDeposito().bajaProducto(ep);
    }

    @Override
    public void modificarProducto(DTEspecificacionProducto ep) throws Exception{
        ValidarEspecificacionProducto(ep);
        FabricaPersistencia.getControladorDeposito().modificarProducto(ep);
    }

    @Override
    public DTEspecificacionProducto buscarProducto(int codigo) throws Exception{
        return FabricaPersistencia.getControladorDeposito().buscarProducto(codigo);
    }

    @Override
    public ArrayList<DTLote> buscarStock(DTEspecificacionProducto ep) throws Exception {
        ValidarEspecificacionProducto(ep);
        return FabricaPersistencia.getControladorDeposito().buscarStock(ep);
    }

    //endregion

    //region Rack

    private void ValidarRack(DTRack rack) throws ExcepcionFrigorifico{
        if(rack == null){
            throw new ExcepcionFrigorifico("¡ERROR! El rack no puede ser nulo.");
        }

        if(rack.getLetra().isEmpty() || rack.getLetra().length() > 1){
            throw new ExcepcionFrigorifico("¡ERROR! La letra del rack no es valida.");
        }

        if(!Character.isLetter(rack.getLetra().charAt(0))){
            throw new ExcepcionFrigorifico("¡ERROR! La letra del Rack no es válida");
        }

        if(rack.getDimAlto() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! La dimension alto del rack no es valida.");
        }

        if(rack.getDimAncho() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! La dimension ancho del rack no es valida.");
        }
    }

    @Override
    public DTRack buscarRack(String letra) throws Exception {
        return FabricaPersistencia.getControladorDeposito().buscarRack(letra);
    }

    @Override
    public void altaRack(DTRack rack) throws Exception {
        ValidarRack(rack);
        FabricaPersistencia.getControladorDeposito().altaRack(rack);
    }

    @Override
    public void bajaRack(DTRack rack) throws Exception {
        FabricaPersistencia.getControladorDeposito().bajaRack(rack);
    }

    //endregion

    //region Lote

    private void ValidarLote(DTLote lote) throws ExcepcionFrigorifico{
        if(lote == null){
            throw new ExcepcionFrigorifico("¡ERROR! El lote no puede ser nulo.");
        }

        if(lote.getId() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El id no puede ser menor que 0.");
        }

        if(lote.getCantUnidades() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! La cantidad de unidades no puede ser menor que 0.");
        }

        ValidarEspecificacionProducto(lote.getProducto());
        ValidarRack(lote.getUbicacion().getRack());

        if(lote.getUbicacion().getFila() < 0 || lote.getUbicacion().getColumna() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! La ubicación del lote no es válida.");
        }
    }

    @Override
    public int altaLote(DTLote lote) throws Exception {
        ValidarLote(lote);
        return FabricaPersistencia.getControladorDeposito().altaLote(lote);
    }

    @Override
    public ArrayList<DTLote> obtenerLotesVencidos() throws Exception {
        return FabricaPersistencia.getControladorDeposito().obtenerLotesVencidos();
    }

    @Override
    public void bajaLote(DTLote lote) throws Exception {
        FabricaPersistencia.getControladorDeposito().bajaLote(lote);
    }

    //endregion
}
