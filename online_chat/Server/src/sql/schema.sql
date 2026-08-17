-- ============================================================
-- Схема базы данных для сетевого чата (PostgreSQL)
-- ============================================================

-- Создаём схему, если её нет (соответствует currentSchema в URL)
CREATE SCHEMA IF NOT EXISTS javadb;

-- Устанавливаем схему по умолчанию
SET search_path TO javadb;

-- ============================================================
-- Таблица ролей
-- ============================================================
CREATE TABLE IF NOT EXISTS roles (
     id SERIAL PRIMARY KEY,
     name VARCHAR(20) UNIQUE NOT NULL
    );

-- Начальные роли
INSERT INTO roles (name) VALUES ('USER'), ('ADMIN')
    ON CONFLICT (name) DO NOTHING;

-- ============================================================
-- Таблица пользователей
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
     id SERIAL PRIMARY KEY,
     email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rating INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- ============================================================
-- Связь пользователей и ролей
-- ============================================================
CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    role_id INT REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
    );

-- ============================================================
-- Таблица банов
-- ============================================================
CREATE TABLE IF NOT EXISTS bans (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    admin_id INT REFERENCES users(id) ON DELETE SET NULL,
    reason TEXT,
    ban_until TIMESTAMP,                    -- NULL = перманентный бан
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- ============================================================
-- Таблица комнат
-- ============================================================
CREATE TABLE IF NOT EXISTS rooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    owner_id INT REFERENCES users(id) ON DELETE CASCADE,
    password VARCHAR(100),                  -- NULL = без пароля
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- ============================================================
-- Участники комнат
-- ============================================================
CREATE TABLE IF NOT EXISTS room_members (
    room_id INT REFERENCES rooms(id) ON DELETE CASCADE,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (room_id, user_id)
    );

-- ============================================================
-- История сообщений комнат
-- ============================================================
CREATE TABLE IF NOT EXISTS room_messages (
     id SERIAL PRIMARY KEY,
     room_id INT REFERENCES rooms(id) ON DELETE CASCADE,
    user_id INT REFERENCES users(id) ON DELETE SET NULL,
    username VARCHAR(50),                   -- сохраняем ник на момент отправки
    message TEXT,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- ============================================================
-- Рейтинги (лайки/дизлайки)
-- ============================================================
CREATE TABLE IF NOT EXISTS ratings (
   id SERIAL PRIMARY KEY,
   from_user_id INT REFERENCES users(id) ON DELETE CASCADE,
    to_user_id INT REFERENCES users(id) ON DELETE CASCADE,
    value INT NOT NULL CHECK (value IN (-1, 1)),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (from_user_id, to_user_id)       -- один пользователь – одна оценка другому
    );

-- ============================================================
-- Таблица плохих слов (для фильтрации)
-- ============================================================
CREATE TABLE IF NOT EXISTS bad_words (
                                         word VARCHAR(50) PRIMARY KEY
    );

-- Начальный набор плохих слов (пример)
INSERT INTO bad_words (word) VALUES
                                 ('дурак'),
                                 ('идиот'),
                                 ('тупой'),
                                 ('придурок'),
                                 ('дебил'),
                                 ('кретин'),
                                 ('ублюдок'),
                                 ('сволочь'),
                                 ('гад'),
                                 ('мерзавец'),
                                 ('негодяй'),
                                 ('подонок'),
                                 ('животное'),
                                 ('скотина'),
                                 ('сука'),
                                 ('блядь'),
                                 ('хуй'),
                                 ('пизда'),
                                 ('ебать'),
                                 ('залупа')
    ON CONFLICT (word) DO NOTHING;

-- ============================================================
-- Индексы для ускорения запросов (опционально)
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_last_activity ON users(last_activity);
CREATE INDEX IF NOT EXISTS idx_bans_user_id ON bans(user_id);
CREATE INDEX IF NOT EXISTS idx_rooms_name ON rooms(name);
CREATE INDEX IF NOT EXISTS idx_rooms_last_activity ON rooms(last_activity);
CREATE INDEX IF NOT EXISTS idx_room_members_room_id ON room_members(room_id);
CREATE INDEX IF NOT EXISTS idx_room_messages_room_id ON room_messages(room_id);
CREATE INDEX IF NOT EXISTS idx_ratings_to_user ON ratings(to_user_id);
CREATE INDEX IF NOT EXISTS idx_ratings_from_user ON ratings(from_user_id);

-- ============================================================
-- Готово
-- ============================================================