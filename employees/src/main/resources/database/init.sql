DROP TABLE IF EXISTS tr_employees_accounts;
DROP TABLE IF EXISTS tz_employees;

CREATE TABLE tz_employees (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(60) NOT NULL,
    lastName VARCHAR(60) NOT NULL,
    inn VARCHAR(20) NOT NULL,
    employmentDate TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE tr_employees_accounts (
    id SERIAL PRIMARY KEY,
    id_employee INTEGER NOT NULL REFERENCES tz_employees (id) ON DELETE CASCADE,
    id_account INTEGER NOT NULL,
    UNIQUE (id_employee, id_account)
);