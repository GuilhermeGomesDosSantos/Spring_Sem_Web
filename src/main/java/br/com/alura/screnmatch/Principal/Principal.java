package br.com.alura.screnmatch.Principal;

import br.com.alura.screnmatch.model.DadosEpisodio;
import br.com.alura.screnmatch.model.DadosSerie;
import br.com.alura.screnmatch.model.DadosTemporada;
import br.com.alura.screnmatch.service.ConsumoAPI;
import br.com.alura.screnmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final String APIKEY = "&apikey=31df3067";
    private final String ENDERECO = "http://www.omdbapi.com/?t=";

    private Scanner leitura = new Scanner(System.in);

    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverteDados conversor = new ConverteDados();

    public void exibirMenu(){
        System.out.println("Digite o nome da série para a busca: ");
        var nomeSerie = leitura.nextLine();

        var serie = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + APIKEY);
        DadosSerie dados = conversor.obterDados(serie, DadosSerie.class);

        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++){
			var jsonTemporada = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + APIKEY);
			DadosTemporada dadosTemporada = conversor.obterDados(jsonTemporada, DadosTemporada.class);

			temporadas.add(dadosTemporada);
		}

        // for avançado
//        temporadas.forEach(System.out::println);

        // for basico
        for (int i = 0; i < temporadas.size(); i++){
			System.out.println(temporadas.get(i));
		}
/*
        //for basico
        var temp = 1;
        var eps = 1;
        for (int i = 0; i < dados.totalTemporadas(); i++){
            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            System.out.println("Temporada '" + (temp++) + "'");

            for (int j = 0; j < episodiosTemporada.size(); j++){
                System.out.println(eps++ +" - " + episodiosTemporada.get(j).Titulo());
            }
        }*/

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.Titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.
                stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episódios");
        dadosEpisodios
                .stream()
                .filter(dEps -> !dEps.Avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::Avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

    }

}
