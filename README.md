# TPO - Sistema de Gestión de Edificios y Unidades

Una aplicación Spring Boot para la gestión integral de edificios, unidades habitacionales, personas, reclamos e imágenes. Sistema diseñado para administrar propiedades, inquilinos, propietarios y el ciclo de vida de las unidades.

## 📋 Descripción General

Este proyecto es un **Backend REST API** desarrollado con Spring Boot 3.3.3 que permite gestionar:

- **Edificios**: Crear y administrar edificios con múltiples unidades
- **Unidades**: Gestionar unidades (departamentos/casas) dentro de edificios
- **Personas**: Administrar datos de propietarios, inquilinos y habitantes
- **Reclamos**: Sistema de tickets para reportar y hacer seguimiento de problemas
- **Imágenes**: Asociar imágenes a las unidades
- **Autenticación**: Sistema de login con soporte a diferentes roles de usuario

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|-----------|---------|----------|
| **Spring Boot** | 3.3.3 | Framework principal |
| **Spring Data JPA** | - | ORM y acceso a datos |
| **Java** | 17 | Lenguaje de programación |
| **H2 Database** | - | Base de datos en memoria (desarrollo) |
| **SQL Server JDBC** | - | Driver para SQL Server (producción) |
| **Jackson Databind** | - | Serialización/Deserialización JSON |
| **Spring Boot DevTools** | - | Herramientas de desarrollo |

---

---

## 🔑 Roles de Usuario

El proyecto soporta diferentes roles identificados por números:

| Rol | ID | Descripción |
|-----|-----|-----------|
| Admin | 0 | Administrador del sistema |
| Propietario | 1 | Dueño de propiedades |
| Inquilino | 2 | Arrendatario |
| Habitante | 3 | Residente que no es propietario |

---

## 🚀 Características Principales

• **CRUD Completo** para Edificios, Unidades, Personas, Reclamos e Imágenes

• **Sistema de Login** con validación de credenciales

• **Relaciones Complejas**:
  - Unidades pertenecen a Edificios (1-M)
  - Personas pueden ser propietarias de múltiples Unidades (M-M)
  - Personas pueden habitar múltiples Unidades (M-M)
  - Reclamos están asociados a Unidades y Personas

• **Gestión de Ciclo de Vida**:
  - Alquilar unidades
  - Transferir propiedad
  - Liberar unidades (desalojo)
  - Reasignar unidades a edificios

• **Sistema de Reclamos** con seguimiento de estado

• **Base de Datos** H2 en memoria para desarrollo y SQL para producción.

• **Serialización JSON** con Jackson

---

## 📚 Base de Datos

El proyecto incluye un archivo [tablas.sql](tablas.sql) con el esquema de referencia. La aplicación usa **Hibernate** para crear y actualizar automáticamente las tablas basadas en las entidades JPA.

