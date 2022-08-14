package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {
    // добовление пользлвателя
    User create(User user);

    // обновление информации о пользователе
    User update(User user);

    //список всех ползователей
    List<User> userAll();
}
