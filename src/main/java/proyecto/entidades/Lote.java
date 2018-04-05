package proyecto.entidades;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Lote {
    private int id;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaIngreso;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaVencimiento;


    private int cantUnidades;

    private EspecificacionProducto producto;
    private Ubicacion ubicacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getCantUnidades() {
        return cantUnidades;
    }

    public void setCantUnidades(int cantUnidades) {
        this.cantUnidades = cantUnidades;
    }

    public EspecificacionProducto getProducto() {
        return producto;
    }

    public void setProducto(EspecificacionProducto producto) {
        this.producto = producto;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Lote(int id, Date fechaIngreso, Date fechaVencimiento, int cantUnidades, EspecificacionProducto producto, Ubicacion ubicacion) {
        this.id = id;
        this.fechaIngreso = fechaIngreso;
        this.fechaVencimiento = fechaVencimiento;
        this.cantUnidades = cantUnidades;
        this.producto = producto;
        this.ubicacion = ubicacion;
    }

    public Lote() {
        this(0, new Date(), new Date(), 0, new EspecificacionProducto(), new Ubicacion());
    }
}
