package co.com.pragma.api.dto;

public record UsuarioRespuestaDto(
        String nombres,
        String apellidos,
        String fechaNacimiento,
        String identificacion,
        String direccion,
        String telefono,
        String correoElectronico,
        String salarioBase,
        String nombreRol
) {
}
