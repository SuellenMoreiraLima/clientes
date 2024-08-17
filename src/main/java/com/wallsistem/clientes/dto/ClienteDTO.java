package com.wallsistem.clientes.dto;

import com.wallsistem.clientes.model.Cliente;
import com.wallsistem.clientes.model.Sexo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String cpf;
    private Sexo sexo;



    public Cliente toEntity(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Cliente.class);
    }


}
