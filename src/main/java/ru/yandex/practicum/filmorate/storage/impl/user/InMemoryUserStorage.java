package ru.yandex.practicum.filmorate.storage.impl.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public Optional<User> create(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        return Optional.ofNullable(users.put(user.getId(), user));
    }

    @Override
    public List<User> userAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(long id) {

        return Optional.ofNullable(users.get(id));
    }

    public Map<Long, User> getUsers() {
        return users;
    }
}
