package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    // Добовления в друзья
    public User addFriend(long id, long idFriend) {
        nullUser(id);
        nullUser(idFriend);
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(idFriend);

        if (id != idFriend) {
            user.getFriends().add(idFriend);
            friend.getFriends().add(id);
        } else {
            throw new ValidationException("Пользователь не может добвасить сам себя в друзья!");
        }

        log.info(String.format("Пользловаетль id:%d, name:%s, добавил в друзья id:%d, name:%s",
                user.getId(),
                user.getName(),
                friend.getId(),
                friend.getName()
        ));

        return user;
    }

    // Удаление из друзей
    public User deleteFriend(long id, long idFriend) {
        nullUser(id);
        nullUser(idFriend);
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(idFriend);

        user.getFriends().remove(idFriend);
        friend.getFriends().remove(id);

        log.info(String.format("Пользловаетль id:%d, name:%s, удалил из друзей id:%d, name:%s",
                user.getId(),
                user.getName(),
                friend.getId(),
                friend.getName()
        ));

        return user;
    }

    // Список общих друзей
    public List<User> listFriends(long id) {
        List<User> friends = new ArrayList<>();

        for (Long idFriend : userStorage.getUsers().get(id).getFriends()) {
            friends.add(userStorage.getUsers().get(idFriend));
        }

        return friends;
    }

    // список друзей, общих с другим пользователем
    public List<User> commonFriends(long id, long otherId) {
        Set<Long> commonList = new HashSet<>(userStorage.getUsers().get(id).getFriends());
        commonList.retainAll(userStorage.getUsers().get(otherId).getFriends());

        List<User> users = new ArrayList<>();

        for (Long commonFriend : commonList) {
            users.add(this.userStorage.getUsers().get(commonFriend));
        }

        return users;
    }

    private void nullUser(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NullObjectException(String.format("Пользователь с id:%d несуществует.", id));
        }
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
