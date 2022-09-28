package ru.yandex.practicum.filmorate.storage.impl.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.user.mapper.UserExtractor;
import ru.yandex.practicum.filmorate.storage.impl.user.mapper.UserMapper;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
@Qualifier(("userDbStorage"))
public class UserDbStorage implements UserStorage {
    /** Добавление пользователя */
    private static final String ADD_USER = "INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";

    /** Обновление данных пользователя */
    private static final String UPDATE = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE ID = ?";

    /** Поиск пользователя по идентификатору */
    private static final String GET_BY_ID = "SELECT ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                                            "FROM USERS " +
                                            "WHERE ID = ?";

    /** Список всех пользователей */
    private static final String ALL_USERS = "SELECT ID, EMAIL, LOGIN, NAME, BIRTHDAY, ID_USER, ID_FRIEND " +
            "FROM USERS " +
            "LEFT OUTER JOIN FRIENDS ON ID = ID_USER " +
            "ORDER BY ID, ID_USER, ID_FRIEND";

    /** Список друзей пользователя */
    private static final String ALL_FRIENDS = "SELECT ID_FRIEND FROM FRIENDS WHERE ID_USER = ?";

    private final JdbcTemplate jdbcTemplate;


    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int indexCreate = jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(ADD_USER, new String[] {"ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getBirthday().toString());

            return stmt;
        }, keyHolder);
        if (indexCreate == 0) {
            return Optional.empty();
        }
        return getUserById(keyHolder.getKey().longValue());
    }

    @Override
    public Optional<User> update(User user) {
         int indexUpdate = jdbcTemplate.update(UPDATE, user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
         );

         if (indexUpdate == 0) {
             return Optional.empty();
         }

         return getUserById(user.getId());
    }

    @Override
    public Optional<User> getUserById(long id) {
        Optional<User> userList = jdbcTemplate.query(GET_BY_ID, new UserMapper(), id).stream().findFirst();
        if (userList.isPresent()) {
            Set<Long> friendsId = new HashSet<>(jdbcTemplate.queryForList(ALL_FRIENDS, Long.class, id));
            userList.get().setFriends(friendsId);
        }
        return userList;
    }

    @Override
    public List<User> userAll() {
        return jdbcTemplate.query(ALL_USERS, new UserExtractor());
    }
}
