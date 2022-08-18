package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    /**
     * добовление фильма
     */
    Film create(Film film);

    /**
     * обновление информации о фильме
     */
    Film update(Film film);

    /**
     * фильм по id
     */
    Film film(long id);

    /**
     * список всех фильмов
     */
    List<Film> filmAll();

    Map<Long, Film> getFilms();
}
