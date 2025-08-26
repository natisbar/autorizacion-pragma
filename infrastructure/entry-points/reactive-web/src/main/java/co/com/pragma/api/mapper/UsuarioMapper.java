package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UsuarioDto;
import co.com.pragma.model.usuario.Usuario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class UsuarioMapper {
    public Usuario convertirDesde(UsuarioDto dto) {
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
                        .build())
                .orElse(null);
    }

    public UsuarioDto convertirA(Usuario model) {
        return Optional.ofNullable(model)
                .map(usuario -> new UsuarioDto(
                        usuario.getNombres(),
                        usuario.getApellidos(),
                        usuario.getFechaNacimiento() != null ?
                                usuario.getFechaNacimiento().toString() : null,
                        usuario.getIdentificacion(),
                        usuario.getDireccion(),
                        usuario.getTelefono(),
                        usuario.getCorreoElectronico(),
                        usuario.getSalarioBase().toString()))
                .orElse(null);
    }
}
