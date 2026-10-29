# Employee Management REST API

A beginner-friendly CRUD REST API built with Java 21, Spring Boot 3.5.16, Spring Web, Spring Data JPA, Bean Validation, and MySQL.

## Features

- Create an employee
- Get all employees
- Get one employee by ID
- Update an employee
- Delete an employee
- Validate request fields
- Reject duplicate email addresses
- Consistent JSON error responses

## Requirements

- JDK 21
- MySQL Server running locally
- Maven 3.6.3 or later, or open the project in IntelliJ IDEA and use its Maven integration

## Setup

1. Extract the project ZIP and open the `employee-management` folder in IntelliJ IDEA.
2. Make sure the Project SDK is set to Java 21.
3. Ensure MySQL Server is running.
4. Open `src/main/resources/application.properties` and set the database username/password as required. The default values in this template are `root` / `root`.
5. Reload Maven dependencies.
6. Run `employee_managementApplication.java`.

The database `employee_management` is created automatically by the JDBC URL when the MySQL user has permission to create databases. Hibernate creates/updates the `employees` table from `Employee.java`.

### Run from a terminal

From the project root:

```bash
mvn spring-boot:run
```

Or build and run:

```bash
mvn clean package
java -jar target/employee-management-0.0.1-SNAPSHOT.jar
```

The API runs at `http://localhost:8080`.

## API endpoints

| Method | URL | Description | Success status |
|---|---|---|---|
| POST | `/api/employees` | Create an employee | 201 Created |
| GET | `/api/employees` | List all employees | 200 OK |
| GET | `/api/employees/{id}` | Get an employee | 200 OK |
| PUT | `/api/employees/{id}` | Replace an employee's editable fields | 200 OK |
| DELETE | `/api/employees/{id}` | Delete an employee | 204 No Content |

## Sample JSON body

```json
{
  "firstName": "Harshit",
  "lastName": "Jajoo",
  "email": "harshit@example.com",
  "department": "Engineering",
  "salary": 75000.00
}
```

Use this JSON as the body for POST and PUT requests, with `Content-Type: application/json`.

### Windows PowerShell examples

Create an employee:

```powershell
$body = @{
  firstName = "Harshit"
  lastName = "Jajoo"
  email = "harshit@example.com"
  department = "Engineering"
  salary = 75000.00
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/employees" -ContentType "application/json" -Body $body
```

List employees:

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/employees"
```

Get employee ID 1:

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/employees/1"
```

Update employee ID 1:

```powershell
$body = @{
  firstName = "Harshit"
  lastName = "Jajoo"
  email = "harshit@example.com"
  department = "Data Engineering"
  salary = 80000.00
} | ConvertTo-Json

Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/employees/1" -ContentType "application/json" -Body $body
```

Delete employee ID 1:

```powershell
Invoke-WebRequest -Method Delete -Uri "http://localhost:8080/api/employees/1"
```

## Error handling

- `400 Bad Request`: invalid field values or malformed JSON
- `404 Not Found`: employee ID does not exist
- `409 Conflict`: email address already belongs to another employee

## Notes

- `spring.jpa.hibernate.ddl-auto=update` is convenient for local learning. For production systems, use managed database migrations and a more restrictive schema strategy.
- Do not commit real passwords or credentials to GitHub. Set environment variables `DB_USERNAME` and `DB_PASSWORD`, or use a local untracked configuration file.
