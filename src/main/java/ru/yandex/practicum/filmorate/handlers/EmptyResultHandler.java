package ru.yandex.practicum.filmorate.handlers;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class EmptyResultHandler {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseBody
    public ResponseEntity<String> handle(final EmptyResultDataAccessException ex) {
        return new ResponseEntity<>("Requested object doesn't exists", HttpStatus.NOT_FOUND);
    }
}
