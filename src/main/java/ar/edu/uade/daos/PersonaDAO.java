package ar.edu.uade.daos;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.uade.modelo.Persona;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaDAO {

    @Autowired
    private PersonaRepository personaRepository;

    public Persona savePersona(Persona persona) {
        return personaRepository.save(persona);
    }

    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }
    
    @Transactional
    public Persona getPersonaById(String documento) {
        Persona persona = personaRepository.findById(documento).orElse(null);
        
        System.out.println("Persona obtenida: " + persona);  // Verificar qué campos contiene
        
        return persona;
    }
    
    

    public void deletePersona(String documento) {
        personaRepository.deleteById(documento);
    }
}
