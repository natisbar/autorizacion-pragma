package co.com.pragma.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static co.com.pragma.api.common.Constantes.PATRON_CORREO;

public record UsuarioDto(
        @NotNull(message = "Los nombres no pueden ser nulos")
        @NotBlank(message = "Los nombres no pueden ser vacios")
        String nombres,
        @NotNull(message = "Los apellidos no pueden ser nulos")
        @NotBlank(message = "Los apellidos no pueden ser vacios")
        String apellidos,
        LocalDate fechaNacimiento,
        @NotNull(message = "La identificación no puede ser nula")
        @NotBlank(message = "La identificación no puede estar vacia")
        String identificacion,
        String direccion,
        String telefono,
        @NotNull(message = "El correoElectronico no puede ser nulo")
        @Pattern(regexp = PATRON_CORREO, message = "El formato del correoElectronico no es correcto")
        String correoElectronico,
        @NotNull
        @DecimalMin(value = "0.00", message = "El salarioBase debe ser mayor o igual a 0")
        @DecimalMax(value = "15000000.00", message = "El salarioBase debe ser menor o igual a 15000000")
        BigDecimal salarioBase
) {
}
