package ar.edu.uade.daos;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.uade.modelo.Imagen;

import java.util.List;


@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Integer> {
    List<Imagen> findByReclamoNroReclamo(int nroReclamo);
}
