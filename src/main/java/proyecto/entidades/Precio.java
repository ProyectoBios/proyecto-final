package proyecto.entidades;

import java.util.Date;

public class Precio {
    private double precio;
    private Date fechaIni;
    private Date fechaFin;

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Precio(double precio, Date fechaIni, Date fechaFin) {
        this.precio = precio;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
    }

    public Precio(double precio, Date fechaIni) {
        this.precio = precio;
        this.fechaIni = fechaIni;
        this.fechaFin = null;
    }
}
