package co.com.pragma.api.handler;

import co.com.pragma.api.dto.UsuarioSolicitudDto;
import co.com.pragma.api.mapper.UsuarioMapper;
import co.com.pragma.api.validador.ValidacionManejador;
import co.com.pragma.model.usuario.common.ex.NegocioException;
import co.com.pragma.usecase.crearusuario.CrearUsuarioUseCase;
import co.com.pragma.usecase.crearusuario.ObtenerUsuarioUseCase;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.pragma.api.common.Constantes.PATRON_CORREO;

@Component
@AllArgsConstructor
public class UsuarioHandler {

    private final ValidacionManejador validacionManejador;
    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ObtenerUsuarioUseCase obtenerUsuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    public static final String ERROR_FORMATO_CORREO = "Formato de correo inválido: %s";

    public Mono<ServerResponse> listenPOSTCrearUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UsuarioSolicitudDto.class)
                .flatMap(validacionManejador::validar)
                .map(usuarioMapper::convertirDesde)
                .flatMap(crearUsuarioUseCase::ejecutar)
                .map(usuarioMapper::convertirA)
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(dto));
    }

    public Mono<ServerResponse> listenPOSTObtenerUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .flatMapMany(Flux::fromIterable)
                .flatMap(email -> {
                    if (email.matches(PATRON_CORREO)) {
                        return Mono.just(email);
                    } else {
                        return Mono.error(new NegocioException(ERROR_FORMATO_CORREO.formatted(email)));
                    }
                })
                .collectList()
                .flatMapMany(obtenerUsuarioUseCase::obtenerTodoPorCorreos)
                .map(usuarioMapper::convertirA)
                .collectList()
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED).bodyValue(dto));
    }
}
