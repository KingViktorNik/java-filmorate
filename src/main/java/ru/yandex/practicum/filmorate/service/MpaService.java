package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Optional<Mpa> getMpaById(long id) {
        Mpa mpa = mpaStorage.getMpaById(id)
                .orElseThrow(()-> new NullObjectException(String.format("Рейтинг с id: '%d' не существует!", id)));

        log.info("Найден рейтинг id:{} name:{}", mpa.getId(), mpa.getName());
        return Optional.of(mpa);
    }

    public List<Mpa> getMpaAll() {
        return mpaStorage.getMpaAll();
    }
}
