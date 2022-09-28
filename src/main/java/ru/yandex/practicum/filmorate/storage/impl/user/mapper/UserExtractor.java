package ru.yandex.practicum.filmorate.storage.impl.user.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        boolean isExit = false; // если isExit истина, то значит конец таблицы
        User user = User.builder().build();
        Set<Long> friends = new HashSet<>(); //список идентификаторов пользователей(друзей)

        long userId = 0; // идентификатор пользователя предыдущей записи

        List<User> users = new ArrayList<>();

        if (rs.next()) {
            while (true) {
                if (userId != rs.getLong("ID")) {
                    userId = rs.getLong("ID");
                    user = User.builder()
                            .id(rs.getLong("ID"))
                            .email(rs.getString("EMAIL"))
                            .login(rs.getString("LOGIN"))
                            .name(rs.getString("NAME"))
                            .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                            .build();
                }

                if (rs.getString("ID_FRIEND") != null) {
                    friends.add(rs.getLong("ID_FRIEND"));
                }

                // переход к следующий записи и проверка является ли запись последней в таблице
                if (!rs.next()) {
                    isExit = true;
                }

                // если запись последняя или относится ли следующая строка к текущему фильму
                if (isExit || userId != rs.getLong("ID")) {
                    user.setFriends(friends);
                    users.add(user);
                    friends = new HashSet<>();
                    if (isExit) {
                        break;
                    }
                }
            }
        }
        return users;
    }
}
