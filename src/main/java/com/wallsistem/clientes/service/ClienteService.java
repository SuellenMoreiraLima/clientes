package com.wallsistem.clientes.service;

import com.wallsistem.clientes.model.Cliente;
import com.wallsistem.clientes.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente listarTodosCliente(){
        List<Cliente> clientes = clienteRepository.findAll().stream().filter(cliente -> cliente.getName());
    }
    public Cliente listarCLientesPorCPF(){

    }

    public Cliente criarNovoClienteValidandoCampos(){

    }

    public Cliente atualizarClientes(){}

    public Cliente deletarClientes(){}
}
