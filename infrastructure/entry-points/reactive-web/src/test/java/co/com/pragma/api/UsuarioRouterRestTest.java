package co.com.pragma.api;

import co.com.pragma.api.dto.UsuarioSolicitudDto;
import co.com.pragma.api.exception.ManejadorGlobalErrores;
import co.com.pragma.api.handler.UsuarioHandler;
import co.com.pragma.api.mapper.UsuarioMapper;
import co.com.pragma.api.router.UsuarioRouterRest;
import co.com.pragma.api.seguridad.PasswordService;
import co.com.pragma.api.seguridad.TestSecurityConfig;
import co.com.pragma.api.seguridad.config.SecurityHeadersConfig;
import co.com.pragma.api.validador.ValidacionManejador;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.crearusuario.CrearUsuarioUseCase;
import co.com.pragma.usecase.crearusuario.ObtenerUsuarioUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UsuarioRouterRest.class, UsuarioHandler.class, ValidacionManejador.class, Validator.class,
        CrearUsuarioUseCase.class, UsuarioMapper.class, ManejadorGlobalErrores.class, SecurityHeadersConfig.class,
        PasswordService.class})
@WebFluxTest
@Import(TestSecurityConfig.class)
class UsuarioRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CrearUsuarioUseCase crearUsuarioUseCase;

    @MockitoBean
    private ObtenerUsuarioUseCase obtenerUsuarioUseCase;

    @Test
    void testListenPOSTUseCase_error400() {
        UsuarioSolicitudDto dto = new UsuarioSolicitudDto("natalia", "barbosa",
                "1993-05-12", "11223344", null, null,
                "correo@correo.com", "-1", "CLIENTE", "AS12as$qwqw");

        webTestClient.post()
                .uri("/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(result -> {
                    String response = new String(result.getResponseBody(), StandardCharsets.UTF_8);
                    System.out.println("Respuesta: " + response);
                })
                .jsonPath("$.estado").isEqualTo(400)
                .jsonPath("$.mensaje").isEqualTo("salarioBase: El salario base debe ser un valor numerico entre 0 y 15000000");
    }

    @Test
    void testListenPOSTUseCase_error500() {
        UsuarioSolicitudDto dto = new UsuarioSolicitudDto("natalia", "barbosa",
                "1993-05-12", "11223344", null, null,
                "correo@correo.com", "5000000", "CLIENTE", "AS12as$qwqw");

        when(crearUsuarioUseCase.ejecutar(any(Usuario.class))).thenReturn(Mono.error(new RuntimeException("error")));

        webTestClient.post()
                .uri("/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin")
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
        UsuarioSolicitudDto dto = new UsuarioSolicitudDto("natalia", "barbosa",
                "1993-05-12", "11223344", null, null,
                "correo@correo.com", "5000000", "CLIENTE", "AS12as$qwqw");

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
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody();
    }

    @Test
    void testListenPOSTObtenerUseCase_error400() {
        List<String> correos = List.of("correo_invalido");

        webTestClient.post()
                .uri("/v1/usuarios/por-correos")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(correos)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.estado").isEqualTo(400)
                .jsonPath("$.mensaje").value(mensaje ->
                        assertTrue(mensaje.toString().contains("Formato de correo inválido")));
    }

    @Test
    void testListenPOSTObtenerUseCase_success201() {
        List<String> correos = List.of("correo@correo.com");

        Usuario usuario = Usuario.builder()
                .nombres("natalia")
                .apellidos("barbosa")
                .correoElectronico("correo@correo.com")
                .identificacion("11223344")
                .salarioBase(BigDecimal.valueOf(2000000))
                .build();

        when(obtenerUsuarioUseCase.obtenerTodoPorCorreos(correos)).thenReturn(Flux.just(usuario));

        webTestClient.post()
                .uri("/v1/usuarios/por-correos")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(correos)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].correoElectronico").isEqualTo("correo@correo.com")
                .jsonPath("$[0].nombres").isEqualTo("natalia");
    }
}
