package ru.yandex.practicum.filmorate.storage.impl.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public Optional<Film> create(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> update(Film film) {
        return Optional.ofNullable(films.put(film.getId(), film));
    }

    @Override
    public List<Film> filmAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        return Optional.ofNullable(films.get(id));
    }

    public Map<Long, Film> getFilms() {
        return films;
    }
}

