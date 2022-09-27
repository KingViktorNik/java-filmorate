package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.impl.film.mapper.FilmExtractor;

import java.util.List;

//@Component
@Repository
public class LikeDbStorage implements LikeStorage{
    /** Добавление лайка к фильму */
    private static final String ADD_LIKE = "INSERT INTO LIKE_FILMS(ID_FILM, ID_USER) VALUES (?, ?)";

    /** Удаление лайка из списка(таблицы) лайков фильма */
    private static final String DELETE_LIKE = "DELETE FROM LIKE_FILMS WHERE ID_FILM = ? AND  ID_USER = ?";

    /** Список популярных фильмов */
    private static final String ALL_LIKE =
            "SELECT FL.ID AS FILM_ID, " +           // идентификатор фильма
                    "FL.NAME AS FILM_NAME, " +      // название
                    "RELEASE_DATE, " +              // дата релиза
                    "DESCRIPTION, " +               // описание
                    "DURATION, " +                  // продолжительность в минутах
                    "RATE, " +                      // оценка фильма
                    "MPA_ID, " +                    // идентификатор рейтинга MPA
                    "M.NAME AS MPA_NAME, " +        // наименование рейтинга MPA
                    "ID_GENRE AS GENRE_ID, " +      // идентификатор жанра
                    "GE.NAME GENRE_NAME, " +        // название жанра
                    "ID_USER, " +                   // идентификатор пользователя, который поставил оценку
                    "(SELECT sum(ID_USER) " +       // количество лайков у фильма
                        "FROM LIKE_FILMS " +
                        "WHERE ID_FILM = FL.ID) AS LIKE_USERS " +
            "FROM FILMS AS FL " +                   // таблица с фильмами
            "LEFT JOIN MPA AS M ON MPA_ID = M.ID " +// связь таблиц Фильмы -< MPA
            "LEFT JOIN FILM_GENRE AS FG ON FL.ID = FG.ID_FILM " +// связь с таблицей составных ключей Жанры >-< Фильмы
            "LEFT JOIN GENRE AS GE ON FG.ID_GENRE = GE.ID " +    // связь таблиц между FILMS(фильмы) и GENRE(жанры)
            "LEFT JOIN LIKE_FILMS AS LF ON FL.ID = LF.ID_FILM " +// связь с таблицей составных ключей Фильмы >-< User
            "LEFT JOIN USERS AS US ON LF.ID_USER = US.ID " +     // связующая таблица между FILMS(фильмы) и USERS(юзеры)
            "WHERE FL.ID IN (SELECT ID " +
                                "FROM FILMS " +
                                "LEFT JOIN LIKE_FILMS AS LF ON ID = LF.ID_FILM " +
                                "GROUP BY ID " +
                                "ORDER BY COUNT(ID_USER) DESC, ID " +
                                "LIMIT ?)" +
            "ORDER BY LIKE_USERS DESC, FILM_ID, ID_USER, GENRE_NAME";                   // количество выгружаемых записей

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean like(long filmId, long userId) {
        return jdbcTemplate.update(ADD_LIKE, filmId, userId) > 0;
    }

    @Override
    public boolean dislike(long filmId, long userId) {
        return jdbcTemplate.update(DELETE_LIKE,filmId,userId) > 0;
    }

    @Override
    public List<Film> topFilms(long count) {
        return jdbcTemplate.query(ALL_LIKE, new FilmExtractor(), count);
    }
}
