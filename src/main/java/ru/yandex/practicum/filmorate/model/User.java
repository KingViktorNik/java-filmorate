package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class User {
    int id; //идентификатор
    String email; // электронная почта
    String login; // логин пользователя
    String name; // имя для отображения
    LocalDate birthday; // дата рождения
}
