package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public User create(User user) {
        dataVerification(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info(String.format("Добавлен пользователь: id: %d, login:%s", user.getId(), user.getLogin()));
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NullObjectException(String.format("Ползователя с id: '%d' не существует!", user.getId()));
        }

        dataVerification(user);
        users.put(user.getId(), user);
        log.info(String.format("Данные пользователя изменены: id: %d, login:%s", user.getId(), user.getLogin()));
        return user;
    }

    @Override
    public List<User> userAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User user(long id) {
        if (!users.containsKey(id))
            throw new NullObjectException(String.format("Ползователя с id: '%d' не существует!", id));
        return users.get(id);
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

    public Map<Long, User> getUsers() {
        return users;
    }
}
