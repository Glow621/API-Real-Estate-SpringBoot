package ar.edu.uade.daos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.uade.modelo.Reclamo;

import java.util.List;
import java.util.Optional;

@Service
public class ReclamoDAO {

    @Autowired
    private ReclamoRepository reclamoRepository;

    public Reclamo saveReclamo(Reclamo reclamo) {
        return reclamoRepository.save(reclamo);
    }

    public List<Reclamo> getAllReclamos() {
        return reclamoRepository.findAll();
    }

    public Optional<Reclamo> getReclamoById(int nroReclamo) {
        return reclamoRepository.findById(nroReclamo);
    }

    public List<Reclamo> getReclamosByUnidad(int unidadId) {
        return reclamoRepository.findByUnidadId(unidadId);
    }

    public List<Reclamo> getReclamosByDocumentoPersona(String documento) {
        return reclamoRepository.findByPersonaReclamo_Documento(documento);
    }

    public void deleteReclamo(int nroReclamo) {
        reclamoRepository.deleteById(nroReclamo);
    }
}
