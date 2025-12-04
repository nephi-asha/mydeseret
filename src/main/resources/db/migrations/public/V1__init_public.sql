-- Enable Vector Extension (For AI)
CREATE EXTENSION IF NOT EXISTS vector;

-- nvers Revision Info (Global Audit Log)
CREATE SEQUENCE IF NOT EXISTS revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS revinfo (
    rev INTEGER NOT NULL PRIMARY KEY,
    revtstmp BIGINT,
    user_id VARCHAR(255)
);

-- Tenants (Businesses)
CREATE TABLE IF NOT EXISTS tenants (
    tenant_id BIGSERIAL PRIMARY KEY,
    tenant_name VARCHAR(255) NOT NULL,
    schema_name VARCHAR(255) NOT NULL UNIQUE,
    user_id UUID, -- Owner Link
    time_zone VARCHAR(50) DEFAULT 'UTC',
    
    -- Subscription Fields
    stripe_customer_id VARCHAR(255),
    subscription_plan VARCHAR(50) DEFAULT 'FREE',
    subscription_status VARCHAR(50) DEFAULT 'ACTIVE',
    current_period_end DATE,

    created_at DATE DEFAULT CURRENT_DATE,
    updated_at DATE DEFAULT CURRENT_DATE
);

-- Tenant Audit Table
CREATE TABLE IF NOT EXISTS tenants_aud (
    tenant_id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    tenant_name VARCHAR(255),
    schema_name VARCHAR(255),
    user_id UUID,
    time_zone VARCHAR(50),
    stripe_customer_id VARCHAR(255),
    subscription_plan VARCHAR(50),
    subscription_status VARCHAR(50),
    current_period_end DATE,
    created_at DATE,
    updated_at DATE,
    PRIMARY KEY (tenant_id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

-- Users
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
    
    -- Password Recovery
    reset_token VARCHAR(255),
    reset_token_expiry TIMESTAMP,

    tenant_id BIGINT REFERENCES tenants(tenant_id),
    created_at DATE DEFAULT CURRENT_DATE,
    updated_at DATE DEFAULT CURRENT_DATE
);

-- User Audit Table
CREATE TABLE IF NOT EXISTS users_aud (
    user_id UUID NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    username VARCHAR(100),
    email VARCHAR(255),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    password_hash VARCHAR(255),
    business_name VARCHAR(150),
    is_active BOOLEAN,
    approval_token VARCHAR(255),
    reset_token VARCHAR(255),
    reset_token_expiry TIMESTAMP,
    tenant_id BIGINT,
    created_at DATE,
    updated_at DATE,
    PRIMARY KEY (user_id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

-- Roles
CREATE TABLE IF NOT EXISTS roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL UNIQUE,
    tenant_id BIGINT,
    created_at DATE DEFAULT CURRENT_DATE,
    updated_at DATE DEFAULT CURRENT_DATE
);

-- Role Audit Table
CREATE TABLE IF NOT EXISTS roles_aud (
    role_id INTEGER NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    role_name VARCHAR(255),
    tenant_id BIGINT,
    created_at DATE,
    updated_at DATE,
    PRIMARY KEY (role_id, rev),
    FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

-- Role Permissions & Audit
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id INTEGER NOT NULL REFERENCES roles(role_id),
    permission VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS role_permissions_aud (
    role_id INTEGER NOT NULL,
    rev INTEGER NOT NULL,
    permission VARCHAR(255),
    revtype SMALLINT,
    PRIMARY KEY (role_id, rev, permission),
    FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

-- User Roles Link & Audit
CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID NOT NULL REFERENCES users(user_id),
    role_id INTEGER NOT NULL REFERENCES roles(role_id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS user_roles_aud (
    rev INTEGER NOT NULL,
    user_id UUID NOT NULL,
    role_id INTEGER NOT NULL,
    revtype SMALLINT,
    PRIMARY KEY (rev, user_id, role_id),
    FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

-- User Custom Permissions & Audit
CREATE TABLE IF NOT EXISTS user_custom_permissions (
    user_id UUID NOT NULL REFERENCES users(user_id),
    permission VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_custom_permissions_aud (
    user_id UUID NOT NULL,
    rev INTEGER NOT NULL,
    permission VARCHAR(255),
    revtype SMALLINT,
    PRIMARY KEY (user_id, rev, permission),
    FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

-- Security & Logs
CREATE TABLE IF NOT EXISTS token_black_list (
    token_black_list_id BIGSERIAL PRIMARY KEY,
    token VARCHAR(512) NOT NULL UNIQUE,
    blacklisted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logs (
    log_id BIGSERIAL PRIMARY KEY,
    level VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    created_at DATE DEFAULT CURRENT_DATE
);

-- Circular FK Fix for Tenants -> Users
ALTER TABLE tenants DROP CONSTRAINT IF EXISTS fk_tenant_owner;
ALTER TABLE tenants ADD CONSTRAINT fk_tenant_owner FOREIGN KEY (user_id) REFERENCES users(user_id);