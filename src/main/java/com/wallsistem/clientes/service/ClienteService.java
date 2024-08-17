package com.wallsistem.clientes.service;

import com.wallsistem.clientes.dto.ClienteDTO;
import com.wallsistem.clientes.model.Cliente;
import com.wallsistem.clientes.repository.ClienteRepository;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Page<ClienteDTO> listarTodosCliente(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome").descending());
         Page<Cliente> clientes = clienteRepository.findAll(pageable);
        Page<ClienteDTO> clienteDTOs = clientes.map(cliente -> cliente.toDTO());
        return clienteDTOs;
    }
    public Cliente buscarClientesPorCPF(String cpf){

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        Optional<Cliente> cliente = clienteRepository.findByCpf(cpfLimpo);

        if (cliente.isPresent()) {
            return cliente.get().toDTO().toEntity();
        } else {
            throw new RuntimeException("Cliente não encontrado com CPF: " + cpf); //
        }
    }

    public ClienteDTO criarNovoClienteValidandoCampos(ClienteDTO clienteDTO){
        String limparCpf = clienteDTO.getCpf().replaceAll("[^0-9]", "");
        if (clienteRepository.existsByCpf(clienteDTO.getCpf())){
            throw new IllegalArgumentException("CPF já cadastrado.");

        }
        Cliente cliente = clienteDTO.toEntity();
        cliente.setCpf(limparCpf);
        clienteRepository.save(cliente);
        return cliente.toDTO();
    }

//
//    public Cliente atualizarClientes(){}
//
//    public Cliente deletarClientes(){}
}
