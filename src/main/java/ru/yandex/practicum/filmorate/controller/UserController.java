package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userStorage) {
        this.userService = userStorage;
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        return userService.getUserStorage().create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {

        return userService.getUserStorage().update(user);
    }

    @GetMapping("/users")
    public List<User> userAll() {
        return userService.getUserStorage().userAll();
    }

    @GetMapping("/users/{id}")
    public User user(@PathVariable long id) {
        return userService.getUserStorage().user(id);
    }


    // добавление в друзья
    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.addFriend(id, friendId);
    }

    // удаление из друзей по id.
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.deleteFriend(id, friendId);
    }

    // возвращает список пользователя, являющиеся друзьями.
    @GetMapping("/users/{id}/friends")
    public List<User> listFriends(@PathVariable long id) {
        return userService.listFriends(id);
    }

    // список друзей, общих с другим пользователем
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.commonFriends(id, otherId);
    }

}
