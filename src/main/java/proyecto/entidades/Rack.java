package proyecto.entidades;

public class Rack {
    private String letra;
    private int dimAlto;
    private int dimAncho;

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public int getDimAlto() {
        return dimAlto;
    }

    public void setDimAlto(int dimAlto) {
        this.dimAlto = dimAlto;
    }

    public int getDimAncho() {
        return dimAncho;
    }

    public void setDimAncho(int dimAncho) {
        this.dimAncho = dimAncho;
    }

    public Rack(String letra, int dimAlto, int dimAncho) {
        this.letra = letra;
        this.dimAlto = dimAlto;
        this.dimAncho = dimAncho;
    }

    public Rack() {
        this("-", 0, 0);
    }
}
