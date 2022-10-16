package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.DBGenreStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class GenreService {

    private DBGenreStorage dbGenreStorage;

    public GenreService(DBGenreStorage dbGenreStorage) {
        this.dbGenreStorage = dbGenreStorage;
    }

    public List<Genre> getAllGenres() {
        log.trace("Gett all genres request recieved.");
        return dbGenreStorage.getAllGenres();
    }

    public Genre getGenre(Integer id) {
        log.trace("Get genre request recieved with id: {}", id);
        return dbGenreStorage.getGenre(id);
    }
}
