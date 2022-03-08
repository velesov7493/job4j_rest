DROP TABLE IF EXISTS tr_chatmessages;
DROP TABLE IF EXISTS tz_chatrooms;

/* Таблицы БД */

CREATE TABLE tz_chatrooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(60) NOT NULL
);

CREATE TABLE tr_chatmessages (
    id BIGSERIAL PRIMARY KEY,
    id_chat INTEGER NOT NULL REFERENCES tz_chatrooms (id) ON DELETE CASCADE,
    id_author INTEGER NOT NULL,
    time TIMESTAMP NOT NULL DEFAULT current_timestamp,
    text TEXT NOT NULL
);