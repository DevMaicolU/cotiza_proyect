package com.devsenior.cotiza.cliente;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TelefonoDuplicadoExcepcion extends RuntimeException {

    public TelefonoDuplicadoExcepcion(String mensaje) {
        super(mensaje);
    }
}
