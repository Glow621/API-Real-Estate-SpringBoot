package ar.edu.uade.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numero;

    private String direccion;
    private String tipo;

    @JsonBackReference(value="reclamo-imagenes")
    @ManyToOne
    @JoinColumn(name = "reclamo_id")
    private Reclamo reclamo;

    public Imagen(String direccion, String tipo, Reclamo reclamo) {
        this.direccion = direccion;
        this.tipo = tipo;
        this.reclamo = reclamo;
    }


    

    public Imagen() {
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Reclamo getReclamo() {
        return reclamo;
    }

    public void setReclamo(Reclamo reclamo) {
        this.reclamo = reclamo;
    }

    
}
