package com.minibank.mini_bank_system;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MiniBankSystemApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(MiniBankSystemApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Mini Bank").version("0.0.1-SNAPSHOT"));
	}
}
