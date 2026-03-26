# Proyecto Kiora: Despliegue con Docker

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

### 1. Preparación de la Infraestructura
Primero, creamos las redes aisladas y el volumen para la persistencia de datos:

```bash
# Creación de redes para segmentar el tráfico
docker network create red-front-back
docker network create red-back-bd

# Creación del volumen para la base de datos
docker volume create vol-db-data
2. Despliegue de la Base de Datos (PostgreSQL)
Lanzamos el motor de base de datos en su red protegida:

Bash
docker run -d \
  --name db-container \
  --network red-back-bd \
  -e POSTGRES_USER=aylin \
  -e POSTGRES_PASSWORD=root \
  -e POSTGRES_DB=pollos_adso \
  -v vol-db-data:/var/lib/postgresql/data \
  postgres:15-alpine
3. Construcción y Ejecución del Backend
Entramos a la carpeta del backend para construir la imagen con Multi-stage build y ejecutarla:

Bash
cd back
docker build -t mi-backend-img .

docker run -d \
  --name back-container \
  --network red-back-bd \
  -p 9090:9090 \
  mi-backend-img

# Conectamos el backend a la red del frontend para permitir la comunicación
docker network connect red-front-back back-container
cd ..
4. Construcción y Ejecución del Frontend
Finalmente, construimos el frontend que incluye la configuración de Nginx como Proxy Inverso:

Bash
cd front
docker build -t mi-frontend-img .

docker run -d \
  --name front-container \
  --network red-front-back \
  -p 80:80 \
  mi-frontend-img
cd ..
- Acceso y Verificación
Una vez finalizado el despliegue, puede acceder a los servicios en:

- Frontend: http://localhost (Puerto 80)

- Backend API: http://localhost:9090/api/v1/users/

Nota sobre el Proxy Inverso: Nginx redirige automáticamente las peticiones desde el puerto 80 hacia el contenedor del backend mediante la directiva proxy_pass.

- Estructura del Repositorio
La estructura cumple estrictamente con los requerimientos del proyecto:

Plaintext
/
├── back/          # Código fuente Java + Dockerfile (Multi-stage)
├── front/         # Código fuente React + Dockerfile + nginx.conf
└── README.md      # Guía de despliegue y documentación
© 2026 - Proyecto de Formación ADSO