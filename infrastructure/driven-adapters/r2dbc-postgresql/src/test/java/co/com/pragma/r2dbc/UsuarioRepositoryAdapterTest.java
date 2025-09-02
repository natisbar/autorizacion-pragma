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
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {

    @InjectMocks
    UsuarioRepositoryAdapter repositoryAdapter;

    @Mock
    UsuarioRepository repository;

    @Mock
    TransactionalOperator transactionalOperator;

    @Mock
    ObjectMapper mapper;

    @Test
    void debeGuardarUsuario() {
        UsuarioData entity = UsuarioData.builder().id(1L).build();
        Usuario usuario = Usuario.builder().id(1L).idRol(UUID.randomUUID().toString()).build();

        when(repository.save(any(UsuarioData.class))).thenReturn(Mono.just(entity));
        when(mapper.map(any(UsuarioData.class), eq(Usuario.class))).thenReturn(usuario);
        when(mapper.map(any(Usuario.class), eq(UsuarioData.class))).thenReturn(entity);
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));


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

    @Test
    void debeObtenerUsuariosPorCorreos() {
        List<String> correos = List.of("correo1@test.com", "correo2@test.com");

        UsuarioData entity1 = UsuarioData.builder().id(1L).correoElectronico("correo1@test.com").build();
        UsuarioData entity2 = UsuarioData.builder().id(2L).correoElectronico("correo2@test.com").build();

        Usuario usuario1 = Usuario.builder().id(1L).correoElectronico("correo1@test.com").build();
        Usuario usuario2 = Usuario.builder().id(2L).correoElectronico("correo2@test.com").build();

        when(repository.findAllByCorreoElectronicoIn(correos))
                .thenReturn(Flux.just(entity1, entity2));

        when(mapper.map(entity1, Usuario.class)).thenReturn(usuario1);
        when(mapper.map(entity2, Usuario.class)).thenReturn(usuario2);

        Flux<Usuario> result = repositoryAdapter.obtenerTodoPorCorreos(correos);

        StepVerifier.create(result)
                .expectNext(usuario1)
                .expectNext(usuario2)
                .verifyComplete();

        verify(repository).findAllByCorreoElectronicoIn(correos);
    }

    @Test
    void debeObtenerUsuarioPorCorreoElectronico() {
        String correo = "correo@test.com";
        UsuarioData entity = UsuarioData.builder().id(1L).correoElectronico(correo).build();
        Usuario usuario = Usuario.builder().id(1L).correoElectronico(correo).build();

        when(repository.findByCorreoElectronico(correo)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.obtenerPorCorreoElectronico(correo);

        StepVerifier.create(result)
                .expectNextMatches(u -> u.getCorreoElectronico().equals(correo))
                .verifyComplete();

        verify(repository).findByCorreoElectronico(correo);
    }

    @Test
    void debeObtenerNombreRolPorCorreoElectronico() {
        String correo = "correo@test.com";

        when(repository.obtenerNombreRolPorCorreoElectronico(correo))
                .thenReturn(Mono.just("ADMIN"));

        Mono<String> result = repositoryAdapter.obtenerNombreRolPorCorreoElectronico(correo);

        StepVerifier.create(result)
                .expectNext("ADMIN")
                .verifyComplete();

        verify(repository).obtenerNombreRolPorCorreoElectronico(correo);
    }

    @Test
    void debeObtenerIdRolPorNombreRol() {
        String nombreRol = "ADMIN";
        UUID uuid = UUID.randomUUID();

        when(repository.obtenerIdRolPorNombreRol(nombreRol)).thenReturn(Mono.just(uuid));

        Mono<String> result = repositoryAdapter.obtenerIdRolPorNombreRol(nombreRol);

        StepVerifier.create(result)
                .expectNext(uuid.toString())
                .verifyComplete();

        verify(repository).obtenerIdRolPorNombreRol(nombreRol);
    }

}
