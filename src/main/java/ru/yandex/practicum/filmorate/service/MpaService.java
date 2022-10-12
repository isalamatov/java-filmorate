package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.DBMpaStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class MpaService {

    private DBMpaStorage dbMpaStorage;

    @Autowired
    public MpaService(DBMpaStorage dbMpaStorage) {
        this.dbMpaStorage = dbMpaStorage;
    }


    public Mpa getMpa(Integer id) {
        log.trace("Get mpa request recieved with id: {}", id);
        return dbMpaStorage.getMpa(id);
    }

    public List<Mpa> getAllMpa() {
        log.trace("Get all mpa request recieved.");
        return dbMpaStorage.getAllMpa();
    }
}
