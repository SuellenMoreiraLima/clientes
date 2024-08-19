package com.wallsistem.clientes.dto;

import com.wallsistem.clientes.model.Cliente;
import com.wallsistem.clientes.model.Sexo;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Modelo de cliente")
public class ClienteDTO {

    @Schema(description = "ID do cliente", example = "1", required = true)
    private Long id;

    @Schema(description = "Nome do cliente", example = "João da Silva", maxLength = 40, required = true)
    private String nome;

    @Schema(description = "Data de nascimento do cliente", example = "1990-01-01", required = true)
    private LocalDate dataNascimento;

    @Schema(description = "CPF do cliente", example = "12345678901", required = true)
    private String cpf;

    @Schema(description = "Sexo do cliente", example = "MASCULINO", required = true)
    private Sexo sexo;

    public Cliente toEntity(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Cliente.class);
    }
}
