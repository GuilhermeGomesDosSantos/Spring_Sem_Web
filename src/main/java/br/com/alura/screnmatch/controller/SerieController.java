package br.com.alura.screnmatch.controller;

import br.com.alura.screnmatch.DTO.SerieDTO;
import br.com.alura.screnmatch.model.Serie;
import br.com.alura.screnmatch.repository.SerieRepository;
import br.com.alura.screnmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO> obterSeries() {
        return service.obterSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obeterTop5Series(){
        return service.obeterTop5Series();
    }
}
