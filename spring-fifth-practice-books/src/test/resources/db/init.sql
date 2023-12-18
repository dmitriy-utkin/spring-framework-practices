CREATE SCHEMA books_schema;

CREATE TABLE IF NOT EXISTS books_schema.categories
(
    id bigint NOT NULL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE SEQUENCE books_id_seq;

CREATE TABLE IF NOT EXISTS books_schema.books
(
    id bigint NOT NULL DEFAULT nextval('books_id_seq') PRIMARY KEY,
    author VARCHAR(255),
    name VARCHAR(255),
    category_id bigint,
    CONSTRAINT fkleqa3hhc0uhfvurq6mil47xk0 FOREIGN KEY (category_id)
        REFERENCES books_schema.categories (id)
);

INSERT INTO categories(id, name) VALUES (1, 'default1');
INSERT INTO categories(id, name) VALUES (2, 'default2');

INSERT INTO books(id, author, name, category_id) VALUES (1, 'author1', 'book1', 1);
INSERT INTO books(id, author, name, category_id) VALUES (2, 'author1', 'book2', 1);
INSERT INTO books(id, author, name, category_id) VALUES (3, 'author2', 'book3', 2);
INSERT INTO books(id, author, name, category_id) VALUES (4, 'author2', 'book4', 2);

ALTER SEQUENCE books_id_seq RESTART WITH 5;

