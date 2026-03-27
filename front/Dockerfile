# Etapa 1: Construcción de React (Vite)
FROM node:20-alpine AS build
WORKDIR /app

# Copiamos solo los archivos de dependencias primero para aprovechar la caché de Docker
COPY package*.json ./

# Instalamos todas las dependencias (incluyendo Vite que es una devDependency)
RUN npm install

# Copiamos el resto del código del frontend
COPY . .

# Generamos la carpeta /dist (Aquí es donde Vite compila todo)
RUN npm run build

# Etapa 2: Servidor Nginx para producción
FROM nginx:alpine

# Limpiamos el directorio por defecto de Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copiamos los archivos compilados desde la etapa anterior
# IMPORTANTE: Verifica que Vite genere la carpeta 'dist'. Si usa 'build', cambia /app/dist por /app/build
COPY --from=build /app/dist /usr/share/nginx/html

# Copiamos tu configuración de Proxy Inverso
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]