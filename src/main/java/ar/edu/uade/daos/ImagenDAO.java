package ar.edu.uade.daos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.uade.modelo.Imagen;

import java.util.List;
import java.util.Optional;

@Service
public class ImagenDAO {

    @Autowired
    private ImagenRepository imagenRepository;

    public Imagen saveImagen(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    public List<Imagen> getAllImagenes() {
        return imagenRepository.findAll();
    }

    public Optional<Imagen> getImagenById(int numero) {
        return imagenRepository.findById(numero);
    }

    public List<Imagen> getImagenesByReclamo(int nroReclamo) {
        return imagenRepository.findByReclamoNroReclamo(nroReclamo);
    }

    public void deleteImagen(int numero) {
        imagenRepository.deleteById(numero);
    }
}
