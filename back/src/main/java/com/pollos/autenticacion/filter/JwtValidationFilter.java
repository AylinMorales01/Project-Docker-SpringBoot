package com.pollos.autenticacion.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pollos.autenticacion.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // Obtención del encabezado, busca el header llamado "Authorization"
        String authHeader = request.getHeader("Authorization");

        // Un token legal debe existir y empezar con la palabra "Bearer " (portador)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Header Authorization is missing in the request\"}");
            return; // Cortamos el flujo y asi la petición no alcanza a llegar al controller
        }

        // Limpieza del token, extraemos solo el string del JWT, saltandonos los
        // primeros 7 caracteres ("Bearer")
        String token = authHeader.substring(7);

        try {
            // Usamos el servicio para ver si la firma es real y si no ha expirado
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                Long userId = jwtService.extractUserId(token);

                request.setAttribute("username", username);
                request.setAttribute("userId", userId);

                // Si todo está bien, continuamos al siguiente paso, puede ser otro filtro o ya
                // directamente al controller
                filterChain.doFilter(request, response);
            } else {
                // Si isTokenValid devuelve false (pero no lanza excepción)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token is not valid\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token is invalid or expired\"}");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Estas rutas son públicas para que el usuario pueda entrar sin ningún token
        // /refresh es público porque el JwtService maneja su propia lógica de tokens
        // vencidos
        return path.equals("/api/v1/users/auth/register") ||
                path.endsWith("/login") ||
                path.endsWith("/refresh");
    }
}
