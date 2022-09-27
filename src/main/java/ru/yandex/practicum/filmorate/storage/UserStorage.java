package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    /**
     *  Добавление нового пользователя
     * @param user необходима передать объект класса User.class
     * @return метод вернет объект класса User.class с id(идентификаторам)
     */
    Optional<User> create(User user);

    /**
     * Обновление информации о пользователе
     * @param user необходима передать объект класса User.class
     * @return метод вернет объект класса User.class
     */
    Optional<User> update(User user);

    /**
     * Поиск пользователя по id(идентификатору)
     * @param id необходимо передать id(идентификатор пользователя)
     * @return метод вернет объект (User.class)
     */
    Optional<User> getUserById(long id);

    /**
     * Список всех пользователей
     * @return метод вернет список (List.class) объектов (User.class)
     */
    List<User> userAll();
}
