package com.wallsistem.clientes.model;

import com.wallsistem.clientes.dto.ClienteDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "Nome é obrigatório")
    @Size(max = 40,  message = "Nome deve ter no máximo 40 caracteres")
    private String nome;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @NotNull
    @CPF
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
    private String cpf;

    @NotNull(message = "Sexo é obrigatório")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    public ClienteDTO toDTO(){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(this, ClienteDTO.class);
    }
}