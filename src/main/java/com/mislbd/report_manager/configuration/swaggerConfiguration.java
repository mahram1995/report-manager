package com.mislbd.report_manager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class swaggerConfiguration {

    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Ababil Admin Api" + " V-5.0.01")
                .select()
                .apis(RequestHandlerSelectors.basePackage("report_manager.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("Ababil Admin Api")
                                .description("Ababil Admin Api")
                                .version("5.0.01")
                                .license("© Millennium Information Solution Limited")
                                .licenseUrl("http://www.mislbd.com")
                                .build());
    }

}