-- Insertar roles
INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');
-- Insertar usuario admin (password encriptada con bcrypt)
INSERT INTO users (username, password)
VALUES ('admin', '$2a$12$TVDk2mfCBz5MVshoThgOxez7RChcW3hiBZdJPEtUuqJA26VaHqDa2');
-- Asignar rol admin al usuario admin
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);