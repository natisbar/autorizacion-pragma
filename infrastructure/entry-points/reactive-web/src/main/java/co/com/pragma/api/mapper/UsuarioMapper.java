package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UsuarioRespuestaDto;
import co.com.pragma.api.dto.UsuarioSolicitudDto;
import co.com.pragma.api.seguridad.PasswordService;
import co.com.pragma.model.usuario.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UsuarioMapper {
    private final PasswordService passwordService;

    public Usuario convertirDesde(UsuarioSolicitudDto dto) {
        return Optional.ofNullable(dto)
                .map(usuarioDto -> Usuario.builder()
                        .nombres(usuarioDto.nombres())
                        .apellidos(usuarioDto.apellidos())
                        .fechaNacimiento(usuarioDto.fechaNacimiento() != null ?
                                LocalDate.parse(usuarioDto.fechaNacimiento(), DateTimeFormatter.ISO_LOCAL_DATE) : null)
                        .identificacion(usuarioDto.identificacion())
                        .direccion(usuarioDto.direccion())
                        .telefono(usuarioDto.telefono())
                        .correoElectronico(usuarioDto.correoElectronico())
                        .salarioBase(new BigDecimal(usuarioDto.salarioBase()))
                        .nombreRol(dto.nombreRol())
                        .contrasena(passwordService.hash(usuarioDto.contrasena()))
                        .build())
                .orElse(null);
    }

    public UsuarioRespuestaDto convertirA(Usuario model) {
        return Optional.ofNullable(model)
                .map(usuario -> new UsuarioRespuestaDto(
                        usuario.getNombres(),
                        usuario.getApellidos(),
                        usuario.getFechaNacimiento() != null ?
                                usuario.getFechaNacimiento().toString() : null,
                        usuario.getIdentificacion(),
                        usuario.getDireccion(),
                        usuario.getTelefono(),
                        usuario.getCorreoElectronico(),
                        usuario.getSalarioBase().toString(),
                        usuario.getNombreRol() != null ? usuario.getNombreRol().toUpperCase() : null))
                .orElse(null);
    }
}
