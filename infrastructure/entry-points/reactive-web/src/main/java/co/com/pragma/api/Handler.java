package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioDto;
import co.com.pragma.api.mapper.UsuarioMapper;
import co.com.pragma.api.validador.ValidacionManejador;
import co.com.pragma.usecase.crearusuario.CrearUsuarioUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class Handler {

    private final ValidacionManejador validacionManejador;
    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final UsuarioMapper usuarioMapper;

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UsuarioDto.class)
                .flatMap(validacionManejador::validar)
                .map(usuarioMapper::convertirDesde)
                .flatMap(crearUsuarioUseCase::ejecutar)
                .map(usuarioMapper::convertirA)
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(dto));
    }
}
