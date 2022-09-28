package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.impl.user.mapper.UserExtractor;

import java.util.List;

@Repository
public class FriendsDbStorage implements FriendsStorage {
    /** Добавление в друзья */
    private static final String ADD_FRIEND = "INSERT INTO FRIENDS(ID_USER, ID_FRIEND) VALUES (?, ?)";

    /** Проверка на дружбу, являются ли пользователи друзьями */
    private static final String FRIENDS =
            "SELECT ID_USER, ID_FRIEND " +
            "FROM FRIENDS " +
            "WHERE ID_USER = ? AND ID_FRIEND = ?";

    /** Список друзей пользователя */
    private static final String ALL_FRIENDS =
            "SELECT ID, EMAIL, LOGIN, NAME, BIRTHDAY, ID_USER, ID_FRIEND " +
            "FROM USERS " +
            "LEFT OUTER JOIN FRIENDS ON ID = ID_USER " +
            "WHERE ID IN (SELECT ID_FRIEND " +
                            "FROM FRIENDS " +
                            "WHERE ID_USER = ?" +
                    ") " +
            "ORDER BY ID, ID_USER, ID_FRIEND" ;

    /** Список общих друзей с другим пользователем */
    private static final String COMMANDS_FRIENDS =
            "SELECT ID, EMAIL, LOGIN, NAME, BIRTHDAY, ID_FRIEND " +
            "FROM USERS " +
            "LEFT OUTER JOIN FRIENDS ON ID = ID_USER " +
            "WHERE ID = (SELECT ID_FRIEND FROM FRIENDS " +
                    "WHERE ID_USER = ? " +
                        "AND ID_FRIEND = ( SELECT ID_FRIEND " +
                            "FROM FRIENDS " +
                            "WHERE ID_USER = ?" +
                        ")" +
                    ")"
            ;

    /** Выполняет удаление из списка друзей */
    private static final String DELETE_FRIENDS = "DELETE FROM FRIENDS WHERE ID_USER = ? AND ID_FRIEND = ?";
    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean addFriend(long id, long friendId) {
        return jdbcTemplate.update(ADD_FRIEND, id, friendId) > 0;
    }

    @Override
    public boolean friends(long id, long friendId) {
        return jdbcTemplate.queryForList(FRIENDS,id,friendId).size() > 0;
    }

    @Override
    public List<User> friendsAll(long id) {
        return jdbcTemplate.query(ALL_FRIENDS, new UserExtractor(), id);
    }

    @Override
    public List<User> commonFriends(long id, long otherId) {
        return jdbcTemplate.query(COMMANDS_FRIENDS, new UserExtractor(), id, otherId);
    }

    @Override
    public boolean deleteFriends(long id, long friendId) {
        return jdbcTemplate.update(DELETE_FRIENDS, id, friendId) > 0;
    }
}
