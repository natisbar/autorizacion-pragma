package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioDto;
import co.com.pragma.api.validador.ValidacionManejador;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@ContextConfiguration(classes = {RouterRest.class, Handler.class, ValidacionManejador.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testListenPOSTUseCase() {
        UsuarioDto usuarioDto = new UsuarioDto("natalia", "barbosa", null, "11223344",
                null, null, "correo@correo.com", BigDecimal.valueOf(2000000));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(usuarioDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioDto.class)
                .value(dto -> {
                            Assertions.assertThat(dto.nombres()).isEqualTo(usuarioDto.nombres());
                        }
                );
    }
}
