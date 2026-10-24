package com.devsenior.cotiza.cliente;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.devsenior.cotiza.cliente.dto.ClienteRequest;
import com.devsenior.cotiza.cliente.dto.ClienteResponse;

@ExtendWith(MockitoExtension.class)
class ClienteControladorTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteServicio clienteServicio;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ClienteControlador(clienteServicio)).build();
    }

    @Test
    @DisplayName("POST /api/clientes con datos válidos retorna 201")
    void crearClienteValidoRetorna201() throws Exception {
        ClienteResponse response = new ClienteResponse(
                1L, "Juan Pérez", "3001234567", "juan@ejemplo.com", LocalDateTime.now()
        );

        when(clienteServicio.crearCliente(any(ClienteRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre": "Juan Pérez",
                            "telefono": "3001234567",
                            "correo": "juan@ejemplo.com"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.telefono").value("3001234567"));
    }

    @Test
    @DisplayName("POST /api/clientes con teléfono duplicado retorna 409")
    void crearClienteTelefonoDuplicadoRetorna409() throws Exception {
        when(clienteServicio.crearCliente(any(ClienteRequest.class)))
                .thenThrow(new TelefonoDuplicadoExcepcion("Ya existe un cliente con el teléfono 3001234567"));

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre": "Juan Pérez",
                            "telefono": "3001234567"
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("Ya existe un cliente con el teléfono 3001234567"));
    }

    @Test
    @DisplayName("POST /api/clientes con datos inválidos retorna 400")
    void crearClienteInvalidoRetorna400() throws Exception {
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre": "",
                            "telefono": "123",
                            "correo": "correo-no-valido"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/clientes retorna 200 y lista")
    void listarClientesRetorna200() throws Exception {
        when(clienteServicio.listarClientes()).thenReturn(List.of(
                new ClienteResponse(1L, "Juan", "3001234567", null, LocalDateTime.now())
        ));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} retorna 200 si existe")
    void obtenerClienteExistenteRetorna200() throws Exception {
        when(clienteServicio.obtenerClientePorId(1L)).thenReturn(
                new ClienteResponse(1L, "Juan", "3001234567", null, LocalDateTime.now())
        );

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} retorna 404 si no existe")
    void obtenerClienteNoExistenteRetorna404() throws Exception {
        when(clienteServicio.obtenerClientePorId(99L))
                .thenThrow(new ClienteNoEncontradoExcepcion("No se encontró el cliente con id 99"));

        mockMvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No se encontró el cliente con id 99"));
    }
}
