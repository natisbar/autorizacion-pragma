package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static co.com.pragma.api.common.Constantes.*;

@Schema(
        name = "UsuarioSolicitudDto",
        requiredProperties = {"nombres", "apellidos", "identificacion", "correoElectronico", "salarioBase"}
)
public record UsuarioSolicitudDto(
        @Schema(example = "Laura Andrea")
        @NotBlank(message = "Los nombres son obligatorios y no pueden estar vacios")
        @Pattern(regexp = PATRON_ALFANUMERICO_ESPACIO, message = "Los nombres solo pueden contener letras, números y espacios")
        String nombres,

        @Schema(example = "Moreno Velasquez")
        @NotBlank(message = "Los apellidos son obligatorios y no pueden estar vacios")
        @Pattern(regexp = PATRON_ALFANUMERICO_ESPACIO, message = "Los nombres solo pueden contener letras, números y espacios")
        String apellidos,

        @Schema(example = "1992-05-07")
        @Pattern(regexp = PATRON_FECHA, message = "Fecha invalida. Debe estar en formato yyyy-MM-dd")
        String fechaNacimiento,

        @Schema(example = "1122334456")
        @NotBlank(message = "La identificacion es obligatoria y no puede estar vacia")
        @Pattern(regexp = PATRON_ALFANUMERICO, message = "La identificación solo puede contener letras y números")
        String identificacion,

        @Schema(example = "Cll 5sur # 24 - 68")
        @Pattern(regexp = PATRON_DIRECCION, message = "La dirección solo puede contener letras, números, espacio y los simbolos numeral (#) y guion (-)")
        String direccion,

        @Schema(example = "3145556262")
        @Pattern(regexp = PATRON_TELEFONO, message = "El teléfono solo puede contener valores numericos y no puede tener mas de 10 digitos")
        String telefono,

        @Schema(example = "laura.velasquez@gmail.com")
        @NotBlank(message = "El correo electronico es obligatorio y no puede estar vacio")
        @Pattern(regexp = PATRON_CORREO, message = "El formato del correo electronico no es correcto")
        String correoElectronico,

        @Schema(example = "3500000")
        @NotBlank(message = "El salario base es obligatorio y no puede estar vacio")
        @Pattern(regexp = PATRON_SALARIO, message = "El salario base debe ser un valor numerico entre 0 y 15000000")
        String salarioBase,

        @Schema(example = "1")
        @NotBlank(message = "El rol es obligatorio y no puede estar vacio")
        @Pattern(regexp = PATRON_ALFANUMERICO, message = "El nombre rol solo puede contener letras y números")
        String nombreRol,

        @Schema(example = "Password1$")
        @NotBlank(message = "La contraseña es obligatorio y no puede estar vacia")
        @Pattern(regexp = PATRON_CONTRASENA, message = "La contraseña debe contener al menos un dígito, una minuscula, " +
                "una mayuscula, un caracter especial (@$!%*?&) y una longitud mínima 8 y máxima 20 caracteres")
        String contrasena
) {
}
