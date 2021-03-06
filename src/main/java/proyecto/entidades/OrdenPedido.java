package proyecto.entidades;

import java.util.ArrayList;
import java.util.Date;

public class OrdenPedido {
    private int id;
    private Date fecha;
    private String estado;
    private String descripcionEntrega;

    private Date ultimaActEst;

    private String direccionEnvio;
    private String contacto;
    private double subtotal;
    private double impuestos;
    private double total;

    private Cliente cliente;

    private Empleado operador;
    private Empleado funcionario;
    private Empleado repartidor;

    private ArrayList<LineaPedido> lineas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcionEntrega() {
        return descripcionEntrega;
    }

    public void setDescripcionEntrega(String descripcionEntrega) {
        this.descripcionEntrega = descripcionEntrega;
    }

    public Date getUltimaActEst() {
        return ultimaActEst;
    }

    public void setUltimaActEst(Date ultimaActEst) {
        this.ultimaActEst = ultimaActEst;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
        this.impuestos = subtotal*0.22;
        this.total = subtotal+impuestos;
    }
    public double getImpuestos() {
        return impuestos;
    }

    public double getTotal() {
        return total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(ArrayList<LineaPedido> lineas) {
        this.lineas = lineas;
    }

    public Empleado getOperador() {
        return operador;
    }

    public void setOperador(Empleado operador) {
        this.operador = operador;
    }

    public Empleado getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Empleado funcionario) {
        this.funcionario = funcionario;
    }


    public Empleado getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Empleado repartidor) {
        this.repartidor = repartidor;
    }

    public OrdenPedido(int id, Date fecha, String estado, String descripcionEntrega, Date ultimaActEst, String direccionEnvio, String contacto, double subtotal, Cliente cliente, Empleado operador, Empleado funcionario, Empleado repartidor, ArrayList<LineaPedido> lineas) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.descripcionEntrega = descripcionEntrega;
        this.ultimaActEst = ultimaActEst;
        this.direccionEnvio = direccionEnvio;
        this.contacto = contacto;
        this.setSubtotal(subtotal);
        this.cliente = cliente;
        this.operador = operador;
        this.funcionario = funcionario;
        this.repartidor = repartidor;
        this.lineas = lineas;
    }

    public OrdenPedido() {
        this(0, new Date(), "N/D", "", new Date(), "", "", 0, null, null, null, null, new ArrayList<LineaPedido>());
    }
}
