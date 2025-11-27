# Document 1: Architecture & Design Specification

## 1\. System Overview

**MyDeseret ERP** is a cloud-native, multi-tenant Enterprise Resource Planning system designed for high-scalability and strict data isolation. It is built to manage complex business operations including Inventory, Manufacturing, Sales, Procurement, HR, and Finance within a single deployed instance that serves multiple distinct business clients.

## 2\. Core Architecture: Schema-Based Multi-Tenancy

### Why this pattern?

We implemented **Schema-per-Tenant** isolation rather than a simple "Discriminator Column" (e.g., `WHERE tenant_id = 1`).

  * **Real-World Scenario:** Imagine "Bakery A" and "Mechanic Shop B" both use your app. If the database had a single table for all sales, a developer's coding error could accidentally show the Mechanic's sales to the Bakery owner.
  * **Our Solution:** Every business gets its own **private database schema** (e.g., `bakery_inc`, `mechanic_shop`). It is physically impossible for one tenant to query another’s data because their connection is restricted to their specific schema at the database level.

### Key Components

  * **`TenantContext`**: A ThreadLocal storage that holds the schema name for the *current* HTTP request only.
  * **`SchemaMultiTenantConnectionProvider`**: This intercepts every database call. Before running SQL, it executes `SET SEARCH_PATH TO tenant_schema`, forcing the database to look only in that specific business's "room."
  * **`Flyway` (Migration)**: We use programmatic Flyway migration to ensure that when a new business signs up, their private schema is automatically created and populated with the latest table structures (`items`, `sales`, etc.).

## 3\. Security Module

### Stateless Authentication (JWT)

  * **Implementation:** We use **JSON Web Tokens (JWT)**. The server does not store "sessions" in memory, allowing it to scale horizontally across multiple AWS servers without synchronization issues.
  * **Token Blacklisting:**
      * **Problem:** Standard JWTs cannot be "logged out" before they expire. If an employee is fired, their token is still valid for 10 hours.
      * **Solution:** We implemented a `TokenBlackListService`. When a user logs out, their token ID is saved to the database. The security filter checks this list on every request, allowing for immediate access revocation.

## 4\. Manufacturing Module (Variance Tracking)

### The Problem

Standard inventory systems just deduct recipe amounts (e.g., "1 Cake = 1kg Flour"). They fail to account for real-world waste (spills, burnt batches).

### The Solution (`ManufacturingService`)

We implemented a **Variance Logic**:

  * **Standard Usage:** The system calculates the theoretical need based on the Blueprint.
  * **Actual Usage:** The API accepts an `actuals` list.
  * **Result:** The difference is logged as `PRODUCTION_WASTE` or `EFFICIENCY_GAIN`.
  * **Business Value:** The owner can see exactly how much money is lost to waste versus how much is used for actual product sales.

## 5\. Financial Module (Advanced Accounting)

### Expense Amortization

  * **Scenario:** A business pays **$12,000** rent in January for the whole year.
  * **The Problem:** If recorded simply, January shows a massive loss, and February shows huge profit. This distorts the financial health.
  * **Our Solution:** We created an `AmortizationSchedule` entity. The system splits the $12,000 into 12 separate `Expense` records dated for future months. The P\&L report then accurately reflects a $1,000 monthly cost.

### Cost of Goods Sold (COGS) Snapshotting

  * **Scenario:** You buy flour at $1.00/kg today. You bake bread. Next week, flour price rises to $1.50/kg.
  * **The Problem:** If you sell the "old" bread today, the profit should be calculated using the *old* cost ($1.00), not the current market price.
  * **Our Solution:** When a sale happens, the `SaleItem` entity creates a **Snapshot** of the `costPrice` at that exact moment. This ensures historical profit reports never change even if supplier prices fluctuate wildly.

-----

# Document 2: API Testing Guide & Dummy Data

**Prerequisite:** All requests (except Login/Register) require the header:
`Authorization: Bearer <YOUR_JWT_TOKEN>`

## Phase 1: Onboarding & Setup

