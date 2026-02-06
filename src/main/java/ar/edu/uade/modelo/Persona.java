package ar.edu.uade.modelo;


import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "documento")
public class Persona {

    @Id
    @JsonView(Views.Basic.class)
    private String documento;
    @JsonView(Views.Basic.class)
    private String nombre;

    @JsonIgnore
    private String password;

    private int rolUsuario;

    @ManyToMany(mappedBy = "propietarios")
    @JsonIgnore
    
    private Set<Unidad> propiedades = new HashSet<>();

    
    @ManyToMany(mappedBy = "habitantes")
    @JsonIgnore

    private Set<Unidad> habitadas = new HashSet<>();


    // Constructor y métodos adicionales

    public Persona(String documento, String nombre, int rolUsuario,String password) {
        this.documento = documento;
        this.nombre = nombre;
        this.rolUsuario= rolUsuario;
        this.password = password;
    }

    
    public Persona(String documento, String nombre,int rolUsuario, Set<Unidad> propiedades, Set<Unidad> habitadas) {
        this.documento = documento;
        this.nombre = nombre;
        this.rolUsuario= rolUsuario;
        this.propiedades = propiedades;
        this.habitadas = habitadas;
    }


    


    public Persona() {
    }




    
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Unidad> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(Set<Unidad> propiedades) {
        this.propiedades = propiedades;
    }

    public Set<Unidad> getHabitadas() {
        return habitadas;
    }

    public void setHabitadas(Set<Unidad> habitadas) {
        this.habitadas = habitadas;
    }


        // Sobrescribir el método toString()
        @Override
        public String toString() {
            return "Persona{" +
                    "documento='" + documento + '\'' +
                    ", nombre='" + nombre + '\'' +
                    ", propiedades=" + propiedades +
                    ", habitadas=" + habitadas +
                    '}';
        }


        public String getPassword() {
            return password;
        }


        public void setPassword(String password) {
            this.password = password;
        }


        public int getRolUsuario() {
            return rolUsuario;
        }


        public void setRolUsuario(int rolUsuario) {
            this.rolUsuario = rolUsuario;
        }

    
}
