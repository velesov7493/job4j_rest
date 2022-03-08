INSERT INTO tz_roles (id, name, authority) VALUES
( 1, 'Администратор', 'ROLE_ADMIN'),
( 2, 'Модератор', 'ROLE_STAFF'),
( 3, 'Пользователь', 'ROLE_USER');

INSERT INTO tz_persons (login, password) VALUES
('admin', '$2a$10$L/PD8CD1zL4nUSpWAErF4O4YrdIgzJegdRQJ4EAwX9LjHEQ.FniVe'),
('moderator', '$2a$10$NYucCa6Klha/GNlDiOnpaeU/YNw93PSpWvPcCJecg91cwdvE0ctnW'),
('user', '$2a$10$cEQ.BPEGg/Oz8MZQ1nVQE.XhM8xnV/.XrYhjo.D.Vqky3XyiJe0CO'),
('chat-service', '$2a$10$h2p4jQLR4z.m2PcNO0Yl7.tH8/EBDWRouu3oFEjjPUNmUhlZW9AKi'),
('employees-service', '$2a$10$h2p4jQLR4z.m2PcNO0Yl7.tH8/EBDWRouu3oFEjjPUNmUhlZW9AKi');

INSERT INTO tr_roles_persons (id_person, id_role) VALUES
(1, 1), (1, 2), (1, 3),
(2, 2), (2, 3),
(3, 3),
(4, 1), (5, 1);