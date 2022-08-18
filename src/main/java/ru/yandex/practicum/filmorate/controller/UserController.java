package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userStorage) {
        this.userService = userStorage;
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        dataVerification(user);
        log.info(String.format("Добавлен пользователь: id: %d, login:%s", user.getId(), user.getLogin()));
        return userService.getUserStorage().create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        if (!userService.getUserStorage().getUsers().containsKey(user.getId())) {
            throw new NullObjectException(String.format("Ползователя с id: '%d' не существует!", user.getId()));
        }

        dataVerification(user);
        log.info(String.format("Данные пользователя изменены: id: %d, login:%s", user.getId(), user.getLogin()));

        return userService.getUserStorage().update(user);
    }

    @GetMapping("/users")
    public List<User> userAll() {
        return userService.getUserStorage().userAll();
    }

    @GetMapping("/users/{id}")
    public User user(@PathVariable long id) {
        if (!userService.getUserStorage().getUsers().containsKey(id))
            throw new NullObjectException(String.format("Ползователя с id: '%d' не существует!", id));

        return userService.getUserStorage().user(id);
    }


    // добавление в друзья
    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriend(id, friendId);
    }

    // удаление из друзей по id.
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    // возвращает список пользователя, являющиеся друзьями.
    @GetMapping("/users/{id}/friends")
    public List<User> listFriends(@PathVariable long id) {
        return userService.listFriends(id);
    }

    // список друзей, общих с другим пользователем
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.commonFriends(id, otherId);
    }

    // Проверка email на корректность
    private static boolean email(String email) {
        int at = email.indexOf("@");
        int dot = email.lastIndexOf(".");
        return !(at > -1 && dot > at);
    }

    private void dataVerification(User user) {
        // электронная почта не может быть пустой и должна содержать символ @
        if (user.getEmail() == null ||
                user.getEmail().isBlank() ||
                email(user.getEmail())) {
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