---

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/ar/edu/uade/
│   │   ├── TPOApplication.java           # Clase principal de Spring Boot
│   │   ├── DataLoader.java               # Carga inicial de datos
│   │   ├── controller/
│   │   │   ├── ControladorWeb.java       # Controlador REST principal
│   │   │   └── WebConfig.java            # Configuración web
│   │   ├── daos/
│   │   │   ├── UnidadDAO.java
│   │   │   ├── EdificioDAO.java
│   │   │   ├── PersonaDAO.java
│   │   │   ├── ReclamoDAO.java
│   │   │   ├── ImagenDAO.java
│   │   │   ├── LoginService.java
│   │   │   ├── *Repository.java          # Interfaces JPA
│   │   │   └── *DTO.java                 # Data Transfer Objects
│   │   └── modelo/
│   │       ├── Edificio.java
│   │       ├── Unidad.java
│   │       ├── Persona.java
│   │       ├── Reclamo.java
│   │       ├── Imagen.java
│   │       └── Views.java
│   └── resources/
│       └── application.properties
├── test/
│   └── java/ar/edu/uade/
│       └── TPOApplicationTests.java
└── tablas.sql                            # Script SQL de la base de datos
```

---

## 📊 Modelos de Datos

### Edificio
Representa un building/edificio que contiene múltiples unidades.

**Atributos:**
- `codigo` (int, PK): ID único del edificio
- `nombre` (String): Nombre del edificio
- `direccion` (String): Dirección postal
- `unidades` (Set<Unidad>): Relación uno a muchos con unidades

### Unidad
Representa un departamento/casa dentro de un edificio.

**Atributos:**
- `id` (int, PK): ID único de la unidad
- `piso` (String): Número o nombre del piso
- `numero` (String): Número de la unidad
- `habitado` (Boolean): Si está ocupada
- `edificio` (Edificio): Referencia al edificio contenedor
- `propietarios` (Set<Persona>): Relación M2M con propietarios
- `habitantes` (Set<Persona>): Relación M2M con habitantes
- `inquilino` (Persona): Inquilino actual
- `reclamos` (List<Reclamo>): Reclamos asociados

### Persona
Representa un usuario del sistema (propietario, inquilino, habitante).

**Atributos:**
- `documento` (String, PK): DNI/Documento de identidad
- `nombre` (String): Nombre completo
- `email` (String): Email de contacto
- `telefono` (String): Teléfono
- `password` (String): Contraseña encriptada
- `rolUsuario` (int): Rol del usuario (0=Admin, 1=Propietario, 2=Inquilino, etc.)
- `propiedades` (Set<Unidad>): Unidades que es propietario
- `habitadas` (Set<Unidad>): Unidades donde habita
- `reclamos` (List<Reclamo>): Reclamos que ha registrado

### Reclamo
Sistema de tickets para reportar problemas en las unidades.

**Atributos:**
- `nroReclamo` (int, PK): ID único del reclamo
- `descripcion` (String): Descripción detallada del problema
- `ubicacion` (String): Ubicación del problema en la unidad
- `tipoReclamo` (String): Tipo (ej: "Reparación", "Limpieza", "Mantenimiento")
- `estado` (String): Estado (ej: "Pendiente", "En Progreso", "Terminado")
- `personaReclamo` (Persona): Persona que reportó el reclamo
- `unidad` (Unidad): Unidad donde ocurre el problema

### Imagen
Para almacenar referencias a imágenes de las unidades.

**Atributos:**
- `numero` (int, PK): ID único
- URL/ruta de la imagen

---

## 🔌 Endpoints de la API

### 🔐 Autenticación

#### POST • `/api/login`
Autentica un usuario con documento y contraseña.

**Parámetros (Query):**
- `documento` (String): DNI/Documento del usuario
- `password` (String): Contraseña

**Respuesta exitosa (200 OK):**
```json
{
  "documento": "12345678",
  "rolUsuario": 1,
  "nombre": "Juan Pérez"
}
```

**Respuesta error (401 Unauthorized):**
```
Credenciales inválidas
```

---

### 🏢 Edificios

#### POST • `/api/edificios`
Crea un nuevo edificio.

**Body (JSON):**
```json
{
  "nombre": "Edificio Centro",
  "direccion": "Av. Rivadavia 1234"
}
```

**Respuesta (201 Created):**
```json
{
  "codigo": 1,
  "nombre": "Edificio Centro",
  "direccion": "Av. Rivadavia 1234",
  "unidades": []
}
```

#### GET • `/api/edificios`
Obtiene todos los edificios.

**Respuesta (200 OK):**
```json
[
  {
    "codigo": 1,
    "nombre": "Edificio Centro",
    "direccion": "Av. Rivadavia 1234",
    "unidades": [...]
  }
]
```

#### GET • `/api/edificios/{codigo}`
Obtiene un edificio específico por su código.

**Respuesta (200 OK):** Objeto Edificio

**Respuesta (404 Not Found):** Si el edificio no existe

#### DELETE • `/api/edificios/{codigo}`
Elimina un edificio.

**Respuesta (204 No Content):** Si se eliminó correctamente

---

### 🏠 Unidades

#### POST • `/api/unidades`
Crea una nueva unidad en un edificio.

**Body (JSON):**
```json
{
  "piso": "2",
  "numero": "201",
  "habitado": false,
  "idEdificio": 1
}
```

**Respuesta (201 Created):** Objeto Unidad creado

**Respuesta (404 Not Found):** Si el edificio no existe

#### GET • `/api/unidades`
Obtiene todas las unidades.

**Respuesta (200 OK):** Lista de unidades

#### GET • `/api/unidades/{id}`
Obtiene una unidad específica por ID.

**Respuesta (200 OK):** Objeto Unidad

**Respuesta (404 Not Found):** Si la unidad no existe

#### GET • `/api/unidades/persona/{id}`
Obtiene todas las unidades de una persona (propietario/habitante/inquilino).

**Parámetros:**
- `id` (String): Documento de la persona

**Respuesta (200 OK):** Lista de unidades asociadas a la persona

#### PUT • `/api/unidades/{unidadId}/agregar/{edificioId}`
Reasigna una unidad a un edificio diferente (admin).

**Respuesta (200 OK):** Unidad actualizada

**Respuesta (404 Not Found):** Si unidad o edificio no existe

#### PUT • `/api/unidades/{id}/alquilar/{documento}`
Un inquilino alquila (ocupa) una unidad.

**Parámetros:**
- `id` (Integer): ID de la unidad
- `documento` (String): Documento del inquilino

**Respuesta (200 OK):** Unidad actualizada con inquilino asignado

**Respuesta (400 Bad Request):** Si la unidad ya tiene inquilino

**Respuesta (404 Not Found):** Si la unidad o persona no existe

#### PUT • `/api/unidades/{id}/transferir/{nuevoPropietarioDNI}`
Transfiere la propiedad de una unidad a otra persona.

**Parámetros:**
- `id` (Integer): ID de la unidad
- `nuevoPropietarioDNI` (String): Documento del nuevo propietario

**Respuesta (200 OK):** Unidad con nuevo propietario

**Respuesta (404 Not Found):** Si unidad o persona no existe

#### PUT • `/api/unidades/{id}/liberar`
Libera una unidad (desaloja inquilino y marca como deshabitada).

**Respuesta (200 OK):** Unidad liberada

**Respuesta (404 Not Found):** Si la unidad no existe

#### DELETE • `/api/unidades/{id}`
Elimina una unidad.

**Respuesta (204 No Content):** Si se eliminó

---

### 👤 Personas

#### POST • `/api/personas`
Crea una nueva persona (usuario).

**Body (JSON):**
```json
{
  "documento": "12345678",
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "telefono": "1122334455",
  "password": "miPassword123",
  "rolUsuario": 1
}
```

**Respuesta (201 Created):** Objeto Persona creado

#### GET • `/api/personas`
Obtiene todas las personas.

**Respuesta (200 OK):** Lista de personas

#### GET • `/api/personas/{documento}`
Obtiene una persona específica por documento.

**Respuesta (200 OK):** Objeto Persona

**Respuesta (404 Not Found):** Si la persona no existe

#### DELETE • `/api/personas/{documento}`
Elimina una persona.

**Respuesta (204 No Content):** Si se eliminó

---

### 🆘 Reclamos

#### POST • `/api/reclamos`
Crea un nuevo reclamo de una persona para una unidad.

**Body (JSON):**
```json
{
  "dniPersona": "12345678",
  "idUnidad": 5,
  "descripcion": "El grifo del baño no cierra bien",
  "ubicacion": "Baño principal",
  "tipoReclamo": "Reparación",
  "estado": "Pendiente"
}
```

**Respuesta (201 Created):** Objeto Reclamo creado

**Respuesta (404 Not Found):** Si la persona o unidad no existe

#### GET • `/api/reclamos`
Obtiene todos los reclamos.

**Respuesta (200 OK):** Lista de reclamos

#### GET • `/api/reclamos/{nroReclamo}`
Obtiene un reclamo específico por número.

**Respuesta (200 OK):** Objeto Reclamo

**Respuesta (404 Not Found):** Si el reclamo no existe

#### GET • `/api/reclamos/unidad/{unidadId}`
Obtiene todos los reclamos de una unidad específica.

**Respuesta (200 OK):** Lista de reclamos de la unidad

**Respuesta (204 No Content):** Si no hay reclamos

#### GET • `/api/reclamos/persona/{id}`
Obtiene todos los reclamos reportados por una persona.

**Parámetros:**
- `id` (String): Documento de la persona

**Respuesta (200 OK):** Lista de reclamos de la persona

**Respuesta (404 Not Found):** Si no hay reclamos

#### PUT • `/api/reclamos/{nroReclamo}/estado`
Actualiza el estado de un reclamo.

**Body (JSON):**
```json
"En Progreso"
```

**Respuesta (200 OK):** Reclamo con estado actualizado

**Respuesta (400 Bad Request):** Si el estado actual es "Terminado" (no se puede cambiar)

**Respuesta (404 Not Found):** Si el reclamo no existe

#### DELETE • `/api/reclamos/{nroReclamo}`
Elimina un reclamo.

**Respuesta (204 No Content):** Si se eliminó

---

### 🖼️ Imágenes

#### POST • `/api/imagenes`
Crea una nueva imagen.

**Body (JSON):**
```json
{
  "url": "https://example.com/imagen1.jpg"
}
```

**Respuesta (201 Created):** Objeto Imagen creado

#### GET • `/api/imagenes`
Obtiene todas las imágenes.

**Respuesta (200 OK):** Lista de imágenes

#### GET • `/api/imagenes/{numero}`
Obtiene una imagen específica por número.

**Respuesta (200 OK):** Objeto Imagen

**Respuesta (404 Not Found):** Si la imagen no existe

#### DELETE • `/api/imagenes/{numero}`
Elimina una imagen.

**Respuesta (204 No Content):** Si se eliminó

---

## 📝 Ejemplos de Uso (cURL)

### 1. Login
```bash
curl -X POST "http://localhost:8080/api/login?documento=12345678&password=miPassword123"
```

### 2. Crear un Edificio
```bash
curl -X POST "http://localhost:8080/api/edificios" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Edificio Moderno",
    "direccion": "Calle Principal 100"
  }'
