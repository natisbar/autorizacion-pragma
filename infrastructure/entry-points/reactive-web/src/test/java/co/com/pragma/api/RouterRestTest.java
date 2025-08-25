package co.com.pragma.api;

import co.com.pragma.api.exception.ManejadorGlobalErrores;
import co.com.pragma.api.mapper.UsuarioMapper;
import co.com.pragma.api.validador.ValidacionManejador;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.crearusuario.CrearUsuarioUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class, ValidacionManejador.class, Validator.class,
        CrearUsuarioUseCase.class, UsuarioMapper.class, ManejadorGlobalErrores.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CrearUsuarioUseCase crearUsuarioUseCase;

    @Test
    void testListenPOSTUseCase_error400() {
        String usuarioJson = """
                {
                "nombres": "natalia",
                "apellidos": "barbosa",
                "fechaNacimiento": "1993-05-30",
                "identificacion": "11223344",
                "direccion": null,
                "telefono": null,
                "correoElectronico": "correo@correo.com",
                "salarioBase": 20000000
                }""";

        webTestClient.post()
                .uri("/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(usuarioJson)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(result -> {
                    String response = new String(result.getResponseBody(), StandardCharsets.UTF_8);
                    System.out.println("Respuesta: " + response);
                })
                .jsonPath("$.estado").isEqualTo(400)
                .jsonPath("$.mensaje").isEqualTo("salarioBase: El salarioBase debe ser menor o igual a 15000000");
    }

    @Test
    void testListenPOSTUseCase_error500() {
        String usuarioJson = """
                {
                "nombres": "natalia",
                "apellidos": "barbosa",
                "fechaNacimiento": "1993-05-30",
                "identificacion": "11223344",
                "direccion": null,
                "telefono": null,
                "correoElectronico": "correo@correo.com",
                "salarioBase": 5000000
                }""";

        when(crearUsuarioUseCase.ejecutar(any(Usuario.class))).thenReturn(Mono.error(new RuntimeException("error")));

        webTestClient.post()
                .uri("/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(usuarioJson)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .consumeWith(result -> {
                    String response = new String(result.getResponseBody(), StandardCharsets.UTF_8);
                    System.out.println("Respuesta: " + response);
                })
                .jsonPath("$.estado").isEqualTo(500)
                .jsonPath("$.mensaje").isEqualTo("Ocurrió un error inesperado, por favor comuniquese comuniquese con el administrador");
    }

    @Test
    void testListenPOSTUseCase_error200() {
        String usuarioJson = """
                {
                "nombres": "natalia",
                "apellidos": "barbosa",
                "fechaNacimiento": "1993-05-30",
                "identificacion": "11223344",
                "direccion": null,
                "telefono": null,
                "correoElectronico": "correo@correo.com",
                "salarioBase": 5000000
                }""";

        Usuario usuario = Usuario.builder()
                .nombres("natalia")
                .apellidos("barbosa")
                .correoElectronico("correo@correo.com")
                .identificacion("11223344")
                .salarioBase(BigDecimal.valueOf(2000000))
                .build();

        when(crearUsuarioUseCase.ejecutar(any(Usuario.class))).thenReturn(Mono.just(usuario));

        webTestClient.post()
                .uri("/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(usuarioJson)
                .exchange()
                .expectStatus().isCreated()
                .expectBody();
    }
}
