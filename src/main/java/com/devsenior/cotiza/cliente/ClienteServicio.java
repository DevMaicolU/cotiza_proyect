package com.devsenior.cotiza.cliente;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsenior.cotiza.cliente.dto.ClienteRequest;
import com.devsenior.cotiza.cliente.dto.ClienteResponse;

@Service
public class ClienteServicio {

    private final ClienteRepositorio clienteRepositorio;

    public ClienteServicio(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    @Transactional
    public ClienteResponse crearCliente(ClienteRequest request) {
        if (clienteRepositorio.existsByTelefono(request.telefono())) {
            throw new TelefonoDuplicadoExcepcion("Ya existe un cliente con el teléfono " + request.telefono());
        }

        String correoLimpio = (request.correo() != null && !request.correo().isBlank())
                ? request.correo().trim()
                : null;

        Cliente nuevoCliente = new Cliente(
                request.nombre().trim(),
                request.telefono().trim(),
                correoLimpio
        );

        Cliente clienteGuardado = clienteRepositorio.save(nuevoCliente);
        return mapearAResponse(clienteGuardado);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarClientes() {
        return clienteRepositorio.findAll()
                .stream()
                .map(this::mapearAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoExcepcion("No se encontró el cliente con id " + id));
        return mapearAResponse(cliente);
    }

    private ClienteResponse mapearAResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getCorreo(),
                cliente.getFechaCreacion()
        );
    }
}
