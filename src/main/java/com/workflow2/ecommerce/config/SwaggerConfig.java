package com.workflow2.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
/**
 * This class is used to configure swagger UI and api specifications
 */
public class SwaggerConfig {
    @Bean
    /**
     * this bean return basic api specification
     * @return Docket class which help us to define open api specification
     */
    public Docket api(){
        return new Docket(DocumentationType.OAS_30).apiInfo(getInfo()).select().apis(RequestHandlerSelectors.basePackage("com.workflow2.ecommerce")).paths(PathSelectors.any()).build();
    }

    /**
     * this bean return information about open api specification
     * @return ApiInfo class which help us to define info of open api specification
     */
    private ApiInfo getInfo() {
        return new ApiInfoBuilder().title("eCommerce API Documentation").description("This is a Backend API for eCommerce App.").contact(new Contact("Persistent Systems","https://www.persistent.com/",null)).version("1.0").license("Apache 2.0").licenseUrl("https://www.apache.org/licenses/LICENSE-2.0").build();
    }

}
