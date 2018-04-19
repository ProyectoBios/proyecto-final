package proyecto.logica;

import proyecto.entidades.*;
import proyecto.entidades.EspecificacionProducto;
import proyecto.persistencia.*;

import java.util.ArrayList;
import java.util.Date;

class LControladorDeposito implements  IDeposito {
    private static LControladorDeposito instancia = null;

    public static LControladorDeposito getInstancia(){
        if(instancia == null){
            instancia = new LControladorDeposito();
        }

        return instancia;
    }

    private LControladorDeposito(){}

    //region Productos

    void ValidarEspecificacionProducto(EspecificacionProducto ep) throws ExcepcionFrigorifico{
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

        for(Precio p : ep.getHistoricoPrecios()){
            ValidarPrecio(p);
        }
    }

    private void ValidarPrecio(Precio p) throws ExcepcionFrigorifico {
        if(p==null){
            throw new ExcepcionFrigorifico("¡ERROR! Precio nulo");
        }

        if(p.getPrecio() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El precio no puede ser menor que 0");
        }
    }

    @Override
    public int altaDeProducto(EspecificacionProducto ep) throws Exception{
        ValidarEspecificacionProducto(ep);
        return FabricaPersistencia.getControladorDeposito().altaDeProducto(ep);
    }

    @Override
    public void bajaProducto(EspecificacionProducto ep) throws Exception{
        FabricaPersistencia.getControladorDeposito().bajaProducto(ep);
    }

    @Override
    public void modificarProducto(EspecificacionProducto ep) throws Exception{
        ValidarEspecificacionProducto(ep);
        FabricaPersistencia.getControladorDeposito().modificarProducto(ep);
    }

    @Override
    public EspecificacionProducto buscarProducto(int codigo) throws Exception{
        return FabricaPersistencia.getControladorDeposito().buscarProducto(codigo);
    }

    @Override
    public ArrayList<Lote> buscarStock(EspecificacionProducto ep) throws Exception {
        ValidarEspecificacionProducto(ep);
        return FabricaPersistencia.getControladorDeposito().buscarStock(ep);
    }

    @Override
    public ArrayList<EspecificacionProducto> listarProductos() throws Exception {
        return FabricaPersistencia.getControladorDeposito().listarProductos();
    }

    //endregion

    //region Rack

