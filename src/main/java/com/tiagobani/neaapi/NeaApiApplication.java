package com.tiagobani.neaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class NeaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeaApiApplication.class, args);
	}

}
