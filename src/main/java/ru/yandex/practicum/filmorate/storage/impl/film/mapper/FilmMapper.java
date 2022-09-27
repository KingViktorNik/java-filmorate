package ru.yandex.practicum.filmorate.storage.impl.film.mapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
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
}
