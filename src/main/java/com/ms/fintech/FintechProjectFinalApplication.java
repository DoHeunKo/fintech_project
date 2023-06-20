package com.ms.fintech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FintechProjectFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(FintechProjectFinalApplication.class, args);
	}

}
