package org.webatrio.backend.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Joel",
                        email = "joel.tchoufa-nkouatchet@web-atrio.com",
                        url = "++++++++++"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification - Joelitok",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:2026"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "++++++++++"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)

@SecurityScheme(
        name = "Bearer Authentication",
        description = "JWT auth description",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER)

@SecurityScheme(
        name = "Basic Authentication",
        description = "JWT auth description",
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {

}
