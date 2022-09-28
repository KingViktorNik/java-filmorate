package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.LikeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // пользователь ставит лайк фильму.
    @PutMapping("/{id}/like/{userId}")
    public Optional<Film> addLike(@PathVariable long id, @PathVariable long userId) {
        return likeService.addLike(id, userId);
    }

    // пользователь удаляет лайк.
    @DeleteMapping("/{id}/like/{userId}")
    public Optional<Film> deleteLike(@PathVariable long id, @PathVariable long userId) {
        return likeService.deleteLike(id, userId);
    }

    // возвращает список фильмов из первых
    @GetMapping("/popular")
    public List<Film> topFilms(@RequestParam (defaultValue = "10", required = false) long count) {
        return likeService.topFilms(count);
    }
}
