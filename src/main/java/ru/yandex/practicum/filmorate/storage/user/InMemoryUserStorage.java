package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;

    @Override
    public User create(User user) {
        user.setId(++id);
        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        // имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> userAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User user(long id) {
        return users.get(id);
    }

    public Map<Long, User> getUsers() {
        return users;
    }
}
