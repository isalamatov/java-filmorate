DROP TABLE IF EXISTS "film" CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS "like" CASCADE;
DROP TABLE IF EXISTS "friend" CASCADE;
DROP TABLE IF EXISTS "genre" CASCADE;
DROP TABLE IF EXISTS "rating" CASCADE;
DROP TABLE IF EXISTS "film_genre" CASCADE;
DROP TABLE IF EXISTS "film_rating" CASCADE;

CREATE TABLE IF NOT EXISTS "genre"
(
    "genre_id" int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    "name"     varchar UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS "rating"
(
    "rating_id" int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    "name"      varchar UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS "user"
(
    "user_id"  int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "email"    varchar UNIQUE NOT NULL,
    "login"    varchar        NOT NULL,
    "name"     varchar        NOT NULL,
    "birthday" date           NOT NULL
);

CREATE TABLE IF NOT EXISTS "film"
(
    "film_id"      int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "name"         varchar NOT NULL,
    "description"  varchar NOT NULL,
    "release_date" date    NOT NULL,
    "duration"     int     NOT NULL,
    "rate"         int,
    "rating_id"    int     NOT NULL,
    CONSTRAINT "fk_film_rating_id" FOREIGN KEY ("rating_id")
        REFERENCES "rating" ("rating_id")
);

CREATE TABLE IF NOT EXISTS "like"
(
    "film_id" int NOT NULL,
    "user_id" int NOT NULL,
    CONSTRAINT "pk_like" PRIMARY KEY (
                                      "film_id", "user_id"
        ),
    CONSTRAINT "fk_like_film_id" FOREIGN KEY ("film_id")
        REFERENCES "film" ("film_id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "fk_like_user_id" FOREIGN KEY ("user_id")
        REFERENCES "user" ("user_id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS "friend"
(
    "user_id"   int     NOT NULL,
    "friend_id" int     NOT NULL,
    "accepted"  boolean NOT NULL,
    CONSTRAINT "pk_friend" PRIMARY KEY (
                                        "user_id", "friend_id"
        ),
    CONSTRAINT "fk_friend_user_id" FOREIGN KEY ("user_id")
        REFERENCES "user" ("user_id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "fk_friend_friend_id" FOREIGN KEY ("friend_id")
        REFERENCES "user" ("user_id") ON DELETE CASCADE ON UPDATE CASCADE
);



CREATE TABLE IF NOT EXISTS "film_rating"
(
    "film_id"   int NOT NULL,
    "rating_id" int NOT NULL,
    CONSTRAINT "pk_film_rating" PRIMARY KEY (
                                             "film_id", "rating_id"
        ),
    CONSTRAINT "fk_film_raiting_film_id" FOREIGN KEY ("film_id")
        REFERENCES "film" ("film_id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "fk_film_raiting_rating_id" FOREIGN KEY ("rating_id")
        REFERENCES "rating" ("rating_id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS "film_genre"
(
    "film_id"  int NOT NULL,
    "genre_id" int NOT NULL,
    CONSTRAINT "pk_film_genre" PRIMARY KEY (
                                            "film_id", "genre_id"
        ),
    CONSTRAINT "fk_film_genre_film_id" FOREIGN KEY ("film_id")
        REFERENCES "film" ("film_id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "fk_film_genre_genre_id" FOREIGN KEY ("genre_id")
        REFERENCES "genre" ("genre_id") ON DELETE CASCADE ON UPDATE CASCADE
);
