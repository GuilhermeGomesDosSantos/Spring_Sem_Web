package br.com.alura.screnmatch;

import br.com.alura.screnmatch.Principal.Principal;
import br.com.alura.screnmatch.model.DadosEpisodio;
import br.com.alura.screnmatch.model.DadosSerie;
import br.com.alura.screnmatch.model.DadosTemporada;
import br.com.alura.screnmatch.service.ConsumoAPI;
import br.com.alura.screnmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScrenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScrenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		System.out.println("Primeiro Projeto Spring, sem Web!");


		/*
		for avançado
		temporadas.forEach(System.out::println);
		 */

		//for básico
//		for (int i = 0; i < temporadas.size(); i++){
//			System.out.println(temporadas.get(i));
//		}

		Principal principal = new Principal();
		principal.exibirMenu();
	}
}
