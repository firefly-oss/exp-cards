package com.firefly.experience.cards.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication(
    scanBasePackages = {
        "com.firefly.experience.cards",
        "org.fireflyframework.web"
    }
)
@EnableWebFlux
@ConfigurationPropertiesScan(basePackages = "com.firefly.experience.cards")
@OpenAPIDefinition(
    info = @Info(
        title = "${spring.application.name}",
        version = "${spring.application.version}",
        description = "${spring.application.description}",
        contact = @Contact(
            name = "${spring.application.team.name}",
            email = "${spring.application.team.email}"
        )
    ),
    servers = {
        @Server(url = "http://experience.getfirefly.io/exp-cards", description = "Development Environment"),
        @Server(url = "/", description = "Local Development Environment")
    }
)
public class ExpCardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpCardsApplication.class, args);
    }
}
