package ar.edu.uade.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Unidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String piso;
    private String numero;
    private Boolean habitado;

	@JsonBackReference(value="edificio-unidad")
    @ManyToOne
    @JoinColumn(name = "edificio_id")
    private Edificio edificio;


    @ManyToMany
    @JoinTable(
        name = "unidad_propietario",
        joinColumns = @JoinColumn(name = "unidad_id"),
        inverseJoinColumns = @JoinColumn(name = "persona_id")
    )
	
    private Set<Persona> propietarios = new HashSet<>();


    @ManyToMany
	@JsonIgnore /// 
    @JoinTable(
        name = "unidad_habitante",
        joinColumns = @JoinColumn(name = "unidad_id"),
        inverseJoinColumns = @JoinColumn(name = "persona_id")
    )
    private Set<Persona> habitantes = new HashSet<>();

	
    @ManyToOne
    @JoinColumn(name = "inquilino_id")
    private Persona inquilino;

	@JsonManagedReference(value="unidad-reclamos")
    @OneToMany(mappedBy = "unidad", cascade = CascadeType.ALL)
    private Set<Reclamo> reclamos = new HashSet<>();

    public Unidad(String piso, String numero, boolean habitado, Edificio edificio) {
        this.piso = piso;
        this.numero = numero;
        this.habitado = habitado;
        this.edificio = edificio;
    }

	

	public Unidad() {
	}


	// Métodos específicos de la unidad
    public void transferir(Persona nuevoDueno) {
        this.propietarios.clear();
        this.propietarios.add(nuevoDueno);
		this.habitado=false;
    }

    public void agregarDueno(Persona dueno) {
        this.propietarios.add(dueno);
    }

    public void alquilar(Persona inquilino) {
        this.inquilino = inquilino;
        this.habitado = true;
    }

    public void agregarInquilino(Persona inquilino) {
        this.inquilino = inquilino;
    }

    public boolean estaHabitado() {
        return habitado;
    }

    public void liberar() {
        this.inquilino = null;
        this.habitado = false;
    }

    public void habitar() {
        this.habitado = true;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Edificio getEdificio() {
		return edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

	public Set<Persona> getPropietarios() {
		return propietarios;
	}

	public void setPropietarios(Set<Persona> propietarios) {
		this.propietarios = propietarios;
	}

	public Set<Persona> getHabitantes() {
		return habitantes;
	}

	public void setHabitantes(Set<Persona> habitantes) {
		this.habitantes = habitantes;
	}

	public Persona getInquilino() {
		return inquilino;
	}

	public void setInquilino(Persona inquilino) {
		this.inquilino = inquilino;
	}

	public Set<Reclamo> getReclamos() {
		return reclamos;
	}

	public void setReclamos(Set<Reclamo> reclamos) {
		this.reclamos = reclamos;
	}

    
}
