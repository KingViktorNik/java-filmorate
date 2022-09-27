package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{id}/friends")
public class FriendController {

    private final FriendService friedService;

    @Autowired
    public FriendController(FriendService userService) {
        this.friedService = userService;
    }

    // добавление в друзья
    @PutMapping("/{friendId}")
    public Optional<User> addFriend(@PathVariable long id, @PathVariable long friendId) {
        return friedService.addFriend(id, friendId);
    }

    // удаление из друзей по id.
    @DeleteMapping("/{friendId}")
    public Optional<User> deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        return friedService.deleteFriend(id, friendId);
    }

    // возвращает список пользователя, являющиеся друзьями.
    @GetMapping
    public List<User> listFriends(@PathVariable long id) {
        return friedService.allFriends(id);
    }

    // список друзей, общих с другим пользователем
    @GetMapping("/common/{otherId}")
    public List<User> commonFriends(@PathVariable long id, @PathVariable long otherId) {
        return friedService.commonFriends(id, otherId);
    }
}
