package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreStorage;

    @Autowired
    public GenreController(GenreService genreStorage) {
        this.genreStorage = genreStorage;
    }

    @GetMapping("/{id}")
    public Optional<Genre> getGenreById(@PathVariable long id) {
        return genreStorage.getGenreById(id);
    }

    @GetMapping
    public List<Genre> getGenreAll(){
        return genreStorage.getGenreAll();
    }
}
