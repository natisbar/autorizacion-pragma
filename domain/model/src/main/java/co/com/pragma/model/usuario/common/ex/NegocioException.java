package co.com.pragma.model.usuario.common.ex;

public class NegocioException extends RuntimeException{
    public NegocioException(String mensaje){ super(mensaje); }

    public NegocioException(String mensaje, Throwable throwable){ super(mensaje, throwable); }
}
