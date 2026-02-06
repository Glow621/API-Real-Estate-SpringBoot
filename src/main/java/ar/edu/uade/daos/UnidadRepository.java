package ar.edu.uade.daos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.uade.modelo.Unidad;

import java.util.List;

@Repository
public interface UnidadRepository extends JpaRepository<Unidad, Integer> {
    List<Unidad> findByEdificioCodigo(int edificioCodigo);
    List<Unidad> findByHabitado(boolean habitado);
        // Consulta personalizada para encontrar unidades donde una persona es propietario, habitante o inquilino
    @Query("SELECT u FROM Unidad u " +
           "LEFT JOIN u.propietarios p " +
           "LEFT JOIN u.habitantes h " +
           "LEFT JOIN u.inquilino i " +
           "WHERE p.documento = :documento OR h.documento = :documento OR i.documento = :documento")
    List<Unidad> findByPersona(String documento);
}
