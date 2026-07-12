

DROP DATABASE IF EXISTS java_db;
CREATE DATABASE java_db;

CREATE SCHEMA IF NOT EXISTS my_java;
SET search_path TO my_java;


DROP TABLE IF EXISTS answers CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS tests CASCADE;


-- ============================================================
-- Создание таблиц с комментариями

CREATE TABLE tests (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE tests IS 'Таблица тестов';
COMMENT ON COLUMN tests.id IS 'Уникальный идентификатор теста (автоинкремент)';
COMMENT ON COLUMN tests.title IS 'Название теста';
COMMENT ON COLUMN tests.description IS 'Описание теста';
COMMENT ON COLUMN tests.created_at IS 'Дата создания теста (заполняется автоматически)';


CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    test_id INTEGER NOT NULL REFERENCES tests(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    order_num INTEGER NOT NULL,
    UNIQUE (test_id, order_num)
);
COMMENT ON TABLE questions IS 'Таблица вопросов';
COMMENT ON COLUMN questions.id IS 'Уникальный идентификатор вопроса (автоинкремент)';
COMMENT ON COLUMN questions.test_id IS 'Ссылка на тест';
COMMENT ON COLUMN questions.question_text IS 'Текст вопроса';
COMMENT ON COLUMN questions.order_num IS 'Порядковый номер вопроса в тесте';


CREATE TABLE answers (
    id SERIAL PRIMARY KEY,
    question_id INTEGER NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    answer_text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    order_num INTEGER NOT NULL,
    UNIQUE (question_id, order_num)
);
COMMENT ON TABLE answers IS 'Таблица вариантов ответов';
COMMENT ON COLUMN answers.id IS 'Уникальный идентификатор ответа (автоинкремент)';
COMMENT ON COLUMN answers.question_id IS 'Ссылка на вопрос';
COMMENT ON COLUMN answers.answer_text IS 'Текст варианта ответа';
COMMENT ON COLUMN answers.is_correct IS 'Флаг правильности ответа (true - правильный)';
COMMENT ON COLUMN answers.order_num IS 'Порядковый номер варианта ответа';

-- Частичный уникальный индекс – гарантирует, что у вопроса ровно один правильный ответ
CREATE UNIQUE INDEX idx_unique_correct_answer ON answers (question_id) WHERE is_correct = true;



-- ============================================================
-- Заполнение синтетическими данными

-- Тесты (названия и описания)
INSERT INTO tests (title, description) VALUES
('ООП и интерфейсы', 'Вопросы по основам ООП, инкапсуляции, интерфейсам и абстракции'),
('Коллекции и перечисления', 'Вопросы о коллекциях, списках и перечислимых типах (Enum)'),
('Многопоточность и переменные', 'Вопросы о потоках, синхронизации и типах переменных');

-- Вопросы (test_id определяется по названию теста)
INSERT INTO questions (test_id, question_text, order_num)
SELECT
    (SELECT id FROM tests WHERE title = 'ООП и интерфейсы'),
    q_text,
    ord
FROM (VALUES
    ('Что такое инкапсуляция?', 1),
    ('Какой принцип ООП позволяет использовать объекты с одинаковым интерфейсом?', 2),
    ('Что такое интерфейс в Java?', 3),
    ('Какое ключевое слово используется для наследования классов?', 4),
    ('Что такое абстрактный класс?', 5)
) AS v(q_text, ord)
UNION ALL
SELECT
    (SELECT id FROM tests WHERE title = 'Коллекции и перечисления'),
    q_text,
    ord
FROM (VALUES
    ('Какой класс реализует динамический массив (список)?', 1),
    ('Какая коллекция не допускает дубликатов?', 2),
    ('Какая структура данных хранит пары ключ-значение?', 3),
    ('Что такое Enum в Java?', 4),
    ('Какой метод добавляет элемент в конец списка (List)?', 5)
) AS v(q_text, ord)
UNION ALL
SELECT
    (SELECT id FROM tests WHERE title = 'Многопоточность и переменные'),
    q_text,
    ord
FROM (VALUES
    ('Какое ключевое слово используется для синхронизации метода?', 1),
    ('Что делает ключевое слово volatile?', 2),
    ('Какой тип переменной хранит значение по умолчанию null для объектов?', 3),
    ('Как создать поток в Java?', 4),
    ('Что такое deadlock?', 5)
) AS v(q_text, ord);

-- Ответы (по 4 варианта, правильный – первый в каждом блоке)
INSERT INTO answers (question_id, answer_text, is_correct, order_num)
-- Ответы для вопросов теста "ООП и интерфейсы"
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'ООП и интерфейсы') AND order_num = 1),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('Механизм скрытия внутренней реализации и предоставления доступа через методы', true, 1),
    ('Возможность наследовать поля и методы', false, 2),
    ('Способность объекта принимать разные формы', false, 3),
    ('Создание абстрактных сущностей', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'ООП и интерфейсы') AND order_num = 2),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('Полиморфизм', true, 1),
    ('Наследование', false, 2),
    ('Инкапсуляция', false, 3),
    ('Абстракция', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'ООП и интерфейсы') AND order_num = 3),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('Контракт, который класс обязуется реализовать', true, 1),
    ('Абстрактный класс без методов', false, 2),
    ('Суперкласс для всех объектов', false, 3),
    ('Специальная аннотация', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'ООП и интерфейсы') AND order_num = 4),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('extends', true, 1),
    ('implements', false, 2),
    ('inherits', false, 3),
    ('super', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'ООП и интерфейсы') AND order_num = 5),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('Класс, который не может быть инстанциирован (создан экземпляр)', true, 1),
    ('Класс, содержащий только абстрактные методы', false, 2),
    ('Класс, от которого нельзя наследовать', false, 3),
    ('Класс с приватным конструктором', false, 4)
) AS v(ans_text, is_corr, ord)

