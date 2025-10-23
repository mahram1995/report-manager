package com.mislbd.report_manager.configuration.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class swaggerConfiguration {
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("Admin API 1.0.01")
                .packagesToScan("com.mislbd.report_manager.controller.admin")
                .addOpenApiCustomizer(openApi -> openApi
                        .info(new Info()
                                .title("Admin API Documentation")
                                .version("1.0.01")
                                .description("Endpoints for Admin functionality")
                                .license(new License()
                                        .name("© Millennium Information Solution Limited")
                                        .url("http://www.mislbd.com")))
                        // ✅ Add security item to this group
                        .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                )
                .build();
    }




    @Bean
    public GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder()
                .group("Customer API 1.0.01")
                .packagesToScan("com.mislbd.report_manager.controller.customer")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("Customer API Documentation")
                        .version("1.0.01")
                        .description("Endpoints for Customer access")
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("© Millennium Information Solution Limited")
                                .url("http://www.mislbd.com"))))
                .build();
    }

    @Bean
    public GroupedOpenApi reportApi() {
        return GroupedOpenApi.builder()
                .group("Report API 1.0.01")
                .packagesToScan("com.mislbd.report_manager.controller.report")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("Report API Documentation")
                        .version("1.0.01")
                        .description("Endpoints for Customer access")
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("© Millennium Information Solution Limited")
                                .url("http://www.mislbd.com"))))
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0.0")
                        .description("Complete API Documentation"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

}
