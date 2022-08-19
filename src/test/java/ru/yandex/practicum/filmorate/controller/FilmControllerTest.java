package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class FilmControllerTest {
    private final FilmController controller = new FilmController(new FilmService(new InMemoryFilmStorage()));
    private Film film;

    @BeforeEach
    void addFilm() {
        film = new Film();
        film.setName("Марсианин");
        film.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        film = controller.create(film);
    }

    @Test
    void create() {
        film = controller.create(film);

        assertFalse(controller.filmAll().isEmpty());
        assertEquals("Марсианин",controller.film(film.getId()).getName());
    }

    @Test
    void createNullName() {
        Film film = new Film();
        film.setName(null);
        film.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(film)
        );
        assertEquals("название не может быть пустым",exception.getMessage());
    }

    @Test
    void createBlankName() {
        Film film = new Film();
        film.setName("         ");
        film.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(film)
        );
        assertEquals("название не может быть пустым",exception.getMessage());
    }

    @Test
    void createNumberOfCharactersIsNotMoreThan200() {
        Film film = new Film();
        film.setName("Марсианин");
        film.setDescription("Экранизацию 'Марсианина' Энди Уира я ждал с нетерпением. Простенькая в художественном и " +
                "сюжетном плане, но наполненная массой интереснейших подробностей в духе 'занимательной науки' " +
                "книга проглатывается влет. "
        );
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(film)
        );
        assertEquals("Описание не должно привышать 200 символов!",exception.getMessage());
    }

    @Test
    void createDateNotEarlierThan1895December28() {
        Film film = new Film();
        film.setName("Марсианин");
        film.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(1895,12,27));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(film)
        );
        assertEquals("дата релиза — не раньше 28 декабря 1895 года",exception.getMessage());
    }

    @Test
    void createDurationNonNegativeValue() {
        Film film = new Film();
        film.setName("Марсианин");
        film.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film.setDuration(-1);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.create(film)
        );
        assertEquals("продолжительность фильма должна быть положительной",exception.getMessage());
    }

    @Test
    void update() {
        film = controller.create(film);

        Film filmNew = new Film();
        filmNew.setId(1);
        filmNew.setName("Марсианин 2");
        filmNew.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        filmNew.setDuration(14);
        filmNew.setReleaseDate(LocalDate.of(2015,9,11));

        filmNew = controller.update(filmNew);

        assertNotEquals(film, controller.film(filmNew.getId()));
        assertEquals("Марсианин 2", controller.film(filmNew.getId()).getName());
        assertEquals(14, controller.film(filmNew.getId()).getDuration());
    }

    @Test
    void updateException() {
        Film film = new Film();
        film.setName("Марсианин");
        film.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        controller.create(film);
        film.setId(555);

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.update(film)
        );
        assertEquals("Фильм с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void userIncorrectId() {
        Film film = new Film();
        film.setName("Марсианин");
        film.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        controller.create(film);

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.film(555)
        );
        assertEquals("Фильм с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void filmId() {
        long id = controller.create(film).getId();
        Film film2 = controller.film(id);
        assertEquals(film, film2);
    }

    @Test
    void nullFilmId() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.film(555L)
        );
        assertEquals("Фильм с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void filmAll() {
        film = controller.create(film);
        List<Film> filmList = controller.filmAll();
        assertNotEquals(-1 , filmList.indexOf(film));
    }

    @Test
    void addHappyLike() {
        film = controller.addLike(film.getId(), 1L);
        assertFalse(film.getLikes().isEmpty());
        List<Long> likeList = new ArrayList<>(film.getLikes());
        assertEquals(likeList.get(0), 1L);
    }

    @Test
    void addLikeNullFilm() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.addLike(555L, 1L)
        );
        assertEquals("Фильмь с id: '555' несуществует.",exception.getMessage());
    }

    @Test
    void deleteLike() {
        film = controller.addLike(film.getId(), 1L);
        assertFalse(film.getLikes().isEmpty());

        controller.deleteLike(film.getId(), 1L);
        assertTrue(film.getLikes().isEmpty());
    }

    @Test
    void NullDeleteLike() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.deleteLike(555L, 1L)
        );
        assertEquals("Фильмь с id: '555' несуществует.",exception.getMessage());
    }

    @Test
    void deleteLikeNullUser() {
        film = controller.addLike(film.getId(), 1L);
        assertFalse(film.getLikes().isEmpty());

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> controller.deleteLike(film.getId(), 555L)
        );
        assertEquals("Пользователя с id: '555' нет в списке likes.",exception.getMessage());

        assertFalse(film.getLikes().isEmpty());
    }

    @Test
    void topFilms() {
        Film film2 = new Film();
        film2.setName("film2");
        film2.setDescription("film2");
        film2.setDuration(100);
        film2.setReleaseDate(LocalDate.of(2001,1,1));

        film2 = controller.create(film2);

        Film film3 = new Film();
        film3.setName("film3");
        film3.setDescription("film3");
        film3.setDuration(100);
        film3.setReleaseDate(LocalDate.of(2001,1,1));

        film3 = controller.create(film3);

        Film film4 = new Film();
        film4.setName("film4");
        film4.setDescription("film4");
        film4.setDuration(100);
        film4.setReleaseDate(LocalDate.of(2001,1,1));

        film4 = controller.create(film4);

        Film film5 = new Film();
        film5.setName("film5");
        film5.setDescription("film5");
        film5.setDuration(100);
        film5.setReleaseDate(LocalDate.of(2001,1,1));

        film5 = controller.create(film5);

        film2 = controller.addLike(film2.getId(), 1L);
        List<Film> topFilms = controller.topFilms(3);
        assertEquals(3 , topFilms.size());
        assertEquals(0, topFilms.indexOf(film2));

        film4 = controller.addLike(film4.getId(), 1L);
        film4 = controller.addLike(film4.getId(), 2L);
        film4 = controller.addLike(film4.getId(), 3L);

        List<Film> topFilms2 = controller.topFilms(3);
        assertEquals(3 , topFilms2.size());
        assertEquals(0, topFilms2.indexOf(film4));
        assertEquals(1, topFilms2.indexOf(film2));
    }
}