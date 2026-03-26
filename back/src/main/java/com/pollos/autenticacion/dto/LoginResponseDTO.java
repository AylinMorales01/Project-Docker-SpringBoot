package com.pollos.autenticacion.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String message; 
    private String jwt;
}
