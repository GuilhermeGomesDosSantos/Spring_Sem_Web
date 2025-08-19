package br.com.alura.screnmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private Integer numeroEpisodio;
    private String titulo;
    private double avaliacao;
    private LocalDate dataLancamento;

    public Episodio(){}
    @ManyToOne
    private Serie serie;

    public Episodio(Integer temporada, DadosEpisodio dadosEpisodio){
        this.temporada = temporada;
        this.numeroEpisodio = dadosEpisodio.Episodio();
        this.titulo = dadosEpisodio.Titulo();
        try {
            this.avaliacao = Double.parseDouble(dadosEpisodio.Avaliacao());
        } catch (NumberFormatException e){
            this.avaliacao = 0.0;
        }
        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.DataLancamento());
        } catch (DateTimeParseException e){
            this.dataLancamento = null;
        }
    }

    public Long getId(){ return id;}
    public void setId(Long id){this.id = id;}
    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                ", numeroEpisodio=" + numeroEpisodio +
                ", titulo='" + titulo + '\'' +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento + "\n";
    }
}
