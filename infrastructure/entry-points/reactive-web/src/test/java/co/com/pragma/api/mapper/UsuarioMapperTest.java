package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UsuarioDto;
import co.com.pragma.model.usuario.Usuario;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UsuarioMapperTest {

    private final UsuarioMapper usuarioMapper = new UsuarioMapper();

    @Test
    void construirUsuarioDto_cuandoUsuarioEsNulo_debeRetornarNull(){
        UsuarioDto usuarioDto = usuarioMapper.convertirA(null);

        assertThat(usuarioDto).isNull();
    }

    @Test
    void construirUsuarioDto_cuandoUsuarioExiste_debeRetornarUsuarioDto(){
        Usuario usuario = Usuario.builder().nombres("natalia").identificacion("112233")
                .salarioBase(BigDecimal.TEN).build();
        UsuarioDto usuarioDto = usuarioMapper.convertirA(usuario);

        assertThat(usuarioDto.nombres()).isEqualTo(usuario.getNombres());
        assertThat(usuarioDto.identificacion()).isEqualTo(usuario.getIdentificacion());
    }

    @Test
    void construirUsuario_cuandoUsuarioDtoExiste_debeRetornarUsuario(){
        UsuarioDto usuarioDto = new UsuarioDto("natalia", null, null, "112233",
                null, null, null, "4000000");
        Usuario usuario = usuarioMapper.convertirDesde(usuarioDto);

        assertThat(usuario.getNombres()).isEqualTo(usuarioDto.nombres());
        assertThat(usuario.getIdentificacion()).isEqualTo(usuarioDto.identificacion());
    }

    @Test
    void construirUsuario_cuandoUsuarioDtoEsNulo_debeRetornarNull(){
        Usuario usuario = usuarioMapper.convertirDesde(null);

        assertThat(usuario).isNull();
    }
}
