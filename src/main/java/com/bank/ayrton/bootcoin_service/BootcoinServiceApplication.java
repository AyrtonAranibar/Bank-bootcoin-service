package com.bank.ayrton.bootcoin_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BootcoinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootcoinServiceApplication.class, args);
	}

	@Bean
	public WebClient yankiClient(WebClient.Builder builder) {
		return builder
				.baseUrl("http://localhost:8086/api/v1/yanki") // Ajusta el puerto si usas otro
				.build();
	}
}