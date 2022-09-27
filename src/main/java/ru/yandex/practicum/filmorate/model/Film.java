package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class Film {
    private long id; // идентификатор
    @NotNull
    private String name; // название
    private LocalDate releaseDate; // дата релиза
    private String description; // описание
    private int duration; // продолжительность фильма
    private int rate; // оценка фильма
    private Mpa mpa; // рейтинг фильма
    private List<Genre> genres; //жанр фильмов
    private Set<Long> likes; // список userId лайков
}
