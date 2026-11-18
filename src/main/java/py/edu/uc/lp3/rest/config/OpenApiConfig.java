package py.edu.uc.lp3.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cs2ArmasOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("CS2 Armas API")
                .description("API REST de armas de Counter-Strike 2 y consulta de objetos externos")
                .version("1.0.0"));
    }
}
