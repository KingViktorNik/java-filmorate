package ru.yandex.practicum.filmorate.storage.impl.film.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        boolean isExit = false; // если isExit истина, то значит конец таблицы
        Film film = Film.builder().build();
        Set<Long> like = new HashSet<>(); // список идентификаторов пользователей оценивших текущий фильм
        List<Genre> genres = new ArrayList<>(); // список жанров

        long userId = 0; // идентификатор пользователя предыдущей записи
        long filmId = 0; // идентификатор фильма предыдущей записи

        List<Film> films = new ArrayList<>();

        if (rs.next()) {
            while (true) {
                if (filmId != rs.getLong("FILM_ID")) {
                    filmId = rs.getLong("FILM_ID");
                    film = Film.builder()
                            .id(rs.getLong("FILM_ID"))
                            .name(rs.getString("FILM_NAME"))
                            .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                            .description(rs.getString("DESCRIPTION"))
                            .duration(rs.getInt("DURATION"))
                            .rate(rs.getInt("RATE"))
                            .mpa(Mpa
                                    .builder()
                                    .id(rs.getLong("MPA_ID"))
                                    .name(rs.getString("MPA_NAME"))
                                    .build())
                            .genres(List.of())
                            .likes(Set.of())
                            .build();
                }

                // если поле с id пользователя не пустое и не содержится в списке like
                if (rs.getString("ID_USER") != null && userId != rs.getLong("ID_USER")) {
                    userId = rs.getLong("ID_USER");
                    like.add(rs.getLong("ID_USER"));
                }

                // если поле с id жанра не пустое
                if (rs.getString("GENRE_ID") != null) {
                    genres.add(Genre.builder()
                            .id(rs.getLong("GENRE_ID"))
                            .name(rs.getString("GENRE_NAME"))
                            .build());
                }

                // переход к следующий записи и проверка является ли запись последней в таблице
                if (!rs.next()) {
                    isExit = true;
                }

                // если запись последняя или относится ли следующая строка к текущему фильму
                if (isExit || filmId != rs.getLong("FILM_ID")) {
                    film.setLikes(like);
                    film.setGenres(genres);
                    films.add(film);
                    like = new HashSet<>();
                    genres = new ArrayList<>();
                    if (isExit) {
                        break;
                    }
                }
            }
        }
        return films;
    }
        }
