package com.primebasket.User.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Prime Basket User Service API",
                version = "v1.0",
                description = "RSET APIs for managing users",
                contact = @Contact(name = "Prime Basket Team",
                email = "vickypagare31@gmail.com")
        )
)
public class SwaggerConfig {

}
