package co.com.pragma.api.handler;

import co.com.pragma.api.dto.LoginRespuestaDto;
import co.com.pragma.api.dto.LoginSolicitudDto;
import co.com.pragma.api.exception.NoAutorizadoException;
import co.com.pragma.api.seguridad.PasswordService;
import co.com.pragma.api.seguridad.jwt.JwtService;
import co.com.pragma.api.validador.ValidacionManejador;
import co.com.pragma.usecase.crearusuario.ObtenerUsuarioUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class LoginHandler {

    private final ValidacionManejador validacionManejador;
    private final ObtenerUsuarioUseCase obtenerUsuarioUseCase;
    private final PasswordService passwordService;
    private final JwtService jwtService;
    private static final String AUTENTICACION_FALLIDA = "Autenticación fallida, usuario o contraseña incorrectos";

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginSolicitudDto.class)
                .flatMap(validacionManejador::validar)
                .flatMap(loginSolicitudDto -> obtenerUsuarioUseCase.obtenerPorCorreo(loginSolicitudDto.correoElectronico())
                        .switchIfEmpty(Mono.error(new NoAutorizadoException(AUTENTICACION_FALLIDA)))
                        .flatMap(usuario -> {
                            if (!passwordService.matches(loginSolicitudDto.contrasena(), usuario.getContrasena())) {
                                return Mono.error(new NoAutorizadoException(AUTENTICACION_FALLIDA));
                            }
                            return Mono.just(new LoginRespuestaDto(jwtService.generarToken(
                                    usuario.getId(),
                                    usuario.getCorreoElectronico(),
                                    usuario.getNombreRol())));
                        }))
                .flatMap(dto -> ServerResponse.status(HttpStatus.OK).bodyValue(dto));
    }
}
