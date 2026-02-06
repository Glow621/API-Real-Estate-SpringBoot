package ar.edu.uade.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Habilitar CORS para todos los endpoints
        registry.addMapping("/**")  // Permite todas las rutas
                .allowedOrigins("https://gestion-inmobiliaria-bf3f2.web.app")  // Permite las solicitudes desde React
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Métodos permitidos
                .allowedHeaders("*")  // Permite todos los encabezados
                .allowCredentials(true);  // Permite el uso de cookies, si es necesario
    }

    
}
