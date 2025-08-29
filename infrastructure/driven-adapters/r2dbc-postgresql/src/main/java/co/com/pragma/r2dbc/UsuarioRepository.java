package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.model.entities.UsuarioData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;


public interface UsuarioRepository extends ReactiveCrudRepository<UsuarioData, Long>, ReactiveQueryByExampleExecutor<UsuarioData> {

    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
    Mono<Boolean> existsByIdentificacion(String identificacion);

    Flux<UsuarioData> findAllByCorreoElectronicoIn(List<String> correos);

    Mono<UsuarioData> findByCorreoElectronico(String correo);

    @Query("""
            SELECT r.nombre FROM usuario u
             INNER JOIN rol r on r.id = u.id_rol
             WHERE u.email = :correo
            """)
    Mono<String> obtenerNombreRolPorCorreoElectronico(String correo);

    @Query("""
            SELECT r.id FROM rol r
             WHERE r.nombre = UPPER(:nombre)
            """)
    Mono<UUID> obtenerIdRolPorNombreRol(String nombre);

}
