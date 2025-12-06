CREATE TABLE activity_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID,
    action VARCHAR(255) NOT NULL,
    details TEXT,
    ip_address VARCHAR(255),
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE INDEX idx_activity_logs_user_id ON activity_logs(user_id);
CREATE INDEX idx_activity_logs_timestamp ON activity_logs(timestamp);
