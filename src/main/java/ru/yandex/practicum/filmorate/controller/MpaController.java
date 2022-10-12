package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.MpaService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable Integer id) {
        return mpaService.getMpa(id);
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        return mpaService.getAllMpa();
    }

}
