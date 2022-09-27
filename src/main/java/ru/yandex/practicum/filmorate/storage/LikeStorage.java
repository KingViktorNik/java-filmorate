package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface LikeStorage {

    /**
     * Добавление лайка к фильму
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     * @return метод вернет true, если лайк успешно добавлен
     */
    boolean like(long filmId, long userId);

    /**
     * Удаление лайка из списка(таблицы) лайков фильма
     * @param filmId идентификатор фильма
     * @param userId идентификатор пользователя
     * @return метод вернет true, если лайк успешно удален
     */
    boolean dislike(long filmId, long userId);

    /**
     * Список популярных фильмов
     * @param count ограничение по количеству выгрузки строк
     * @return метод вернет список(List.class) объектов (Film.class)
     */
    List<Film> topFilms(long count);
}
