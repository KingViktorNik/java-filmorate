package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    /**
     * Поиск жанра по id(идентификатору)
     * @param id идентификатор жанра
     * @return метод вернет объект класса Genre.class
     */
    Optional<Genre> getGenreById(long id);

    /**
     *  Список всех жанров
     * @return метод вернет список (List.class) объект (Genre.class)
     */
    List<Genre> getGenreAll();
}
