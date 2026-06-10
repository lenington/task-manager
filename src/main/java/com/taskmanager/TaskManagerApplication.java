package com.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Lenington Rios
 *
 * Rodar essa classe de entrada da aplicação Spring Boot.
 *
 * <p>
 * A anotação {@link SpringBootApplication} habilita a configuração automática do Spring
 * </p>
 */
@SpringBootApplication
public class TaskManagerApplication {

	/**
	 * Inicia a aplicação.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}
}
