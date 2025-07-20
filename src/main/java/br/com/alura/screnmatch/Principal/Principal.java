package br.com.alura.screnmatch.Principal;

import br.com.alura.screnmatch.model.DadosEpisodio;
import br.com.alura.screnmatch.model.DadosSerie;
import br.com.alura.screnmatch.model.DadosTemporada;
import br.com.alura.screnmatch.model.Episodio;
import br.com.alura.screnmatch.service.ConsumoAPI;
import br.com.alura.screnmatch.service.ConverteDados;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

        /*
        System.out.println(dadosEpisodios);
        System.out.println("\nTop 10 episódios");
        dadosEpisodios
                .stream()
                .filter(dEps -> !dEps.Avaliacao().equalsIgnoreCase("N/A"))
                .peek(dEps -> System.out.println("Primeiro filtro (N/A): " + dEps))
                .sorted(Comparator.comparing(DadosEpisodio::Avaliacao).reversed())
                .peek(dEps -> System.out.println("Ordenação: " + dEps))
                .limit(10)
                .peek(dEps -> System.out.println("Limite: " + dEps))
                .map(dEps -> dEps.Titulo().toUpperCase())
                .peek(dEps -> System.out.println("Mapeamento: " + dEps))
                .forEach(System.out::println);

         */

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(dEps -> new Episodio(t.episodio(), dEps)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        /*
        System.out.println("Digite um trecho do título do episódio");
        var trechoTitulo = leitura.nextLine();

        Optional<Episodio> primeiroIndice = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        primeiroIndice.ifPresentOrElse(pIndice -> {
            System.out.println("Episódio Encontrado!");
            System.out.println("Temporada: " + pIndice.getTemporada());
            System.out.println("Episódio: " + pIndice.getTitulo());
        },
                () -> {
                    System.out.println("Episódio não encontrado!");
                });
*/
//        primeiroIndice.ifPresent(pIndice -> System.out.println("Temporada encontrada!\n" + "Temporada: "+ pIndice.getTemporada() + "\nEpisódio: " + pIndice.getTitulo()));

        /*
        if(primeiroIndice.isPresent()){
            System.out.println("Episódio Encontrado!");
            System.out.println("Temporada: "+ primeiroIndice.get().getTemporada());
        }else {
            System.out.println("Episódio não Encontrado!");
        }
         */

        /*
        System.out.println("A partir de qual ano você deseja ver os episódios ?");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() !=null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                ", Episódio: " + e.getTitulo() +
                                ", Data Lançamento: " + e.getDataLancamento().format(formatador)
                ));
         */

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.groupingBy(e -> e.getTemporada(), Collectors.averagingDouble(e -> e.getAvaliacao()))); // forma básica
        .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao))); // forma avançada

        System.out.println(avaliacoesPorTemporada);
    }

}
