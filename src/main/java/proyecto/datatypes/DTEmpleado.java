package proyecto.datatypes;

import java.util.Date;

public class DTEmpleado {
    private String ci;
    private String nombre;
    private int edad;
    private Date fechaContratacion;
    private String telefono;

    private String rol;

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public DTEmpleado(String ci, String nombre, int edad, Date fechaContratacion, String telefono, String rol) {
        this.ci = ci;
        this.nombre = nombre;
        this.edad = edad;
        this.fechaContratacion = fechaContratacion;
        this.telefono = telefono;
        this.rol = rol;
    }

    public DTEmpleado() {
        this("", "", 0, new Date(), "", "");
    }
}

