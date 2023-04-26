-- Use this file to insert test data
-- ${flyway:timestamp}
DELETE FROM users;
INSERT INTO users
    VALUES
        (1, 'test', 'aA123456'),
        (2, 'test2', 'aA123456');