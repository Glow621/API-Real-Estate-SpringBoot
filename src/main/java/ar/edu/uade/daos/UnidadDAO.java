package ar.edu.uade.daos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.uade.modelo.Unidad;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadDAO {

    @Autowired
    private UnidadRepository unidadRepository;

    public Unidad saveUnidad(Unidad unidad) {
        return unidadRepository.save(unidad);
    }

    public List<Unidad> getAllUnidades() {
        return unidadRepository.findAll();
    }

    public Optional<Unidad> getUnidadById(int id) {
        return unidadRepository.findById(id);
    }

    public List<Unidad> getUnidadesByEdificio(int edificioCodigo) {
        return unidadRepository.findByEdificioCodigo(edificioCodigo);
    }

    public List<Unidad> getUnidadesByPersona(String documento) {
        return unidadRepository.findByPersona(documento);
    }

    public void deleteUnidad(int id) {
        unidadRepository.deleteById(id);
    }
}
