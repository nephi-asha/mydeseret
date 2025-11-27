-- 1. CRM & SRM 
CREATE TABLE IF NOT EXISTS customers (
    customer_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    address VARCHAR(255),
    credit_limit NUMERIC(19, 2) DEFAULT 1000.00,
    current_debt NUMERIC(19, 2) DEFAULT 0.00,
    created_at DATE DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20),
    address VARCHAR(255),
    created_at DATE DEFAULT CURRENT_DATE
);

-- 2. UPDATE EXISTING TABLES (COGS & Links)
ALTER TABLE sales ADD COLUMN IF NOT EXISTS customer_id BIGINT REFERENCES customers(customer_id);
ALTER TABLE purchase_orders ADD COLUMN IF NOT EXISTS supplier_id BIGINT REFERENCES suppliers(supplier_id);

ALTER TABLE sale_items ADD COLUMN IF NOT EXISTS cost_price NUMERIC(19, 2) DEFAULT 0;

-- 3. EXPENSE AMORTIZATION
CREATE TABLE IF NOT EXISTS amortization_schedules (
    schedule_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    total_amount NUMERIC(19, 2),
    total_months INTEGER,
    start_date DATE,
    end_date DATE
);

ALTER TABLE expenses ADD COLUMN IF NOT EXISTS amortization_schedule_id BIGINT REFERENCES amortization_schedules(schedule_id);

-- 4. INTERNAL REQUISITIONS
CREATE TABLE IF NOT EXISTS requisitions (
    requisition_id BIGSERIAL PRIMARY KEY,
    requester_id BIGINT NOT NULL REFERENCES employees(employee_id),
    item_id BIGINT NOT NULL REFERENCES items(item_id),
    quantity INTEGER NOT NULL,
    reason VARCHAR(255),
    needed_by_date DATE,
    status VARCHAR(50),
    approver_id UUID, -- Links to public.users
    approval_date DATE,
    rejection_reason VARCHAR(255),
    created_at DATE DEFAULT CURRENT_DATE
);