```

### 3. Crear una Unidad
```bash
curl -X POST "http://localhost:8080/api/unidades" \
  -H "Content-Type: application/json" \
  -d '{
    "piso": "3",
    "numero": "301",
    "habitado": false,
    "idEdificio": 1
  }'
```

### 4. Crear una Persona
```bash
curl -X POST "http://localhost:8080/api/personas" \
  -H "Content-Type: application/json" \
  -d '{
    "documento": "37654321",
    "nombre": "María García",
    "email": "maria@example.com",
    "telefono": "1187654321",
    "password": "password123",
    "rolUsuario": 2
  }'
```

### 5. Alquilar una Unidad
```bash
curl -X PUT "http://localhost:8080/api/unidades/1/alquilar/37654321"
```

### 6. Crear un Reclamo
```bash
curl -X POST "http://localhost:8080/api/reclamos" \
  -H "Content-Type: application/json" \
  -d '{
    "dniPersona": "37654321",
    "idUnidad": 1,
    "descripcion": "Fuga de agua en la cocina",
    "ubicacion": "Cocina",
    "tipoReclamo": "Reparación",
    "estado": "Pendiente"
  }'
```

### 7. Actualizar Estado de Reclamo
```bash
curl -X PUT "http://localhost:8080/api/reclamos/1/estado" \
  -H "Content-Type: application/json" \
  -d '"En Progreso"'
```

### 8. Obtener Unidades de una Persona
```bash
curl "http://localhost:8080/api/unidades/persona/37654321"
```

**Última actualización**: Febrero 2026

