package com.wallsistem.clientes.controller;

import com.wallsistem.clientes.dto.ClienteDTO;
import com.wallsistem.clientes.repository.ClienteRepository;
import com.wallsistem.clientes.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    ClienteService clienteService;

    public ClienteController(ClienteService clienteService, ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public Page<ClienteDTO> listarTodosClientes(
            @RequestParam(defaultValue =   "0") int page,
            @RequestParam(defaultValue =  "5") int size
    ){
        return clienteService.listarTodosCliente(page, size);
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> salvarCliente(@RequestBody ClienteDTO clienteDTO){
        ClienteDTO cliente = clienteService.criarNovoClienteValidandoCampos(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDTO);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteDTO> buscarClientePorCPF(@PathVariable String cpf){
        ClienteDTO cliente = clienteService.buscarClientesPorCPF(cpf).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClienteDTO> deletarClientes(@PathVariable Long id){
        clienteService.deletarClientes(id);
        return ResponseEntity.noContent().build();
    }
}
