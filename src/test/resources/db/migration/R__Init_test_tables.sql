-- Use this file to insert test data
-- ${flyway:timestamp}
DELETE FROM users;
INSERT INTO users (id, login, password, telegram_id, telegram_first_name, telegram_nickname)
    VALUES
        (1, 'test', 'aA123456', 1, 'test', 'test'),
        (2, 'test2', 'aA123456', 2, 'test2', 'test2');