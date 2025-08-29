package co.com.pragma.usecase.crearusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class ObtenerUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    public Flux<Usuario> obtenerTodoPorCorreos(List<String> correos) {
        return usuarioGateway.obtenerTodoPorCorreos(correos);
    }

    public Mono<Usuario> obtenerPorCorreo(String correo){
        return usuarioGateway.obtenerPorCorreoElectronico(correo)
                .flatMap(usuario -> usuarioGateway
                        .obtenerNombreRolPorCorreoElectronico(usuario.getCorreoElectronico())
                        .map(nombreRol -> usuario.toBuilder()
                                .nombreRol(nombreRol).build()));
    }

}
