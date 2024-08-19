package com.wallsistem.clientes;

import static com.wallsistem.clientes.model.Sexo.FEMININO;
import static com.wallsistem.clientes.model.Sexo.MASCULINO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.wallsistem.clientes.dto.ClienteDTO;
import com.wallsistem.clientes.handler.ConflictException;
import com.wallsistem.clientes.handler.NoContentException;
import com.wallsistem.clientes.handler.NotFoundClientesFoundException;
import com.wallsistem.clientes.model.Cliente;
import com.wallsistem.clientes.repository.ClienteRepository;
import com.wallsistem.clientes.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClienteServiceTests {

    private ClienteRepository clienteRepository;
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        clienteRepository = mock(ClienteRepository.class);
        clienteService = new ClienteService(clienteRepository);
    }

    @Test
    void listarTodosClienteDeveOrdenarPorNome() {
        Cliente cliente1 = new Cliente(1L, "Maria", LocalDate.of(1990, 5, 10), "12345678901", FEMININO);
        Cliente cliente2 = new Cliente(2L, "João", LocalDate.of(1985, 8, 15), "98765432100", MASCULINO);
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").descending());
        Page<Cliente> page = new PageImpl<>(clientes, pageable, clientes.size());

        when(clienteRepository.findAll(pageable)).thenReturn(page);

        Page<ClienteDTO> result = clienteService.listarTodosCliente(0, 10);

        List<ClienteDTO> clienteDTOs = result.getContent();
        assertEquals(2, clienteDTOs.size());
        assertEquals("Maria", clienteDTOs.get(0).getNome());
        assertEquals("João", clienteDTOs.get(1).getNome());
    }


    @Test
    public void testLitarTodosClientes() {
        Cliente cliente1 = new Cliente(1L, "Rosario", LocalDate.of(1997, 7, 6), "40030293085", FEMININO);
        Cliente cliente2 = new Cliente(2L, "Maria", LocalDate.of(1995, 5, 10), "40030293086", FEMININO);
        Cliente cliente3 = new Cliente(3L, "Ana", LocalDate.of(2000, 3, 15), "40030293087", FEMININO);

        Page<Cliente> pageClientes = new PageImpl<>(List.of(cliente1, cliente2, cliente3));
        Pageable pageable = PageRequest.of(0, 5, Sort.by("nome").descending());
        Mockito.when(clienteRepository.findAll(pageable)).thenReturn(pageClientes);

    Page<ClienteDTO> result = clienteService.listarTodosCliente(0, 5);

    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isEqualTo(3);
    assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
    assertThat(result.getContent().get(0).getNome()).isEqualTo("Rosario");
    assertThat(result.getContent().get(0).getCpf()).isEqualTo("40030293085");
    assertThat(result.getContent().get(0).getDataNascimento()).isEqualTo(
            LocalDate.of(1997, 7, 6));
    assertThat(result.getContent().get(0).getSexo()).isEqualTo(FEMININO);

    verify(clienteRepository, times(1)).findAll(pageable);
}

    @Test
    public void testBuscarTodosClientesQuandoNãoTemCliente() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("nome").descending());
        Mockito.when(clienteRepository.findAll(pageable)).thenReturn(Page.empty());

        Exception exception = assertThrows(NotFoundClientesFoundException.class, () -> {
            clienteService.listarTodosCliente(0, 5);
        });

        assertThat(exception.getMessage()).isEqualTo("Não tem clientes cadastrados.");
        verify(clienteRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testBuscarClientePorCPF() {
        String cpf = "40030293085";
        Cliente cliente = new Cliente(1L, "Rosario", LocalDate.of(1997, 7, 6), cpf, FEMININO);
        Mockito.when(clienteRepository.findByCpf(cpf)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.buscarClientesPorCPF(cpf);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("Rosario");
        assertThat(result.getCpf()).isEqualTo(cpf);
        assertThat(result.getDataNascimento()).isEqualTo(LocalDate.of(1997, 7, 6));
        assertThat(result.getSexo()).isEqualTo(FEMININO);

        verify(clienteRepository, times(1)).findByCpf(cpf);
    }

    @Test
    public void testBuscarClientePorCPFInvalido() {
        String cpfInvalido = "00000000000";
        Mockito.when(clienteRepository.findByCpf(cpfInvalido)).thenReturn(Optional.empty());

        assertThrows(NoContentException.class, () -> {
            clienteService.buscarClientesPorCPF(cpfInvalido);
        });

        verify(clienteRepository, times(1)).findByCpf(cpfInvalido);
    }

@Test
public void testCriarCliente() {
    ClienteDTO clienteDTO = new ClienteDTO(1L, "Nome", LocalDate.of(1990, 1, 1), "12345678900", MASCULINO);
    Cliente cliente = clienteDTO.toEntity();
    Cliente clienteSalvo = cliente;

    Mockito.when(clienteRepository.existsByCpf(cliente.getCpf())).thenReturn(false);
    Mockito.when(clienteRepository.existsById(cliente.getId())).thenReturn(false);
    Mockito.when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

    ClienteDTO resultado = clienteService.criarNovoClienteValidandoCampos(clienteDTO);

    assertThat(resultado).isNotNull();
    assertThat(resultado.getId()).isEqualTo(1L);
    assertThat(resultado.getNome()).isEqualTo("Nome");
    assertThat(resultado.getCpf()).isEqualTo("12345678900");
    assertThat(resultado.getDataNascimento()).isEqualTo(LocalDate.of(1990, 1, 1));
    assertThat(resultado.getSexo()).isEqualTo(MASCULINO);

    verify(clienteRepository, times(1)).existsByCpf(clienteDTO.getCpf());
    verify(clienteRepository, times(1)).existsById(clienteDTO.getId());

    ArgumentCaptor<Cliente> clienteCaptor = ArgumentCaptor.forClass(Cliente.class);
    verify(clienteRepository, times(1)).save(clienteCaptor.capture());

    Cliente clienteCapturado = clienteCaptor.getValue();

    assertThat(clienteCapturado).isNotNull();
    assertThat(clienteCapturado.getCpf()).isEqualTo("12345678900");
    assertThat(clienteCapturado.getNome()).isEqualTo("Nome");
    assertThat(clienteCapturado.getDataNascimento()).isEqualTo(LocalDate.of(1990, 1, 1));
    assertThat(clienteCapturado.getSexo()).isEqualTo(MASCULINO);
}

//    @Test
//    public void testCriarClienteComDadosVazios() {
//        ClienteDTO clienteDTO = new ClienteDTO();
//        clienteDTO.setNome("");
//        clienteDTO.setCpf("");
//        clienteDTO.setDataNascimento(null);
//        clienteDTO.setSexo(null);
//
//        assertThrows(ConstraintViolationException.class, () -> {
//            clienteService.buscarClientesPorCPF(clienteDTO.getCpf());
//        });
//    }

    @Test
    public void testCriarClienteComIdExistenteERetornarQueJaExisteRegistroComEsseId() {
        String cpfExistente = "40030293085";
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L); // ID já existente
        clienteDTO.setNome("Rosario");
        clienteDTO.setCpf(cpfExistente);
        clienteDTO.setDataNascimento(LocalDate.of(1997, 07, 06));
        clienteDTO.setSexo(FEMININO);

        Mockito.when(clienteRepository.existsById(clienteDTO.getId())).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            clienteService.criarNovoClienteValidandoCampos(clienteDTO);
        });

        assertThat(exception.getMessage()).isEqualTo("ID já cadastrado.");
    }


    @Test
    public void testCriarClienteCPFExistente() {
        ClienteDTO clienteDTO = new ClienteDTO(1L, "Nome", LocalDate.of(1990, 1, 1), "12345678900", MASCULINO);

        Mockito.when(clienteRepository.existsByCpf(clienteDTO.getCpf())).thenReturn(true);

        assertThrows(ConflictException.class, () -> clienteService.criarNovoClienteValidandoCampos(clienteDTO));

        verify(clienteRepository, times(1)).existsByCpf(clienteDTO.getCpf());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
@Test
public void testAtualizarCliente() {
    ClienteDTO clienteDTO = new ClienteDTO(1L, "Rosa", LocalDate.of(1990, 1, 1), "12345678900", MASCULINO);
    Cliente cliente = clienteDTO.toEntity();

    Mockito.when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.of(cliente));
    Mockito.when(clienteRepository.save(cliente)).thenReturn(cliente);

    ClienteDTO resultado = clienteService.atualizarClientes(clienteDTO.getId(), clienteDTO);

    assertThat(resultado).isNotNull();
    assertThat(resultado.getId()).isEqualTo(1L);
    assertThat(resultado.getNome()).isEqualTo("Rosa");

    verify(clienteRepository, times(1)).findById(clienteDTO.getId());
    verify(clienteRepository, times(1)).save(cliente);
}


    @Test
    public void testAtualizarClienteInexistente() {
        ClienteDTO clienteDTO = new ClienteDTO(
                1L, "Nome", LocalDate.of(1990, 1, 1), "12345678900", MASCULINO);

        Mockito.when(clienteRepository.findById(clienteDTO.getId())).thenReturn(Optional.empty());

        assertThrows(NoContentException.class, () -> clienteService.atualizarClientes(clienteDTO.getId(), clienteDTO));

        verify(clienteRepository, times(1)).findById(clienteDTO.getId());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }


    @Test
    public void testDeletarCliente() {
        Long clienteId = 1L;

        Mockito.when(clienteRepository.existsById(clienteId)).thenReturn(true);

        clienteService.deletarClientes(clienteId);

        verify(clienteRepository, times(1)).existsById(clienteId);
        verify(clienteRepository, times(1)).deleteById(clienteId);
    }

    @Test
    public void testDeletarClientes_ClienteNaoExiste() {
        Long id = 1L;
        Mockito.when(clienteRepository.existsById(id)).thenReturn(false);

        ConflictException thrown = assertThrows(ConflictException.class, () -> {
            clienteService.deletarClientes(id);
        });

        assertEquals("Cliente com o id:" + id + "não encontrado!", thrown.getMessage());
    }
}
