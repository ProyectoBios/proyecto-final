package proyecto.entidades;

import java.util.ArrayList;

public class Viaje {
    private int id;

    private Repartidor repartidor;
    private Vehiculo vehiculo;
    private ArrayList<OrdenPedido> pedidos;

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

    public Viaje(int id, Repartidor repartidor, Vehiculo vehiculo, ArrayList<OrdenPedido> pedidos) {
        this.id = id;
        this.repartidor = repartidor;
        this.vehiculo = vehiculo;
        this.pedidos = pedidos;
    }

    public Viaje() {
        this(0, new Repartidor(), new Vehiculo(), new ArrayList<>());
    }
}
