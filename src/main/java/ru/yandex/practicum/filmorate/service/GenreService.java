package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Optional<Genre> getGenreById(long id) {
        Genre genre = genreStorage.getGenreById(id)
                .orElseThrow(()-> new NullObjectException(String.format("Жанр с id: '%d' не существует!", id)));

        log.info("Найден жанр:{} {}", genre.getId(), genre.getName());
        return Optional.of(genre);
    }

    public List<Genre> getGenreAll() {
        return genreStorage.getGenreAll();
    }
}
