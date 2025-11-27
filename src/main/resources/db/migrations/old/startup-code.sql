-- ==========================================
-- 1. CREATE TABLES (Public Schema)
-- ==========================================

-- ROLES
CREATE TABLE IF NOT EXISTS roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL UNIQUE,
    tenant_id BIGINT, -- Nullable for Global Roles
    created_at DATE DEFAULT CURRENT_DATE,
    updated_at DATE DEFAULT CURRENT_DATE
);

-- ROLE PERMISSIONS
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id INTEGER NOT NULL REFERENCES roles(role_id),
    permission VARCHAR(255) NOT NULL
);

-- TENANTS
CREATE TABLE IF NOT EXISTS tenants (
    tenant_id BIGSERIAL PRIMARY KEY,
    tenant_name VARCHAR(255) NOT NULL,
    schema_name VARCHAR(255) NOT NULL UNIQUE,
    user_id UUID,
    created_at DATE DEFAULT CURRENT_DATE,
    updated_at DATE DEFAULT CURRENT_DATE
);

-- USERS
CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    business_name VARCHAR(150) NOT NULL,
    is_active BOOLEAN DEFAULT FALSE,
    approval_token VARCHAR(255),
    tenant_id BIGINT REFERENCES tenants(tenant_id),
    created_at DATE DEFAULT CURRENT_DATE,
    updated_at DATE DEFAULT CURRENT_DATE
);

-- USER ROLES (Link User <-> Role)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID NOT NULL REFERENCES users(user_id),
    role_id INTEGER NOT NULL REFERENCES roles(role_id),
    PRIMARY KEY (user_id, role_id)
);

-- USER CUSTOM PERMISSIONS
CREATE TABLE IF NOT EXISTS user_custom_permissions (
    user_id UUID NOT NULL REFERENCES users(user_id),
    permission VARCHAR(255) NOT NULL
);

-- TOKEN BLACKLIST (For Logout)
CREATE TABLE IF NOT EXISTS token_black_list (
    token_black_list_id BIGSERIAL PRIMARY KEY,
    token VARCHAR(512) NOT NULL UNIQUE,
    blacklisted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- LOGS
CREATE TABLE IF NOT EXISTS logs (
    log_id BIGSERIAL PRIMARY KEY,
    level VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    created_at DATE DEFAULT CURRENT_DATE
);

ALTER TABLE tenants 
ADD CONSTRAINT fk_tenant_owner 
FOREIGN KEY (user_id) REFERENCES users(user_id);