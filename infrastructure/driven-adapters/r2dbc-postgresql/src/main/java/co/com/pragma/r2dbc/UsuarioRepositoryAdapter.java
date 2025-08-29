package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioGateway;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.model.entities.UsuarioData;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Repository
public class UsuarioRepositoryAdapter extends ReactiveAdapterOperations<Usuario, UsuarioData, Long, UsuarioRepository>
        implements UsuarioGateway {

    private final TransactionalOperator transactionalOperator;

    public UsuarioRepositoryAdapter(UsuarioRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Usuario> guardar(Usuario usuario) {
        return Mono.just(toData(usuario))
                .map(data -> data.toBuilder()
                        .idRol(UUID.fromString(usuario.getIdRol())).build())
                .flatMap(this::saveData)
                .map(this::toEntity)
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Boolean> existePorCorreoElectronico(String correoElectronico) {
        return this.repository.existsByCorreoElectronico(correoElectronico);
    }

    @Override
    public Mono<Boolean> existePorIdentificacion(String identificacion) {
        return this.repository.existsByIdentificacion(identificacion);
    }

    @Override
    public Flux<Usuario> obtenerTodoPorCorreos(List<String> correos) {
        return repository.findAllByCorreoElectronicoIn(correos)
                .map(this::toEntity);
    }

    @Override
    public Mono<Usuario> obtenerPorCorreoElectronico(String correoElectronico) {
        return repository.findByCorreoElectronico(correoElectronico)
                .map(this::toEntity);
    }

    @Override
    public Mono<String> obtenerNombreRolPorCorreoElectronico(String correoElectronico) {
        return repository.obtenerNombreRolPorCorreoElectronico(correoElectronico);
    }

    @Override
    public Mono<String> obtenerIdRolPorNombreRol(String nombreRol) {
        return repository.obtenerIdRolPorNombreRol(nombreRol)
                .map(UUID::toString);
    }
}
