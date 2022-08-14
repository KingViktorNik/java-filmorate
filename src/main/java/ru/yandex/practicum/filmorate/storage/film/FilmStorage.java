package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    // добовление фильма
    Film create(Film film);

    // обновление информации о фильме
    Film update(Film film);

    // список всех фильмов
    List<Film> filmAll();
}
