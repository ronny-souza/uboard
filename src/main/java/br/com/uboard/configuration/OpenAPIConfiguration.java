package br.com.uboard.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Setting up the OpenAPI specification for documentation with Swagger
 *
 * @author Ronyeri Marinho
 * @since 1.0
 */

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI configureOpenAPI() {
        Contact contact = new Contact()
                .name("Ronyeri Marinho")
                .email("ronyerimarinho19@gmail.com")
                .url("https://www.linkedin.com/in/ronyeri-marinho/");

        Info info = new Info()
                .title("uBoard")
                .description("Automation and facilitation of task management in repositories.")
                .version("1.0.0")
                .contact(contact);

        return new OpenAPI().components(new Components()).info(info);
    }
}
