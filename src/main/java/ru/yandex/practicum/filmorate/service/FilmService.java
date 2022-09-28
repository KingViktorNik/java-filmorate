package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Qualifier("FilmDbService")
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Optional<Film> create(Film film) {
        dataVerification(film);

        Film filmNew = filmStorage.create(film)
                .orElseThrow(() -> new NullObjectException(
                        String.format("Произошла ошибка при добавлении фильма: id: %d, name:%s",
                                film.getId(),
                                film.getName())));

        log.info(String.format("Добавлен фильм: id: %d, name:%s", filmNew.getId(), filmNew.getName()));

        return Optional.of(filmNew);
    }

    public Optional<Film> update(Film film) {
        dataVerification(film);
        filmStorage.getFilmById(film.getId())
                .orElseThrow(()-> new NullObjectException(String.format("Фильм с id: '%d' не существует!", film.getId())));

        Film filmUp = filmStorage.update(film).orElseThrow(()->new NullObjectException(
                String.format("Произошла ошибка при изменении данных о фильме: id: %d, name:%s",
                        film.getId(),
                        film.getName())));

        log.info(String.format("Данные о фильме изменены: id: %d, name:%s", filmUp.getId(), filmUp.getName()));

        return Optional.of(filmUp);
    }

    public List<Film> filmAll() {
        return filmStorage.filmAll();
    }

    public Optional<Film> film( long id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NullObjectException(String.format("Фильм с id: '%d' не существует!", id)));

        log.info("Найден фильм:{} {}", film.getId(), film.getName());
        return Optional.of(film);
    }

    private void dataVerification(Film film) {
        //название не может быть пустым
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("название не может быть пустым");
        }
        //максимальная длина описания — 200 символов
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно превышать 200 символов!");
        }
        //дата релиза — не раньше 28 декабря 1895 года
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        //продолжительность фильма должна быть положительной.
        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        // Рейтинг фильма MPA
        if (film.getMpa() == null) {
            throw new ValidationException("MPA не должен быть NULL");
        }
    }
}
