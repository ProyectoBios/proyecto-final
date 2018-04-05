package proyecto.entidades;

public class Ubicacion {
    private int fila;
    private int columna;

    private Rack rack;

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    public String getUbicacionString(){
        String ubicacion = "";

        ubicacion+= this.getRack().getLetra();
        if(this.getFila() <= 9){
            ubicacion+="0" + this.getFila();
        }else{
            ubicacion+=this.getFila();
        }

        if(this.getColumna() <= 9){
            ubicacion+="0" + this.getColumna();
        }else{
            ubicacion+=this.getColumna();
        }

        return ubicacion;
    }

    public Ubicacion(int fila, int columna, Rack rack) {
        this.fila = fila;
        this.columna = columna;
        this.rack = rack;
    }

    public Ubicacion() {
        this(0,0, new Rack());
    }
}
