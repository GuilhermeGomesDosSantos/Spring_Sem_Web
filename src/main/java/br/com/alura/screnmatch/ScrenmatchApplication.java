package br.com.alura.screnmatch;

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
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("http://www.omdbapi.com/?t=gilmore+girls&apikey=31df3067");
		System.out.println(json);
//		json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
//		System.out.println(json);

		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		var jsonEpisodio = consumoAPI.obterDados("http://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=31df3067");
		DadosEpisodio episodio = conversor.obterDados(jsonEpisodio, DadosEpisodio.class);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i < dados.totalTemporadas(); i++){
			var jsonTemporada = consumoAPI.obterDados("http://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=31df3067");
			DadosTemporada dadosTemporada = conversor.obterDados(jsonTemporada, DadosTemporada.class);

			temporadas.add(dadosTemporada);
		}

		/*
		for avançado
		temporadas.forEach(System.out::println);
		 */

		//for básico
		for (int i = 0; i < temporadas.size(); i++){
			System.out.println(temporadas.get(i));
		}

	}
}
