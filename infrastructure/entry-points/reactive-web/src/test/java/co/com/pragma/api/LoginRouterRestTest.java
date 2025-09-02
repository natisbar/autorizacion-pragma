package co.com.pragma.api;

import co.com.pragma.api.dto.LoginSolicitudDto;
import co.com.pragma.api.exception.ManejadorGlobalErrores;
import co.com.pragma.api.handler.LoginHandler;
import co.com.pragma.api.mapper.UsuarioMapper;
import co.com.pragma.api.router.LoginRouterRest;
import co.com.pragma.api.seguridad.PasswordService;
import co.com.pragma.api.seguridad.TestSecurityConfig;
import co.com.pragma.api.seguridad.config.SecurityHeadersConfig;
import co.com.pragma.api.seguridad.jwt.JwtService;
import co.com.pragma.api.validador.ValidacionManejador;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.crearusuario.ObtenerUsuarioUseCase;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static co.com.pragma.api.handler.LoginHandler.AUTENTICACION_FALLIDA;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {LoginRouterRest.class, LoginHandler.class, ValidacionManejador.class, Validator.class,
        ObtenerUsuarioUseCase.class, UsuarioMapper.class, ManejadorGlobalErrores.class, SecurityHeadersConfig.class,
        PasswordService.class, JwtService.class})
@WebFluxTest
@Import(TestSecurityConfig.class)
@TestPropertySource(properties = {
        "security.jwt.secretkey=MiClaveSuperSecretaDePrueba123456789012345",
        "security.jwt.expiration=3600000"
})
class LoginRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ObtenerUsuarioUseCase obtenerUsuarioUseCase;

    @Test
    void testListenPOSTUseCase_error400() {
        LoginSolicitudDto dto = new LoginSolicitudDto("correo@correo.com", "Asssssa");

        webTestClient.post()
                .uri("/v1/login")
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
                .jsonPath("$.mensaje").isEqualTo("contrasena: La contraseña debe contener al " +
                        "menos un dígito, una minuscula, una mayuscula, un caracter especial (@$!%*?&) y una longitud mínima 8 y máxima 20 caracteres");
    }

    @Test
    void testListenPOSTUseCase_error500() {
        LoginSolicitudDto dto = new LoginSolicitudDto("correo@correo.com", "Asssssa123$");

        when(obtenerUsuarioUseCase.obtenerPorCorreo(anyString())).thenReturn(Mono.error(new RuntimeException("error")));

        webTestClient.post()
                .uri("/v1/login")
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
    void testListenPOSTUseCase_error400_contrasenaNoCoincide() {
        LoginSolicitudDto dto = new LoginSolicitudDto("correo@correo.com", "Asssssa123$");

        Usuario usuario = Usuario.builder()
                .nombres("natalia")
                .apellidos("barbosa")
                .correoElectronico("correo@correo.com")
                .identificacion("11223344")
                .salarioBase(BigDecimal.valueOf(2000000))
                .build();

        when(obtenerUsuarioUseCase.obtenerPorCorreo(anyString())).thenReturn(Mono.just(usuario));

        webTestClient.post()
                .uri("/v1/login")
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
                .jsonPath("$.mensaje").isEqualTo(AUTENTICACION_FALLIDA);
    }

    @Test
    void testListenPOSTUseCase_todoOk() {
        LoginSolicitudDto dto = new LoginSolicitudDto("correo@correo.com", "Asssssa123$");

        Usuario usuario = Usuario.builder()
                .nombres("natalia")
                .apellidos("barbosa")
                .correoElectronico("correo@correo.com")
                .identificacion("11223344")
                .salarioBase(BigDecimal.valueOf(2000000))
                .contrasena("$2a$12$Y1JV0a018AS.4R2B5t45Z.eIXoyC9BdSsQVyXEIyIhkQEMZTyIOda")
                .build();

        when(obtenerUsuarioUseCase.obtenerPorCorreo(anyString())).thenReturn(Mono.just(usuario));

        webTestClient.post()
                .uri("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }
}
