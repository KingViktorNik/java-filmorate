package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {
    private final FilmService filmService;
    private final LikeService likeService;
    private final UserService userService;
    private Film film;

    @BeforeEach
    void addFilm() {
        film = Film.builder()
                .name("Марсианин")
                .releaseDate(LocalDate.of(2015,9,11))
                .description("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.")
                .duration(144)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .build();

        film = filmService.create(film).get();
    }

    @Test
    void create() {
        film = filmService.create(film).get();

        assertFalse(filmService.filmAll().isEmpty());
        assertEquals("Марсианин", filmService.film(film.getId()).get().getName());
    }

    @Test
    void createNullName() {
        Film film = Film.builder()
                .name(null)
                .description("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.")
                .duration(144)
                .releaseDate(LocalDate.of(2015,9,11))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> filmService.create(film)
        );
        assertEquals("название не может быть пустым",exception.getMessage());
    }

    @Test
    void createBlankName() {
        Film film = Film.builder()
                .name("         ")
                .description("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.")
                .duration(144)
                .releaseDate(LocalDate.of(2015,9,11))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> filmService.create(film)
        );
        assertEquals("название не может быть пустым",exception.getMessage());
    }

    @Test
    void createNumberOfCharactersIsNotMoreThan200() {
        Film film = Film.builder()
                .name("Марсианин")
                .description("Экранизацию 'Марсианина' Энди Уира я ждал с нетерпением. Простенькая в художественном и " +
                "сюжетном плане, но наполненная массой интереснейших подробностей в духе 'занимательной науки' " +
                "книга проглатывается влет. "
                ).build();
        film.setDuration(144);
        film.setReleaseDate(LocalDate.of(2015,9,11));

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> filmService.create(film)
        );
        assertEquals("Описание не должно превышать 200 символов!",exception.getMessage());
    }

    @Test
    void createDateNotEarlierThan1895December28() {
        Film film = Film.builder()
                .name("Марсианин")
                .description("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.")
                .duration(144)
                .releaseDate(LocalDate.of(1895,12,27))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> filmService.create(film)
        );
        assertEquals("дата релиза — не раньше 28 декабря 1895 года",exception.getMessage());
    }

    @Test
    void createDurationNonNegativeValue() {
        Film film = Film.builder()
                .name("Марсианин")
                .description("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.")
                .duration(-1)
                .releaseDate(LocalDate.of(2015,9,11))
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class, ()-> filmService.create(film)
        );
        assertEquals("продолжительность фильма должна быть положительной",exception.getMessage());
    }

    @Test
    void update() {
        film = filmService.create(film).get();

        Film filmNew = Film.builder()
                .id(1)
                .name("Марсианин 2")
                .description("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.")
                .duration(14)
                .releaseDate(LocalDate.of(2015,9,11))
                .mpa(Mpa.builder().id(2).build())
                .build();

        filmNew = filmService.update(filmNew).get();

        assertNotEquals(film, filmService.film(filmNew.getId()).get());
        assertEquals("Марсианин 2", filmService.film(filmNew.getId()).get().getName());
        assertEquals(14, filmService.film(filmNew.getId()).get().getDuration());
    }

    @Test
    void updateException() {
        Film film = Film.builder()
                .name("Марсианин")
                .description("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.")
                .duration(144)
                .releaseDate(LocalDate.of(2015,9,11))
                .mpa(Mpa.builder().id(2).build())
                .build();

        filmService.create(film);
        film.setId(555);

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> filmService.update(film)
        );
        assertEquals("Фильм с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void userIncorrectId() {
        Film film = Film.builder()
                .name("Марсианин")
                .description("Красивый, хорошее оформление съёмочных площадок, добрый, незатянутый сюжет.")
                .duration(144)
                .releaseDate(LocalDate.of(2015,9,11))
                .mpa(Mpa.builder().id(2).build())
                .build();

        filmService.create(film);

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> filmService.film(555)
        );
        assertEquals("Фильм с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void filmId() {
        Film film2 = filmService.film(film.getId()).get();
        assertEquals(film, film2);
    }

    @Test
    void nullFilmId() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> filmService.film(555L)
        );
        assertEquals("Фильм с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void filmAll() {
        film = filmService.create(film).get();
        List<Film> filmList = filmService.filmAll();
        assertNotEquals(-1 , filmList.indexOf(film));
    }

    @Test
    void addHappyLike() {
        User user = User.builder()
                .email("jimi@mail.com")
                .login("Jim878")
                .name("Jimi")
                .birthday(LocalDate.of(1990,5,5))
                .build();
        user = userService.create(user).get();
        film = likeService.addLike(film.getId(), user.getId()).get();
        assertFalse(film.getLikes().isEmpty());
        List<Long> likeList = new ArrayList<>(film.getLikes());
        assertEquals(likeList.get(0), user.getId());
    }

    @Test
    void addLikeNullFilm() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> likeService.addLike(555L, 1L)
        );
        assertEquals("Фильм с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void deleteLike() {
        User user = User.builder()
                .email("jimi@mail.com")
                .login("Jim878")
                .name("Jimi")
                .birthday(LocalDate.of(1990,5,5))
                .build();
        user = userService.create(user).get();

        film = likeService.addLike(film.getId(), user.getId()).get();
        assertFalse(film.getLikes().isEmpty());

        film = likeService.deleteLike(film.getId(), user.getId()).get();
        assertTrue(film.getLikes().isEmpty());
    }

    @Test
    void NullDeleteLike() {
        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> likeService.deleteLike(555L, 1L)
        );
        assertEquals("Фильм с id: '555' не существует!",exception.getMessage());
    }

    @Test
    void deleteLikeNullUser() {
        User user = User.builder()
                .email("jimi@mail.com")
                .login("Jim878")
                .name("Jimi")
                .birthday(LocalDate.of(1990,5,5))
                .build();
        user = userService.create(user).get();

        film = likeService.addLike(film.getId(), user.getId()).get();
        assertFalse(film.getLikes().isEmpty());

        final NullObjectException exception = assertThrows(
                NullObjectException.class, ()-> likeService.deleteLike(film.getId(), 555L)
        );
        assertEquals("Пользователя с id: '555' не существует!",exception.getMessage());

        assertFalse(film.getLikes().isEmpty());
    }

    @Test
    void topFilms() {
        Film film2 = Film.builder()
                .name("film2")
                .description("film2")
                .duration(100)
                .releaseDate(LocalDate.of(2001, 1, 1))
                .mpa(Mpa.builder().id(2).build())
                .build();

        film2 = filmService.create(film2).get();

        Film film3 = Film.builder()
                .name("film3")
                .description("film3")
                .duration(100)
                .releaseDate(LocalDate.of(2001, 1, 1))
                .mpa(Mpa.builder().id(2).build())
                .build();

        film3 = filmService.create(film3).get();

        Film film4 = Film.builder()
                .name("film4")
                .description("film4")
                .duration(100)
                .releaseDate(LocalDate.of(2001, 1, 1))
                .mpa(Mpa.builder().id(2).build())
                .build();

        film4 = filmService.create(film4).get();

        Film film5 = Film.builder()
                .name("film5")
                .description("film5")
                .duration(100)
                .releaseDate(LocalDate.of(2001, 1, 1))
                .mpa(Mpa.builder().id(2).build())
                .build();

        film5 = filmService.create(film5).get();

        User user = User.builder()
                .email("user1@mail.com")
                .login("user1")
                .name("user1")
                .birthday(LocalDate.of(1990, 5, 5))
                .build();

        user = userService.create(user).get();

        User user2 = User.builder()
                .email("user2@mail.com")
                .login("user2")
                .name("user2")
                .birthday(LocalDate.of(1990, 5, 5))
                .build();

        user2 = userService.create(user2).get();

        User user3 = User.builder()
                .email("user3@mail.com")
                .login("user3")
                .name("user3")
                .birthday(LocalDate.of(1990, 5, 5))
                .build();

        user3 = userService.create(user3).get();

        User user4 = User.builder()
                .email("user4@mail.com")
                .login("user4")
                .name("user4")
                .birthday(LocalDate.of(1990, 5, 5))
                .build();

        user4 = userService.create(user4).get();

        User user5 = User.builder()
                .email("user5@mail.com")
                .login("user5")
                .name("user5")
                .birthday(LocalDate.of(1990, 5, 5))
                .build();

        user5 = userService.create(user5).get();

        User user6 = User.builder()
                .email("user6@mail.com")
                .login("user6")
                .name("user6")
                .birthday(LocalDate.of(1990, 5, 5))
                .build();

        user6 = userService.create(user6).get();

        film2 = likeService.addLike(film2.getId(), user2.getId()).get();
        List<Film> topFilms = likeService.topFilms(3);
        assertEquals(3, topFilms.size());
        assertEquals(film2.getId(), topFilms.get(0).getId());
        film4 = likeService.addLike(film4.getId(), user.getId()).get();
        film4 = likeService.addLike(film4.getId(), user2.getId()).get();
        film4 = likeService.addLike(film4.getId(), user3.getId()).get();

        List<Film> topFilms2 = likeService.topFilms(3);
        assertEquals(3, topFilms2.size());
        assertEquals(film4.getId(), topFilms2.get(0).getId());
        assertEquals(film2.getId(), topFilms2.get(1).getId());
    }
}