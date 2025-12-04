-- =======================================================
--  CORE BUSINESS TABLES
-- =======================================================

-- Categories
CREATE TABLE IF NOT EXISTS categories (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE
);
-- Audit
CREATE TABLE IF NOT EXISTS categories_aud (
    category_id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    name VARCHAR(255),
    description VARCHAR(255),
    is_active BOOLEAN,
    created_at DATE,
    PRIMARY KEY (category_id, rev)
);

-- Items (Inventory)
CREATE TABLE IF NOT EXISTS items (
    item_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    unit_of_measure VARCHAR(50) NOT NULL,
    cost_price NUMERIC(19, 2) DEFAULT 0,
    selling_price NUMERIC(19, 2) DEFAULT 0,
    quantity_on_hand INTEGER DEFAULT 0,
    reorder_point INTEGER DEFAULT 10,
    category_id BIGINT REFERENCES categories(category_id),
    image_key VARCHAR(255), 
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE,
    updated_at DATE DEFAULT CURRENT_DATE
);
-- Audit
CREATE TABLE IF NOT EXISTS items_aud (
    item_id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    name VARCHAR(255),
    sku VARCHAR(255),
    description VARCHAR(255),
    unit_of_measure VARCHAR(50),
    cost_price NUMERIC(19, 2),
    selling_price NUMERIC(19, 2),
    quantity_on_hand INTEGER,
    reorder_point INTEGER,
    category_id BIGINT,
    image_key VARCHAR(255),
    is_active BOOLEAN,
    created_at DATE,
    updated_at DATE,
    PRIMARY KEY (item_id, rev)
);

