package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.model.entities.UsuarioData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface UsuarioRepository extends ReactiveCrudRepository<UsuarioData, Long>, ReactiveQueryByExampleExecutor<UsuarioData> {

    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
    Mono<Boolean> existsByIdentificacion(String identificacion);

    @Query("""
            insert into usuario_relacion (relacion_id)
            values(:idUsuario);
            """)
    Mono<Void> crear(long idUsuario);

}
