package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Genre {
    @Id
    private final long id;
    @NotNull
    private final String name;
}
