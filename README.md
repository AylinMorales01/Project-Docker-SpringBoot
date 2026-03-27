# Proyecto SpringBoot: Despliegue con Docker

Este proyecto consiste en el despliegue manual de una aplicación **Full-Stack** (Frontend, Backend y Base de Datos) utilizando contenedores Docker. Se aplican conceptos de redes aisladas, persistencia de datos y optimización de imágenes mediante **Multi-stage builds**.

---

## Tecnologías Utilizadas

| Componente | Tecnología | Rol |
| :--- | :--- | :--- |
| **Frontend** | React (Vite) | Interfaz de usuario servida por Nginx |
| **Backend** | Java 17 (Spring Boot 3.x) | API REST y lógica de negocio |
| **Base de Datos** | PostgreSQL 15 (Alpine) | Motor de base de datos relacional |
| **Servidor Web** | Nginx | Servidor estático y **Proxy Inverso** |
| **Infraestructura**| Docker | Contenedores y redes manuales |

---

## Instrucciones de Despliegue Paso a Paso

Siga estos comandos en el orden exacto para levantar la solución completa desde cero.

### Preparación de la Infraestructura
Primero, se deben crear las redes aisladas para segmentar el tráfico y el volumen para la persistencia de los datos:

- Creación de redes para segmentar el tráfico
```
docker network create red-front-back
docker network create red-back-bd
```
- Creación del volumen para la base de datos
```
docker volume create vol-db-data
```
### Despliegue de la Base de Datos (PostgreSQL)
Se lanza el motor de base de datos dentro de su red protegida y se asocia el volumen de datos:

- Windows
```
docker run -d ^
  --name db-container ^
  --network red-back-bd ^
  -e POSTGRES_USER=aylin ^
  -e POSTGRES_PASSWORD=root ^
  -e POSTGRES_DB=pollos_adso ^
  -v vol-db-data:/var/lib/postgresql/data ^
  postgres:15-alpine
```
- Linux
```
docker run -d \
  --name db-container \
  --network red-back-bd \
  -e POSTGRES_USER=aylin \
  -e POSTGRES_PASSWORD=root \
  -e POSTGRES_DB=pollos_adso \
  -v vol-db-data:/var/lib/postgresql/data \
  postgres:15-alpine
```
### Construcción y Ejecución del Backend
Se accede a la carpeta del backend para construir la imagen utilizando Multi-stage build y se ejecuta en la red de la base de datos:
```
cd back
docker build -t mi-backend-img .
```
- Windows
```
docker run -d ^
  --name back-container ^
  --network red-back-bd ^
  -p 9090:9090 ^
  mi-backend-img
```
- Linux
```
docker run -d \
  --name back-container \
  --network red-back-bd \
  -p 9090:9090 \
  mi-backend-img
```
### Conexión manual a la segunda red para permitir comunicación con el frontend
```
docker network connect red-front-back back-container
cd ..
```
Construcción y Ejecución del Frontend
Se construye el frontend, el cual incluye la configuración de Nginx como Proxy Inverso para redirigir las peticiones al API:
```
cd front
docker build -t mi-frontend-img .
```
- Windows
```
docker run -d ^
  --name front-container ^
  --network red-front-back ^
  -p 80:80 ^
  mi-frontend-img
```
- Linux
```
docker run -d \
  --name front-container \
  --network red-front-back \
  -p 80:80 \
  mi-frontend-img
cd ..
```
### Acceso y Verificación
Una vez finalizado el despliegue, la aplicación estará disponible en las siguientes rutas:

- Frontend: http://localhost (Puerto 80 por defecto)

- Backend API: http://localhost:9090/api/v1/users/

Nota técnica: Nginx actúa como Proxy Inverso redirigiendo automáticamente las peticiones desde el puerto 80 hacia el puerto 9090 del contenedor backend.

### Estructura del Repositorio

/
├── back/          # Código fuente Java + Dockerfile (Multi-stage)
├── front/         # Código fuente React + Dockerfile + nginx.conf
└── README.md      # Guía de despliegue y documentación técnica

© 2026 - Proyecto de Formación ADSO