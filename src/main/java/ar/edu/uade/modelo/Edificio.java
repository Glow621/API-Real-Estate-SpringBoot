package ar.edu.uade.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Edificio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    private String nombre;
    private String direccion;

	@JsonManagedReference(value="edificio-unidad")
    @OneToMany(mappedBy = "edificio", cascade = CascadeType.ALL)
    private Set<Unidad> unidades = new HashSet<>();

    public void agregarUnidad(Unidad unidad) {
        unidad.setEdificio(this);
        this.unidades.add(unidad);
    }

    public Set<Unidad> habilitados() {
        Set<Unidad> habitadas = new HashSet<>();
        for (Unidad unidad : unidades) {
            if (unidad.estaHabitado()) {
                habitadas.add(unidad);
            }
        }
        return habitadas;
    }

    public Set<Persona> duenos() {
        Set<Persona> propietarios = new HashSet<>();
        for (Unidad unidad : unidades) {
            propietarios.addAll(unidad.getPropietarios());
        }
        return propietarios;
    }

    public Set<Persona> habitantes() {
        Set<Persona> habitantes = new HashSet<>();
        for (Unidad unidad : unidades) {
            habitantes.addAll(unidad.getHabitantes());
        }
        return habitantes;
    }

    public Set<Persona> inquilinos() {
        Set<Persona> inquilinos = new HashSet<>();
        for (Unidad unidad : unidades) {
            if (unidad.getInquilino() != null) {
                inquilinos.add(unidad.getInquilino());
            }
        }
        return inquilinos;
    }




	public Edificio() {
	}

	public Edificio(Integer codigo, String nombre, String direccion) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.direccion = direccion;
	}

	public Edificio(Integer codigo, String nombre, String direccion, Set<Unidad> unidades) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.direccion = direccion;
		this.unidades = unidades;
	}


	// get y setters

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Set<Unidad> getUnidades() {
		return unidades;
	}

	public void setUnidades(Set<Unidad> unidades) {
		this.unidades = unidades;
	}

   
}