-- Stock Moves 
CREATE TABLE IF NOT EXISTS stock_moves (
    move_id BIGSERIAL PRIMARY KEY,
    item_id BIGINT NOT NULL REFERENCES items(item_id),
    quantity_change INTEGER NOT NULL,
    reason VARCHAR(50) NOT NULL,
    reference_id VARCHAR(255),
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customers (CRM)
CREATE TABLE IF NOT EXISTS customers (
    customer_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    address VARCHAR(255),
    credit_limit NUMERIC(19, 2) DEFAULT 1000.00,
    current_debt NUMERIC(19, 2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE
);
-- Audit
CREATE TABLE IF NOT EXISTS customers_aud (
    customer_id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    address VARCHAR(255),
    credit_limit NUMERIC(19, 2),
    current_debt NUMERIC(19, 2),
    is_active BOOLEAN,
    created_at DATE,
    PRIMARY KEY (customer_id, rev)
);

-- Suppliers (SRM)
CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    address VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE
);
-- Audit
CREATE TABLE IF NOT EXISTS suppliers_aud (
    supplier_id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    name VARCHAR(255),
    contact_person VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    address VARCHAR(255),
    is_active BOOLEAN,
    created_at DATE,
    PRIMARY KEY (supplier_id, rev)
);

-- Employees (HR)
CREATE TABLE IF NOT EXISTS employees (
    employee_id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL, 
    job_title VARCHAR(255),
    department VARCHAR(50),
    status VARCHAR(50),
    salary NUMERIC(19, 2),
    profile_image_key VARCHAR(255),
    hire_date DATE DEFAULT CURRENT_DATE
);
-- Audit
CREATE TABLE IF NOT EXISTS employees_aud (
    employee_id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    user_id UUID,
    job_title VARCHAR(255),
    department VARCHAR(50),
    status VARCHAR(50),
    salary NUMERIC(19, 2),
    profile_image_key VARCHAR(255),
    hire_date DATE,
    PRIMARY KEY (employee_id, rev)
);

-- Sales (POS)
CREATE TABLE IF NOT EXISTS sales (
    sale_id BIGSERIAL PRIMARY KEY,
    receipt_number VARCHAR(255) NOT NULL UNIQUE,
    customer_id BIGINT REFERENCES customers(customer_id),
    status VARCHAR(50),
    payment_method VARCHAR(50),
    total_amount NUMERIC(19, 2) DEFAULT 0,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Audit
CREATE TABLE IF NOT EXISTS sales_aud (
    sale_id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    receipt_number VARCHAR(255),
    customer_id BIGINT,
    status VARCHAR(50),
    payment_method VARCHAR(50),
    total_amount NUMERIC(19, 2),
    sale_date TIMESTAMP,
    PRIMARY KEY (sale_id, rev)
);

-- Sale Items
CREATE TABLE IF NOT EXISTS sale_items (
    sale_item_id BIGSERIAL PRIMARY KEY,
    sale_id BIGINT REFERENCES sales(sale_id),
    item_id BIGINT REFERENCES items(item_id),
    quantity INTEGER,
    unit_price NUMERIC(19, 2),
    cost_price NUMERIC(19, 2) DEFAULT 0,
    sub_total NUMERIC(19, 2)
);
-- Audit
CREATE TABLE IF NOT EXISTS sale_items_aud (
    sale_item_id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    sale_id BIGINT,
    item_id BIGINT,
    quantity INTEGER,
    unit_price NUMERIC(19, 2),
    cost_price NUMERIC(19, 2),
    sub_total NUMERIC(19, 2),
    PRIMARY KEY (sale_item_id, rev)
);

-- Purchase Orders (Procurement)
CREATE TABLE IF NOT EXISTS purchase_orders (
    purchase_order_id BIGSERIAL PRIMARY KEY,
    po_number VARCHAR(255) NOT NULL UNIQUE,
    supplier_id BIGINT REFERENCES suppliers(supplier_id),
    status VARCHAR(50),
    total_cost NUMERIC(19, 2) DEFAULT 0,
    created_date DATE DEFAULT CURRENT_DATE
);
-- Audit
CREATE TABLE IF NOT EXISTS purchase_orders_aud (
    purchase_order_id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    po_number VARCHAR(255),
    supplier_id BIGINT,
    status VARCHAR(50),
    total_cost NUMERIC(19, 2),
    created_date DATE,
    PRIMARY KEY (purchase_order_id, rev)
);

-- PO Items
CREATE TABLE IF NOT EXISTS purchase_order_items (
    purchase_order_item_id BIGSERIAL PRIMARY KEY,
    purchase_order_id BIGINT REFERENCES purchase_orders(purchase_order_id),
    item_id BIGINT REFERENCES items(item_id),
    quantity INTEGER,
    unit_cost NUMERIC(19, 2)
);
-- Audit
CREATE TABLE IF NOT EXISTS purchase_order_items_aud (
    purchase_order_item_id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    purchase_order_id BIGINT,
    item_id BIGINT,
    quantity INTEGER,
    unit_cost NUMERIC(19, 2),
    PRIMARY KEY (purchase_order_item_id, rev)
);

-- Amortization & Expenses (Finance)
CREATE TABLE IF NOT EXISTS amortization_schedules (
    schedule_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    total_amount NUMERIC(19, 2),
    total_months INTEGER,
    start_date DATE,
    end_date DATE
);

CREATE TABLE IF NOT EXISTS expenses (
    expense_id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255),
    amount NUMERIC(19, 2),
    expense_date DATE DEFAULT CURRENT_DATE,
    receipt_image_key VARCHAR(255),
    purchase_order_id BIGINT REFERENCES purchase_orders(purchase_order_id),
    amortization_schedule_id BIGINT REFERENCES amortization_schedules(schedule_id)
);

-- Payroll & Tasks
CREATE TABLE IF NOT EXISTS payrolls (
    payroll_id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(employee_id),
    pay_period DATE,
    base_salary NUMERIC(19, 2),
    bonuses NUMERIC(19, 2) DEFAULT 0,
    deductions NUMERIC(19, 2) DEFAULT 0,
    net_pay NUMERIC(19, 2),
    status VARCHAR(50),
    payment_date DATE
);

CREATE TABLE IF NOT EXISTS tasks (
    task_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(255),
    priority VARCHAR(50),
    status VARCHAR(50),
    due_date DATE,
    assignee_id BIGINT REFERENCES employees(employee_id),
    reporter_id UUID
);

-- Manufacturing
CREATE TABLE IF NOT EXISTS blueprints (
    blueprint_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    output_item_id BIGINT NOT NULL REFERENCES items(item_id),
    output_quantity INTEGER DEFAULT 1
);

CREATE TABLE IF NOT EXISTS blueprint_items (
    blueprint_item_id BIGSERIAL PRIMARY KEY,
    blueprint_id BIGINT REFERENCES blueprints(blueprint_id),
    input_item_id BIGINT REFERENCES items(item_id),
    quantity_needed INTEGER
);

-- Requisitions & Notifications
CREATE TABLE IF NOT EXISTS requisitions (
    requisition_id BIGSERIAL PRIMARY KEY,
    requester_id BIGINT NOT NULL REFERENCES employees(employee_id),
    item_id BIGINT NOT NULL REFERENCES items(item_id),
    quantity INTEGER NOT NULL,
    reason VARCHAR(255),
    needed_by_date DATE,
    status VARCHAR(50),
    approver_id UUID, 
    approval_date DATE,
    rejection_reason VARCHAR(255),
    created_at DATE DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL, 
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    link VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =======================================================
--  PERFORMANCE INDEXES
-- =======================================================
CREATE INDEX IF NOT EXISTS idx_sales_date_status ON sales (sale_date, status);
CREATE INDEX IF NOT EXISTS idx_sale_items_sale_id ON sale_items (sale_id);
CREATE INDEX IF NOT EXISTS idx_items_sku ON items (sku);
CREATE INDEX IF NOT EXISTS idx_stock_moves_item ON stock_moves (item_id);
CREATE INDEX IF NOT EXISTS idx_employees_user_id ON employees (user_id);
CREATE INDEX IF NOT EXISTS idx_purchase_orders_supplier ON purchase_orders (supplier_id);