package com.pollos.autenticacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pollos.autenticacion.dto.UsersResponseDTO;
import com.pollos.autenticacion.service.UsersService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RequestMapping("users")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsersController {

    private final UsersService usersService;

    @GetMapping("get-users")
    public ResponseEntity<List<UsersResponseDTO>> getUsers() {
        try {
            List<UsersResponseDTO> response = usersService.getUsers();
            return ResponseEntity.status(HttpStatus.FOUND).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
