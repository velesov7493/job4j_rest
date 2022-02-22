DROP TABLE IF EXISTS tr_chatmessages;
DROP TABLE IF EXISTS tz_chatrooms;
DROP TABLE IF EXISTS tz_actors;
DROP TABLE IF EXISTS tz_roles;

/* Таблицы БД */

CREATE TABLE tz_roles (
    id INTEGER PRIMARY KEY,
    name VARCHAR(90) NOT NULL UNIQUE,
    authority VARCHAR(120) NOT NULL
);

CREATE TABLE tz_actors (
    id SERIAL PRIMARY KEY,
    id_role INTEGER NOT NULL REFERENCES tz_roles (id) ON DELETE RESTRICT,
    id_account INTEGER NOT NULL,
    name VARCHAR(90) NOT NULL,
    email VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE tz_chatrooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(60) NOT NULL
);

CREATE TABLE tr_chatmessages (
    id BIGSERIAL PRIMARY KEY,
    id_chat BIGINT NOT NULL REFERENCES tz_chatrooms (id) ON DELETE CASCADE,
    id_author BIGINT REFERENCES tz_actors (id) ON DELETE SET NULL,
    time TIMESTAMP NOT NULL DEFAULT current_timestamp,
    text TEXT NOT NULL
);