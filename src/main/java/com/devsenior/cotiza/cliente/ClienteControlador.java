package com.devsenior.cotiza.cliente;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.cotiza.cliente.dto.ClienteRequest;
import com.devsenior.cotiza.cliente.dto.ClienteResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteControlador {

    private final ClienteServicio clienteServicio;

    public ClienteControlador(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crearCliente(@Valid @RequestBody ClienteRequest request) {
        return clienteServicio.crearCliente(request);
    }

    @GetMapping
    public List<ClienteResponse> listarClientes() {
        return clienteServicio.listarClientes();
    }

    @GetMapping("/{id}")
    public ClienteResponse obtenerClientePorId(@PathVariable Long id) {
        return clienteServicio.obtenerClientePorId(id);
    }

    @ExceptionHandler(ClienteNoEncontradoExcepcion.class)
    public ResponseEntity<Map<String, String>> manejarClienteNoEncontrado(ClienteNoEncontradoExcepcion excepcion) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", excepcion.getMessage()));
    }

    @ExceptionHandler(TelefonoDuplicadoExcepcion.class)
    public ResponseEntity<Map<String, String>> manejarTelefonoDuplicado(TelefonoDuplicadoExcepcion excepcion) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", excepcion.getMessage()));
    }
}