    private void ValidarRack(Rack rack) throws ExcepcionFrigorifico{
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
    public Rack buscarRack(String letra) throws Exception {
        return FabricaPersistencia.getControladorDeposito().buscarRack(letra);
    }

    @Override
    public void altaRack(Rack rack) throws Exception {
        ValidarRack(rack);
        FabricaPersistencia.getControladorDeposito().altaRack(rack);
    }

    @Override
    public boolean esUbicacionVacia(Ubicacion ubicacion) throws Exception{
        return FabricaPersistencia.getControladorDeposito().obtenerUbicacion(ubicacion) == null;
    }

    @Override
    public void bajaRack(Rack rack) throws Exception {
        FabricaPersistencia.getControladorDeposito().bajaRack(rack);
    }

    @Override
    public ArrayList<Rack> listarRacks() throws Exception {
        return FabricaPersistencia.getControladorDeposito().listarRacks();
    }

    //endregion

    //region Lote

    private void ValidarLote(Lote lote) throws ExcepcionFrigorifico{
        if(lote == null){
            throw new ExcepcionFrigorifico("¡ERROR! El lote no puede ser nulo.");
        }

        if(lote.getId() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! El id no puede ser menor que 0.");
        }

        if(lote.getCantUnidades() <= 0) {
            throw new ExcepcionFrigorifico("¡ERROR! La cantidad de unidades no puede ser menor o igual que 0.");
        }

        int c = lote.getFechaVencimiento().compareTo(lote.getFechaIngreso());
        if(c <= 0){
            throw new ExcepcionFrigorifico("¡ERROR! La fecha de vencimiento debe ser posterior a la fecha de hoy");
        }

        ValidarEspecificacionProducto(lote.getProducto());
        ValidarRack(lote.getUbicacion().getRack());

        if(lote.getUbicacion().getFila() < 0 || lote.getUbicacion().getColumna() < 0){
            throw new ExcepcionFrigorifico("¡ERROR! La ubicación del lote no es válida.");
        }

        if(lote.getUbicacion().getFila() > lote.getUbicacion().getRack().getDimAlto() || lote.getUbicacion().getColumna() > lote.getUbicacion().getRack().getDimAncho()){
            throw new ExcepcionFrigorifico("¡ERROR! La fila y columna de la ubicacion debe estar dentro de las dimensiones del rack: " + lote.getUbicacion().getRack().getDimAlto() + "x" + lote.getUbicacion().getRack().getDimAncho());
        }
    }

    @Override
    public int altaLote(Lote lote) throws Exception {
        ValidarLote(lote);
        if(!esUbicacionVacia(lote.getUbicacion())){
            throw new ExcepcionFrigorifico("¡ERROR! La ubicacion está ocupada");
        }
        return FabricaPersistencia.getControladorDeposito().altaLote(lote);
    }

    @Override
    public ArrayList<Lote> obtenerLotesVencidos() throws Exception {
        return FabricaPersistencia.getControladorDeposito().obtenerLotesVencidos();
    }

    @Override
    public void bajaLote(Lote lote) throws Exception {
        FabricaPersistencia.getControladorDeposito().bajaLote(lote);
    }

    @Override
    public Lote buscarLote(int id) throws Exception {
        return FabricaPersistencia.getControladorDeposito().buscarLote(id);
    }

    @Override
    public void moverLote(int idLote, String ubicacion) throws Exception {
        try {
            String letraRack = ubicacion.substring(0,1);
            int fila = Integer.parseInt(ubicacion.substring(1,3));
            int columna = Integer.parseInt(ubicacion.substring(3,5));

            Ubicacion nuevaUbicacion = new Ubicacion(fila, columna, new Rack(letraRack, 0, 0));
            Lote lote = new Lote(idLote, new Date(), new Date(), 0, new EspecificacionProducto(), nuevaUbicacion);

            FabricaPersistencia.getControladorDeposito().moverLote(lote);

        }catch(Exception ex){
            throw ex;
        }
    }


    private ArrayList<Lote> listarLotesXRack(String letra) throws Exception {
        return FabricaPersistencia.getControladorDeposito().listarLotesXRack(letra);
    }

    @Override
    public ArrayList<ArrayList<Lote>> obtenerRack(Rack rack) throws Exception{
        ArrayList<ArrayList<Lote>> rackResultado = new ArrayList<ArrayList<Lote>>();

        ArrayList<Lote> lotes = listarLotesXRack(rack.getLetra());

        for(int i = 1; i<=rack.getDimAlto();i++){
            ArrayList<Lote> fila = new ArrayList<>();
            for(int j = 1; j<=rack.getDimAncho();j++){
                Lote lote = new Lote();
                lote.setUbicacion(new Ubicacion(i, j, rack));
                for (Lote l : lotes) {
                    if(l.getUbicacion().getFila()>i || (l.getUbicacion().getFila()==i && l.getUbicacion().getColumna()>j)){
                        break;
                    }
                    if(l.getUbicacion().getFila()==i && l.getUbicacion().getColumna()==j){
                        lote = l;
                        break;
                    }
                }
                fila.add(lote);
            }
            rackResultado.add(fila);
        }
        return  rackResultado;
    }

    @Override
    public void deshacerBajaLogicaLote(Lote lote) throws Exception {
        FabricaPersistencia.getControladorDeposito().deshacerBajaLogicaLote(lote);
    }

    @Override
    public void actualizarStock(Lote lote, int cant) throws Exception {
        FabricaPersistencia.getControladorDeposito().actualizarStock(lote,cant);
    }

    //endregion
}
