# Library Management System for Front Desk Employees

This project is a full-stack web application designed for the **front desk employees** of a library, with two types of users: **Regular Users** (front desk employees) and **Administrator Users**. Both users are required to log in using a username and password to access the system. The application supports various operations such as managing book information, buying and selling books, processing bills, and generating reports for employee activities.

## Features

### 1. User Roles: Regular User and Administrator

#### Regular User (Front Desk Employee):
- **Add/Update/View Book Information**:
  - Manage book details such as:
    - Book name
    - Published date
    - Author
    - Price
  - Keep book inventory up to date with the latest information.

- **Create/Update/Delete/View Book Details**:
  - Add new books to the inventory.
  - Edit or delete existing book records.
  - View detailed information about each book in the system.

- **Sell and Buy Books**:
  - Front desk employees can sell books directly to customers.
  - Buy books to restock the library's inventory.

- **Process Bills**:
  - Generate and process bills for book sales and purchases.

#### Administrator User:
- **CRUD on Employee Information**:
  - Manage employee records:
    - Create, read, update, and delete employee details such as names, roles, and contact information.

- **Generate Reports**:
  - Generate reports for a specified period containing the activities performed by each employee.

### 2. Data Management
- All data is stored securely in a database.
- The application follows the **Layers architectural pattern**, separating concerns for better scalability and maintainability:
  - **Presentation Layer**: Handles the user interface and user interactions.
  - **Service Layer**: Contains business logic for processing operations.
  - **Data Access Layer**: Manages communication with the database.

### 3. Input Validation
- All inputs are validated to ensure the accuracy and integrity of the data before it is submitted and saved in the database.
