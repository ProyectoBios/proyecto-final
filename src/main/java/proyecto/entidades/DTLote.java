package proyecto.entidades;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DTLote {
    private int id;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaIngreso;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaVencimiento;


    private int cantUnidades;

    private DTEspecificacionProducto producto;
    private DTUbicacion ubicacion;

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

    public DTEspecificacionProducto getProducto() {
        return producto;
    }

    public void setProducto(DTEspecificacionProducto producto) {
        this.producto = producto;
    }

    public DTUbicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(DTUbicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public DTLote(int id, Date fechaIngreso, Date fechaVencimiento, int cantUnidades, DTEspecificacionProducto producto, DTUbicacion ubicacion) {
        this.id = id;
        this.fechaIngreso = fechaIngreso;
        this.fechaVencimiento = fechaVencimiento;
        this.cantUnidades = cantUnidades;
        this.producto = producto;
        this.ubicacion = ubicacion;
    }

    public DTLote() {
        this(0, new Date(), new Date(), 0, new DTEspecificacionProducto(), new DTUbicacion());
    }
}
