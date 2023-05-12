-- Use this file to insert test data
-- ${flyway:timestamp}
DELETE FROM users;
INSERT INTO users (id, login, password, telegram_id, telegram_first_name, telegram_nickname)
    VALUES
        (1, 'test', 'aA123456', 1, 'test', 'test'),
        (2, 'test2', 'aA123456', 2, 'test2', 'test2');

DELETE FROM hosts;
INSERT INTO hosts (id, name, ipaddress, port, host_internal_network_prefix, host_password, host_public_key)
    VALUES
        (1, 'test', '127.0.0.1', 1, '127.0.0.0', '123456', 'lolkek'),
        (2, 'test2', '127.0.0.1', 2, '127.0.0.0', '123456', 'lolkek'),
        (3, 'test3', '127.0.0.1', 5000, '127.0.0.0', '5543678', 'lolkek');

DELETE FROM peers;
INSERT INTO peers (id, peer_conf_name, peer_ip, peer_private_key, peer_public_key, is_activated, user_id, host_id)
    VALUES
        (1, 'test', '10.8.0.10', 'private', 'public', TRUE, 1, 1),
        (2, 'test2', '10.8.0.11', 'private', 'public', TRUE, 1, 1),
        (3, 'test3', '10.8.0.12', 'private', 'public', TRUE, 2, 1),
        (4, 'test', '10.8.0.10', 'private', 'public', TRUE, 1, 2);