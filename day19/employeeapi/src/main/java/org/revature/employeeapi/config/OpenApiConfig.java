package org.revature.employeeapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeManagementOpenAPI() {
        return new OpenAPI()
                .info(buildApiInfo())
                .servers(buildServerList());

    }

    private Info buildApiInfo() {

        return new Info()
                .title("Revature Employee Management API")
                .version("v1.0.0")
                .description("""
                        ## Overview
                        This API provides complete employee lifecycele
                        management for Revature.
                        
                        ## Capabilities
                        - Create and manage employees records for Revature.
                        - Search employees by department or salary range
                        - Soft-delete employees(records are preserved for audit purpose)
                        
                        ## Authentication
                        Currently uses API Key authentication. Include header
                        `X-API-Key: your-key-here`
                        
                        ## Rate limiting
                        100 requests per minute per api
                        
                        """)
                .contact(new Contact()
                        .name("Revature Bankend Platform Team")
                        .email("backend@revature.com")
                        .url("https://internal.revature.com/api-support"))
                .license(new License()
                        .name("Revature Internal Use Only")
                        .url("https:revature.com/api-license"));



    }

    private List<Server> buildServerList() {
        Server localServer =  new Server()
                .url("http://localhost:8080")
                .description("Local Development Server");

        Server stagingServer =   new Server()
                .url("https://api-staging.revature.com")
                .description("Staging Server - for integration testing");

        Server productionServer =   new Server()
                .url("https://api-production.com")
                .description("Production Server - for  Live data");

        return List.of(localServer, stagingServer, productionServer);
    }

    public List<Tag> buildTagDefinitions() {
        return List.of(
                new Tag()
                        .name("Employee Management API")
                        .description("""
                                Operations for creating, reading, updating, and deleting employee
                                records. All operations require Authentication. Deletion is soft delete only.
                                employee records are deactivated, never permanently removed.
                                """),
                new Tag()
                        .name("Employee Search")
                        .description(
                                """
                                        Advanced search and filtering operations for employee records.
                                        These endpoints are read-only and support filter combination
                                        """
                        ),
                new Tag()
                        .name("Health and Diagnostics")
                        .description("Internal Health check and system status endpoints")
        );
    }
}
