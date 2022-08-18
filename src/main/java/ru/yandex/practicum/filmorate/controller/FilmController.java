package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmStorage;

    @Autowired
    public FilmController(FilmService filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        dataVerification(film);
        log.info(String.format("Добавлен фильм: id: %d, name:%s", film.getId(), film.getName()));
        return filmStorage.getFilmStorage().create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        if (!filmStorage.getFilmStorage().getFilms().containsKey(film.getId())) {
            throw new NullObjectException(String.format("Фильм с id: '%d' не существует!", film.getId()));
        }

        dataVerification(film);
        log.info(String.format("Данные о фильме изменены: id: %d, name:%s", film.getId(), film.getName()));

        return filmStorage.getFilmStorage().update(film);
    }

    @GetMapping("/films")
    public List<Film> filmAll() {
        return filmStorage.getFilmStorage().filmAll();
    }

    @GetMapping("/films/{id}")
    public Film film(@PathVariable long id) {
        if (!filmStorage.getFilmStorage().getFilms().containsKey(id))
            throw new NullObjectException(String.format("Фильм с id: '%d' не существует!", id));

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

    private void dataVerification(Film film) {
        //название не может быть пустым
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("название не может быть пустым");
        }
        //максимальная длина описания — 200 символов
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно привышать 200 символов!");
        }
        //дата релиза — не раньше 28 декабря 1895 года
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        //продолжительность фильма должна быть положительной.
        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
