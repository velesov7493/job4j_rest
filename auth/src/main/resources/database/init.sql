DROP TABLE IF EXISTS tr_roles_persons;
DROP TABLE IF EXISTS tz_persons;
DROP TABLE IF EXISTS tz_roles;

CREATE TABLE tz_roles (
    id INTEGER PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    authority VARCHAR(90) NOT NULL
);

CREATE TABLE tz_persons (
    id SERIAL PRIMARY KEY,
    login VARCHAR(60) NOT NULL UNIQUE,
    password VARCHAR(250) NOT NULL,
    enabled BOOLEAN DEFAULT true
);

CREATE TABLE tr_roles_persons (
    id_role INTEGER NOT NULL,
    id_person INTEGER NOT NULL,
    PRIMARY KEY (id_role, id_person)
);