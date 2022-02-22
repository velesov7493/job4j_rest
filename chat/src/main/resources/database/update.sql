INSERT INTO tz_roles (id, name, authority) VALUES
( 1, 'Администратор', 'ROLE_ADMIN'),
( 2, 'Модератор', 'ROLE_STAFF'),
( 3, 'Пользователь', 'ROLE_USER');

INSERT INTO tz_actors (id_role, id_account, name, email) VALUES
(1, 1, 'Власов Александр Сергеевич', 'velesov7493@yandex.ru');