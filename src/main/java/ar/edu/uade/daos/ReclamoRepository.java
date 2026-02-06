package ar.edu.uade.daos;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.uade.modelo.Reclamo;

import java.util.List;

@Repository
public interface ReclamoRepository extends JpaRepository<Reclamo, Integer> {
    List<Reclamo> findByUnidadId(int unidadId);
    List<Reclamo> findByPersonaReclamo_Documento(String documento);
}
