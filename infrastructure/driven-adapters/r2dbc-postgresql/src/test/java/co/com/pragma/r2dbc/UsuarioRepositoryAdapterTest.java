package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.model.entities.UsuarioData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {

    @InjectMocks
    UsuarioRepositoryAdapter repositoryAdapter;

    @Mock
    UsuarioRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void debeGuardarUsuario() {
        UsuarioData entity = UsuarioData.builder().id(1L).build();
        Usuario usuario = Usuario.builder().id(1L).build();

        when(repository.save(any(UsuarioData.class))).thenReturn(Mono.just(entity));
        when(mapper.map(any(UsuarioData.class), eq(Usuario.class))).thenReturn(usuario);
        when(mapper.map(any(Usuario.class), eq(UsuarioData.class))).thenReturn(entity);

        Mono<Usuario> result = repositoryAdapter.guardar(usuario);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(entity.getId()))
                .verifyComplete();
    }

    @Test
    void debeVerSiExistePorCorreoElectronico() {
        when(repository.existsByCorreoElectronico(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> result = repositoryAdapter.existePorCorreoElectronico("correo@corre.com");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(Boolean.TRUE))
                .verifyComplete();
    }

    @Test
    void debeVerSiExistePorIdentificacion() {
        when(repository.existsByIdentificacion(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> result = repositoryAdapter.existePorIdentificacion("112233");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(Boolean.TRUE))
                .verifyComplete();
    }

    @Test
    void mustFindValueById() {
        UsuarioData entity = UsuarioData.builder().id(1L).build();
        Usuario usuario = Usuario.builder().id(1L).build();

        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(any(UsuarioData.class), eq(Usuario.class))).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(entity.getId()))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UsuarioData entity = UsuarioData.builder().id(1L).build();
        Usuario usuario = Usuario.builder().id(1L).build();

        when(repository.findAll()).thenReturn(Flux.fromIterable(List.of(entity)));
        when(mapper.map(any(UsuarioData.class), eq(Usuario.class))).thenReturn(usuario);

        Flux<Usuario> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(entity.getId()))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        UsuarioData entity = UsuarioData.builder().id(1L).build();
        Usuario usuario = Usuario.builder().id(1L).build();

        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(entity));
        when(mapper.map(any(UsuarioData.class), eq(Usuario.class))).thenReturn(usuario);
        when(mapper.map(any(Usuario.class), eq(UsuarioData.class))).thenReturn(entity);

        Flux<Usuario> result = repositoryAdapter.findByExample(usuario);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(entity.getId()))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        UsuarioData entity = UsuarioData.builder().id(1L).build();
        Usuario usuario = Usuario.builder().id(1L).build();

        when(repository.save(any(UsuarioData.class))).thenReturn(Mono.just(entity));
        when(mapper.map(any(UsuarioData.class), eq(Usuario.class))).thenReturn(usuario);
        when(mapper.map(any(Usuario.class), eq(UsuarioData.class))).thenReturn(entity);

        Mono<Usuario> result = repositoryAdapter.save(usuario);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.getId().equals(entity.getId()))
                .verifyComplete();
    }
}
