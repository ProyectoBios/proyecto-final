package proyecto.entidades;

public class Rack {
    private String letra;
    private int dimAlto;
    private int dimAncho;

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) throws ExcepcionFrigorifico{
        if(letra.isEmpty()) {
            throw new ExcepcionFrigorifico("La letra del rack no puede quedar vacía.");
        }
        if(!letra.matches("[A-Z]")) {
            throw new ExcepcionFrigorifico("La letra del rack debe ser una letra mayúscula.");
        }
        this.letra = letra;
    }

    public int getDimAlto() {
        return dimAlto;
    }

    public void setDimAlto(int dimAlto) throws ExcepcionFrigorifico{
        if (dimAlto <= 0) {
            throw new ExcepcionFrigorifico("El alto debe ser mayor a cero");
        }
        this.dimAlto = dimAlto;
    }

    public int getDimAncho() {
        return dimAncho;
    }

    public void setDimAncho(int dimAncho) throws ExcepcionFrigorifico{
        if (dimAncho <= 0) {
            throw new ExcepcionFrigorifico("El ancho debe ser mayor a cero");
        }
        this.dimAncho = dimAncho;
    }

    public Rack(String letra, int dimAlto, int dimAncho) throws ExcepcionFrigorifico{
        this.setLetra(letra);
        this.setDimAlto(dimAlto);
        this.setDimAncho(dimAncho);
    }

    public Rack(){
        this.letra = "-";
        this.dimAlto = 0;
        this.dimAncho = 0;
    }
}
