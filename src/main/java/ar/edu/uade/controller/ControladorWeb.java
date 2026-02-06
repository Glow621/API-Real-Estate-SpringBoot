package ar.edu.uade.controller;


import ar.edu.uade.daos.*;
import ar.edu.uade.modelo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ControladorWeb {

    @Autowired
    private UnidadDAO UnidadDAO;

    @Autowired
    private EdificioDAO EdificioDAO;

    @Autowired
    private PersonaDAO PersonaDAO;

    @Autowired
    private ReclamoDAO ReclamoDAO;

    @Autowired
    private ImagenDAO ImagenDAO;


    @Autowired
    private LoginService loginService;

    // Endpoint de login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String documento, @RequestParam String password) {
        // Llamar al servicio de autenticación
        Persona usuario = loginService.autenticarUsuario(documento, password);

        if (usuario != null) {
            // Si las credenciales son correctas, devolvemos el rolUsuario y documento
            return ResponseEntity.ok()
                    .body(new LoginResponse(usuario.getDocumento(), usuario.getRolUsuario(), usuario.getNombre()));
        } else {
            // Si las credenciales son incorrectas, devolvemos un error 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    // Clase para la respuesta de login (solo rolUsuario, documento y nombre)
    public static class LoginResponse {
        private String documento;
        private int rolUsuario;
        private String nombre;

        public LoginResponse(String documento, int rolUsuario, String nombre) {
            this.documento = documento;
            this.rolUsuario = rolUsuario;
            this.nombre = nombre;
        }

        public String getDocumento() {
            return documento;
        }

        public int getRolUsuario() {
            return rolUsuario;
        }

        public String getNombre() {
            return nombre;
        }
    }



    // Endpoints for Unidad

    @PostMapping("/unidades")
    public ResponseEntity<?> crearUnidad(@RequestBody UnidadDTO unidadDTO) {
        // Buscar el edificio por su ID
        Edificio edificio = EdificioDAO.getEdificioById(unidadDTO.getIdEdificio()).orElse(null);
        if (edificio == null) {
            // Si el edificio no se encuentra, devolvemos un error (No Encontrado)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        // Crear la nueva unidad
        Unidad nuevaUnidad = new Unidad();
        nuevaUnidad.setPiso(unidadDTO.getPiso());
        nuevaUnidad.setNumero(unidadDTO.getNumero());
        nuevaUnidad.setHabitado(unidadDTO.getHabitado());
        nuevaUnidad.setEdificio(edificio); // Asociamos la unidad con el edificio

        // Agregar la nueva unidad al edificio
        edificio.getUnidades().add(nuevaUnidad);  // Agregamos la unidad al set de unidades del edificio

        // Guardar la nueva unidad 
        Unidad unidadGuardada = UnidadDAO.saveUnidad(nuevaUnidad);

        // Guardar el edificio por la relación es bidireccional 
        EdificioDAO.saveEdificio(edificio); 

        // Retornar la unidad guardada con estado CREATED
        return new ResponseEntity<>(unidadGuardada, HttpStatus.CREATED);
    }


    @GetMapping("/unidades")
    public List<Unidad> getAllUnidades() {
        return UnidadDAO.getAllUnidades();
    }


    @GetMapping("/unidades/{id}")
    public ResponseEntity<Unidad> getUnidadById(@PathVariable int id) {
        Optional<Unidad> unidad = UnidadDAO.getUnidadById(id);
        return unidad.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    ///// Busca las unidades de una persona (ya sea propietario, habitante o inquilino)
    /// 

    @GetMapping("/unidades/persona/{id}")
    public List<Unidad> getUnidadesByPersona(@PathVariable String id) {
        String loggedInUserDocument = id;
        return UnidadDAO.getUnidadesByPersona(loggedInUserDocument);
    }
    

    @PutMapping("/unidades/{unidadId}/agregar/{edificioId}")  // En caso de error del administrador podemos reubicar unidades con este endpoint
    public ResponseEntity<Unidad> agregarUnidadAEdificio(
            @PathVariable Integer unidadId, 
            @PathVariable Integer edificioId) {

        // Buscar la unidad por su ID
        Unidad unidad = UnidadDAO.getUnidadById(unidadId).orElse(null);
        if (unidad == null) {
            // Si la unidad no se encuentra, devolvemos un error (No Encontrado)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        // Buscar el edificio por su ID
        Edificio edificio = EdificioDAO.getEdificioById(edificioId).orElse(null);
        if (edificio == null) {
            // Si el edificio no se encuentra, devolvemos un error (No Encontrado)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        // Agregar la unidad al edificio
        unidad.setEdificio(edificio);
        edificio.agregarUnidad(unidad);

        // Guardar los cambios en la base de datos
        EdificioDAO.saveEdificio(edificio);
        UnidadDAO.saveUnidad(unidad);

        // Retornar la unidad asociada al edificio con el código de estado 200 (OK)
        return ResponseEntity.ok(unidad);
    }


    @DeleteMapping("/unidades/{id}")
    public ResponseEntity<Void> deleteUnidad(@PathVariable int id) {
        UnidadDAO.deleteUnidad(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/unidades/{id}/alquilar/{documento}")
    public ResponseEntity<Unidad> alquilarUnidad(@PathVariable String documento, @PathVariable Integer id) {
        // Obtener el documento del usuario autenticado
        String loggedInUserDocument = documento;  
        
        // Buscar la unidad por su ID (pasado en el cuerpo de la solicitud)
        Unidad unidad = UnidadDAO.getUnidadById(id).orElse(null);
        if (unidad == null) {
            // Si la unidad no se encuentra, devolver un error (Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    
        // Si la unidad ya tiene un inquilino, devolver un error (Bad Request)
        if (unidad.getInquilino() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
        }
    
        // Buscar la persona (inquilino) que corresponde al usuario autenticado (el inquilino será la misma persona)
        Persona inquilino = PersonaDAO.getPersonaById(loggedInUserDocument);
        if (inquilino == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    
        // Agregar al inquilino a la unidad (relación ManyToMany)
        unidad.getHabitantes().add(inquilino);
        unidad.agregarInquilino(inquilino);
    
        // Agregar la unidad a la lista de unidades habitadas por el inquilino
        inquilino.getHabitadas().add(unidad);
    
        // Marcar la unidad como habitada
        unidad.setHabitado(true);
    
        // Guardar los cambios en la base de datos
        UnidadDAO.saveUnidad(unidad);
        PersonaDAO.savePersona(inquilino);
    
        // Retornar la unidad con el nuevo inquilino
        return new ResponseEntity<>(unidad, HttpStatus.OK);
    }
    
    

    @PutMapping("/unidades/{id}/transferir/{nuevoPropietarioDNI}")
    public ResponseEntity<Unidad> transferirUnidad(
        @PathVariable Integer id, 
        @PathVariable String nuevoPropietarioDNI
    ) {
        Unidad unidad = UnidadDAO.getUnidadById(id).orElse(null);
        if (unidad == null) {
            // Si la unidad, devolvemos un error (No Encontrado)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        Persona nuevoPropietario = PersonaDAO.getPersonaById(nuevoPropietarioDNI);
        if (nuevoPropietario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        nuevoPropietario.getPropiedades().add(unidad);
        unidad.transferir(nuevoPropietario);
        return new ResponseEntity<>(UnidadDAO.saveUnidad(unidad), HttpStatus.OK);
    }


    @PutMapping("/unidades/{id}/liberar")
    public ResponseEntity<Unidad> liberarUnidad(@PathVariable Integer id) {
        Unidad unidad = UnidadDAO.getUnidadById(id).orElse(null);
        if (unidad == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        
        // Obtener al inquilino (si existe)
        Persona inquilino = unidad.getInquilino();
        
        if (inquilino != null) {
            // Eliminar la unidad de la colección de habitadas del inquilino
            inquilino.getHabitadas().remove(unidad);
            
            // Eliminar el inquilino de la lista de habitantes de la unidad (en caso de que esté presente)
            unidad.getHabitantes().remove(inquilino);
            
            // Guardar la persona después de eliminar la unidad de habitadas 
            PersonaDAO.savePersona(inquilino); 
        }
        
        // Liberar la unidad
        unidad.liberar();
        
        // Guardar los cambios en la unidad
        UnidadDAO.saveUnidad(unidad);
        
        return ResponseEntity.ok(unidad);
    }
    
    


    // Endpoints for Edificio


    @PostMapping("/edificios")
    public ResponseEntity<Edificio> createEdificio(@RequestBody Edificio edificio) {
        try {
            Edificio nuevoEdificio = EdificioDAO.saveEdificio(edificio); // Guardamos el edificio
            return new ResponseEntity<>(nuevoEdificio, HttpStatus.CREATED); // Retornamos el edificio creado con el status 201
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Manejo de errores
        }
    }


    @GetMapping("/edificios")
    public List<Edificio> getAllEdificios() {
        return EdificioDAO.getAllEdificios();
    }


    @GetMapping("/edificios/{codigo}")
    public ResponseEntity<Edificio> getEdificioById(@PathVariable int codigo) {
        Optional<Edificio> edificio = EdificioDAO.getEdificioById(codigo);
        return edificio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/edificios/{codigo}")
    public ResponseEntity<Void> deleteEdificio(@PathVariable int codigo) {
        EdificioDAO.deleteEdificio(codigo);
        return ResponseEntity.noContent().build();
    }

    


    

    // Endpoints for Persona

    @PostMapping("/personas")
    public ResponseEntity<Persona> createPersona(@RequestBody Persona persona) {
        return new ResponseEntity<>(PersonaDAO.savePersona(persona), HttpStatus.CREATED);
    }


    @GetMapping("/personas")
    public List<Persona> getAllPersonas() {
        return PersonaDAO.getAllPersonas();
    }


    @GetMapping("/personas/{documento}")
    public ResponseEntity<Persona> getPersonaById(@PathVariable String documento) {
        Optional<Persona> persona = Optional.of(PersonaDAO.getPersonaById(documento));
        return persona.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/personas/{documento}")
    public ResponseEntity<Void> deletePersona(@PathVariable String documento) {
        PersonaDAO.deletePersona(documento);
        return ResponseEntity.noContent().build();
    }






    // Endpoints for Reclamo

    //// POST UTILIZANDO UN DTO (los valores tipo string los convierto en objetos luego buscandolos por id)

        @PostMapping("/reclamos")
        public ResponseEntity<Reclamo> crearReclamo(@RequestBody ReclamoDTO reclamoDTO) {
            // Obtener el DNI del usuario autenticado desde el contexto de seguridad
            String dniPersona = reclamoDTO.getDniPersona();  // 'authentication.getName()' devuelve el nombre del usuario (su documento)
        
            // Buscar la persona por el DNI obtenido de la autenticación
            Persona persona = PersonaDAO.getPersonaById(dniPersona);
            if (persona == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        
            // Buscar la unidad por ID
            Unidad unidad = UnidadDAO.getUnidadById(reclamoDTO.getIdUnidad()).orElse(null);
            if (unidad == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
        
            // Crear el reclamo
            Reclamo nuevoReclamo = new Reclamo();
            nuevoReclamo.setUbicacion(reclamoDTO.getUbicacion());
            nuevoReclamo.setDescripcion(reclamoDTO.getDescripcion());
            nuevoReclamo.setEstado(reclamoDTO.getEstado());
            nuevoReclamo.setPersonaReclamo(persona);
            nuevoReclamo.setUnidad(unidad);
            nuevoReclamo.setTipoReclamo(reclamoDTO.getTipoReclamo());
        
            // Guardar el reclamo y actualizar la asociación con la unidad
            Reclamo reclamoGuardado = ReclamoDAO.saveReclamo(nuevoReclamo);
        
            unidad.getReclamos().add(nuevoReclamo);
            UnidadDAO.saveUnidad(unidad);
        
            return new ResponseEntity<>(reclamoGuardado, HttpStatus.CREATED);
        }
        
        @PutMapping("/reclamos/{nroReclamo}/estado")
        public ResponseEntity<Reclamo> actualizarEstadoReclamo(@PathVariable Integer nroReclamo, @RequestBody String nuevoEstado) {
            // Buscar el reclamo por su número
            Reclamo reclamo = ReclamoDAO.getReclamoById(nroReclamo).orElse(null);
            if (reclamo == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Si no se encuentra el reclamo
            }
        
            // Validar si el estado actual ya es "Terminado"
            if ("Terminado".equals(reclamo.getEstado())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // No se puede cambiar el estado si está "Terminado"
            }
        
            // Actualizar el estado del reclamo
            reclamo.setEstado(nuevoEstado);
        
            // Guardar los cambios
            Reclamo reclamoActualizado = ReclamoDAO.saveReclamo(reclamo);
        
            return new ResponseEntity<>(reclamoActualizado, HttpStatus.OK);
        }
        


    @GetMapping("/reclamos")
    public List<Reclamo> getAllReclamos() {
        return ReclamoDAO.getAllReclamos();
    }

    @GetMapping("/reclamos/{nroReclamo}")
    public ResponseEntity<Reclamo> getReclamoById(@PathVariable int nroReclamo) {
        Optional<Reclamo> reclamo = ReclamoDAO.getReclamoById(nroReclamo);
        return reclamo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/reclamos/unidad/{unidadId}")
    public ResponseEntity<List<Reclamo>> getReclamosByUnidad(@PathVariable int unidadId) {
        List<Reclamo> reclamos = ReclamoDAO.getReclamosByUnidad(unidadId);
        
        if (reclamos.isEmpty()) {
            return ResponseEntity.noContent().build();  // Devuelve un 204 si no hay reclamos
        }
        
        return ResponseEntity.ok(reclamos);  // Devuelve los reclamos encontrados con un 200 OK
    }


    @GetMapping("/reclamos/persona/{id}")
    public ResponseEntity<List<Reclamo>> getReclamosByPersona(@PathVariable String id) {
        // Buscar los reclamos por el id de la persona proporcionado en la URL
        List<Reclamo> reclamos = ReclamoDAO.getReclamosByDocumentoPersona(id);
        
        if (reclamos.isEmpty()) {
            // Si no hay reclamos, devolver un error 404 (Not Found)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Si hay reclamos, devolverlos con un 200 (OK)
        return new ResponseEntity<>(reclamos, HttpStatus.OK);
    }



    @DeleteMapping("/reclamos/{nroReclamo}")
    public ResponseEntity<Void> deleteReclamo(@PathVariable int nroReclamo) {
        ReclamoDAO.deleteReclamo(nroReclamo);
        return ResponseEntity.noContent().build();
    }





    // Endpoints for Imagen

    @PostMapping("/imagenes")
    public ResponseEntity<Imagen> createImagen(@RequestBody Imagen imagen) {
        return new ResponseEntity<>(ImagenDAO.saveImagen(imagen), HttpStatus.CREATED);
    }

    @GetMapping("/imagenes")
    public List<Imagen> getAllImagenes() {
        return ImagenDAO.getAllImagenes();
    }

    @GetMapping("/imagenes/{numero}")
    public ResponseEntity<Imagen> getImagenById(@PathVariable int numero) {
        Optional<Imagen> imagen = ImagenDAO.getImagenById(numero);
        return imagen.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/imagenes/{numero}")
    public ResponseEntity<Void> deleteImagen(@PathVariable int numero) {
        ImagenDAO.deleteImagen(numero);
        return ResponseEntity.noContent().build();
    }

}
