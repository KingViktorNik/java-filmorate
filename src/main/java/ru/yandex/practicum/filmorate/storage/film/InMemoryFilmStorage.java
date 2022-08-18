package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public Film create(Film film) {
        dataVerification(film);
        film.setId(++id);
        films.put(film.getId(), film);
        log.info(String.format("Добавлен фильм: id: %d, name:%s", film.getId(), film.getName()));
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NullObjectException(String.format("Фильм с id: '%d' не существует!", film.getId()));
        }
        dataVerification(film);
        films.put(film.getId(), film);
        log.info(String.format("Данные о фильме изменены: id: %d, name:%s", film.getId(), film.getName()));
        return film;
    }

    @Override
    public List<Film> filmAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film film(long id) {
        if (!films.containsKey(id))
            throw new NullObjectException(String.format("Фильм с id: '%d' не существует!", id));
        return films.get(id);
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

    public Map<Long, Film> getFilms() {
        return films;
    }
}

