CREATE SCHEMA IF NOT EXISTS contacts_schema;

CREATE TABLE IF NOT EXISTS contacts_schema.contact
(
    id INTEGER PRIMARY KEY,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL
);