---
description: Spec-Driven Development + DDD + Clean Architecture Workflow
---
# Spec-Driven Development with DDD and Clean Architecture (Backend)

When implementing a new feature in the backend, follow these steps strictly to ensure alignment with our Domain-Driven Design and Clean Architecture principles.

## 1. Write the Specification (BDD)
- Create a `.feature` file in `backend/src/test/resources/features/` documenting the behavior using Gherkin syntax.
- Discuss the specification with the user to ensure all edge cases and domain rules are captured.

## 2. Define the Domain Model (Core)
- **Entities & Value Objects**: Implement core business objects in `backend/src/main/java/com/tasklist/api/domain/`. These classes must **NOT** have dependencies on external frameworks, except that **Domain entities CAN use Lombok**. 
- **Invariants**: Domain entities must manage their own invariants (e.g. validate state in constructors and mutator methods to prevent invalid states).
- **Domain Exceptions**: Create specific exception classes for business rule violations.
- **Repository Interfaces**: Define the expected storage behavior via interfaces (Ports) in the domain or application layer.

## 3. Implement the Application Layer (Use Cases)
- **Use Cases/Services**: Create classes in `backend/src/main/java/com/tasklist/api/application/` to orchestrate domain entities. These also have **NO** framework dependencies.
- **DTOs**: Define Data Transfer Objects for crossing layer boundaries.

## 4. Implement the Infrastructure Layer
- **Repositories**: Implement the Domain Repository interfaces in `backend/src/main/java/com/tasklist/api/infrastructure/`. Use Spring Data JPA here, mapping Domain Entities to JPA/Database Entities if necessary.
- **Configuration**: Set up any needed Spring `@Configuration` beans to inject Application Use Cases as beans.

## 5. Implement the Presentation Layer
- **Controllers**: Create Spring `@RestController` classes in `backend/src/main/java/com/tasklist/api/presentation/`.
- Validate incoming HTTP requests and map them to Application DTOs.

## 6. Automate and Verify
- Implement the Cucumber Step Definitions in `backend/src/test/java/com/tasklist/api/steps/`.
- Ensure all tests pass (`./mvnw test`). Refactor the implementation if necessary.
