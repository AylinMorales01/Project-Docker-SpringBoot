package com.pollos.autenticacion.dto;

import lombok.Data;

@Data
public class RefreshTokenDTO {
    private String message;
    private String jwt;
}
