package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UsuarioRespuestaDto;
import co.com.pragma.api.dto.UsuarioSolicitudDto;
import co.com.pragma.api.seguridad.PasswordService;
import co.com.pragma.model.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioMapperTest {

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private UsuarioMapper usuarioMapper;

    @Test
    void debeConvertirDesdeDtoAUsuario() {
        UsuarioSolicitudDto dto = new UsuarioSolicitudDto(
                "Natalia",              // nombres
                "Barbosa",              // apellidos
                "1990-01-01",           // fechaNacimiento
                "112233",               // identificacion
                "Calle 123",            // direccion
                "3001234567",           // telefono
                "correo@test.com",      // correo
                "5000",                 // salarioBase
                "ADMIN",                // nombreRol
                "plainPassword"         // contrasena
        );

        when(passwordService.hash("plainPassword")).thenReturn("hashedPassword");

        Usuario usuario = usuarioMapper.convertirDesde(dto);

        assertNotNull(usuario);
        assertEquals("Natalia", usuario.getNombres());
        assertEquals("Barbosa", usuario.getApellidos());
        assertEquals(LocalDate.of(1990, 1, 1), usuario.getFechaNacimiento());
        assertEquals("112233", usuario.getIdentificacion());
        assertEquals("Calle 123", usuario.getDireccion());
        assertEquals("3001234567", usuario.getTelefono());
        assertEquals("correo@test.com", usuario.getCorreoElectronico());
        assertEquals(new BigDecimal("5000"), usuario.getSalarioBase());
        assertEquals("ADMIN", usuario.getNombreRol());
        assertEquals("hashedPassword", usuario.getContrasena());
    }

    @Test
    void debeRetornarNullSiDtoEsNull() {
        Usuario usuario = usuarioMapper.convertirDesde(null);
        assertNull(usuario);
    }

    @Test
    void debeConvertirDesdeUsuarioARespuestaDto() {
        Usuario usuario = Usuario.builder()
                .nombres("Natalia")
                .apellidos("Barbosa")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .identificacion("112233")
                .direccion("Calle 123")
                .telefono("3001234567")
                .correoElectronico("correo@test.com")
                .salarioBase(new BigDecimal("5000"))
                .nombreRol("admin")
                .build();

        UsuarioRespuestaDto dto = usuarioMapper.convertirA(usuario);

        assertNotNull(dto);
        assertEquals("Natalia", dto.nombres());
        assertEquals("Barbosa", dto.apellidos());
        assertEquals("1990-01-01", dto.fechaNacimiento());
        assertEquals("112233", dto.identificacion());
        assertEquals("Calle 123", dto.direccion());
        assertEquals("3001234567", dto.telefono());
        assertEquals("correo@test.com", dto.correoElectronico());
        assertEquals("5000", dto.salarioBase());
        assertEquals("ADMIN", dto.nombreRol()); // debe estar en mayúsculas
    }

    @Test
    void debeRetornarNullSiUsuarioEsNull() {
        UsuarioRespuestaDto dto = usuarioMapper.convertirA(null);
        assertNull(dto);
    }
}
