package com.pollos.autenticacion.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pollos.autenticacion.dto.LoginRequestDTO;
import com.pollos.autenticacion.dto.LoginResponseDTO;
import com.pollos.autenticacion.dto.MessageResponseDTO;
import com.pollos.autenticacion.dto.RefreshTokenDTO;
import com.pollos.autenticacion.dto.RegisterRequestDTO;
import com.pollos.autenticacion.entity.Users;
import com.pollos.autenticacion.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    /**
     * Método de registro
     * @param request RegisterRequestDTO
     * @return MessageResponseDTO
     */
    public MessageResponseDTO register(RegisterRequestDTO request) {
        MessageResponseDTO response = new MessageResponseDTO();
        Users user = new Users();

        Optional<Users> userExist = usersRepository.findByEmail(request.getEmail());

        if (userExist.isPresent()) {
            response.setMessage("Este correo ya está registrado");
            return response;
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        usersRepository.save(user);

        response.setMessage("Registro exitoso");

        return response;
    }

    /**
     *  Método de login
     * @param request LoginRequestDTO
     * @return LoginResponseDTO
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        Optional<Users> userOptional = usersRepository.findByEmail(request.getEmail());
        LoginResponseDTO response = new LoginResponseDTO();

        if (userOptional.isEmpty()) {
            response.setMessage("Este usuario no está registrado");
            return response;
        }

        Users user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña o correo incorrectos");
        }

        String token = jwtService.generateToken(user.getId(), user.getUsername());

        response.setMessage("Inicio de sesión exitoso");
        response.setJwt(token);
        return response;
    }

    /**
     * Método de refresco de token
     * @param token
     * @return RefreshTokenDTO
     */
    public RefreshTokenDTO refreshToken(String token) {
        // Llamamos al servicion de refrestoken para que valide el token que nos esta llegando
        String jwt = jwtService.refreshToken(token);

        // Creamos la respuesta vacía
        RefreshTokenDTO response = new RefreshTokenDTO();
        // Seteamos mensaje y token
        response.setMessage("Ok");
        response.setJwt(jwt);

        // Retornamos respuesta
        return response;
    }
}
