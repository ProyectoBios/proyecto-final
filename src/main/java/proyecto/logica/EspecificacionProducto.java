package proyecto.logica;

class EspecificacionProducto {
    private int codigo;
    private String nombre;
    private int minStock;
    private int stockCritico;
    private int maxStock;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public int getStockCritico() {
        return stockCritico;
    }

    public void setStockCritico(int stockCritico) {
        this.stockCritico = stockCritico;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }

    public EspecificacionProducto(int codigo, String nombre, int minStock, int stockCritico, int maxStock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.minStock = minStock;
        this.stockCritico = stockCritico;
        this.maxStock = maxStock;
    }

    public EspecificacionProducto() {
        this(0, "N/D", 0, 0, 0);
    }
}
