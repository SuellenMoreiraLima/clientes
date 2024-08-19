package com.wallsistem.clientes;

import com.wallsistem.clientes.controller.ClienteController;
import com.wallsistem.clientes.dto.ClienteDTO;
import com.wallsistem.clientes.handler.ConflictException;
import com.wallsistem.clientes.handler.NoContentException;
import com.wallsistem.clientes.model.Cliente;
import com.wallsistem.clientes.repository.ClienteRepository;
import com.wallsistem.clientes.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static com.wallsistem.clientes.model.Sexo.MASCULINO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    private ClienteService clienteService;
    private ClienteController clienteController;
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        clienteService = mock(ClienteService.class);
        clienteRepository = mock(ClienteRepository.class);
        clienteController = new ClienteController(clienteService, clienteRepository);
    }

    @Test
    void listarTodosClientes() {
        ClienteDTO clienteDTO = new ClienteDTO();
        Page<ClienteDTO> clientesPage = new PageImpl<>(Arrays.asList(clienteDTO));

        when(clienteService.listarTodosCliente(anyInt(), anyInt())).thenReturn(clientesPage);

        Page<ClienteDTO> result = clienteController.listarTodosClientes(0, 5);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(clienteService, times(1)).listarTodosCliente(anyInt(), anyInt());
    }

    @Test
    void salvarCliente() {
        ClienteDTO clienteDTO = new ClienteDTO();
        when(clienteService.criarNovoClienteValidandoCampos(any(ClienteDTO.class))).thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = clienteController.salvarCliente(clienteDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clienteDTO, response.getBody());
        verify(clienteService, times(1)).criarNovoClienteValidandoCampos(any(ClienteDTO.class));
    }

    @Test
    void buscarClientePorCPF() {
        String cpf = "12345678901";
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setCpf(cpf);

        when(clienteService.buscarClientesPorCPF(cpf)).thenReturn(clienteDTO.toEntity());

        ResponseEntity<ClienteDTO> response = clienteController.buscarClientePorCPF(cpf);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(clienteDTO.getCpf(), response.getBody().getCpf());
        assertEquals(clienteDTO.getNome(), response.getBody().getNome());

        verify(clienteService, times(1)).buscarClientesPorCPF(cpf);
    }


    @Test
    void atualizarClientes() {
        ClienteDTO clienteDTO = new ClienteDTO();
        when(clienteService.atualizarClientes(anyLong(), any(ClienteDTO.class))).thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = clienteController.atualizarClientes(1L, clienteDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteDTO, response.getBody());
        verify(clienteService, times(1)).atualizarClientes(anyLong(), any(ClienteDTO.class));
    }

    @Test
    void atualizarClienteInexistenteERetornarStatus204() {
        when(clienteService.atualizarClientes(anyLong(), any(ClienteDTO.class))).thenThrow(new NoContentException());

        ResponseEntity<ClienteDTO> response = clienteController.atualizarClientes(1L, new ClienteDTO());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteService, times(1)).atualizarClientes(anyLong(), any(ClienteDTO.class));
    }

    @Test
    void deletarCliente() {
        doNothing().when(clienteService).deletarClientes(anyLong());

        ResponseEntity<ClienteDTO> response = clienteController.deletarClientes(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteService, times(1)).deletarClientes(anyLong());
    }

    @Test
    void deletarClienteInexistenteERetornaStatus404() {
        doThrow(new ConflictException("Cliente com o id não encontrado!")).when(clienteService).deletarClientes(anyLong());

        assertThrows(ConflictException.class, () -> clienteController.deletarClientes(1L));
        verify(clienteService, times(1)).deletarClientes(anyLong());
    }
}
