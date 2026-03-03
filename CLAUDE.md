# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A full-stack task management application built with Clean Architecture, Domain-Driven Design (DDD), and Spec-Driven Development (BDD with Cucumber).

## Commands

### Backend (from `backend/` directory)

```bash
# Run all tests (including Cucumber BDD tests)
./mvnw test

# Run only Cucumber tests
./mvnw test -Dtest=CucumberTest

# Build the application
./mvnw clean package

# Run the Spring Boot application (starts on port 8080)
./mvnw spring-boot:run
```

### Frontend (from `frontend/` directory)

```bash
# Install dependencies
npm install

# Start development server (port 3000)
npm run dev

# Production build
npm run build

# Run ESLint
npm run lint

# Add shadcn components
npx shadcn@latest add <component-name>
```

## Architecture

### Backend (Hexagonal/Clean Architecture)

The backend follows a layered architecture with strict dependency rules - inner layers never depend on outer layers.

```
com.tasklist.api/
├── domain/              # Core business logic (innermost)
│   ├── Task.java        # Domain entity with validation
│   ├── repository/      # Repository port interface
│   └── exception/       # Domain exceptions
├── application/         # Use cases orchestrating domain
│   ├── usecase/         # CreateTask, UpdateTask, GetTask, DeleteTask, ListTasks
│   └── port/in/         # Input commands (CreateTaskCommand, UpdateTaskCommand)
├── infrastructure/      # External adapters (outermost)
│   └── persistence/     # JPA adapter, entity, mapper (MapStruct)
└── presentation/        # REST API layer
    └── rest/            # Controllers, DTOs, error handling, HATEOAS resources
```

**Key patterns:**
- Repository interface in domain (`TaskRepository`), implementation in infrastructure (`TaskRepositoryAdapter`)
- MapStruct for entity-to-domain mapping (`TaskPersistenceMapper`)
- HATEOAS for REST resource representation (`TaskResource`, `TaskResourceAssembler`)

### Frontend (Next.js with Server Components)

```
frontend/src/
├── app/                 # Next.js App Router
├── components/
│   ├── tasks/           # Task-specific components (TaskList, TaskItem, TaskFormDialog)
│   └── ui/              # shadcn/ui components
├── services/
│   └── api.ts           # API client for backend communication
├── types/
│   └── index.ts         # TypeScript interfaces matching backend DTOs
└── lib/
    └── utils.ts         # Utility functions (cn for classnames)
```

**API configuration:** Set `NEXT_PUBLIC_API_URL` environment variable (defaults to `http://localhost:8080/api/v1`)

### Technology Stack

- **Backend:** Java 23, Spring Boot 4.0.0-M3, Spring Data JPA, H2 (in-memory), MapStruct, Lombok
- **Frontend:** Next.js 16, React 19, TypeScript 5, Tailwind CSS 4, shadcn/ui, Radix UI
- **Testing:** Cucumber 7.20.1 (BDD), JUnit Platform

## Testing Approach

BDD tests are defined in Gherkin format at `backend/src/test/resources/features/task.feature`. Step definitions are in `backend/src/test/java/com/tasklist/api/steps/TaskSteps.java`.

When adding new features, write the Cucumber scenario first, then implement the step definitions and production code.
