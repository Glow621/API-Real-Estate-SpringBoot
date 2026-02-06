package ar.edu.uade.modelo;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nroReclamo")
public class Reclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nroReclamo;

    private String ubicacion;
    private String descripcion;
    private String estado;

	@ManyToOne
    @JoinColumn(name = "persona_queja")
    private Persona personaReclamo;


	@JsonBackReference(value="unidad-reclamos")
    @ManyToOne
    @JoinColumn(name = "unidad_id")
    private Unidad unidad;

	@JsonManagedReference(value="reclamo-imagenes")
    @OneToMany(mappedBy = "reclamo", cascade = CascadeType.ALL)
    private Set<Imagen> imagenes = new HashSet<>();

	private String tipoReclamo;


    public Reclamo(String string, String string2, String string3, Unidad unidad3) {}

    public Reclamo(String ubicacion, String descripcion, String estado, Unidad unidad, String tipoReclamo, Persona personaReclamo) {
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.estado = estado;
        this.unidad = unidad;
        this.tipoReclamo = tipoReclamo;
		this.personaReclamo = personaReclamo;
    }

	

	public Reclamo(Integer nroReclamo, String ubicacion, String descripcion, String estado, Unidad unidad,
			Persona personaReclamo) {
		this.nroReclamo = nroReclamo;
		this.ubicacion = ubicacion;
		this.descripcion = descripcion;
		this.estado = estado;
		this.unidad = unidad;
		this.personaReclamo = personaReclamo;
	}

	public Reclamo() {
        //TODO Auto-generated constructor stub
    }

    public void agregarFoto(String direccion, String tipo) {
        Imagen imagen = new Imagen(direccion, tipo, this);
        this.imagenes.add(imagen);
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }




	public Integer getNroReclamo() {
		return nroReclamo;
	}

	public void setNroReclamo(Integer nroReclamo) {
		this.nroReclamo = nroReclamo;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Unidad getUnidad() {
		return unidad;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	public Set<Imagen> getImagenes() {
		return imagenes;
	}

	public void setImagenes(Set<Imagen> imagenes) {
		this.imagenes = imagenes;
	}

	public Persona getPersonaReclamo() {
		return personaReclamo;
	}

	public void setPersonaReclamo(Persona personaReclamo) {
		this.personaReclamo = personaReclamo;
	}

	public String getTipoReclamo() {
		return tipoReclamo;
	}

	public void setTipoReclamo(String tipoReclamo) {
		this.tipoReclamo = tipoReclamo;
	}

    



}
