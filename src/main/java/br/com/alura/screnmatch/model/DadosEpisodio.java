package br.com.alura.screnmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio (@JsonAlias("Title")String Titulo,
                             @JsonAlias("Episode")int Episodio,
                             @JsonAlias("imdbRating")String Avaliacao,
                             @JsonAlias("Released")String DataLancamento) {
}
