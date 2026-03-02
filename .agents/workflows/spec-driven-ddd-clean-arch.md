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
- **Repository Interfaces**: Define the expected storage behavior via interfaces (Ports) in the domain layer (e.g., `backend/src/main/java/com/tasklist/api/domain/repository/`), NOT the application layer.

## 3. Implement the Application Layer (Use Cases)
- **Use Cases/Services**: Create classes in `backend/src/main/java/com/tasklist/api/application/usecase/` to orchestrate domain entities. 
  - They should use Lombok's `@RequiredArgsConstructor`.
  - They should use Spring's `@Transactional` annotation.
  - They must call methods on the Domain Entity to modify internal values instead of using setters or rebuilding entities.
- **DTOs**: Define Data Transfer Objects for crossing layer boundaries.
- **Mappers**: Use **MapStruct** for mapping between entities, DTOs, and JPA entities.

## 4. Implement the Infrastructure Layer
- **Repositories**: Implement plain Spring Data JPA interfaces in persistence package. Adapters implement the Domain Repository interfaces in `backend/src/main/java/com/tasklist/api/infrastructure/`.
- **JPA Entities**: Use **Lombok** for boilerplate (`@Getter`, `@Setter`, `@NoArgsConstructor`, etc.) on JPA persistence Entities.
- **Configuration**: Set up any needed Spring `@Configuration` beans to inject Application Use Cases as beans.

## 5. Implement the Presentation Layer
- **Controllers**: Create Spring `@RestController` classes in `backend/src/main/java/com/tasklist/api/presentation/`.
  - Controllers MUST return `ResponseEntity<T>`.
- **Mapping**: Validate incoming HTTP requests and map them to Application DTOs using MapStruct.
- **Error Handling**: API error responses MUST be standardized using a structured DTO (e.g., `ErrorResponse`).

## 6. Automate and Verify
- Implement the Cucumber Step Definitions in `backend/src/test/java/com/tasklist/api/steps/`.
- Ensure all tests pass (`./mvnw test`). Refactor the implementation if necessary.
