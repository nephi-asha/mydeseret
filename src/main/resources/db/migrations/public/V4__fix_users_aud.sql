ALTER TABLE users_aud ADD COLUMN mfa_enabled BOOLEAN;
ALTER TABLE users_aud ADD COLUMN mfa_secret VARCHAR(255);