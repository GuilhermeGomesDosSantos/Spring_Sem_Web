package br.com.alura.screnmatch.Principal;

import br.com.alura.screnmatch.model.*;
import br.com.alura.screnmatch.repository.SerieRepository;
import br.com.alura.screnmatch.service.ConsumoAPI;
import br.com.alura.screnmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private List<Serie> series = new ArrayList<>();
    private SerieRepository repositorio;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar Séries buscadas
                    4 - Buscar Série por titulo
                    5 - Buscar Série por Autor
                    6 - Top 5 Séries
                    7 - Buscar Séries por categoria
                    8 - Buscar Séries por N° maximo de temporadas
                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    BuscarSerieAutor();
                    break;
                case 6:
                    BuscarTop5Series();
                    break;
                case 7:
                    BuscarSeriesPorCategorias();
                    break;
                case 8:
                    BuscarSeriesPorNumMaximoDeTemporadas();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        //dadosSeries.add(dados);
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
//        DadosSerie dadosSerie = getDadosSerie();

        listarSeriesBuscadas();
        System.out.println("-------------------------------");
        System.out.println("Escolha uma Série pelo nome: ");
        var nomeSerie = leitura.nextLine();

//        Optional<Serie> serie = series
//                .stream()
//                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
//                .findFirst();
        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas
                    .stream()
                    .flatMap(dt -> dt.episodios().stream()
                            .map(e -> new Episodio(dt.episodio(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void listarSeriesBuscadas(){
            series = repositorio.findAll();
            series.stream()
                            .sorted(Comparator.comparing(Serie::getGenero))
                                    .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolhe uma série pelo Titulo");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            System.out.println("Série encontrada: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void BuscarSerieAutor(){
        System.out.println("Qual o nome do autor ?");
        var nomeAtor = leitura.nextLine();

        System.out.println("A partir de qual avaliação ?");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);

        System.out.println("Séries em que " + nomeAtor + " trabalhou");
        seriesEncontradas.forEach(sE -> System.out.println(sE.getTitulo() + " - Avaliação: " + sE.getAvaliacao()));
    }

    private void BuscarTop5Series(){
        List<Serie> topSeries = repositorio.findTop5ByOrderByAvaliacaoDesc();

        topSeries.forEach(tS -> System.out.println(tS.getTitulo() + " - Avaliação: " + tS.getAvaliacao()));
    }

    private void BuscarSeriesPorCategorias(){
        System.out.println("Deseja buscar séries de que categoria/gênero ?");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromFromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria " + nomeGenero);
        seriesPorCategoria.forEach(sC -> System.out.println(sC.getTitulo()));
    }

    private void BuscarSeriesPorNumMaximoDeTemporadas(){
        System.out.println("Deseja buscar séries com no maximo quantas temporadas ? ");
        var maximoTemporadas = leitura.nextInt();

        System.out.println("A partir de qual avaliação ?");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();

        List<Serie> maximoTemporadasSerie = repositorio.seriesPorTemporadaEAvaliacao(maximoTemporadas, avaliacao);

        maximoTemporadasSerie.forEach(mT -> System.out.println(mT.getTitulo() + " - Avaliação: " + mT.getAvaliacao()));
    }
}