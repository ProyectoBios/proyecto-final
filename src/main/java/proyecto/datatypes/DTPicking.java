package proyecto.datatypes;

import java.util.ArrayList;

public class DTPicking {
    private DTEspecificacionProducto producto;
    private int cantidad;
    private ArrayList<DTLote> lotes;

    public DTEspecificacionProducto getProducto() {
        return producto;
    }

    public void setProducto(DTEspecificacionProducto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public ArrayList<DTLote> getLotes() {
        return lotes;
    }

    public void setLotes(ArrayList<DTUbicacion> ubicaciones) {
        this.lotes = lotes;
    }

    public int getCantidadUnidadesTotal(){
        int cantidades = 0;
        for(DTLote l : this.lotes){
            cantidades += l.getCantUnidades();
        }
        return cantidades;
    }

    public String getUbicacionesString(){
        String ubicaciones = "";
        for(DTLote l : this.lotes){
            ubicaciones+= l.getUbicacion().getUbicacionString() + ", ";
        }
        return ubicaciones.substring(0, ubicaciones.length() - 2);
    }

    public DTPicking(DTEspecificacionProducto producto, int cantidad, ArrayList<DTLote> lotes) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.lotes = lotes;
    }

    public DTPicking() {
        this(new DTEspecificacionProducto(), 0, new ArrayList<>());
    }
}
