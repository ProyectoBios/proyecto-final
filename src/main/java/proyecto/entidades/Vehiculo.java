package proyecto.entidades;

public class Vehiculo {
    private String matricula;
    private String marca;
    private String modelo;
    private int cargaMax;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) throws ExcepcionFrigorifico{
        if (matricula.length() > 8){
            throw new ExcepcionFrigorifico("La matrícula no debe tener más de 8 carecteres");
        }

        if (!matricula.matches("[A-Z]{3} [0-9]{4}")){
            throw new ExcepcionFrigorifico("La matrícula debe tener el formato AAA 1234");
        }
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) throws ExcepcionFrigorifico{
        if (marca.isEmpty()){
            throw new ExcepcionFrigorifico("La marca no puede quedar vacía");
        }
        if (marca.length() > 20) {
            throw new ExcepcionFrigorifico("La marca no debe tener más de 20 caracteres");
        }
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) throws ExcepcionFrigorifico{
        if (modelo.isEmpty()) {
            throw new ExcepcionFrigorifico("El modelo no puede quedar vacíó");
        }
        if (modelo.length() > 20){
            throw new ExcepcionFrigorifico("El modelo no debe tener más de 20 caracteres");
        }
        this.modelo = modelo;
    }

    public int getCargaMax() {
        return cargaMax;
    }

    public void setCargaMax(int cargaMax) throws ExcepcionFrigorifico{
        if (cargaMax <= 0){
            throw new ExcepcionFrigorifico("La carga máxima debe ser mayor a cero");
        }
        this.cargaMax = cargaMax;
    }

    public Vehiculo(String matricula, String marca, String modelo, int cargaMax) throws ExcepcionFrigorifico{
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.cargaMax = cargaMax;
    }

    public Vehiculo() throws ExcepcionFrigorifico{
        this("", "", "", 0);
    }
}
