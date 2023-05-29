package com.workflow2.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
/**
 * This is the main class of Backend API for e-commerce Application
 *
 * @Author Workflow-2 Backend Team
 *
 */
public class EcommerceApiApplication {

	/**
	 * This method starts the application with the help of run method
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(EcommerceApiApplication.class, args);
	}

}
