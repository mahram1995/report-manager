package com.mislbd.report_manager.configuration;


import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class swaggerConfiguration {
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("Admin API 1.0.01") // will show as "admin" in dropdown
                .packagesToScan("com.mislbd.report_manager.controller.admin")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("Admin API Documentation")
                        .version("1.0.01")
                        .description("Endpoints for Admin functionality")
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("© Millennium Information Solution Limited")
                                .url("http://www.mislbd.com"))))
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

}
