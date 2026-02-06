package ar.edu.uade.daos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.uade.modelo.Edificio;

@Repository
public interface EdificioRepository extends JpaRepository<Edificio, Integer> {
}
