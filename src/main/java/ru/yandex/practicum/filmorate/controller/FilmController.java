package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        return inMemoryFilmStorage.update(film);
    }

    @GetMapping("/films")
    public List<Film> filmAll() {
        return inMemoryFilmStorage.filmAll();
    }
}
