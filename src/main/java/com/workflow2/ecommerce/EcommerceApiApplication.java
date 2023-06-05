package com.workflow2.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This is the main class of Backend API for e-commerce Application
 *
 * @author krishna_rawat
 * @version v0.0.1
 */
@SpringBootApplication
@EnableWebMvc
@EnableAsync
public class EcommerceApiApplication {

	/**
	 * This method starts the application with the help of run method
	 * @param args it takes arguments from command line
	 */
	public static void main(String[] args) {
		SpringApplication.run(EcommerceApiApplication.class, args);
	}

	/**
	 * This bean help us to allow all origins so CORS error will not throw while we access it from anywhere
	 * @return it return WebMvcConfigurer  which have all origins allowed
	 */
	@Bean
	public WebMvcConfigurer configure() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry reg) {
				reg.addMapping("/**").allowedOrigins("*");
			}
		};

	}
}
