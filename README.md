# Despliegue de Aplicación Web con Contenedores (Proyecto Kiora)

Este proyecto consiste en el despliegue manual de una aplicación full-stack (Frontend, Backend y Base de Datos) utilizando Docker. Se aplican conceptos de redes aisladas, persistencia de datos y construcción eficiente de imágenes mediante Multi-stage builds.

# Tecnologías Utilizadas

    Frontend: React (Vite) / Servido con Nginx (Proxy Inverso).

    Backend: Java 17 / Framework Spring Boot 3.x.

    Base de Datos: PostgreSQL 15 (Imagen Alpine).

    Infraestructura: Docker (Comandos manuales, sin orquestadores).

# Instrucciones de Despliegue Paso a Paso

Siga estos comandos en el orden indicado para levantar la solución completa desde cero.
1. Creación de Redes Aisladas

Se definen dos redes para segmentar el tráfico según los requisitos:
Bash

docker network create red-front-back
docker network create red-back-bd

2. Creación del Volumen de Persistencia

Para asegurar que los datos de PostgreSQL no se pierdan al borrar el contenedor:
Bash

docker volume create vol-db-data

3. Despliegue de la Base de Datos (PostgreSQL)
Bash

docker run -d \
  --name db-container \
  --network red-back-bd \
  -e POSTGRES_USER=aylin \
  -e POSTGRES_PASSWORD=root \
  -e POSTGRES_DB=pollos_adso \
  -v vol-db-data:/var/lib/postgresql/data \
  postgres:15-alpine

4. Construcción y Ejecución del Backend (Spring Boot)

El Dockerfile del backend utiliza Multi-stage building (Maven para construcción y Temurin JRE para ejecución).
Bash

cd back
docker build -t mi-backend-img .
docker run -d \
  --name back-container \
  --network red-back-bd \
  -p 9090:9090 \
  mi-backend-img

# Conexión manual a la segunda red (Frontend <-> Backend)
docker network connect red-front-back back-container
cd ..

5. Construcción y Ejecución del Frontend (React + Nginx)

El Dockerfile del frontend utiliza Multi-stage building (Node.js para compilar y Nginx para servir). Nginx está configurado como Proxy Inverso.
Bash

cd front
docker build -t mi-frontend-img .
docker run -d \
  --name front-container \
  --network red-front-back \
  -p 80:80 \
  mi-frontend-img
cd ..

Acceso y Verificación

Una vez ejecutados los comandos anteriores, la aplicación estará disponible en:

    Frontend: http://localhost (Puerto 80)

    Backend API: http://localhost:9090/api/v1/users/

Verificación de Proxy Inverso

Nginx redirige automáticamente las peticiones desde el puerto 80 hacia el puerto 9090 del contenedor backend mediante la configuración de proxy_pass.

Estructura del Repositorio

/
├── back/          # Código fuente Java + Dockerfile
├── front/         # Código fuente React + Dockerfile + nginx.conf
└── README.md      # Guía de despliegue