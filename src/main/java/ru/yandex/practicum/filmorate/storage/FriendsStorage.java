package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    /**
     * Добавление пользователя в друзья
     * @param id идентификатор пользователя
     * @param friendId идентификатор пользователя добавляемого в друзья
     * @return возвращает true, если добавление прошло успешно
     */
    boolean addFriend(long id, long friendId);

    /**
     * Проверка на дружбу, являются ли пользователи друзьями
     * @param id идентификатор пользователя
     * @param friendId идентификатор проверяемого пользователя в списке друзей
     * @return возвращает true, если пользователь есть в списке друзей
     */
    boolean friends(long id, long friendId);

    /**
     * Список всех друзей пользователя
     * @param id идентификатор пользователя
     * @return Возвращает List<User> список друзей пользователя
     */
    List<User> friendsAll(long id);

    /**
     * Список общих друзей с другим пользователем
     * @param id идентификатор пользователя
     * @param otherId идентификатор проверяемого пользователя
     * @return возвращает список (List.class) объектов (User.class) список общих друзей
     */
    List<User> commonFriends(long id, long otherId);

    /**
     * Выполняет удаление из списка друзей
     * @param id идентификатор пользователя
     * @param friendId идентификатор проверяемого пользователя
     * @return возвращает true, если пользователь был успешно удален
     */
    boolean deleteFriends(long id, long friendId);
}
