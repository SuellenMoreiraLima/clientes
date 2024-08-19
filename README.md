# API de Clientes

## Descrição

A API de Clientes permite gerenciar informações de clientes com operações CRUD (Criar, Ler, Atualizar e Excluir).
Oferece endpoints para listar todos os clientes, buscar clientes por CPF, adicionar novos clientes, atualizar e 
remover clientes existentes. Utiliza Spring Boot, JPA, e H2 para persistência de dados, e Swagger para 
documentação interativa da API.



## Requisitos

- Java 17
- Maven
- Git

## Instruções

1. **Clone o repositório:**

    ```bash
    git clone https://github.com/SuellenMoreiraLima/clientes.git
    cd clientes
    ```

2. **Compile e execute:**

    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

   A aplicação será iniciada na porta 8080.

3. **Dados na Tabela:**

    Para adicionar dados na tabela, use o console H2:
    - Acesse [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
    - URL de conexão: `jdbc:h2:mem:testdb`
    - usuario: sa
    - senha: password

    Execute os comandos SQL:

    ```sql
    INSERT INTO cliente (nome, data_nascimento, cpf, sexo) VALUES ('Maria', '1990-05-10', '12345678901', 'FEMININO');
    INSERT INTO cliente (nome, data_nascimento, cpf, sexo) VALUES ('João', '1985-08-15', '98765432100', 'MASCULINO');
    INSERT INTO cliente (nome, data_nascimento, cpf, sexo) VALUES ('Ana', '2000-12-25', '45678912300', 'FEMININO');
    ```

4. **Rodar Testes:**

    ```bash
    ./mvnw test
    ```

5. **Documentação Swagger:**

    Acesse [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) para ver a documentação da API.

## Estrutura do Projeto

- Código-fonte: `src/main/java/com/wallsistem/clientes/`
- Testes: `src/test/java/com/wallsistem/clientes/`
