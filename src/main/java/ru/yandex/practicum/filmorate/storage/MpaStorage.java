package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    /**
     * Поиск рейтинга MPA по id(идентификатору)
     * @param id необходимо передать id(идентификатор MPA)
     * @return Метод вернет объект (Mpa.class)
     */
    Optional<Mpa> getMpaById(long id);

    /**
     * Список всех MPA
     * @return Метод вернет список (List.class) объектов (Mpa.class)
     */
    List<Mpa> getMpaAll();
}
