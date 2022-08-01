package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class FilmControllerTest {
    private final FilmController controller = new FilmController();

    @Test
    void create() {
        Film film = new Film();
        film.setName("Марсианин");
        film.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        controller.create(film);

        assertFalse(controller.getFilms().isEmpty());
        assertEquals("Марсианин",controller.getFilms().get(1L).getName());
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
        Film film = new Film();
        film.setName("Марсианин");
        film.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        controller.create(film);

        Film filmNew = new Film();
        filmNew.setId(1);
        filmNew.setName("Марсианин 2");
        filmNew.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        filmNew.setDuration(14);
        filmNew.setReleaseDate(LocalDate.of(2015,9,11));

        controller.update(filmNew);

        assertNotEquals(film, controller.getFilms().get(1));
        assertEquals("Марсианин 2", controller.getFilms().get(1L).getName());
        assertEquals(14, controller.getFilms().get(1L).getDuration());
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

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> controller.update(film)
        );
        assertEquals("Фильма с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void userAll() {
        Film film = new Film();
        film.setName("Марсианин");
        film.setDescription("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.");
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        controller.create(film);
        List<Film> filmList = controller.filmAll();
        assertEquals(film, filmList.get(0));
    }
}