package com.wallsistem.clientes.service;

import com.wallsistem.clientes.dto.ClienteDTO;
import com.wallsistem.clientes.handler.ConflictException;
import com.wallsistem.clientes.handler.NoContentException;
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

        return clienteRepository.findByCpf(cpfLimpo)
                .orElseThrow(() -> new NoContentException());
        }


    public ClienteDTO criarNovoClienteValidandoCampos(ClienteDTO clienteDTO){
        String limparCpf = clienteDTO.getCpf().replaceAll("[^0-9]", "");
        if (clienteRepository.existsByCpf(clienteDTO.getCpf())){
            throw new ConflictException("CPF já cadastrado.");
        }
        Cliente cliente = clienteDTO.toEntity();
        cliente.setCpf(limparCpf);
        clienteRepository.save(cliente);
        return cliente.toDTO();
    }

    public ClienteDTO atualizarClientes(Long id, ClienteDTO clienteDTO){
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            Cliente clienteAtualizar = cliente.get();

            clienteAtualizar.setNome(clienteDTO.getNome());
            clienteAtualizar.setCpf(clienteDTO.getCpf());
            clienteAtualizar.setDataNascimento(clienteDTO.getDataNascimento());
            clienteAtualizar.setSexo(clienteDTO.getSexo());
            return clienteRepository.save(clienteAtualizar).toDTO();

        }else {
            throw new ConflictException("Cliente com ID " + clienteDTO.getId() + "Não encontrado");
        }
    }

    public void deletarClientes(Long id){
        if (clienteRepository.existsById(id)){
            clienteRepository.deleteById(id);
        }else {
            throw new ConflictException("Cliente não encontrado!");
        }

    }
}
