# Spring Boot Layered Architecture Documentation

## Overview

This project uses the standard layered architecture:

```text
Controller → Service → CrudService → Repository → Database
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
|  CrudService   |
|  Layer         |
|                |
| - Basic CRUD   |
|   Operations   |
| - Generic      |
|   Create/Read/ |
|   Update/Delete|
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

## 3. CrudService Layer

#### Purpose

The CrudService layer provides generic, reusable implementations of basic
CRUD (Create, Read, Update, Delete) operations, sitting between the Service
layer and the Repository layer.

#### Responsibilities

- Implement generic create, read, update, and delete operations
- Delegate persistence calls to the Repository layer
- Provide a reusable base that Service classes can build on top of
- Avoid duplicating basic CRUD logic across multiple services

#### Example

```java
public interface CrudService<T, ID> {

    T create(T entity);

    T getById(ID id);

    List<T> getAll();

    T update(ID id, T entity);

    void delete(ID id);
}

@Service
public class UserCrudService implements CrudService<User, Long> {

    private final UserRepository userRepository;

    public UserCrudService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(Long id, User entity) {
        entity.setId(id);
        return userRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
```

#### Should NOT contain

- Complex business rules
- Cross-entity workflows
- HTTP-specific logic

---

## 4. Repository Layer

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

## 5. Database Layer

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

The service determines what data is needed and delegates the basic lookup to
the CrudService.

---

### Step 3: CrudService performs the CRUD operation

```text
UserCrudService
        |
        v
getById(10)
```

The CrudService calls the repository to perform the actual data access.

---

### Step 4: Repository accesses database

```text
UserRepository
        |
        v
SELECT * FROM users WHERE id = 10
```

The repository retrieves the entity from the database.

---

### Step 5: Response returns

```text
Database
    |
    v
Repository
    |
    v
CrudService
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
├── crudservice
│   └── UserCrudService.java
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
CrudService
    ↓
Repository
    ↓
Database
```

Allowed:

```text
Controller → Service
Service → CrudService
CrudService → Repository
Repository → Database
```

Avoid:

```text
Controller → Repository    ❌
Controller → CrudService   ❌
Controller → Database      ❌
Service → Repository       ❌
Repository → CrudService   ❌
Repository → Service       ❌
```

---

## Benefits

### Separation of Concerns

Each layer has a single responsibility.

Example:

- Controller handles HTTP
- Service handles business rules
- CrudService handles basic CRUD operations
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

The Controller-Service-CrudService-Repository architecture is a common Spring
Boot design pattern that separates application responsibilities into clear
layers:

| Layer       | Responsibility                     |
| ----------- | ----------------------------------- |
| Controller  | Handles API requests and responses |
| Service     | Implements business logic          |
| CrudService | Handles basic CRUD operations      |
| Repository  | Handles data access                |
| Database    | Stores persistent data             |

Following this structure creates applications that are easier to develop, test,
maintain, and scale.

```text
Client
  ↓
Controller Layer    (Web/API layer)
  ↓
Service Layer       (Business logic layer)
  ↓
CrudService Layer   (Basic CRUD layer)
  ↓
Repository Layer    (Data access layer)
  ↓
Database            (Persistence layer)
```
