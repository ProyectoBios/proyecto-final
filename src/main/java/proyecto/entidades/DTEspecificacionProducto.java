package proyecto.entidades;

import java.util.ArrayList;
import java.util.Date;

public class DTEspecificacionProducto {
    private int codigo;
    private String nombre;
    private int minStock;
    private int stockCritico;
    private int maxStock;

    private ArrayList<DTPrecio> historicoPrecios;


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

    public ArrayList<DTPrecio> getHistoricoPrecios() {
        return historicoPrecios;
    }

    public void setHistoricoPrecios(ArrayList<DTPrecio> historicoPrecios) {
        this.historicoPrecios = historicoPrecios;
    }

    public double getPrecioActual(){
        if(historicoPrecios.size() > 0){
            return historicoPrecios.get(0).getPrecio();
        }else{
            return 0;
        }
    }

    public void setPrecioActual(double precio){
        DTPrecio p = new DTPrecio(precio, new Date());
        if (historicoPrecios.size() > 0){
            historicoPrecios.get(0).setFechaFin(new Date());
        }

        historicoPrecios.add(p);
    }

    public DTEspecificacionProducto(int codigo, String nombre, int minStock, int stockCritico, int maxStock, ArrayList<DTPrecio> historico) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.minStock = minStock;
        this.stockCritico = stockCritico;
        this.maxStock = maxStock;
        this.historicoPrecios = historico;
    }

    public DTEspecificacionProducto() {
        this(0, "N/D", 0, 0, 0, new ArrayList<DTPrecio>());
    }
}
