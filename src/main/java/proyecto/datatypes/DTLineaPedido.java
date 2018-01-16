package proyecto.datatypes;

public class DTLineaPedido {
    private int numero;
    private int cantidad;
    private double importe;

    private DTEspecificacionProducto producto;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public DTEspecificacionProducto getProducto() {
        return producto;
    }

    public void setProducto(DTEspecificacionProducto producto) {
        this.producto = producto;
    }

    public DTLineaPedido(int numero, int cantidad, double importe, DTEspecificacionProducto producto) {
        this.numero = numero;
        this.cantidad = cantidad;
        this.importe = importe;
        this.producto = producto;
    }

    public DTLineaPedido(int numero, int cantidad, DTEspecificacionProducto producto) {
        this.numero = numero;
        this.cantidad = cantidad;
        this.producto = producto;
        this.importe = cantidad * producto.getPrecioActual();
    }

    public DTLineaPedido() {
        this(0, 0, 0, null);
    }
}
