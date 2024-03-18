DROP TABLE author,book,book_author;

CREATE TABLE author
(
    id          serial PRIMARY KEY,
    name        VARCHAR(255),
    second_name VARCHAR(255)
);

CREATE TABLE book
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(255)

);

CREATE TABLE book_author
(
    id          SERIAL PRIMARY KEY,
    book_id      BIGINT REFERENCES book (id) ON DELETE CASCADE,
    author_id BIGINT references author (id) ON DELETE RESTRICT
);

