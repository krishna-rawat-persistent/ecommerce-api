package com.workflow2.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
/**
 * This class is used to configure swagger UI and api specifications
 * @author krishna_rawat
 */
public class SwaggerConfig {

    /**
     * this method return API Key for authorization header that is used by securitySchemes
     * @return it return's API key
     */
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    /**
     * This method provides list of Security contexts that are used by Docket API class
     * @return it Return's list of Security Contexts
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    /**
     * This method returns Security References that is required to build Security Context
     * @return it return's list of Security References
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    @Bean
    /**
     * this bean return basic api specification
     * @return Docket class which help us to define open api specification
     */
    public Docket api(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(getInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.workflow2.ecommerce"))
                .paths(PathSelectors.any()).build();
    }

    /**
     * this bean return information about open api specification
     * @return ApiInfo class which help us to define info of open api specification
     */
    private ApiInfo getInfo() {
        return new ApiInfoBuilder().title("eCommerce API Documentation").description("This is a Backend API for eCommerce App.").contact(new Contact("Persistent Systems","https://www.persistent.com/",null)).version("1.0").license("Apache 2.0").licenseUrl("https://www.apache.org/licenses/LICENSE-2.0").build();
    }

}
