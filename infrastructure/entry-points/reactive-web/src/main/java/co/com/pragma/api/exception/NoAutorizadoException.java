package co.com.pragma.api.exception;

import co.com.pragma.model.usuario.common.ex.NegocioException;

public class NoAutorizadoException extends NegocioException {
    public NoAutorizadoException(String message) {
        super(message);
    }
}
