package proyecto.datatypes;

public class DTUbicacion {
    private int fila;
    private int columna;

    private DTRack rack;

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

    public DTRack getRack() {
        return rack;
    }

    public void setRack(DTRack rack) {
        this.rack = rack;
    }

    public DTUbicacion(int fila, int columna, DTRack rack) {
        this.fila = fila;
        this.columna = columna;
        this.rack = rack;
    }

    public DTUbicacion() {
        this(0,0, new DTRack());
    }
}
