package ru.yandex.practicum.filmorate.storage.impl.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.impl.mpa.mapper.MpaMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {
    /**
     * Поиск MPA по идентификатору
     */
    public static final String GET_BY_ID = "SELECT ID, NAME FROM MPA WHERE ID = ?";

    /**
     * Выгрузка всех MPA
     */
    public static final String GET_ALL = "SELECT ID, NAME FROM MPA";

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> getMpaById(long id) {
        return jdbcTemplate.query(GET_BY_ID, new MpaMapper(), id)
                .stream().findFirst();
    }

    @Override
    public List<Mpa> getMpaAll() {
        return jdbcTemplate.query(GET_ALL, new MpaMapper());
    }
}
