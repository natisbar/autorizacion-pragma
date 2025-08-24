package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.model.entities.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface UsuarioRepository extends ReactiveCrudRepository<UsuarioEntity, Long>, ReactiveQueryByExampleExecutor<UsuarioEntity> {

    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
    Mono<Boolean> existsByIdentificacion(String identificacion);

}
