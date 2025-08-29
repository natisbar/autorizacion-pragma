package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UsuarioSolicitudDto;
import co.com.pragma.model.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UsuarioMapperTest {

    private final UsuarioMapper usuarioMapper = new UsuarioMapper();

    @Test
    void construirUsuarioDto_cuandoUsuarioEsNulo_debeRetornarNull(){
        UsuarioSolicitudDto usuarioSolicitudDto = usuarioMapper.convertirA(null);

        assertThat(usuarioSolicitudDto).isNull();
    }

    @Test
    void construirUsuarioDto_cuandoUsuarioExiste_debeRetornarUsuarioDto(){
        Usuario usuario = Usuario.builder().nombres("natalia").identificacion("112233")
                .salarioBase(BigDecimal.TEN).build();
        UsuarioSolicitudDto usuarioSolicitudDto = usuarioMapper.convertirA(usuario);

        assertThat(usuarioSolicitudDto.nombres()).isEqualTo(usuario.getNombres());
        assertThat(usuarioSolicitudDto.identificacion()).isEqualTo(usuario.getIdentificacion());
    }

    @Test
    void construirUsuario_cuandoUsuarioDtoExiste_debeRetornarUsuario(){
        UsuarioSolicitudDto usuarioSolicitudDto = new UsuarioSolicitudDto("natalia", null, null, "112233",
                null, null, null, "4000000");
        Usuario usuario = usuarioMapper.convertirDesde(usuarioSolicitudDto);

        assertThat(usuario.getNombres()).isEqualTo(usuarioSolicitudDto.nombres());
        assertThat(usuario.getIdentificacion()).isEqualTo(usuarioSolicitudDto.identificacion());
    }

    @Test
    void construirUsuario_cuandoUsuarioDtoEsNulo_debeRetornarNull(){
        Usuario usuario = usuarioMapper.convertirDesde(null);

        assertThat(usuario).isNull();
    }
}
