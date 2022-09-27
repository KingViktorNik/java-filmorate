package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FriendService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public FriendService(FriendsStorage friendsStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }
    // Добавления в друзья
    public Optional<User> addFriend(long id, long idFriend) {
        User user = userStorage.getUserById(id)
                .orElseThrow(()-> new NullObjectException(String.format("Пользователь с id:%d не существует.", id)));
        User friend = userStorage.getUserById(idFriend)
                .orElseThrow(()-> new NullObjectException(String.format("Пользователь с id:%d не существует.", idFriend)));

        if (id == idFriend) {
            throw new ValidationException("Пользователь не может добавить сам себя в друзья!");
        }

        if (!friendsStorage.friends(id,idFriend)) {
            if (friendsStorage.addFriend(id, idFriend)){
                log.info(String.format("Пользователь id:%d, name:%s, добавил в друзья id:%d, name:%s",
                        user.getId(),
                        user.getName(),
                        friend.getId(),
                        friend.getName()
                ));
            };
        }

        return userStorage.getUserById(id);
    }

    // Удаление из друзей
    public Optional<User> deleteFriend(long id, long idFriend) {
        User user = userStorage.getUserById(id)
                .orElseThrow(()-> new NullObjectException(String.format("Пользователь с id:%d не существует.", id)));
        User friend = userStorage.getUserById(idFriend)
                .orElseThrow(()-> new NullObjectException(String.format("Пользователь с id:%d не существует.", idFriend)));

        user.getFriends().remove(idFriend);
        friend.getFriends().remove(id);

        if (friendsStorage.deleteFriends(id, idFriend)) {
            log.info(String.format("Пользователь id:%d, name:%s, удалил из друзей id:%d, name:%s",
                    user.getId(),
                    user.getName(),
                    friend.getId(),
                    friend.getName()
            ));
            return userStorage.getUserById(id);
        };
        return Optional.empty();
    }

    // Список общих друзей
    public List<User> allFriends(long id) {
        return friendsStorage.friendsAll(id);
    }

    // список друзей, общих с другим пользователем
    public List<User> commonFriends(long id, long otherId) {
        userStorage.getUserById(id)
                .orElseThrow(()-> new NullPointerException(String.format("Пользователь с id: %d нет в базе", id)));
        userStorage.getUserById(otherId)
                .orElseThrow(()-> new NullPointerException(String.format("Пользователь с id: %d нет в базе", otherId)));
        return friendsStorage.commonFriends(id, otherId);
    }
}
