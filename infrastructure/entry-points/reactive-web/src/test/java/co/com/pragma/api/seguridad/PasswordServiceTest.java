package co.com.pragma.api.seguridad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordServiceTest {

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        passwordService = new PasswordService();
    }

    @Test
    void debeGenerarHashMonoDeContrasena() {
        String rawPassword = "MiClaveSegura123!";

        Mono<String> hashMono = passwordService.hashMono(rawPassword);

        StepVerifier.create(hashMono)
                .assertNext(hash -> {
                    assertThat(hash).isNotNull();
                    assertThat(hash).isNotBlank();
                    assertThat(hash).startsWith("$2");
                })
                .verifyComplete();
    }

    @Test
    void debeValidarContrasenaCorrecta() {
        String rawPassword = "MiClaveSegura123!";
        String hash = new BCryptPasswordEncoder(12).encode(rawPassword);

        Mono<Boolean> matchesMono = passwordService.matches(rawPassword, hash);

        StepVerifier.create(matchesMono)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void debeFallarContrasenaIncorrecta() {
        String rawPassword = "MiClaveSegura123!";
        String otraPassword = "ClaveIncorrecta456$";
        String hash = new BCryptPasswordEncoder(12).encode(rawPassword);

        Mono<Boolean> matchesMono = passwordService.matches(otraPassword, hash);

        StepVerifier.create(matchesMono)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void debeGenerarHashesDistintosParaLaMismaContrasena() {
        String rawPassword = "RepetidaClave789!";

        Mono<String> hash1 = passwordService.hashMono(rawPassword);
        Mono<String> hash2 = passwordService.hashMono(rawPassword);

        StepVerifier.create(Mono.zip(hash1, hash2))
                .assertNext(tuple -> {
                    String h1 = tuple.getT1();
                    String h2 = tuple.getT2();
                    assertThat(h1).isNotEqualTo(h2); // BCrypt siempre genera salt distinto
                    assertThat(new BCryptPasswordEncoder().matches(rawPassword, h1)).isTrue();
                    assertThat(new BCryptPasswordEncoder().matches(rawPassword, h2)).isTrue();
                })
                .verifyComplete();
    }
}

