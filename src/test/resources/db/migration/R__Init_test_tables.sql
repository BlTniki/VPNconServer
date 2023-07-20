-- Use this file to insert test data
-- ${flyway:timestamp}
DELETE FROM users;
INSERT INTO users (id, login, password, role, telegram_id, telegram_nickname)
    VALUES
        (101, 'telegramBot', '$2a$12$wZxtK/hdO9ylNN3BrvrB4uXPWF3JHukBEuTnbx2x60F62wvQTFnTO', 'ADMIN', null, null),
        (102, 'accountant', '$2a$12$OmT1v3R579qFuryy6Hms0e/DxjYP0Ad8Wi6lEdSpZRQ.GK2BVyREi', 'ADMIN', null, null),
        (103, 'test', 'aA123456', 'ACTIVATED_USER', 1, 'test'),
        (104, 'test2', 'aA123456', 'ACTIVATED_USER', 2, 'test2'),
        (105, 'testMeta', 'aA123456', 'DEACTIVATED_USER', 2, 'testMeta'); -- user for metacodes test

DELETE FROM hosts;
INSERT INTO hosts (id, name, ipaddress, port, host_internal_network_prefix, host_password, host_public_key)
    VALUES
        (101, 'test', '127.0.0.1', 1, '127.0.0.0', '123456', 'lolkek'),
        (102, 'test2', '127.0.0.1', 2, '127.0.0.0', '123456', 'lolkek'),
        (103, 'test3', '127.0.0.1', 5000, '127.0.0.0', '5543678', 'lolkek');

DELETE FROM peers;
INSERT INTO peers (id, peer_conf_name, peer_ip, peer_private_key, peer_public_key, is_activated, user_id, host_id)
    VALUES
        (101, 'test', '10.8.0.10', 'private', 'public', TRUE, 103, 101),
        (102, 'test2', '10.8.0.11', 'private', 'public', TRUE, 103, 101),
        (103, 'test3', '10.8.0.12', 'private', 'public', TRUE, 104, 101),
        (104, 'test', '10.8.0.10', 'private', 'public', FALSE, 103, 102),
        (105, 'test123', '10.8.0.14', 'private', 'public', TRUE, 103, 102);

DELETE FROM metacodes;
INSERT INTO metacodes (id, code, operation)
    VALUES
        (101, 'blabla1', 'UPDATE_ROLE_TO_ACTIVATED_CLOSE_USER'),
        (102, 'blabla2', 'UPDATE_ROLE_TO_ADMIN');