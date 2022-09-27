package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Optional<User> create(User user) {
        dataVerification(user);

        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        User addUser = userStorage.create(user)
                .orElseThrow(() -> new NullObjectException("Пользователь не добавлен: id: %d, login:%s"));

        log.info(String.format("Добавлен пользователь: id: %d, login:%s", addUser.getId(), addUser.getLogin()));

        return Optional.of(addUser);
    }

    public Optional<User> update(User user) {
        userStorage.getUserById(user.getId())
                .orElseThrow(()-> new NullObjectException(
                        String.format("Пользователя с id: '%d' не существует!", user.getId()))
                );
    // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User userUp = userStorage.update(user)
                .orElseThrow(()-> new NullObjectException(String.format("Данные пользователя id:%d name:%s не изменены!",user.getId(),user.getName())));
        return Optional.of(userUp);
    }

    public List<User> userAll() {
        return userStorage.userAll();
    }

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userStorage.getUserById(id)
                .orElseThrow(() -> new NullObjectException(String.format("Пользователя с id: '%d' не существует!", id))));
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
            throw new ValidationException("email не корректный.");
        }
        // логин не может быть пустым и содержать пробелы;
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("login не корректный.");
        }
        // дата рождения не может быть в будущем.
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата не может быть в будущем.");
        }
    }
}
