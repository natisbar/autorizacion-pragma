package co.com.pragma;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class MainApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethodRuns() {
        MainApplication.main(new String[] {});
    }
}
