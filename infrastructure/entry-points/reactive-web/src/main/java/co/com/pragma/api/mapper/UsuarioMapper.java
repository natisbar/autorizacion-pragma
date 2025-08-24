package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UsuarioDto;
import co.com.pragma.model.usuario.Usuario;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioMapper {
    public Usuario convertirDesde(UsuarioDto dto) {
        return Optional.of(dto)
                .map(usuarioDto -> Usuario.builder()
                        .nombres(usuarioDto.nombres())
                        .apellidos(usuarioDto.apellidos())
                        .fechaNacimiento(usuarioDto.fechaNacimiento())
                        .identificacion(usuarioDto.identificacion())
                        .direccion(usuarioDto.direccion())
                        .telefono(usuarioDto.telefono())
                        .correoElectronico(usuarioDto.correoElectronico())
                        .salarioBase(usuarioDto.salarioBase())
                        .build())
                .orElse(null);
    }

    public UsuarioDto convertirA(Usuario model) {
        return Optional.of(model)
                .map(usuario -> new UsuarioDto(
                        usuario.getNombres(),
                        usuario.getApellidos(),
                        usuario.getFechaNacimiento(),
                        usuario.getIdentificacion(),
                        usuario.getDireccion(),
                        usuario.getTelefono(),
                        usuario.getCorreoElectronico(),
                        usuario.getSalarioBase()))
                .orElse(null);
    }
}
