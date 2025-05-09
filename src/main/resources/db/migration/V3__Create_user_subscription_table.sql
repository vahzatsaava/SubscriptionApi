CREATE TABLE user_subscription
(
    id                    VARCHAR(255) PRIMARY KEY,
    start_subscription_at DATE           NOT NULL,
    end_subscription_at   DATE,
    status                VARCHAR(20)    NOT NULL,
    user_id               BIGINT REFERENCES users (id) ON DELETE CASCADE,
    subscription_id       BIGINT REFERENCES subscription (id) ON DELETE CASCADE,
    CONSTRAINT unique_user_subscription UNIQUE (user_id, subscription_id)
);