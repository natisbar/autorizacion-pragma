package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioGateway {
    Mono<Usuario> guardar(Usuario usuario);
    Mono<Boolean> existePorCorreoElectronico(String correoElectronico);
    Mono<Boolean> existePorIdentificacion(String identificacion);
}
