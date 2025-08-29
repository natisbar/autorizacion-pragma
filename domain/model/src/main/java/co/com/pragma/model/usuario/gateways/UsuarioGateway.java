package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UsuarioGateway {
    Mono<Usuario> guardar(Usuario usuario);
    Mono<Boolean> existePorCorreoElectronico(String correoElectronico);
    Mono<Boolean> existePorIdentificacion(String identificacion);
    Flux<Usuario> obtenerTodoPorCorreos(List<String> correos);
    Mono<Usuario> obtenerPorCorreoElectronico(String correoElectronico);
    Mono<String> obtenerNombreRolPorCorreoElectronico(String correoElectronico);
    Mono<String> obtenerIdRolPorNombreRol(String nombreRol);
}
