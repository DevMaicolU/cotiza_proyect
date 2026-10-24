package com.devsenior.cotiza.cliente;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNoEncontradoExcepcion extends RuntimeException {

    public ClienteNoEncontradoExcepcion(String mensaje) {
        super(mensaje);
    }
}
