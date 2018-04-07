package proyecto.entidades;

public class Vehiculo {
    private String matricula;
    private String marca;
    private String modelo;
    private int cargaMax;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCargaMax() {
        return cargaMax;
    }

    public void setCargaMax(int cargaMax) {
        this.cargaMax = cargaMax;
    }

    public Vehiculo(String matricula, String marca, String modelo, int cargaMax) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.cargaMax = cargaMax;
    }

    public Vehiculo() {
        this("", "", "", 0);
    }
}
