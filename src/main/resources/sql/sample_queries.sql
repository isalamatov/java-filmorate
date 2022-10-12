SELECT f."name",
       f."description",
       f."release_date",
       f."duration",
       r."name",
       g2."name"
FROM "film" AS f
JOIN "film_genre" fg on f."film_id" = fg."film_id"
JOIN "genre" g2 on g2."genre_id" = fg."genre_id"
JOIN "rating" r on r."rating_id" = f."rating_id";

SELECT * FROM "genre";
