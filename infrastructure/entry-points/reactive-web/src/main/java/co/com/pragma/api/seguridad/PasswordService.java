package co.com.pragma.api.seguridad;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class PasswordService {
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Mono<String> hashMono(String rawPassword) {
        return Mono.fromCallable(() -> hash(rawPassword))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Boolean> matches(String rawPassword, String hash) {
        return Mono.fromCallable(() -> encoder.matches(rawPassword, hash))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
