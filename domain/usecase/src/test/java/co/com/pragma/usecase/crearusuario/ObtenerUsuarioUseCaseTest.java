package co.com.pragma.usecase.crearusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerUsuarioUseCaseTest {
    @InjectMocks
    ObtenerUsuarioUseCase obtenerUsuarioUseCase;

    @Mock
    UsuarioGateway usuarioGateway;

    @Test
    @DisplayName("Debe retornar lista de usuarios cuando se buscan por correos")
    void testObtenerTodoPorCorreos() {
        List<String> correos = List.of("correo1@test.com", "correo2@test.com");

        Usuario usuario1 = Usuario.builder().correoElectronico("correo1@test.com").build();
        Usuario usuario2 = Usuario.builder().correoElectronico("correo2@test.com").build();

        when(usuarioGateway.obtenerTodoPorCorreos(correos))
                .thenReturn(Flux.just(usuario1, usuario2));

        Flux<Usuario> resultado = obtenerUsuarioUseCase.obtenerTodoPorCorreos(correos);

        StepVerifier.create(resultado)
                .expectNext(usuario1)
                .expectNext(usuario2)
                .verifyComplete();

        verify(usuarioGateway).obtenerTodoPorCorreos(correos);
    }

    @Test
    @DisplayName("Debe retornar usuario con rol al buscar por correo")
    void testObtenerPorCorreo_ok() {
        String correo = "correo1@test.com";

        Usuario usuario = Usuario.builder()
                .correoElectronico(correo)
                .nombres("Natalia")
                .build();

        when(usuarioGateway.obtenerPorCorreoElectronico(correo))
                .thenReturn(Mono.just(usuario));

        when(usuarioGateway.obtenerNombreRolPorCorreoElectronico(correo))
                .thenReturn(Mono.just("ADMIN"));

        Mono<Usuario> resultado = obtenerUsuarioUseCase.obtenerPorCorreo(correo);

        StepVerifier.create(resultado)
                .expectNextMatches(u ->
                        u.getCorreoElectronico().equals(correo) &&
                                u.getNombres().equals("Natalia") &&
                                u.getNombreRol().equals("ADMIN"))
                .verifyComplete();

        verify(usuarioGateway).obtenerPorCorreoElectronico(correo);
        verify(usuarioGateway).obtenerNombreRolPorCorreoElectronico(correo);
    }


}
