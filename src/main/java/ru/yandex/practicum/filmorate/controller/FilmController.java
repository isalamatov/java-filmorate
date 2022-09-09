package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class FilmController {
    HashMap<Integer, Film> films = new HashMap<>();
    private Integer idCounter = 0;

    @GetMapping("/films")
    public List<Film> getAll() {
        log.trace("Get all films request received.");
        return new ArrayList<>(films.values());
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        log.trace("Update film request received with data: {}", film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Film with ID" + film.getId() + "doesn't exists.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @PostMapping("/films")
    public Film add(@Valid @RequestBody Film film) {
        log.trace("Add film request received with data: {}", film);
        Optional<Integer> id = Optional.ofNullable(film.getId());
        if (id.isPresent() && films.containsKey(film.getId())) {
            throw new ValidationException("Film with ID" + film.getId() + "already exists, use PUT method to update it.");
        }
        if (id.isEmpty()) {
            id = Optional.of(++idCounter);
        }
        film.setId(id.get());
        films.put(id.get(), film);
        return film;
    }

}
