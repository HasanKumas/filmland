package com.sogeti.filmland;

import com.sogeti.filmland.repositories.UserAccountRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserAccountRepository.class)
@EnableScheduling
public class FilmlandApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmlandApplication.class, args);
	}

}
