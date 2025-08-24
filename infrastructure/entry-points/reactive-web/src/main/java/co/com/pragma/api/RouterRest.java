package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioDto;
import co.com.pragma.api.exception.Error;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
public class RouterRest {

    @Bean
    @RouterOperation(
            operation = @Operation(
                    operationId = "crearUsuario",
                    summary = "Crear un nuevo usuario",
                    tags = {"Usuario"},
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioDto.class)
                            )
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Usuario creado exitosamente",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = UsuarioDto.class)
                                    )
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Datos inválidos en la solicitud",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Error.class)
                                    )
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/v1/usuarios"), handler::listenPOSTUseCase);
    }
}
