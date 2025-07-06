package br.com.alura.screnmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScrenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Primeiro Projeto Spring, sem Web!");
	}
}
