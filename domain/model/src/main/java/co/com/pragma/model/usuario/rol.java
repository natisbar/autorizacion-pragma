package co.com.pragma.model.usuario;

import java.util.Arrays;

public enum rol {
    ADMIN,
    ASESOR,
    CLIENTE;

    public static boolean existeRol(String nombreRol){
        return Arrays.stream(rol.values()).anyMatch(rol -> rol.name().equals(nombreRol));
    }
}
