package com.pollos.autenticacion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pollos.autenticacion.dto.LoginRequestDTO;
import com.pollos.autenticacion.dto.LoginResponseDTO;
import com.pollos.autenticacion.dto.MessageResponseDTO;
import com.pollos.autenticacion.dto.RefreshTokenDTO;
import com.pollos.autenticacion.dto.RegisterRequestDTO;
import com.pollos.autenticacion.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

// Para pruebas, permite todas las conexiones

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        try {
            MessageResponseDTO response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authService.login(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<RefreshTokenDTO> refresh(HttpServletRequest request) {
        // Creamos la respuesta vacía
        RefreshTokenDTO response = new RefreshTokenDTO();

        // Obtenemos el header de "Authorization"
        String autheader = request.getHeader("Authorization");

        // Validamos que en la petición nos llegue el authorization
        if (autheader == null || !autheader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Obtenemos el token sin el bearer
        String token = autheader.substring(7);

        try {
            // Llamamos al servicio para retornar la respuesta
            response = authService.refreshToken(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            // Atrapamos alguna exception
            response.setMessage("Token expirado");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
