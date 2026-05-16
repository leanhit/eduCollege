package com.educollege.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI eduCollegeOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:" + serverPort);
        server.setDescription("Development Server");

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("EduCollege API")
                        .description("Vietnamese University Management System API\n\n" +
                                "## Vietnamese ID System\n" +
                                "- Student ID: SVYYFACULTYSEQUENCE (e.g., SV24CNTT00101)\n" +
                                "- Teacher ID: GVDEPARTMENTSEQUENCE (e.g., GVCNPM0001)\n" +
                                "- Staff ID: NVFACULTYDEPTTYPESEQUENCE (e.g., NVCNTTHC0001)\n\n" +
                                "## Academic Rules\n" +
                                "- Minimum GPA: 2.0\n" +
                                "- Maximum failed credits: 12\n" +
                                "- Minimum attendance: 75%\n" +
                                "- Credit limits by level: DAIHOC(25), CAODANG(20), THACSI(18), TIENSI(15)")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("EduCollege Team")
                                .email("support@educollege.edu.vn")
                                .url("https://educollege.edu.vn"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(server))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token authentication. Use the token returned from /api/v1/auth/login")));
    }
}
