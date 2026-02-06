package ar.edu.uade.daos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.uade.modelo.Edificio;

import java.util.List;
import java.util.Optional;

@Service
public class EdificioDAO {

    @Autowired
    private EdificioRepository edificioRepository;

    public Edificio saveEdificio(Edificio edificio) {
        return edificioRepository.save(edificio);
    }

    public List<Edificio> getAllEdificios() {
        return edificioRepository.findAll();
    }

    public Optional<Edificio> getEdificioById(int codigo) {
        return edificioRepository.findById(codigo);
    }

    public void deleteEdificio(int codigo) {
        edificioRepository.deleteById(codigo);
    }
}
