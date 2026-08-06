# Spring Boot Layered Architecture Documentation

## Overview

This project uses the standard layered architecture:

```text
Controller → Service → Repository → Database
```

This design separates application responsibilities into independent layers,
improving maintainability, testability, and scalability.

The architecture is commonly known as:

- **Layered Architecture**
- **Controller-Service-Repository Pattern**
- **Three-Tier Architecture (with persistence layer)**

---

## Architecture Flow

```text
+----------------+
|     Client     |
| (Web / Mobile) |
+-------+--------+
        |
        v
+----------------+
|  Controller    |
|  Layer         |
|                |
| - REST API     |
| - Validation   |
| - Request DTO  |
+-------+--------+
        |
        v
+----------------+
|  Service       |
|  Layer         |
|                |
| - Business     |
|   Logic        |
| - Transactions |
| - Rules        |
+-------+--------+
        |
        v
+----------------+
|  Repository    |
|  Layer         |
|                |
| - Data Access  |
| - Queries      |
| - Persistence  |
+-------+--------+
        |
        v
+----------------+
|   Database     |
|                |
| - Tables       |
| - Records      |
| - Storage      |
+----------------+
```

---

## Layer Responsibilities

### 1. Controller Layer

#### Purpose

The Controller layer handles communication between external clients and the
application.

#### Responsibilities

- Receive HTTP requests
- Validate request data
- Convert request payloads into DTOs
- Call service methods
- Return HTTP responses

#### Example

```java
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}
```

#### Should NOT contain

- Business rules
- Database queries
- Complex calculations
- Transaction handling

---

## 2. Service Layer

#### Purpose

The Service layer contains the application's business logic.

#### Responsibilities

- Implement business rules
- Map between DTOs and entities
- Coordinate multiple repositories
- Manage transactions
- Transform data between layers
- Handle application workflows

#### Example

```java
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow();

        return new UserDto(user);
    }
}
```

#### Should NOT contain

- HTTP-specific logic
- Direct request/response handling
- SQL queries

---

## 3. Repository Layer

#### Purpose

The Repository layer provides an abstraction for database operations.

#### Responsibilities

* Perform CRUD operations
* Execute database queries
* Communicate with ORM frameworks
* Manage entity persistence

Spring Data JPA example:

```java
@Repository
public interface UserRepository 
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
```

#### Should NOT contain

- Business decisions
- API logic
- User workflow rules

---

## 4. Database Layer

#### Purpose

The database stores application data permanently.

Examples:

- PostgreSQL
- MySQL
- Oracle Database
- Microsoft SQL Server
- MongoDB

Responsibilities:

- Store records
- Maintain relationships
- Enforce constraints
- Provide data persistence

---

## Request Flow Example

A client requests user information:

```http
GET /users/10
```

### Step 1: Controller receives request

```text
UserController
        |
        v
getUser(10)
```

The controller validates the request and calls the service.

---

### Step 2: Service executes business logic

```text
UserService
        |
        v
getUser(10)
```

The service determines what data is needed and calls the repository.

---

### Step 3: Repository accesses database

```text
UserRepository
        |
        v
SELECT * FROM users WHERE id = 10
```

The repository retrieves the entity from the database.

---

### Step 4: Response returns

```text
Database
    |
    v
Repository
    |
    v
Service
    |
    v
Controller
    |
    v
Client
```

---

## Recommended Package Structure

A typical resource structure:

```text
src/main/java/io/github/thienaang_dev/store/user

├── controller
│   └── UserController.java
│
├── service
│   ├── UserService.java
│   └── impl
│       └── UserServiceImpl.java
│
├── repository
│   └── UserRepository.java
│
├── entity
│   └── User.java
│
├── dto
│   └── UserDto.java
│
└── exception
│   ├── UserExceptionHandler.java
    └── UserNotFoundException.java
```

---

## Layer Dependency Rules

The dependency direction should always flow downward:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Allowed:

```text
Controller → Service
Service → Repository
Repository → Database
```

Avoid:

```text
Controller → Repository   ❌
Controller → Database     ❌
Repository → Service      ❌
```

---

## Benefits

### Separation of Concerns

Each layer has a single responsibility.

Example:

- Controller handles HTTP
- Service handles business rules
- Repository handles persistence

---

### Easier Testing

Each layer can be tested independently.

Examples:

- Controller tests with mocked services
- Service tests with mocked repositories
- Repository tests with test databases

---

### Better Maintainability

Changes are isolated.

Examples:

- Changing database technology affects repository code
- Changing API format affects controller code
- Changing business rules affects service code

---

### Scalability

The architecture supports:

- Additional APIs
- Multiple clients
- Complex business workflows
- Larger development teams

---

## Summary

The Controller-Service-Repository architecture is a common Spring Boot design
pattern that separates application responsibilities into clear layers:

| Layer      | Responsibility                     |
| ---------- | ---------------------------------- |
| Controller | Handles API requests and responses |
| Service    | Implements business logic          |
| Repository | Handles data access                |
| Database   | Stores persistent data             |

Following this structure creates applications that are easier to develop, test,
maintain, and scale.

```text
Client
  ↓
Controller Layer   (Web/API layer)
  ↓
Service Layer      (Business logic layer)
  ↓
Repository Layer   (Data access layer)
  ↓
Database           (Persistence layer)
```
