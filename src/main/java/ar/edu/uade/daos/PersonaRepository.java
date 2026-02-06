package ar.edu.uade.daos;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.uade.modelo.Persona;


@Repository
public interface PersonaRepository extends JpaRepository<Persona, String> {
}
