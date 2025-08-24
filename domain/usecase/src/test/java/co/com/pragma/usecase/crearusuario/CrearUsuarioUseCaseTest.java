package co.com.pragma.usecase.crearusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.common.ex.NegocioException;
import co.com.pragma.model.usuario.gateways.UsuarioGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static co.com.pragma.usecase.crearusuario.CrearUsuarioUseCase.CORREO_ELECTRONICO_EXISTE;
import static co.com.pragma.usecase.crearusuario.CrearUsuarioUseCase.DOCUMENTO_EXISTE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearUsuarioUseCaseTest {
    @InjectMocks
    CrearUsuarioUseCase crearUsuarioUseCase;

    @Mock
    UsuarioGateway usuarioGateway;

    @Test
    void debeCrearElUsuario() {
        Usuario usuario = Usuario.builder().correoElectronico("correo@correo.com").identificacion("112233").build();

        when(usuarioGateway.existePorIdentificacion(usuario.getIdentificacion())).thenReturn(Mono.just(false));
        when(usuarioGateway.existePorCorreoElectronico(usuario.getCorreoElectronico())).thenReturn(Mono.just(false));
        when(usuarioGateway.guardar(any(Usuario.class))).thenReturn(Mono.just(usuario.toBuilder().id(1L).build()));

        Mono<Usuario> result = crearUsuarioUseCase.ejecutar(usuario);

        StepVerifier.create(result)
                .expectNextMatches(respuesta -> respuesta.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void debeGenerarExcepcion() {
        Usuario usuario = Usuario.builder().correoElectronico("correo@correo.com").identificacion("112233").build();
        List<String> listaErrores = List.of(CORREO_ELECTRONICO_EXISTE, DOCUMENTO_EXISTE);

        when(usuarioGateway.existePorIdentificacion(usuario.getIdentificacion())).thenReturn(Mono.just(true));
        when(usuarioGateway.existePorCorreoElectronico(usuario.getCorreoElectronico())).thenReturn(Mono.just(true));

        Mono<Usuario> result = crearUsuarioUseCase.ejecutar(usuario);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof NegocioException &&
                                throwable.getMessage().equals(String.join(", ", listaErrores))
                )
                .verify();
    }
}
