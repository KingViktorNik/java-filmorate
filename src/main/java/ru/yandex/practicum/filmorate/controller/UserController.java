package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userStorage) {
        this.userService = userStorage;
    }

    @PostMapping
    public Optional<User> create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public Optional<User> update(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping
    public List<User> userAll() {
        return userService.userAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

}