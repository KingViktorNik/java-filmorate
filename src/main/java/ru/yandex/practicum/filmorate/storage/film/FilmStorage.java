package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    // добовление фильма
    Film create(Film film);

    // обновление информации о фильме
    Film update(Film film);

    // фильм по id
    Film film(long id);

    // список всех фильмов
    List<Film> filmAll();

    public Map<Long, Film> getFilms();
}
