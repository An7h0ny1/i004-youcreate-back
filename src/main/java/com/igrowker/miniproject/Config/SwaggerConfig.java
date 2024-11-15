package com.igrowker.miniproject.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;


@OpenAPIDefinition(
        info = @Info(
                title = "YOU CREATE API",
                description = "API para la gestión financiera integral de creadores de contenido.",
                termsOfService = "www.youcreate.com/terms/",
                version = "1.0.0",
                contact = @Contact(
                        name = "Equipo de Soporte de YouCreate",
                        url = "www.youcreate.com/contact/",
                        email = "soporte@youcreate.com"
                ),
                license = @License(
                        name = "Standard Apache License Version 2.0 for YouCreate",
                        url = "www.youcreate.com/license/",
                        identifier = "Apache-2.0"
                )
        ),
        servers = {
                @Server(
                        description = "Local Server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Render Server",
                        url = "https://youcreate.com"
                )
        }
        /*security = @SecurityRequirement(
                name = "securityToken"
        )*/
)
@SecurityScheme(
        name = "securityToken",
        description = "Access Token For My API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
