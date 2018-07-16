package proyecto.entidades;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Empleado {
    private String ci;
    private String nombre;
    private String contrasenia;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaDeNacimiento;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaContratacion;

    private String telefono;

    private String rol;

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) throws ExcepcionFrigorifico{
        if(ci.length() > 8){
            throw new ExcepcionFrigorifico("La cédula no puede tener más de 8 caracteres.");
        }
        this.ci = ci;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) throws ExcepcionFrigorifico{
        if(nombre.length() > 30){
            throw new ExcepcionFrigorifico("El nombre no puede tener más de 30 caracteres.");
        }

        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) throws ExcepcionFrigorifico{
        if(contrasenia.length() > 64){
            throw new ExcepcionFrigorifico("La contraseña no puede tener más de 64 caracteres.");
        }

        this.contrasenia = contrasenia;
    }

    public Date getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) throws ExcepcionFrigorifico{
        if(fechaDeNacimiento.after(new Date())){
            throw new ExcepcionFrigorifico("La fecha de nacimiento debe ser anterior a la fecha actual.");
        }

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

    public void setTelefono(String telefono) throws ExcepcionFrigorifico{
        if(telefono.length()>10){
            throw new ExcepcionFrigorifico("El telefono no puede tener más de 10 caracteres.");
        }

        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) throws ExcepcionFrigorifico{
        if(!rol.equals("gerente") && !rol.equals("funcionario") && !rol.equals("operador") && !rol.equals("repartidor") && !rol.equals("")){
            throw new ExcepcionFrigorifico("El rol no es válido.");
        }

        this.rol = rol;
    }

    public Empleado(String ci, String nombre, String contrasenia, Date fechaDeNacimiento, Date fechaContratacion, String telefono, String rol) throws ExcepcionFrigorifico{
        this.setCi(ci);
        this.setNombre(nombre);
        this.setContrasenia(contrasenia);
        this.setFechaDeNacimiento(fechaDeNacimiento);
        this.setFechaContratacion(fechaContratacion);
        this.setTelefono(telefono);
        this.setRol(rol);
    }

    public Empleado() throws ExcepcionFrigorifico{
        this("", "", "", new Date(), new Date(), "", "");
    }
}

