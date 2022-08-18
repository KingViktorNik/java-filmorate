package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    // добовление пользлвателя
    User create(User user);

    // обновление информации о пользователе
    User update(User user);

    // пользователь по id
    User user(long id);
    //список всех ползователей
    List<User> userAll();

    Map<Long, User> getUsers();
}
