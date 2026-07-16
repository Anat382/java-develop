-- Создание схемы и установка пути поиска
CREATE SCHEMA IF NOT EXISTS otus_java;
SET search_path TO otus_java;

-- Удаление таблиц (для чистоты, если пересоздаём)
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- Таблица пользователей
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Таблица ролей
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Связующая таблица (многие-ко-многим)
CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Добавление ролей (больше ролей для демонстрации)
INSERT INTO roles (name) VALUES 
    ('ADMIN'), 
    ('USER'), 
    ('MANAGER'),
    ('GUEST'),
    ('MODERATOR');

-- Добавление пользователей (больше записей)
INSERT INTO users (email, password) VALUES
    ('alice@example.com', 'alice_pass'),
    ('bob@example.com', 'bob_pass'),
    ('charlie@example.com', 'charlie_pass'),
    ('diana@example.com', 'diana_pass'),
    ('eve@example.com', 'eve_pass');

-- Назначение ролей
-- Alice – ADMIN, USER, MODERATOR
INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 1), (1, 2), (1, 5);
-- Bob – USER, GUEST
INSERT INTO user_roles (user_id, role_id) VALUES
    (2, 2), (2, 4);
-- Charlie – MANAGER, USER
INSERT INTO user_roles (user_id, role_id) VALUES
    (3, 3), (3, 2);
-- Diana – ADMIN, MANAGER
INSERT INTO user_roles (user_id, role_id) VALUES
    (4, 1), (4, 3);
-- Eve – GUEST
INSERT INTO user_roles (user_id, role_id) VALUES
    (5, 4);

select * from users