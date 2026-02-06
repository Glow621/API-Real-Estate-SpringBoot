package ar.edu.uade;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ar.edu.uade.daos.EdificioRepository;
import ar.edu.uade.daos.ImagenRepository;
import ar.edu.uade.daos.PersonaRepository;
import ar.edu.uade.daos.ReclamoRepository;
import ar.edu.uade.daos.UnidadRepository;
import ar.edu.uade.modelo.Edificio;
import ar.edu.uade.modelo.Imagen;
import ar.edu.uade.modelo.Persona;
import ar.edu.uade.modelo.Reclamo;
import ar.edu.uade.modelo.Unidad;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EdificioRepository edificioRepository;

    @Autowired
    private UnidadRepository unidadRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ReclamoRepository reclamoRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear Edificios
        Edificio edificio1 = new Edificio(1, "Edificio Central", "Av. Principal 123");
        Edificio edificio2 = new Edificio(2, "Edificio Norte", "Calle Norte 456");
        edificioRepository.saveAll(Arrays.asList(edificio1, edificio2));

        // Crear Personas
        Persona propietario1 = new Persona("11111111", "Juan Perez",1,"password");
        Persona propietario2 = new Persona("22222222", "Ana Gomez",2,"password");
        Persona inquilino1 = new Persona("33333333", "Carlos Lopez",1,"password");
        Persona inquilino2 = new Persona("44444444", "Laura Martinez",2,"password");
        Persona Test1 = new Persona("55555555", "Pedro Gomez",2,"password");
        Persona Test2 = new Persona("66666666", "Roberto estropajo",2,"password");
        personaRepository.saveAll(Arrays.asList(propietario1, propietario2, inquilino1, inquilino2, Test1, Test2));

        // Crear Unidades
        Unidad unidad1 = new Unidad("1", "101", true, edificio1);
        Unidad unidad2 = new Unidad("2", "202", false, edificio1);
        Unidad unidad3 = new Unidad("3", "303", true, edificio2);
        unidad1.agregarDueno(propietario1);
        unidad2.agregarDueno(propietario2);
        unidad3.alquilar(inquilino1);
        unidadRepository.saveAll(Arrays.asList(unidad1, unidad2, unidad3));

        // Crear Reclamos
        Reclamo reclamo1 = new Reclamo("Cocina - Fuga de agua", "Se detectó una fuga de agua bajo el fregadero.", "Abierto", unidad1,"Agua",propietario1);
        Reclamo reclamo2 = new Reclamo("Baño - Cortocircuito", "Problema de cortocircuito en el enchufe.", "Abierto", unidad2,"Electricidad",propietario2);
        Reclamo reclamo3 = new Reclamo("Sala - Olor a gas", "Olor fuerte a gas en la sala de estar.", "Abierto", unidad3,"Gas",inquilino1);
        reclamoRepository.saveAll(Arrays.asList(reclamo1, reclamo2, reclamo3));

        // Crear Imágenes asociadas a Reclamos
        Imagen imagen1 = new Imagen("https://alfafugass.cl/wp-content/uploads/2024/01/FILTRACION-DE-AGUA-EN-DEPARTAMENTOS.webp.jpg", "JPG", reclamo1);
        Imagen imagen2 = new Imagen("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTGG2K8MvtcWUz65IYoLb5zDkKoZ4cGcoXx_w&s", "JPG", reclamo2);
        Imagen imagen3 = new Imagen("https://www.unrc.edu.ar/unrc/mmedios/2021/08/05/img/35963_05172626000000.jpg", "JPG", reclamo3);
        imagenRepository.saveAll(Arrays.asList(imagen1, imagen2, imagen3));

        System.out.println("Datos de prueba cargados correctamente.");
    }
}