UNION ALL

-- Ответы для вопросов теста "Коллекции и перечисления"
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'Коллекции и перечисления') AND order_num = 1),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('ArrayList', true, 1),
    ('LinkedList', false, 2),
    ('Vector', false, 3),
    ('Stack', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'Коллекции и перечисления') AND order_num = 2),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('HashSet', true, 1),
    ('ArrayList', false, 2),
    ('HashMap', false, 3),
    ('TreeSet', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'Коллекции и перечисления') AND order_num = 3),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('HashMap', true, 1),
    ('ArrayList', false, 2),
    ('HashSet', false, 3),
    ('LinkedList', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'Коллекции и перечисления') AND order_num = 4),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('Перечисление (набор констант)', true, 1),
    ('Класс для хранения чисел', false, 2),
    ('Интерфейс', false, 3),
    ('Аннотация', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'Коллекции и перечисления') AND order_num = 5),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('add()', true, 1),
    ('push()', false, 2),
    ('put()', false, 3),
    ('insert()', false, 4)
) AS v(ans_text, is_corr, ord)

UNION ALL

-- Ответы для вопросов теста "Многопоточность и переменные"
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'Многопоточность и переменные') AND order_num = 1),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('synchronized', true, 1),
    ('volatile', false, 2),
    ('transient', false, 3),
    ('native', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'Многопоточность и переменные') AND order_num = 2),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('Гарантирует видимость изменений переменной между потоками', true, 1),
    ('Блокирует доступ к объекту', false, 2),
    ('Создаёт локальную копию переменной', false, 3),
    ('Отключает сериализацию', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'Многопоточность и переменные') AND order_num = 3),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('Ссылочный (reference)', true, 1),
    ('Примитивный', false, 2),
    ('Логический', false, 3),
    ('Символьный', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'Многопоточность и переменные') AND order_num = 4),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('extends Thread или implements Runnable', true, 1),
    ('implements Thread', false, 2),
    ('extends Runnable', false, 3),
    ('с помощью new Thread() без реализации', false, 4)
) AS v(ans_text, is_corr, ord)
UNION ALL
SELECT
    (SELECT id FROM questions WHERE test_id = (SELECT id FROM tests WHERE title = 'Многопоточность и переменные') AND order_num = 5),
    ans_text,
    is_corr,
    ord
FROM (VALUES
    ('Взаимная блокировка (deadlock) потоков', true, 1),
    ('Ошибка компиляции', false, 2),
    ('Исключение времени выполнения', false, 3),
    ('Переполнение стека', false, 4)
) AS v(ans_text, is_corr, ord);


-- ============================================================
-- Проверка работоспособности структуры (вывод статистики)

-- Количество вопросов по тестам
SELECT t.title, COUNT(q.id) AS question_count
FROM tests t
LEFT JOIN questions q ON t.id = q.test_id
GROUP BY t.title
ORDER BY t.id;

---- Правильные ответы на вопросы:
SELECT q.question_text, a.answer_text AS correct_answer
FROM questions q
JOIN answers a ON q.id = a.question_id
WHERE a.is_correct = true
ORDER BY q.test_id, q.order_num;