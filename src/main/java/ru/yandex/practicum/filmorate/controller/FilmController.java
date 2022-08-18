package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {
    private final FilmService filmStorage;

    @Autowired
    public FilmController(FilmService filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.getFilmStorage().create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        return filmStorage.getFilmStorage().update(film);
    }

    @GetMapping("/films")
    public List<Film> filmAll() {
        return filmStorage.getFilmStorage().filmAll();
    }

    @GetMapping("/films/{id}")
    public Film film(@PathVariable long id) {
        return filmStorage.getFilmStorage().film(id);
    }

    // пользователь ставит лайк фильму.
    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        return filmStorage.addLike(id, userId);
    }

    // пользователь удаляет лайк.
    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        return filmStorage.deleteLike(id, userId);
    }

    // возвращает список фильмов из первых
    @GetMapping("/films/popular")
    public List<Film> topFilms(@RequestParam (defaultValue = "10", required = false) long count) {
        return filmStorage.topFilms(count);
    }

}
