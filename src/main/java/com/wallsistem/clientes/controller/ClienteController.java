package com.wallsistem.clientes.controller;

import com.wallsistem.clientes.dto.ClienteDTO;
import com.wallsistem.clientes.handler.NoContentException;
import com.wallsistem.clientes.repository.ClienteRepository;
import com.wallsistem.clientes.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    ClienteService clienteService;

    public ClienteController(ClienteService clienteService, ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }


    @Operation(summary = "Retorna uma lista de cliente", description = "Retorna uma lista de clientes ordenada pelo nome com paginação.")
    @GetMapping
    public Page<ClienteDTO> listarTodosClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return clienteService.listarTodosCliente(page, size);
    }

    @Operation(summary = "Salvar um novo cliente", description = "Cria um novo cliente com as informações fornecidas.")
    @PostMapping
    public ResponseEntity<ClienteDTO> salvarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
            ClienteDTO cliente = clienteService.criarNovoClienteValidandoCampos(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDTO);
    }

    @Operation(summary = "Obter um cliente pelo CPF", description = "Retorna o cliente correspondente ao CPF fornecido.")
    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteDTO> buscarClientePorCPF(@PathVariable String cpf) {
        clienteRepository.existsByCpf(cpf);

                ClienteDTO cliente = clienteService.buscarClientesPorCPF(cpf).toDTO();
                return ResponseEntity.status(HttpStatus.OK).body(cliente);

    }

    @Operation(summary = "Atualizar um cliente existente", description = "Atualiza as informações de um cliente existente.")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizarClientes(@PathVariable Long id,@Valid @RequestBody ClienteDTO clienteDTO){
        try {
            ClienteDTO cliente = clienteService.atualizarClientes(id, clienteDTO);
            return ResponseEntity.status(HttpStatus.OK).body(cliente);
        } catch (NoContentException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Operation(summary = "Remover um cliente", description = "Remove um cliente com base no ID fornecido.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ClienteDTO> deletarClientes(@PathVariable Long id){
        clienteService.deletarClientes(id);
        return ResponseEntity.noContent().build();
    }
}
