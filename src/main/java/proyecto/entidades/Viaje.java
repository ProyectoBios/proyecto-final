package proyecto.entidades;

import java.util.ArrayList;
import java.util.Date;

public class Viaje {
    private int id;

    private Repartidor repartidor;
    private Vehiculo vehiculo;
    private ArrayList<OrdenPedido> pedidos;
    private Date fechaHora;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public ArrayList<OrdenPedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<OrdenPedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Viaje(int id, Repartidor repartidor, Vehiculo vehiculo, ArrayList<OrdenPedido> pedidos, Date fechaHora) {
        this.id = id;
        this.repartidor = repartidor;
        this.vehiculo = vehiculo;
        this.pedidos = pedidos;
        this.fechaHora = fechaHora;
    }

    public Viaje() throws Exception{
        this(0, new Repartidor(), new Vehiculo(), new ArrayList<>(), new Date());
    }
}
