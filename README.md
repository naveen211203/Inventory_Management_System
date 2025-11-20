<h1 align="center" style="border-bottom: none">
    <b>
        Inventory Management System
    </b><br>
</h1>
The Inventory Management System is a Java-based application built using JDBC and MySQL to simplify and automate the management of products, staff, and sales in small businesses. It ensures secure login, role-based access, accurate stock tracking, and detailed sales reports.

---

## Problem Statement
Many small businesses still manage their inventory manually, which often leads to miscalculations, missing records, inability to track stock movement, and lack of sales history. Staff management also becomes challenging without a controlled system.

Manual processes suffer from:
- No real-time stock monitoring  
- High chances of human error  
- Poor sales tracking  
- No user access control  
- Zero automation for low-stock alerts  

A digital Inventory Management System is needed to make the entire process efficient, secure, and reliable.

---

## About the Project
This project is a **console-based application** developed with **Java + JDBC + MySQL** that maintains product records, handles stock, supports staff accounts, and processes sales with real-time updates.

It supports **two user roles**:
- **Admin** (full access)
- **Staff** (limited access)

The system ensures smooth inventory operations with an easy-to-use menu-driven interface.

---

## The Problem It Solves
- **Incorrect manual stock calculations** are prevented through an automated database.  
- **Unauthorized operations** are eliminated with role-based login.  
- **Sales tracking issues** are handled by storing every sale with bill ID and timestamp.  
- **Low-stock situations** are alerted automatically to avoid product shortages.  
- **Staff management problems** are solved by allowing the admin to create and manage staff accounts.

---

## Features of the System

### User Roles & Access
- **Admin**
  - Add Products
  - View Products
  - Update Stock
  - Remove Products
  - Add Staff
  - View Staff
  - Remove Staff
  - Make Sales
  - View Sales Report
  - Logout / Exit

- **Staff**
  - View Products
  - Make Sales
  - Logout / Exit

### Functional Highlights
- Product addition with name, price, and quantity  
- Automatic stock update after each sale  
- Low stock warning when below safe limit  
- Unique bill ID for every sale  
- Timestamped sales logging  
- Viewable sales history  
- Secure login using stored user credentials  

---

## Technical Implementation
- The application is written entirely in **Java**.  
- Uses **JDBC** to connect with the MySQL database.  
- SQL operations implemented using **PreparedStatements** to avoid SQL injection.  
- Database contains three tables: `users`, `products`, `sales`.  
- Each operation (add, update, delete, sell, report) is handled by dedicated methods.  
- Sales are grouped under bill IDs for easier reporting.  

---

## Tech Stacks Used
`Java` , `MySQL` , `JDBC`

## How to Run Locally
1. Install **Java JDK** and **MySQL Server**.  
2. Create the `inventorydb` database and required tables.  
3. Insert the default admin user: `('admin', 'admin123', 'Admin')`.  
4. Open the project and update MySQL username/password in the code.  
5. Run **InventoryManagementSystem.java** to start the application.  
