CREATE TABLE payments (
    uuid VARCHAR PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,
    to_pay MONEY NOT NULL,
    time_stamp TIMESTAMP NOT NULL,
    status VARCHAR NOT NULL
);