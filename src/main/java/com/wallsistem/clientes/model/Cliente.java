package com.wallsistem.clientes.model;

import com.wallsistem.clientes.dto.ClienteDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;

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
    @NotNull
    @Size(max = 40)
    private String nome;
    @NotNull
    private LocalDate dataNascimento;
    @NotNull
    @CPF
    private String cpf;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    public ClienteDTO toDTO(){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(this, ClienteDTO.class);
    }
}