package com.devsenior.cotiza.cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devsenior.cotiza.cliente.dto.ClienteRequest;
import com.devsenior.cotiza.cliente.dto.ClienteResponse;

@ExtendWith(MockitoExtension.class)
class ClienteServicioTest {

    @Mock
    private ClienteRepositorio clienteRepositorio;

    private ClienteServicio clienteServicio;

    @BeforeEach
    void setUp() {
        clienteServicio = new ClienteServicio(clienteRepositorio);
    }

    @Test
    @DisplayName("crearCliente guarda y retorna el cliente cuando los datos son válidos")
    void crearClienteExitoso() {
        ClienteRequest request = new ClienteRequest("Pedro Gomez", "3109876543", "pedro@test.com");
        when(clienteRepositorio.existsByTelefono("3109876543")).thenReturn(false);

        Cliente clienteGuardado = new Cliente("Pedro Gomez", "3109876543", "pedro@test.com");
        clienteGuardado.setId(1L);
        clienteGuardado.setFechaCreacion(LocalDateTime.now());

        when(clienteRepositorio.save(any(Cliente.class))).thenReturn(clienteGuardado);

        ClienteResponse response = clienteServicio.crearCliente(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Pedro Gomez", response.nombre());
        assertEquals("3109876543", response.telefono());
        verify(clienteRepositorio).save(any(Cliente.class));
    }

    @Test
    @DisplayName("crearCliente lanza TelefonoDuplicadoExcepcion si el teléfono ya existe")
    void crearClienteTelefonoDuplicadoLanzaExcepcion() {
        ClienteRequest request = new ClienteRequest("Pedro Gomez", "3109876543", "pedro@test.com");
        when(clienteRepositorio.existsByTelefono("3109876543")).thenReturn(true);

        assertThrows(TelefonoDuplicadoExcepcion.class, () -> clienteServicio.crearCliente(request));
    }

    @Test
    @DisplayName("obtenerClientePorId retorna cliente si existe")
    void obtenerClienteExistente() {
        Cliente cliente = new Cliente("Pedro Gomez", "3109876543", null);
        cliente.setId(1L);
        cliente.setFechaCreacion(LocalDateTime.now());

        when(clienteRepositorio.findById(1L)).thenReturn(Optional.of(cliente));

        ClienteResponse response = clienteServicio.obtenerClientePorId(1L);

        assertNotNull(response);
        assertEquals("Pedro Gomez", response.nombre());
    }

    @Test
    @DisplayName("obtenerClientePorId lanza ClienteNoEncontradoExcepcion si no existe")
    void obtenerClienteInexistenteLanzaExcepcion() {
        when(clienteRepositorio.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ClienteNoEncontradoExcepcion.class, () -> clienteServicio.obtenerClientePorId(99L));
    }

    @Test
    @DisplayName("listarClientes retorna lista completa mapeada")
    void listarClientesExitoso() {
        Cliente cliente = new Cliente("Pedro Gomez", "3109876543", null);
        cliente.setId(1L);
        cliente.setFechaCreacion(LocalDateTime.now());

        when(clienteRepositorio.findAll()).thenReturn(List.of(cliente));

        List<ClienteResponse> lista = clienteServicio.listarClientes();

        assertEquals(1, lista.size());
        assertEquals("Pedro Gomez", lista.get(0).nombre());
    }
}
