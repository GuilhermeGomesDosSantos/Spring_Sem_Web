package br.com.alura.screnmatch.repository;

import br.com.alura.screnmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {
}
