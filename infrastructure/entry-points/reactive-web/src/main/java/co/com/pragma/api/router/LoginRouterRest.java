package co.com.pragma.api.router;

import co.com.pragma.api.dto.LoginRespuestaDto;
import co.com.pragma.api.dto.LoginSolicitudDto;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouterRest {

    @Bean
    @RouterOperation(
            path = "/v1/login",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.POST,
            beanClass = LoginHandler.class,
            beanMethod = "listenPOSTUseCase",
            operation = @Operation(
                    operationId = "login",
                    summary = "Autenticar usuario",
                    description = "Este endpoint permite autenticar un usuario en el sistema. "
                            + "Si las credenciales son válidas, devuelve un token JWT que debe usarse "
                            + "en las siguientes solicitudes en el encabezado **Authorization: Bearer {token}**.",
                    tags = {"Autenticación"},
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginSolicitudDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo de login",
                                                    value = "{ \"correoElectronico\": \"laura.velasquez@gmail.com\", "
                                                            + "\"contrasena\": \"Password1$\" }"
                                            )
                                    }
                            )
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Autenticación exitosa",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = LoginRespuestaDto.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Token de acceso",
                                                            value = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\" }"
                                                    )
                                            }
                                    )
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Solicitud inválida: datos mal formados",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Error.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Error de validación",
                                                            value = "{ \"codigo\": \"400\", \"mensaje\": \"El correo electronico es obligatorio y no puede estar vacio\" }"
                                                    )
                                            }
                                    )
                            ),
                            @ApiResponse(
                                    responseCode = "401",
                                    description = "Credenciales inválidas",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Error.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Login fallido",
                                                            value = "{ \"codigo\": \"401\", \"mensaje\": \"Autenticación fallida, usuario o contraseña incorrectos\" }"
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

