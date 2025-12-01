-- PRIVATE BUSINESS TABLES

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE
);

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
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE,
    updated_at DATE DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS stock_moves (
    move_id BIGSERIAL PRIMARY KEY,
    item_id BIGINT NOT NULL REFERENCES items(item_id),
    quantity_change INTEGER NOT NULL,
    reason VARCHAR(50) NOT NULL,
    reference_id VARCHAR(255),
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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

CREATE TABLE IF NOT EXISTS sales (
    sale_id BIGSERIAL PRIMARY KEY,
    receipt_number VARCHAR(255) NOT NULL UNIQUE,
    customer_name VARCHAR(255),
    status VARCHAR(50),
    payment_method VARCHAR(50),
    total_amount NUMERIC(19, 2) DEFAULT 0,
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sale_items (
    sale_item_id BIGSERIAL PRIMARY KEY,
    sale_id BIGINT REFERENCES sales(sale_id),
    item_id BIGINT REFERENCES items(item_id),
    quantity INTEGER,
    unit_price NUMERIC(19, 2),
    sub_total NUMERIC(19, 2)
);

CREATE TABLE IF NOT EXISTS purchase_orders (
    purchase_order_id BIGSERIAL PRIMARY KEY,
    po_number VARCHAR(255) NOT NULL UNIQUE,
    vendor_name VARCHAR(255),
    status VARCHAR(50),
    total_cost NUMERIC(19, 2) DEFAULT 0,
    created_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS purchase_order_items (
    purchase_order_item_id BIGSERIAL PRIMARY KEY,
    purchase_order_id BIGINT REFERENCES purchase_orders(purchase_order_id),
    item_id BIGINT REFERENCES items(item_id),
    quantity INTEGER,
    unit_cost NUMERIC(19, 2)
);

CREATE TABLE IF NOT EXISTS expenses (
    expense_id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255),
    amount NUMERIC(19, 2),
    expense_date DATE DEFAULT CURRENT_DATE,
    purchase_order_id BIGINT REFERENCES purchase_orders(purchase_order_id)
);

CREATE TABLE IF NOT EXISTS employees (
    employee_id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL, 
    job_title VARCHAR(255),
    department VARCHAR(50),
    status VARCHAR(50),
    salary NUMERIC(19, 2),
    hire_date DATE DEFAULT CURRENT_DATE
);

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

CREATE TABLE IF NOT EXISTS customers (
    customer_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    address VARCHAR(255),
    credit_limit NUMERIC(19, 2) DEFAULT 1000.00,
    current_debt NUMERIC(19, 2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20),
    address VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE
);


ALTER TABLE sales ADD COLUMN IF NOT EXISTS customer_id BIGINT REFERENCES customers(customer_id);
ALTER TABLE purchase_orders ADD COLUMN IF NOT EXISTS supplier_id BIGINT REFERENCES suppliers(supplier_id);

ALTER TABLE sale_items ADD COLUMN IF NOT EXISTS cost_price NUMERIC(19, 2) DEFAULT 0;


CREATE TABLE IF NOT EXISTS amortization_schedules (
    schedule_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    total_amount NUMERIC(19, 2),
    total_months INTEGER,
    start_date DATE,
    end_date DATE
);

ALTER TABLE expenses ADD COLUMN IF NOT EXISTS amortization_schedule_id BIGINT REFERENCES amortization_schedules(schedule_id);

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