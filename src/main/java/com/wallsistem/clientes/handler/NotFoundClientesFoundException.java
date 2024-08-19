package com.wallsistem.clientes.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundClientesFoundException extends RuntimeException {
    public NotFoundClientesFoundException(String mensagem) {
        super(mensagem);
    }
}
