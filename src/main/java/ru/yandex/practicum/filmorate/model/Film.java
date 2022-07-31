package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Film {
    int id; // идентификатор
    String name; // название
    String description; // описание
    LocalDate releaseDate; // дата релиза
    int duration; // продолжительность фильма
}
