package proyecto.entidades;

import java.util.ArrayList;

public class Picking {
    private EspecificacionProducto producto;
    private int cantidad;
    private ArrayList<Lote> lotes;

    public EspecificacionProducto getProducto() {
        return producto;
    }

    public void setProducto(EspecificacionProducto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public ArrayList<Lote> getLotes() {
        return lotes;
    }

    public void setLotes(ArrayList<Ubicacion> ubicaciones) {
        this.lotes = lotes;
    }

    public int getCantidadUnidadesTotal(){
        int cantidades = 0;
        for(Lote l : this.lotes){
            cantidades += l.getCantUnidades();
        }
        return cantidades;
    }

    public String getUbicacionesString(){
        String ubicaciones = "";
        for(Lote l : this.lotes){
            ubicaciones+= l.getUbicacion().getUbicacionString() + ", ";
        }
        return ubicaciones.substring(0, ubicaciones.length() - 2);
    }

    public Picking(EspecificacionProducto producto, int cantidad, ArrayList<Lote> lotes) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.lotes = lotes;
    }

    public Picking() {
        this(new EspecificacionProducto(), 0, new ArrayList<>());
    }
}
