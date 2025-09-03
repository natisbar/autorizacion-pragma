package co.com.pragma.api.router;

import co.com.pragma.api.dto.UsuarioRespuestaDto;
import co.com.pragma.api.dto.UsuarioSolicitudDto;
import co.com.pragma.api.exception.Error;
import co.com.pragma.api.handler.UsuarioHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UsuarioRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/v1/usuarios",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "listenPOSTCrearUseCase",
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
                                                                    value = "{ \"codigo\": \"400\", \"mensaje\": \"Los nombres son obligatorios y no pueden estar vacíos\" }"
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
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del sistema",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = Error.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Error interno del sistema",
                                                                    value = "{ \"codigo\": \"500\", \"mensaje\": \"Ocurrió un error inesperado, por favor comuniquese comuniquese con el administrador\" }"
                                                            )
                                                    }
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/v1/usuarios/por-correos",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = UsuarioHandler.class,
                    beanMethod = "listenPOSTObtenerUseCase",
                    operation = @Operation(
                            operationId = "obtenerUsuariosPorCorreos",
                            summary = "Obtener usuarios por correo electrónico",
                            description = "Este endpoint permite consultar múltiples usuarios a partir de una lista de correos electrónicos.",
                            tags = {"Usuario"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = String.class, type = "array"),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Lista de correos",
                                                            value = "{ \"correos\": [\"laura.martinez3@gmail.com\", \"carlos.rojas@test.com\"] }"
                                                    )
                                            }
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuarios encontrados",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UsuarioRespuestaDto.class, type = "array"),
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
                                            description = "Solicitud inválida",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = Error.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Datos inválidos",
                                                                    value = "{ \"codigo\": \"400\", \"mensaje\": \"Los nombres son obligatorios y no pueden estar vacíos\" }"
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del sistema",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = Error.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Error interno del sistema",
                                                                    value = "{ \"codigo\": \"500\", \"mensaje\": \"Ocurrió un error inesperado, por favor comuniquese comuniquese con el administrador\" }"
                                                            )
                                                    }
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> usuarioRouterFunction(UsuarioHandler usuarioHandler) {
        return route(POST("/v1/usuarios"), usuarioHandler::listenPOSTCrearUseCase)
                .andRoute(POST("/v1/usuarios/por-correos"), usuarioHandler::listenPOSTObtenerUseCase);
    }
}

