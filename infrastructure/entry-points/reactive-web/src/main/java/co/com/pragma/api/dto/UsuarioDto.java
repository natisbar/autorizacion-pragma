package co.com.pragma.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static co.com.pragma.api.common.Constantes.PATRON_CORREO;

public record UsuarioDto(
        @NotBlank(message = "Los nombres son obligatorios y no pueden estar vacios")
        String nombres,
        @NotBlank(message = "Los apellidos son obligatorios y no pueden estar vacios")
        String apellidos,
        LocalDate fechaNacimiento,
        @NotBlank(message = "La identificacion es obligatoria y no puede estar vacia")
        String identificacion,
        String direccion,
        String telefono,
        @NotBlank(message = "El correo electronico es obligatorio y no puede estar vacio")
        @Pattern(regexp = PATRON_CORREO, message = "El formato del correo electronico no es correcto")
        String correoElectronico,
        @NotNull
        @DecimalMin(value = "0.00", message = "El salario base debe ser mayor o igual a 0")
        @DecimalMax(value = "15000000.00", message = "El salario base debe ser menor o igual a 15000000")
        BigDecimal salarioBase
) {
}
