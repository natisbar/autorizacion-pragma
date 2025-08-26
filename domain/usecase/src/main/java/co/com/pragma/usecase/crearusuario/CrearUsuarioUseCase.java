package co.com.pragma.usecase.crearusuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.common.ex.ConflictoException;
import co.com.pragma.model.usuario.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CrearUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;
    public static final String CORREO_ELECTRONICO_EXISTE = "El correo electrónico proporcionado ya está en uso";
    public static final String DOCUMENTO_EXISTE = "El documento de identificación proporcionado ya está en uso";

    public Mono<Usuario> ejecutar(Usuario usuario) {
        Mono<Boolean> existeCorreo = usuarioGateway.existePorCorreoElectronico(usuario.getCorreoElectronico());
        Mono<Boolean> existeDocumento = usuarioGateway.existePorIdentificacion(usuario.getIdentificacion());
        return Mono.zip(existeCorreo, existeDocumento)
                .flatMap(tuple -> {
                    boolean correoExiste = tuple.getT1();
                    boolean documentoExiste = tuple.getT2();
                    List<String> listaErrores = new ArrayList<>();
                    if (correoExiste) listaErrores.add(CORREO_ELECTRONICO_EXISTE);
                    if (documentoExiste) listaErrores.add(DOCUMENTO_EXISTE);
                    if (!listaErrores.isEmpty()) return Mono.error(new ConflictoException(String.join(", ", listaErrores)));
                    return usuarioGateway.guardar(usuario);
                });
    }

}
