package br.com.alura.screnmatch;

import br.com.alura.screnmatch.service.ConsumoAPI;
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
//		System.out.println("Primeiro Projeto Spring, sem Web!");
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("http://www.omdbapi.com/?t=gilmore+girls&Season=1&apikey=31df3067");
		System.out.println(json);
		json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
	}
}
