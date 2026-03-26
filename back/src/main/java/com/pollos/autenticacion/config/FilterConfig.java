package com.pollos.autenticacion.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pollos.autenticacion.filter.JwtValidationFilter;

@Configuration // Indica que esta clase es una "fábrica de beans"
// springboot la lee al arrancar la app para configurar el comportamiento de esta misma
public class FilterConfig {
    @Bean
    FilterRegistrationBean<JwtValidationFilter> jwtFilter(JwtValidationFilter jwtValidationFilter) {
        // Creamos un contenedor de Registro del bean para el filtro
        FilterRegistrationBean<JwtValidationFilter> registrationBean = new FilterRegistrationBean<>();

        // Es decirle a spring que este es el filtro que quiero que trabaje
        registrationBean.setFilter(jwtValidationFilter);

        // Definir el alcance de este filtro, quiero que revise todas las peticiones que entren en mi app
        registrationBean.addUrlPatterns("/*");

        // Establecer la prioridad de ejecución de los filtros
        // Este filtro se va a ejecutar antes que otros internos
        registrationBean.setOrder(1);

        // Retornamos el registro configurado para que spring lo guarde en su contexto
        return registrationBean;
    }
}
