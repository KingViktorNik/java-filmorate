/**
 * Класс FilmDbStorage служит для загрузки и выгрузки данных из БД. Данных о фильмах.
 */

package ru.yandex.practicum.filmorate.storage.impl.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.impl.film.mapper.FilmExtractor;
import ru.yandex.practicum.filmorate.storage.impl.film.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.impl.genre.mapper.GenreMapper;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    /**
     * Запрос выполняет выгрузку данных списка всех фильмов. И связанных таблиц.
     */
    private static final String GET_ALL =
            "SELECT FL.ID AS FILM_ID, " +       // идентификатор фильма
                    "FL.NAME AS FILM_NAME, " +  // название
                    "RELEASE_DATE, " +          // дата релиза
                    "DESCRIPTION, " +           // описание
                    "DURATION, " +              // продолжительность в минутах
                    "RATE, " +                  // оценка фильма
                    "MPA_ID, " +                // идентификатор рейтинга MPA
                    "M.NAME AS MPA_NAME, " +    // наименование рейтинга MPA
                    "ID_GENRE AS GENRE_ID, " +  // идентификатор жанра
                    "GE.NAME GENRE_NAME, " +    // название жанра
                    "ID_USER " +                // идентификатор пользователя, который поставил оценку
            "FROM FILMS AS FL " +               // таблица с фильмами
            "LEFT JOIN MPA AS M ON MPA_ID = M.ID " + // связь таблиц Фильмы -< MPA
            "LEFT JOIN FILM_GENRE AS FG ON FL.ID = FG.ID_FILM " + // связь с таблицей составных ключей Жанры >-< Фильмы
            "LEFT JOIN GENRE AS GE ON FG.ID_GENRE = GE.ID " +     // связь таблиц между FILMS(фильмы) и GENRE(жанры)
            "LEFT JOIN LIKE_FILMS AS LF ON FL.ID = LF.ID_FILM " + // связь с таблицей составных ключей Фильмы >-< User
            "LEFT JOIN USERS AS US ON LF.ID_USER = US.ID " + // связующая таблица между FILMS(фильмы) и USERS(юзеры)
            "ORDER BY FILM_ID, ID_USER, GENRE_NAME";

    /**
     * Запрос выполняет поиск в БД фильма по идентификатору и возвращает
     */
    private static final String GET_BY_ID =
            "SELECT FL.ID AS FILM_ID, " +
                    "FL.NAME AS FILM_NAME, " +
                    "RELEASE_DATE, " +
                    "DESCRIPTION, " +
                    "DURATION, " +
                    "RATE, " +
                    "MPA_ID, " +
                    "M.NAME AS MPA_NAME " +
            "FROM FILMS AS FL " +
            "JOIN MPA AS M ON MPA_ID = M.ID " +
            "WHERE FL.ID = ?";
    /**
     *  Добавление в БД фильма
     */
    private static final String CREATE = "INSERT INTO FILMS(NAME, RELEASE_DATE, DESCRIPTION, DURATION, RATE, MPA_ID) " +
                                            "VALUES (?, ?, ?, ?, ?, ?)";
    /**
     * Обновления данных о фильме
     */
    private static final String UPDATE = "UPDATE FILMS SET " +
                                            "NAME = ?, " +
                                            "RELEASE_DATE = ?, " +
                                            "DESCRIPTION = ?, " +
                                            "DURATION = ?, " +
                                            "RATE = ?, " +
                                            "MPA_ID = ?";
    /**
     * Выгрузка из БД всех идентификаторов пользователей по идентификатору фильма.
     */
    private static final String USERS_LIKE = "SELECT ID_USER AS ID " +
                                                "FROM LIKE_FILMS " +
                                                "WHERE ID_FILM = ?";
    /**
     * Выгрузка из БД всех жанров по идентификатору фильма.
     */
    private static final String GENRE_FILM = "SELECT ID_GENRE AS ID, NAME FROM FILM_GENRE " +
                                                "LEFT JOIN GENRE ON ID = ID_GENRE " +
                                                "WHERE ID_FILM = ?";
    /**
     * Добавление в БД данных о жанре фильма
     */
    private static final String ADD_GENRE_FILM = "INSERT INTO FILM_GENRE(ID_FILM, ID_GENRE) VALUES ( ?, ?)";
    /**
     * Удаление из БД данных о жанре фильма
     */
    private static final String DELETE_GENRE_FILM = "DELETE FROM FILM_GENRE where ID_FILM = ?";

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(CREATE, new String[] {"ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getReleaseDate().toString());
            stmt.setString(3, film.getDescription());
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(ADD_GENRE_FILM, Objects.requireNonNull(keyHolder.getKey()).longValue(), genre.getId());
            }
        }

        return getFilmById(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public Optional<Film> update(Film film) {
        int indexUpdate = jdbcTemplate.update(UPDATE,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId()
        );

        if (indexUpdate == 0) {
            return Optional.empty();
        }

        // Изменение данных о жанрах фильма
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            Set<Genre> genreList = new HashSet<>(film.getGenres());
            jdbcTemplate.update(DELETE_GENRE_FILM,film.getId());
            for (Genre genre : genreList) {
                jdbcTemplate.update(ADD_GENRE_FILM, film.getId(), genre.getId());
            }
        } else {
            jdbcTemplate.update(DELETE_GENRE_FILM,film.getId());
        }

        return getFilmById(film.getId());
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        Optional<Film> film = jdbcTemplate.query(GET_BY_ID, new FilmMapper(), id)
                .stream().findFirst();

        if (film.isPresent()) {
            film.get().setGenres(jdbcTemplate.query(GENRE_FILM, new GenreMapper(), id));
            film.get().setLikes(new HashSet<>(jdbcTemplate.queryForList(USERS_LIKE, Long.class, id)));
        }

        return film;
    }

    @Override
    public List<Film> filmAll() {
        return jdbcTemplate.query(GET_ALL, new FilmExtractor());
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }
}