### 1\. Register New Business

  * **Endpoint:** `POST /api/v1/users/register`
  * **Purpose:** Creates the Tenant in the `public` schema but keeps it inactive.
  * **Payload:**
    ```json
    {
      "userName": "bakery_owner",
      "first_name": "John",
      "last_name": "Doe",
      "email": "owner@bakery.com",
      "password": "password123",
      "business_name": "Downtown Bakery",
      "timeZone": "America/New_York"
    }
    ```

### 2\. Approve Business (As Super Admin)

  * **Login:** `POST /api/v1/users/login` (Use Super Admin credentials).
  * **Get Pending ID:** `GET /api/v1/admin/pending-owners`.
  * **Approve:** `PUT /api/v1/admin/approve/{userId}`.
      * *Action:* This triggers the creation of the private schema (`downtown_bakery`) and runs Flyway migrations to create all tables.

## Phase 2: Operational Setup (As Owner)

### 3\. Create Inventory Categories

  * **Endpoint:** `POST /api/v1/categories`
  * **Payload:**
    ```json
    {
      "name": "Raw Ingredients",
      "description": "Flour, Sugar, Yeast"
    }
    ```

### 4\. Define Items (Raw Materials)

  * **Endpoint:** `POST /api/v1/items`
  * **Payload:**
    ```json
    {
      "name": "White Flour",
      "sku": "RAW-FLR-001",
      "unit_of_measure": "KG",
      "cost_price": 1.50,
      "reorderPoint": 50,
      "category_id": 1
    }
    ```

### 5\. Define Items (Finished Goods)

  * **Payload:**
    ```json
    {
      "name": "Sourdough Loaf",
      "sku": "PRD-SRD-001",
      "unit_of_measure": "UNIT",
      "selling_price": 8.00,
      "category_id": 2
    }
    ```

### 6\. Create Manufacturing Blueprint (Recipe)

  * **Endpoint:** `POST /api/v1/manufacturing/BluePrints`
  * **Purpose:** Defines that "1 Loaf" needs "0.5kg Flour".
  * **Payload:**
    ```json
    {
      "name": "Standard Sourdough Recipe",
      "outputItemId": 2, 
      "outputQuantity": 1,
      "components": [
        { "inputItemId": 1, "quantity": 1 } 
      ]
    }
    ```
    *(Note: In this dummy data, 1 unit of Item 2 requires 1 unit of Item 1).*

## Phase 3: Day-to-Day Operations

### 7\. Procurement (Buying Stock)

  * **Create Supplier:** `POST /api/v1/suppliers` -\> `{"name": "Grain Corp"}`.
  * **Create PO:** `POST /api/v1/purchase-orders/create`.
    ```json
    {
      "supplierId": 1,
      "items": [ { "itemId": 1, "quantity": 500, "unitCost": 1.50 } ]
    }
    ```
  * **Receive Goods:** `PUT /api/v1/purchase-orders/{id}/receive`.
      * *Result:* Inventory of Flour increases by 500.

### 8\. Manufacturing (Making Products)

  * **Endpoint:** `POST /api/v1/manufacturing/build/{blueprintId}`
  * **Payload (With Waste):**
    ```json
    {
      "quantity": 10,
      "actuals": [
        { "inputItemId": 1, "actualQuantity": 12 } 
      ]
    }
    ```
      * *Result:* 10 Loaves added. 10kg Flour used (Standard). 2kg Flour logged as Waste.

### 9\. Sales (Selling to Customers)

  * **Endpoint:** `POST /api/v1/sales/create`
  * **Payload:**
    ```json
    {
      "paymentMethod": "CASH",
      "items": [
        { "itemId": 2, "quantity": 5 }
      ]
    }
    ```

### 10\. Financial Reporting

  * **Endpoint:** `GET /api/v1/reports/pnl`
  * **Result:**
      * **Revenue:** $40.00 (5 loaves \* $8.00)
      * **COGS:** $7.50 (5 loaves \* 1kg flour \* $1.50 cost)
      * **Gross Profit:** $32.50

-----

