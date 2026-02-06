package ar.edu.uade.daos;

public class UnidadDTO {

    private String piso;
    private String numero;
    private Boolean habitado;
    private Integer idEdificio; // Este campo nos permitirá asociar la unidad con un edificio

    // Getters y setters

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Boolean getHabitado() {
        return habitado;
    }

    public void setHabitado(Boolean habitado) {
        this.habitado = habitado;
    }

    public Integer getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(Integer idEdificio) {
        this.idEdificio = idEdificio;
    }
}
