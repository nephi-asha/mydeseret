package com.mydeseret.mydeseret.model.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mydeseret.mydeseret.model.enums.ApplicationPermission.*;

public enum DefaultRole {

    SUPER_ADMIN(
            Stream.of(ApplicationPermission.values()).collect(Collectors.toSet())),

    OWNER(
            Stream.of(ApplicationPermission.values())
                    .filter(p -> !p.name().startsWith("BUSINESS_"))
                    .collect(Collectors.toSet())),

    WAREHOUSE_MANAGER(
            Set.of(
                    CATEGORY_CREATE, CATEGORY_READ, CATEGORY_UPDATE, CATEGORY_DELETE,
                    ITEM_CREATE, ITEM_READ, ITEM_UPDATE, ITEM_DELETE,
                    INVENTORY_ADJUST,
                    SUPPLIER_READ,
                    PURCHASE_ORDER_RECEIVE,
                    REQUISITION_CREATE,
                    BLUEPRINT_READ)),

    SALES_MANAGER(
            Set.of(
                    SALE_CREATE, SALE_READ, SALE_UPDATE, SALE_DELETE,
                    CUSTOMER_CREATE, CUSTOMER_READ, CUSTOMER_UPDATE, CUSTOMER_DELETE,
                    ITEM_READ)),

    HR_MANAGER(
            Set.of(
                    EMPLOYEE_CREATE, EMPLOYEE_READ, EMPLOYEE_UPDATE, EMPLOYEE_DELETE,
                    PAYROLL_GENERATE, PAYROLL_READ, PAYROLL_PROCESS,
                    TASK_CREATE, TASK_READ, TASK_UPDATE, TASK_DELETE,
                    REQUISITION_APPROVE,
                    REQUISITION_READ)),

    ACCOUNTANT(
            Set.of(
                    EXPENSE_CREATE, EXPENSE_READ, EXPENSE_UPDATE, EXPENSE_DELETE,
                    FINANCIAL_REPORT_READ,
                    PURCHASE_ORDER_PAY,
                    PURCHASE_ORDER_READ,
                    PAYROLL_READ,
                    SALE_READ,
                    SUPPLIER_READ)),

    PRODUCTION_MANAGER(
            Set.of(
                    BLUEPRINT_CREATE, BLUEPRINT_READ, BLUEPRINT_UPDATE, BLUEPRINT_DELETE,
                    MANUFACTURING_BUILD,
                    ITEM_READ,
                    INVENTORY_ADJUST,
                    REQUISITION_CREATE)),

    PROCUREMENT_OFFICER(
            Set.of(
                    SUPPLIER_CREATE, SUPPLIER_READ, SUPPLIER_UPDATE, SUPPLIER_DELETE,
                    PURCHASE_ORDER_CREATE, PURCHASE_ORDER_READ, PURCHASE_ORDER_UPDATE, PURCHASE_ORDER_DELETE,
                    ITEM_READ,
                    REQUISITION_READ)),

    CASHIER(
            Set.of(
                    SALE_CREATE, SALE_READ,
                    CUSTOMER_CREATE, CUSTOMER_READ,
                    ITEM_READ)),

    AUDITOR(
            Set.of(
                    CATEGORY_READ, ITEM_READ,
                    SUPPLIER_READ, PURCHASE_ORDER_READ,
                    EXPENSE_READ, FINANCIAL_REPORT_READ,
                    BLUEPRINT_READ,
                    CUSTOMER_READ, SALE_READ,
                    EMPLOYEE_READ, PAYROLL_READ,
                    TASK_READ,
                    REQUISITION_READ,
                    LOG_READ,
                    USER_READ, ROLE_READ)),

    EMPLOYEE(
            Set.of(
                    TASK_READ, TASK_UPDATE,
                    REQUISITION_CREATE,
                    ITEM_READ,
                    USER_READ// To see colleagues?
            ));

    private final Set<ApplicationPermission> permissions;

    DefaultRole(Set<ApplicationPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationPermission> getPermissions() {
        return permissions;
    }
}
