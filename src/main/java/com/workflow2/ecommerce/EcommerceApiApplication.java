package com.workflow2.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * This is the main class of Backend API for e-commerce Application
 *
 * @author krishna_rawat
 * @version v0.0.1
 */
@SpringBootApplication
@EnableWebMvc
public class EcommerceApiApplication {

	/**
	 * This method starts the application with the help of run method
	 * @param args it takes arguments from command line
	 */
	public static void main(String[] args) {
		SpringApplication.run(EcommerceApiApplication.class, args);
	}

}
