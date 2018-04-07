package proyecto.entidades;

import java.util.Date;

public class Repartidor extends Empleado{
    private Date vencLibreta;

    public Date getVencLibreta() {
        return vencLibreta;
    }

    public void setVencLibreta(Date vencLibreta) {
        this.vencLibreta = vencLibreta;
    }

    public Repartidor(String ci, String nombre, int edad, Date fechaContratacion, String telefono, String rol, Date vencLibreta) {
        super(ci, nombre, edad, fechaContratacion, telefono, rol);
        this.vencLibreta = vencLibreta;
    }

    public Repartidor(){
        super();
        this.vencLibreta = new Date();
    }
}
