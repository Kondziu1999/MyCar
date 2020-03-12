package com.KOndziu.researchserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ResearchServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResearchServerApplication.class, args);
	}


}

