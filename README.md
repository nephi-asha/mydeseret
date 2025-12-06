# MyDeseret Business Management System API

**MyDeseret** is an enterprise-grade, multi-tenant ERP (Enterprise Resource Planning) backend designed to streamline operations for businesses of all sizes. It features strict data isolation using a **Schema-per-Tenant** architecture, ensuring security and scalability for high-volume enterprise clients.

It provides a complete suite of modules including Inventory, Manufacturing, Sales (POS), Procurement, HR, Payroll, and Financial Reporting.

-----

## 🚀 Tech Stack

  * **Core Framework:** Java 25, Spring Boot 3.4
  * **Database:** PostgreSQL 17 (Schema-Based Multi-Tenancy)
  * **Security:** Spring Security 6, JWT (Stateless), RBAC, **2FA (Aerogear OTP)**
  * **Persistence:** Spring Data JPA (Hibernate 6), **Redis (Caching)**
  * **Messaging:** **RabbitMQ (Asynchronous Processing)**
  * **Resilience:** **Resilience4j (Circuit Breakers)**
  * **Migration:** Flyway (Automated Schema Provisioning)
  * **Documentation:** OpenAPI 3 (Swagger UI)
  * **Testing/Tools:** Maven, Lombok

-----

## 🏛️ Architecture: Schema-Based Multi-Tenancy

This application uses a **Schema-per-Tenant** strategy for maximum data security.

1.  **Public Schema (`public`):** Stores shared data like `Users`, `Tenants`, `Roles`, and `TokenBlacklist`.
2.  **Tenant Schemas (e.g., `bakery_inc`, `steel_corp`):** Every registered business gets its own isolated database schema containing their specific tables (`items`, `sales`, `employees`, etc.).
3.  **Dynamic Routing:** The application automatically switches the database connection context based on the logged-in user's JWT token.

-----

## 📦 Key Modules & Features

### 1\. Inventory & Warehousing

  * **Double-Entry Ledger:** Stock updates are tracked via `StockMove` records for full auditability.
  * **Category & Item Management:** Track SKUs, Costs, and Reorder Points.
  * **Automated Alerts:** Daily scheduler checks for low stock and emails managers.

### 2\. Manufacturing (Production)

  * **Blueprints (Recipes):** Define inputs (Raw Materials) needed to create outputs (Finished Goods).
  * **Variance Tracking:** Record "Actual Usage" vs "Standard Recipe" to track waste and efficiency losses.
  * **Automated Stock Adjustment:** Deducts raw materials and increments finished goods automatically upon build completion.

### 3\. Sales & CRM

  * **Point of Sale (POS):** Record sales, link to Customers, and handle multiple payment methods.
  * **Credit Limits:** Enforce credit limits for B2B customers before allowing "Credit" payment transactions.
  * **COGS Calculation:** Snapshots the Cost Price at the moment of sale for accurate Gross Profit reporting.

### 4\. Procurement & SRM

  * **Supplier Management:** Track supplier details.
  * **Purchase Orders:** Full lifecycle: Draft $\to$ Ordered $\to$ Received (Stock Increase) $\to$ Paid (Expense Created).
  * **Internal Requisitions:** Employee request system with Manager Approval workflow.

### 5\. Finance & Accounting

  * **Profit & Loss Reports:** Real-time P\&L generation calculating Revenue, COGS, Gross Profit, Expenses, and Net Profit.
  * **Expense Amortization:** Smart handling of large prepaid expenses (e.g., Rent) by spreading them across multiple months in the database.

### 6\. HR & Payroll

  * **Employee Management:** Hiring, Job Titles, Departments.
  * **Payroll Processing:** Monthly payroll generation and payment processing (linked to Expenses).
  * **Task Management:** Assign tasks to employees with priorities and due dates.

### 7\. Advanced Analytics & Reporting

  * **Real-Time Dashboards:** Track Top Selling Items, Revenue Trends, and Profitability.
  * **Asynchronous PDF Generation:** Generate complex reports (e.g., P&L) in the background using **RabbitMQ** to avoid blocking the UI.
  * **AI Integration:** (Optional) AI-driven insights for inventory optimization.

