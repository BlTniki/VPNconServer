-- SMS
CREATE TABLE IF NOT EXISTS roles (
    role VARCHAR(255) PRIMARY KEY NOT NULL
);

INSERT INTO roles VALUES
    ('ACTIVATED_USER'),
    ('ACTIVATED_CLOSE_USER'),
    ('ADMIN'),
    ('DEACTIVATED_USER')
;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    telegram_id BIGINT UNIQUE NOT NULL,
    username VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL REFERENCES roles (role)
);

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    role VARCHAR(255) NOT NULL REFERENCES roles (role),
    price_in_rubles MONEY NOT NULL,
    allowed_active_peers_count INT NOT NULL,
    duration INTERVAL NOT NULL
);

CREATE TABLE IF NOT EXISTS users_subscriptions (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users (telegram_id),
    subscription_id BIGINT NOT NULL REFERENCES subscriptions (id),
    expiration_date DATE NOT NULL
);


-- CMS
CREATE TABLE IF NOT EXISTS peer_statuses (
    peer_status VARCHAR(255) PRIMARY KEY NOT NULL
);

INSERT INTO peer_statuses VALUES
    ('PENDING'),
    ('ACTIVE'),
    ('INACTIVE_MANUAL'),
    ('INACTIVE_BURNED')
;

CREATE TABLE IF NOT EXISTS hosts (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    ipaddress CIDR NOT NULL,
    port INT,
    host_password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS peers (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    peer_status VARCHAR(255) NOT NULL REFERENCES peer_statuses(peer_status),
    owner_id BIGINT NOT NULL UNIQUE REFERENCES users (telegram_id),
    host_id BIGINT NOT NULL UNIQUE REFERENCES hosts (id),

    CONSTRAINT peers_name_owner_id_key UNIQUE(name, owner_id)
);
