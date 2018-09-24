package proyecto.entidades;

import java.util.ArrayList;
import java.util.Date;

public class EspecificacionProducto {
    private int codigo;
    private String nombre;
    private int minStock;
    private int stockCritico;
    private int maxStock;

    private ArrayList<Precio> historicoPrecios;


    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws ExcepcionFrigorifico{
        if(nombre.equals("")){
            throw new ExcepcionFrigorifico("El nombre no puede quedar vacio.");
        }
        if (nombre.length() > 40){
            throw new ExcepcionFrigorifico("Debe especificar un nombre menor a 40 caractéres");
        }
        this.nombre = nombre;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) throws ExcepcionFrigorifico{
        if (minStock < 0){
            throw new ExcepcionFrigorifico("El Stock Mínimo debe ser mayor a 0");
        }
        this.minStock = minStock;
    }

    public int getStockCritico() {
        return stockCritico;
    }

    public void setStockCritico(int stockCritico) throws ExcepcionFrigorifico{
        if (stockCritico < 0){
            throw new ExcepcionFrigorifico("El Stock Crítico debe ser mayor a 0");
        }
        this.stockCritico = stockCritico;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(int maxStock) throws ExcepcionFrigorifico{
        if (maxStock < 0){
            throw new ExcepcionFrigorifico("El Stock Máximo debe ser mayor a 0");
        }
        this.maxStock = maxStock;
    }

    public ArrayList<Precio> getHistoricoPrecios() {
        return historicoPrecios;
    }

    public void setHistoricoPrecios(ArrayList<Precio> historicoPrecios) {
        this.historicoPrecios = historicoPrecios;
    }

    public double getPrecioActual(){
        if(historicoPrecios.size() > 0){
            return historicoPrecios.get(0).getPrecio();
        }else{
            return 0;
        }
    }

    public void setPrecioActual(double precio) throws ExcepcionFrigorifico {
        if(precio <= 0){
            throw new ExcepcionFrigorifico("El precio no puede ser menor o igual que 0.");
        }

        Precio p = new Precio(precio, new Date());
        if (historicoPrecios.size() > 0){
            historicoPrecios.get(0).setFechaFin(new Date());
        }

        historicoPrecios.add(p);
    }

    public EspecificacionProducto(int codigo, String nombre, int minStock, int stockCritico, int maxStock, ArrayList<Precio> historico) throws ExcepcionFrigorifico{
        this.setCodigo(codigo);
        this.setNombre(nombre);
        this.setMinStock(minStock);
        this.setStockCritico(stockCritico);
        this.setMaxStock(maxStock);
        this.setHistoricoPrecios(historico);
    }

    public EspecificacionProducto(){
        this.codigo = -1;
        this.nombre = "";
        this.minStock = 0;
        this.stockCritico = 0;
        this.maxStock = 0;
        this.historicoPrecios = new ArrayList<Precio>();
    }
}
