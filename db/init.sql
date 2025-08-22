-- Crear rol
-- Crear rol y base de datos
CREATE ROLE admin WITH LOGIN PASSWORD 'admin123' SUPERUSER;
CREATE DATABASE serverdb;
GRANT ALL PRIVILEGES ON DATABASE serverdb TO admin;