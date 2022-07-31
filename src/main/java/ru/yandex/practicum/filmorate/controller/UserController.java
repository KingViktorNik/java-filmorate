package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        dataVerification(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info(String.format("Добавлен пользователь: id: %d, login:%s", user.getId(),user.getLogin()));
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Ползователя с id: '" + user.getId() + "' не существует!");
        }
        dataVerification(user);
        users.put(user.getId(), user);
        log.info(String.format("Данные пользователя изменены: id: %d, login:%s", user.getId(),user.getLogin()));
        return user;
    }

    @GetMapping("/users")
    public List<User> userAll() {
        return new ArrayList<>(users.values());
    }

    // Проверка email на корректность
    private static boolean addressVerification(String email) {
        int at = email.indexOf("@");
        int dot = email.lastIndexOf(".");
        return !(at > -1 && dot > at);
    }

    private void dataVerification(User user) {
        // электронная почта не может быть пустой и должна содержать символ @
        if (user.getEmail() == null ||
                user.getEmail().isBlank() ||
                addressVerification(user.getEmail())) {
            throw new ValidationException("email не коректный.");
        }
        // логин не может быть пустым и содержать пробелы;
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("login не коректный.");
        }
        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        // дата рождения не может быть в будущем.
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата не может быть в будущем.");
        }
    }
}
