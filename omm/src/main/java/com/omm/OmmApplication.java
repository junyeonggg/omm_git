package com.omm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OmmApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmmApplication.class, args);
	}

}
