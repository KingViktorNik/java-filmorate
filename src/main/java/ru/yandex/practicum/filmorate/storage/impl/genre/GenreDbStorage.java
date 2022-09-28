package ru.yandex.practicum.filmorate.storage.impl.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.impl.genre.mapper.GenreMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final String GET_BY_ID = "SELECT ID, NAME FROM GENRE WHERE ID = ?";
    private final String GET_ALL = "SELECT ID, NAME FROM GENRE";

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        return jdbcTemplate.query(GET_BY_ID, new GenreMapper(), id)
                .stream().findFirst();
    }

    @Override
    public List<Genre> getGenreAll() {
        return jdbcTemplate.query(GET_ALL, new GenreMapper());
    }
}
