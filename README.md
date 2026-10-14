
# API REST - Sistema de Gestión de Alquiler de Vehículos (AlquilaYa)

## Descripción de la API

El sistema AlquilaYa está compuesto por una arquitectura basada en servicios REST diseñada para una empresa de renta de vehículos. Su objetivo es gestionar eficientemente la flota automotriz, los clientes, los contratos de alquiler y las reparaciones, dividiéndose en las siguientes APIs principales:

- **API de Vehículos (`/api/vehiculos`):** Gestiona el inventario de la flota, especificaciones técnicas (marca, modelo, año, matrícula) y tarifas. Controla estrictamente el estado operativo del vehículo (Disponible, Alquilado, Mantenimiento)[cite: 11, 12].
- **API de Clientes (`/api/clientes`):** Administra la información de los usuarios del sistema, incluyendo datos de contacto y licencias de conducir. Permite su registro, consulta y baja[cite: 9, 16].
- **API de Alquileres (`/api/alquileres`):** Gestiona los contratos vinculando a un cliente con un vehículo. Calcula automáticamente el costo total, previene alquileres de autos no disponibles y gestiona la devolución (cambio de estado)[cite: 13, 17].
- **API de Mantenimientos (`/api/mantenimientos`):** Registra los ingresos al taller (fecha, descripción, costo, kilometraje). Al registrarse o finalizar, actualiza automáticamente el estado del vehículo en la flota[cite: 7, 14].
- **API de Oficinas (`/api/oficinas`):** Gestiona las distintas sucursales físicas (dirección, ciudad, región) donde se distribuyen los vehículos[cite: 10, 21].
- **Vistas Web (`/`):** El sistema incluye un controlador (`VistaController`) que expone interfaces gráficas en HTML para cada módulo (clientes, vehículos, alquileres, mantenimientos, oficinas)[cite: 15].

## Tecnologías utilizadas

- **Lenguaje:** Java[cite: 19]
- **Framework:** Spring Boot[cite: 19]
- **Persistencia de Datos:** Spring Data JPA / Hibernate[cite: 24, 25, 26, 27, 28]
- **Base de Datos:** MySQL (o compatible con JPA)
- **Validación:** Jakarta Validation (`@Valid`, `@NotBlank`, `@Positive`)[cite: 11, 12]

## Pasos de instalación y ejecución local

1. Asegúrate de tener instalado Java (JDK 17 o superior), un IDE (como Eclipse, IntelliJ o VS Code) y un servidor MySQL ejecutándose localmente.
2. Crea una base de datos en MySQL llamada `alquilaya`.
3. Clona o descarga el repositorio del proyecto en tu máquina local.
4. Importa el proyecto como un proyecto Maven en tu IDE.
5. Modifica el archivo `src/main/resources/application.properties` con tus credenciales de MySQL (usuario y contraseña) y asegúrate de que el puerto coincida.
6. Ejecuta la clase principal `AlquilayaApplication.java`[cite: 19]. El servidor iniciará (por defecto en el puerto 8080) y Spring Data JPA creará/actualizará las tablas automáticamente gracias a las entidades definidas.
7. Abre `http://localhost:8080/` en tu navegador para ver la interfaz de usuario[cite: 15].

## Variables de entorno necesarias

Configura tu archivo `application.properties` con los siguientes parámetros básicos antes de arrancar la aplicación:

```properties
spring.application.name=alquilaya
server.port=8080

# Configuración de Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/alquilaya?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD_AQUI

# Configuración de Hibernate/JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

```

## Listado de endpoints

**Clientes (`/api/clientes`)**

* `GET /api/clientes`: Obtiene la lista de todos los clientes.
* `GET /api/clientes/{id}`: Obtiene los datos de un cliente específico.
* `POST /api/clientes`: Crea un nuevo cliente.
* `DELETE /api/clientes/{id}`: Elimina un cliente.

**Vehículos (`/api/vehiculos`)**

* `GET /api/vehiculos`: Obtiene el catálogo completo de la flota.
* `POST /api/vehiculos`: Registra un nuevo vehículo.
* `PUT /api/vehiculos/{id}`: Actualiza los datos de un vehículo existente.
* `DELETE /api/vehiculos/{id}`: Elimina un vehículo (validando que no tenga dependencias).

**Oficinas (`/api/oficinas`)**

* `GET /api/oficinas`: Lista todas las oficinas o sucursales.
* `GET /api/oficinas/{id}`: Obtiene una oficina específica.
* `POST /api/oficinas`: Registra una nueva oficina.
* `DELETE /api/oficinas/{id}`: Elimina una oficina.

**Alquileres (`/api/alquileres`)**

* `GET /api/alquileres`: Lista todos los contratos de alquiler.
* `GET /api/alquileres/{id}`: Obtiene un contrato específico.
* `POST /api/alquileres`: Genera un nuevo contrato (valida disponibilidad del vehículo).
* `DELETE /api/alquileres/{id}`: Elimina un contrato (solo permitido si está en estado "Completado").
* `POST /api/alquileres/{idAlquiler}/devolver`: Marca el contrato como completado y libera el vehículo a estado "Disponible".

**Mantenimientos (`/api/mantenimientos`)**

* `GET /api/mantenimientos`: Consulta el historial de reparaciones.
* `POST /api/mantenimientos`: Registra un mantenimiento (cambia el vehículo a estado "Mantenimiento").
* `DELETE /api/mantenimientos/{id}`: Elimina un registro de mantenimiento y libera el vehículo.
* `POST /api/mantenimientos/{id}/finalizar`: Termina la reparación y devuelve el vehículo a estado "Disponible".

## Ejemplos de Peticiones y Respuestas

**1. Registrar un Cliente (POST `/api/clientes`)**


Cuerpo de la Petición (JSON):

```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@email.com",
  "licenciaConducir": "LIC-998877",
  "telefono": "555-1234"
}

```

Respuesta Esperada (JSON):

```json
{
  "idCliente": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@email.com",
  "licenciaConducir": "LIC-998877",
  "telefono": "555-1234"
}

```

**2. Registrar un Vehículo (POST `/api/vehiculos`)**


Cuerpo de la Petición (JSON):

```json
{
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2024,
  "matricula": "XYZ-999",
  "tarifaDiaria": 1200.00,
  "oficina": {
      "idOficina": 1
  }
}

```

**3. Generar un Contrato de Alquiler (POST `/api/alquileres`)**


Cuerpo de la Petición (JSON):

```json
{
  "cliente": { "idCliente": 1 },
  "vehiculo": { "idVehiculo": 1 },
  "fechaAlquiler": "2026-10-20",
  "fechaDevolucion": "2026-10-25"
}

```

```

```
