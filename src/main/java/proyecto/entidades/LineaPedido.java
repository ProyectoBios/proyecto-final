package proyecto.entidades;

public class LineaPedido {
    private int numero;
    private int cantidad;
    private double importe;

    private EspecificacionProducto producto;

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

    public EspecificacionProducto getProducto() {
        return producto;
    }

    public void setProducto(EspecificacionProducto producto) {
        this.producto = producto;
    }

    public LineaPedido(int numero, int cantidad, double importe, EspecificacionProducto producto) {
        this.numero = numero;
        this.cantidad = cantidad;
        this.importe = importe;
        this.producto = producto;
    }

    public LineaPedido(int numero, int cantidad, EspecificacionProducto producto) {
        this.numero = numero;
        this.cantidad = cantidad;
        this.producto = producto;
        this.importe = cantidad * producto.getPrecioActual();
    }

    public LineaPedido() {
        this(0, 0, 0, null);
    }
}
