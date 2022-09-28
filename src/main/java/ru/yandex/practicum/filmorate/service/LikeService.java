package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LikeService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public LikeService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    // Добавление лайка
    public Optional<Film> addLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(()-> new NullObjectException(String.format("Фильм с id: '%d' не существует!", id)));

        userStorage.getUserById(userId)
                .orElseThrow(()-> new NullObjectException(String.format("Пользователя с id: '%d' не существует!", userId)));

        if (likeStorage.like(id, userId)) {
            log.info(String.format("Добавлен userId в список likes фильма: id:%d, name:%s, likes: ++userId:%d",
                    film.getId(),
                    film.getName(),
                    userId
            ));
            return filmStorage.getFilmById(id);
        }
        return Optional.of(film);
    }

    // Удаление лайка
    public Optional<Film> deleteLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(()-> new NullObjectException(String.format("Фильм с id: '%d' не существует!", id)));

        userStorage.getUserById(userId)
                .orElseThrow(()-> new NullObjectException(String.format("Пользователя с id: '%d' не существует!", userId)));

        if (likeStorage.dislike(id, userId)) {
            log.info(String.format("Удалён userId из список likes фильма: id:%d, name:%s, likes: --userId:%d",
                    film.getId(),
                    film.getName(),
                    userId
            ));

            return filmStorage.getFilmById(id);
        }
        return Optional.empty();
    }

    // Вывод 10(count) популярных фильмов по количеству лайков
    public List<Film> topFilms(long count) {
        return likeStorage.topFilms(count);
    }
}
