INSERT INTO "film" ("name", "description", "release_date", "duration", "rating_id")
    VALUES ('Melancholia',
            'Two sisters find their already strained relationship challenged as a mysterious new planet threatens to collide with Earth.',
            '2011-05-18',
            135,
            4);

INSERT INTO "film" ("name", "description", "release_date", "duration", "rating_id")
VALUES ('The Machinist',
        'An industrial worker who hasn''t slept in a year begins to doubt his own sanity.',
        '2004-01-18',
        101,
        4);

INSERT INTO "user" ("email", "login", "name", "birthday")
VALUES ('jane@doe.com', 'Janedoe', 'Jane Doe', '1970-01-01');

INSERT INTO "user" ("email", "login", "name", "birthday")
VALUES ('john@doe.com', 'Johnedoe', 'John Doe', '1970-12-12');

INSERT INTO "film_genre" ("film_id", "genre_id")
VALUES (1, 3);