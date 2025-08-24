package co.com.pragma.r2dbc;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioGateway;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.model.entities.UsuarioEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UsuarioRepositoryAdapter extends ReactiveAdapterOperations<Usuario, UsuarioEntity, Long, UsuarioRepository>
        implements UsuarioGateway {

    public UsuarioRepositoryAdapter(UsuarioRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }

    @Override
    public Mono<Usuario> guardar(Usuario usuario) {
        return this.save(usuario);
    }

    @Override
    public Mono<Boolean> existePorCorreoElectronico(String correoElectronico) {
        return this.repository.existsByCorreoElectronico(correoElectronico);
    }

    @Override
    public Mono<Boolean> existePorIdentificacion(String identificacion) {
        return this.repository.existsByIdentificacion(identificacion);
    }
}
