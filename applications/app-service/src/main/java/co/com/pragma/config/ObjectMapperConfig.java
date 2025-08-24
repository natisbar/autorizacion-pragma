package co.com.pragma.config;

import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapperImp objectMapperImp() {
        return new ObjectMapperImp();
    }

}