### 8\. Security & Compliance

  * **Two-Factor Authentication (2FA):** Secure user accounts with TOTP (Google Authenticator).
  * **Activity Logging:** Comprehensive audit trails for all critical actions.
  * **Rate Limiting:** Protect APIs from abuse using Redis-backed rate limiting.

-----

## 🛠️ Getting Started

### Prerequisites

  * Java 21 or higher (Java 25 recommended)
  * PostgreSQL 15+
  * Maven

### 1\. Database Setup

Create a PostgreSQL database. The application will handle the rest.

```sql
CREATE DATABASE mydeseret_db;
```

### 2\. Environment Variables

Create a `.env` file in the root directory (or set system environment variables). **Do not hardcode these in `application.properties`.**

```properties
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/mydeseret_db
DB_USERNAME=postgres
DB_PASSWORD=your_password

# JWT Security (Must be 256-bit Base64 encoded)
JWT_SECRET=YOUR_LONG_SECURE_BASE64_STRING

# Email Settings (For Notifications)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379

# RabbitMQ Configuration
SPRING_RABBITMQ_HOST=localhost
SPRING_RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest

# AWS S3 (For File Uploads)
AWS_ACCESS_KEY_ID=your_access_key
AWS_SECRET_ACCESS_KEY=your_secret_key
AWS_REGION=us-east-1
AWS_S3_BUCKET_NAME=your-bucket-name

# AI Integration
OPENAI_API_KEY=your_openai_key

# Super Admin Seeding (Required for first run)
SUPER_ADMIN_EMAIL=admin@mydeseret.com
SUPER_ADMIN_PASSWORD=secure_admin_password
# Optional Super Admin Overrides
# SUPER_ADMIN_FIRST_NAME=Nephi
# SUPER_ADMIN_LAST_NAME=Asha
# SUPER_ADMIN_USERNAME=ashanephi
```

### 3\. Installation

```bash
# Clone the repository
git clone https://github.com/your-repo/mydeseret.git

# Build the project
mvn clean package -DskipTests

# Run the application
java -jar target/mydeseret-0.0.1-SNAPSHOT.jar
```

-----

## 📚 API Documentation

Once the application is running, you can access the interactive API documentation (Swagger UI) at:

👉 **http://localhost:8080/swagger-ui.html**

### Authentication Flow

1.  **Register:** `POST /api/v1/users/register` (Creates a new Business Tenant).
2.  **Login:** `POST /api/v1/users/login` (Returns a Bearer Token).
3.  **Authorize:** Click the "Authorize" button in Swagger and paste your token.

-----

## 🚢 Deployment (AWS)

This application is optimized for deployment on AWS EC2 with an RDS PostgreSQL database.

### Service Configuration (Systemd)

Create `/etc/systemd/system/mydeseret.service`:

```ini
[Unit]
Description=MyDeseret API
After=syslog.target

[Service]
User=ubuntu
Environment="DB_URL=jdbc:postgresql://<RDS-ENDPOINT>:5432/mydeseret_db"
Environment="DB_USERNAME=postgres"
Environment="DB_PASSWORD=secure_password"
Environment="JWT_SECRET=secure_jwt_key"
Environment="MAIL_USERNAME=admin@mydeseret.com"
Environment="MAIL_PASSWORD=email_password"
ExecStart=/usr/bin/java -jar /home/ubuntu/app.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

### Update & Restart

To deploy a new version:

```bash
sudo systemctl restart mydeseret
```

-----

## 🔒 Security Features

  * **Stateless Authentication:** Fully RESTful API using JWT.
  * **Token Blacklisting:** Immediate logout capability by blacklisting tokens in Redis/DB.
  * **Method-Level Security:** `@PreAuthorize("hasAuthority('ITEM_CREATE')")` ensures granular permission checks on every endpoint.
  * **Password Hashing:** BCrypt encryption for all user passwords.

-----

## 🤝 Contribution

1.  Fork the repository.
2.  Create a feature branch (`git checkout -b feature/NewModule`).
3.  Commit your changes.
4.  Push to the branch.
5.  Open a Pull Request.