package com.api.costs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CostsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CostsApplication.class, args);
	}

}
