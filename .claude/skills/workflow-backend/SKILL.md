---
name: workflow-backend
description: >
  Use this skill when implementing any new backend feature in this project following
  Spec-Driven Development (BDD with Cucumber), Domain-Driven Design, and Clean
  Architecture (Hexagonal). Trigger for any backend feature request, API endpoint
  creation, domain modeling task, or when the user says "implement [feature] in the
  backend". This skill defines the mandatory workflow: BDD spec first → domain model
  → application use case → infrastructure adapter → REST controller → Cucumber tests.
---

# Workflow: Spec-Driven Backend (DDD + Clean Architecture)

> When this skill is active, use this as the authoritative workflow for implementing backend features using Spec-Driven Development, Domain-Driven Design, and Clean Architecture principles.

---

When implementing a new feature in the backend, follow these steps strictly to ensure alignment with our Domain-Driven Design and Clean Architecture principles.

---

## 1. Write the Specification (BDD)

- Create a `.feature` file in `backend/src/test/resources/features/` documenting the behavior using Gherkin syntax.
- Discuss the specification with the user to ensure all edge cases and domain rules are captured.

**Example:**
```gherkin
Feature: Task Management
  As a user
  I want to manage my tasks
  So that I can track my work

  Scenario: Create a new task
    Given no tasks exist
    When I create a task with title "Buy groceries"
    Then the task should be saved with title "Buy groceries"
    And the task should have status "PENDING"
```

---

## 2. Define the Domain Model (Core)

- **Entities & Value Objects**: Implement core business objects in `backend/src/main/java/com/tasklist/api/domain/`.
  - These classes must **NOT** have dependencies on external frameworks.
  - **Exception:** Domain entities CAN use Lombok.
- **Invariants**: Domain entities must manage their own invariants (validate state in constructors and mutator methods to prevent invalid states).
- **Domain Exceptions**: Create specific exception classes for business rule violations.
- **Repository Interfaces**: Define the expected storage behavior via interfaces (Ports) in the domain layer, **NOT** the application layer.

**Key rule:** The domain layer has zero dependencies on Spring, JPA, or any framework.

---

## 3. Implement the Application Layer (Use Cases)

- **Use Cases/Services**: Create classes in `backend/src/main/java/com/tasklist/api/application/usecase/` to orchestrate domain entities.
  - Use Lombok's `@RequiredArgsConstructor`.
  - Use Spring's `@Transactional` annotation.
  - **Must** call methods on the Domain Entity to modify internal values instead of using setters or rebuilding entities.
- **DTOs**: Define Data Transfer Objects for crossing layer boundaries.
- **Mappers**: Use **MapStruct** for mapping between entities, DTOs, and JPA entities.

---

## 4. Implement the Infrastructure Layer

- **Repositories**: Implement plain Spring Data JPA interfaces in persistence package. Adapters implement the Domain Repository interfaces in `backend/src/main/java/com/tasklist/api/infrastructure/`.
- **JPA Entities**: Use **Lombok** for boilerplate (`@Getter`, `@Setter`, `@NoArgsConstructor`, etc.) on JPA persistence Entities.
- **Configuration**: Set up any needed Spring `@Configuration` beans to inject Application Use Cases as beans.

**Pattern:**
```
Domain: TaskRepository (interface/port)
Infrastructure: TaskRepositoryAdapter implements TaskRepository
Infrastructure: TaskJpaRepository extends JpaRepository<TaskJpaEntity, Long>
Infrastructure: TaskPersistenceMapper (MapStruct)
```

---

## 5. Implement the Presentation Layer

- **Controllers**: Create Spring `@RestController` classes in `backend/src/main/java/com/tasklist/api/presentation/`.
  - Controllers **MUST** return `ResponseEntity<T>`.
- **Mapping**: Validate incoming HTTP requests and map them to Application DTOs using MapStruct.
- **Error Handling**: API error responses **MUST** be standardized using a structured DTO (e.g., `ErrorResponse`).

**Example:**
```java
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;

    @PostMapping
    public ResponseEntity<TaskResource> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskCommand command = mapper.toCommand(request);
        Task task = createTaskUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(task));
    }
}
```

---

## 6. Automate and Verify

- Implement the Cucumber Step Definitions in `backend/src/test/java/com/tasklist/api/steps/`.
- Ensure all tests pass (`./mvnw test`).
- Refactor the implementation if necessary, keeping all tests green.

---

## Layer Dependency Rules

```
Presentation → Application → Domain ← Infrastructure
     ↓               ↓          ↑
   DTOs          Commands    Repository
                              Interface
```

- **Domain**: No external dependencies. Pure Java + Lombok only.
- **Application**: Depends on Domain only.
- **Infrastructure**: Depends on Domain (implements ports). Uses Spring Data JPA.
- **Presentation**: Depends on Application (uses use cases). Uses Spring MVC.

---

## Quick Reference: Package Structure

```
com.tasklist.api/
├── domain/
│   ├── Task.java                    # Domain entity
│   ├── repository/
│   │   └── TaskRepository.java      # Repository port (interface)
│   └── exception/
│       └── TaskNotFoundException.java
├── application/
│   ├── usecase/
│   │   ├── CreateTaskUseCase.java
│   │   ├── UpdateTaskUseCase.java
│   │   ├── GetTaskUseCase.java
│   │   ├── DeleteTaskUseCase.java
│   │   └── ListTasksUseCase.java
│   └── port/in/
│       ├── CreateTaskCommand.java
│       └── UpdateTaskCommand.java
├── infrastructure/
│   └── persistence/
│       ├── TaskRepositoryAdapter.java  # Implements domain port
│       ├── TaskJpaRepository.java      # Spring Data JPA
│       ├── TaskJpaEntity.java          # JPA entity
│       └── TaskPersistenceMapper.java  # MapStruct
└── presentation/
    └── rest/
        ├── TaskController.java
        ├── dto/
        │   ├── CreateTaskRequest.java
        │   └── ErrorResponse.java
        └── resource/
            ├── TaskResource.java
            └── TaskResourceAssembler.java
```
