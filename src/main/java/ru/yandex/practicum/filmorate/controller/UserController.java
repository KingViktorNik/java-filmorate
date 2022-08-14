package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;

@RestController
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        return inMemoryUserStorage.update(user);
    }

    @GetMapping("/users")
    public List<User> userAll() {
        return inMemoryUserStorage.userAll();
    }
}
