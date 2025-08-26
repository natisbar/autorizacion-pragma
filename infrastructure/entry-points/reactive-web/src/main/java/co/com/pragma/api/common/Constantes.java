package co.com.pragma.api.common;

public class Constantes {
    public static final String PATRON_CORREO = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final String PATRON_FECHA = "\\d{4}-\\d{2}-\\d{2}";
    public static final String PATRON_SALARIO = "^(?:\\d{1,7}(?:\\.\\d{1,2})?|15000000(?:\\.00?)?)$";
    public static final String PATRON_ALFANUMERICO = "^[a-zA-Z0-9]+$";
    public static final String PATRON_ALFANUMERICO_ESPACIO = "^[a-zA-Z0-9 ]+$";
    public static final String PATRON_TELEFONO = "^[0-9]{1,10}$";
    public static final String PATRON_DIRECCION = "^[A-Za-z0-9#\\- ]+$";
}
