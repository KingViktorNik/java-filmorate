package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    private final FilmService filmService = new FilmService( new InMemoryFilmStorage());
    private Film film1;

    @BeforeEach
    void addFilm() {
        film1 = new Film();
        film1.setName("Марсианин");
        film1.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film1.setDuration(144);
        film1.setReleaseDate(LocalDate.of(2015,9,11));

        film1 = filmService.getFilmStorage().create(film1);
    }

    @AfterEach
    void clearFilms(){
        filmService.getFilmStorage().getFilms().clear();
    }

    @Test
    void addHappyLike() {
        film1 = filmService.addLike(film1.getId(), 1L);
        assertFalse(film1.getLikes().isEmpty());
        List<Long> likeList = new ArrayList<>(film1.getLikes());
        assertEquals(likeList.get(0), 1L);
    }

    @Test
    void addLikeNullFilm() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> filmService.addLike(555L, 1L)
        );
        assertEquals("Фильмь с id: '555' несуществует.",exception.getMessage());
    }

    @Test
    void deleteLike() {
        film1 = filmService.addLike(film1.getId(), 1L);
        assertFalse(film1.getLikes().isEmpty());

        filmService.deleteLike(film1.getId(), 1L);
        assertTrue(film1.getLikes().isEmpty());
    }

    @Test
    void deleteLikeNullUser() {
        film1 = filmService.addLike(film1.getId(), 1L);
        assertFalse(film1.getLikes().isEmpty());

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> filmService.deleteLike(film1.getId(), 555L)
        );
        assertEquals("Пользователя с id: '555' нет в списке likes.",exception.getMessage());

        assertFalse(film1.getLikes().isEmpty());
    }

    @Test
    void topFilms() {
        Film film2 = new Film();
        film2.setName("film2");
        film2.setDescription("film2");
        film2.setDuration(100);
        film2.setReleaseDate(LocalDate.of(2001,1,1));

        film2 = filmService.getFilmStorage().create(film2);

        Film film3 = new Film();
        film3.setName("film3");
        film3.setDescription("film3");
        film3.setDuration(100);
        film3.setReleaseDate(LocalDate.of(2001,1,1));

        film3 = filmService.getFilmStorage().create(film3);

        Film film4 = new Film();
        film4.setName("film4");
        film4.setDescription("film4");
        film4.setDuration(100);
        film4.setReleaseDate(LocalDate.of(2001,1,1));

        film4 = filmService.getFilmStorage().create(film4);

        Film film5 = new Film();
        film5.setName("film5");
        film5.setDescription("film5");
        film5.setDuration(100);
        film5.setReleaseDate(LocalDate.of(2001,1,1));

        film5 = filmService.getFilmStorage().create(film5);

        filmService.addLike(film2.getId(), 1L);
        List<Film> topFilms = filmService.topFilms(3);
        assertEquals(3 , topFilms.size());
        assertEquals(topFilms.get(0), filmService.getFilmStorage().getFilms().get(film2.getId()));

        filmService.addLike(film4.getId(), 1L);
        filmService.addLike(film4.getId(), 2L);
        filmService.addLike(film4.getId(), 3L);

        List<Film> topFilms2 = filmService.topFilms(3);
        assertEquals(3 , topFilms2.size());
        assertEquals(topFilms2.get(0), filmService.getFilmStorage().getFilms().get(film4.getId()));
        assertEquals(topFilms2.get(1), filmService.getFilmStorage().getFilms().get(film2.getId()));
    }
}