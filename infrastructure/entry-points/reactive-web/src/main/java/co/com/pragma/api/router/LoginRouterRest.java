package co.com.pragma.api.router;

import co.com.pragma.api.dto.UsuarioSolicitudDto;
import co.com.pragma.api.exception.Error;
import co.com.pragma.api.handler.LoginHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouterRest {

    @Bean
    @RouterOperation(
            operation = @Operation(
                    operationId = "crearUsuario",
                    summary = "Crear un nuevo usuario",
                    description = "Este endpoint permite registrar un usuario en el sistema. "
                            + "Requiere que la identificación y el correo electrónico sean únicos. "
                            + "El salario base debe estar en el rango de 0 a 15'000.000.",
                    tags = {"Usuario"},
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioSolicitudDto.class)
                            )
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Usuario creado exitosamente",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UsuarioSolicitudDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Usuario creado",
                                                            value = "{ \"nombres\": \"Laura\", " +
                                                                    "\"apellidos\": \"Martinez\", " +
                                                                    "\"fechaNacimiento\": \"1990-05-04\", " +
                                                                    "\"identificacion\": \"10072884\", " +
                                                                    "\"direccion\": \"cll 6 sur\", " +
                                                                    "\"telefono\": \"1111111111\", " +
                                                                    "\"correoElectronico\": \"laura.martinez3@gmail.com\", " +
                                                                    "\"salarioBase\": \"2000000\" }"
                                                    )
                                            }

                                    )
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Datos inválidos en la solicitud",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Error.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Datos inválidos",
                                                            value = "{ \"codigo\": \"400\", \"mensaje\": \"Los nombres son obligatorios y no pueden estar vacios, " +
                                                                    "El teléfono solo puede contener valores numericos y no puede tener mas de 10 digitos\" }"
                                                    )
                                            }

                                    )
                            ),
                            @ApiResponse(
                                    responseCode = "409",
                                    description = "Conflicto: el correo o la identificación ya están registrados",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Error.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Usuario ya existe",
                                                            value = "{ \"codigo\": \"409\", \"mensaje\": \"El correo electrónico proporcionado ya está en uso\" }"
                                                    )
                                            }
                                    )
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> routerFunction(LoginHandler loginHandler) {
        return route(POST("/v1/login"), loginHandler::listenPOSTUseCase);
    }
}
