package proyecto.entidades;

import java.util.Date;

public class Empleado {
    private String ci;
    private String nombre;
    private String contrasenia;
    private Date fechaDeNacimiento;
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

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Date getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
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

    public Empleado(String ci, String nombre, String contrasenia, Date fechaDeNacimiento, Date fechaContratacion, String telefono, String rol) {
        this.ci = ci;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.fechaContratacion = fechaContratacion;
        this.telefono = telefono;
        this.rol = rol;
    }

    public Empleado() {
        this("", "", "", new Date(), new Date(), "", "");
    }
}

