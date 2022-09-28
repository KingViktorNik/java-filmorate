package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
    /**
     * Добавление нового фильма
     * @param film необходима передать объект класса Film.class
     * @return метод вернет объект класса Film.class с id(идентификаторам)
     */
    Optional<Film> create(Film film);

    /**
     * Обновление информации о фильме
     * @param film необходима передать объект класса Film.class
     * @return метод вернет объект класса Film.class
     */
    Optional<Film> update(Film film);

    /**
     * Поиск фильма по id(идентификатору)
     * @param id необходимо передать id(идентификатор фильма)
     * @return метод вернет объект (Film.class)
     */
    Optional<Film> getFilmById(long id);

    /**
     * Список всех фильмов
     * @return метод вернет список (List.class) объектов (Film.class)
     */
    List<Film> filmAll();

    public Map<Long, Film> getFilms();
}
