package br.com.tech.challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final Contact DEFAULT_CONTACT = new Contact()
            .name("#Dev-Rise - G8")
            .url("https://github.com/rafaelfelixmoraes/3SOAT-tech-challenge")
            .email("teste@gmail.com");

    @Bean
    public OpenAPI javaSpringwebfluxOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("3SOAT - Tech Challenge")
                        .contact(DEFAULT_CONTACT)
                        .description("Project for FIAP/3SOAT - Tech challenge using reactive API with Spring Webflux")
                        .version(getClass().getPackage().getImplementationVersion())
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
