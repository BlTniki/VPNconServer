INSERT INTO subscriptions (role, price_in_rubles, allowed_active_peers_count, period) VALUES
    ('ADMIN', 1, 1000, '10 years'),
    ('ACTIVATED_CLOSE_USER', 50, 100, '1 months'),
    ('ACTIVATED_USER', 70, 1, '1 month'),
    ('ACTIVATED_USER', 100, 5, '1 month')
;