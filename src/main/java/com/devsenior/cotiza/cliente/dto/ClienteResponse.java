package com.devsenior.cotiza.cliente.dto;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nombre,
        String telefono,
        String correo,
        LocalDateTime fechaCreacion
) {
}
