package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static co.com.pragma.api.common.Constantes.PATRON_CONTRASENA;
import static co.com.pragma.api.common.Constantes.PATRON_CORREO;

@Schema(
        name = "UsuarioDto",
        requiredProperties = {"correoElectronico", "contrasena"}
)
public record LoginSolicitudDto(
        @Schema(example = "laura.velasquez@gmail.com")
        @NotBlank(message = "El correo electronico es obligatorio y no puede estar vacio")
        @Pattern(regexp = PATRON_CORREO, message = "El formato del correo electronico no es correcto")
        String correoElectronico,

        @Schema(example = "Password1$")
        @NotBlank(message = "La contraseña es obligatorio y no puede estar vacia")
        @Pattern(regexp = PATRON_CONTRASENA, message = "La contraseña debe contener al menos un dígito, una minuscula, " +
                "una mayuscula, un caracter especial (@$!%*?&) y una longitud mínima 8 y máxima 20 caracteres")
        String contrasena
) {
}
