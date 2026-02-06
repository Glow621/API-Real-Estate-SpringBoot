package ar.edu.uade.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.uade.modelo.Persona;

@Service
public class LoginService {

    @Autowired
    private PersonaDAO personaDAO;

    public Persona autenticarUsuario(String documento, String password) {
        // Buscar el usuario en la base de datos por documento
        Persona usuario = personaDAO.getPersonaById(documento);

        // Verificar si el usuario existe y la contraseña coincide
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;  // Retorna el usuario si las credenciales son correctas
        } else {
            return null;  // Si no coincide, retorna null
        }
    }
}
