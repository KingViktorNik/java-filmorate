package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    // Добовление лайка
    public Film addLike(long id, long userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NullObjectException(String.format("Фильмь с id: '%d' несуществует.", id));
        }

        Film film = filmStorage.getFilms().get(id);
        film.getLikes().add(userId);
        log.info(String.format("Добавлен userId в список likes фильма: id:%d, name:%s, likes: ++userId:%d",
                film.getId(),
                film.getName(),
                userId
        ));

        return film;
    }

    // Удаление лайка
    public Film deleteLike(long id, long userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NullObjectException(String.format("Фильмь с id: '%d' несуществует.", id));
        }

        Film film = filmStorage.getFilms().get(id);

        if (!film.getLikes().contains(userId)) {
            throw new NullObjectException(String.format("Пользователя с id: '%d' нет в списке likes.", userId));
        }

        film.getLikes().remove(userId);
        log.info(String.format("Удалён userId из список likes фильма: id:%d, name:%s, likes: ++userId:%d",
                film.getId(),
                film.getName(),
                userId
        ));

        return film;
    }

    // Вывод 10(count) полулярных фильмов по количеству лайков
    public List<Film> topFilms(long count) {
        return filmStorage.getFilms().values().stream()
                   .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                   .limit(count)
                   .collect(Collectors.toList()
               );
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }
}
