package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Optional<Film> create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Optional<Film> update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> filmAll() {
        return filmService.filmAll();
    }

    @GetMapping("/{id}")
    public Optional<Film> film(@PathVariable long id) {
        return filmService.film(id);
    }
}

