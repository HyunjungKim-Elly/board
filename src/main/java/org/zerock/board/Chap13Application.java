package org.zerock.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Chap13Application {

	public static void main(String[] args) {
		SpringApplication.run(Chap13Application.class, args);
	}

}
