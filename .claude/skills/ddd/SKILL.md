---
name: ddd
description: >
  Use this skill for Domain-Driven Design: strategic modeling, bounded contexts,
  aggregates, value objects, domain events, CQRS, event sourcing, and sagas.
  Trigger whenever the user mentions DDD, bounded contexts, aggregates, ubiquitous
  language, domain events, or is modeling a complex business domain. Also trigger
  when the user needs to decide if DDD is appropriate, design a context map,
  define repository contracts, or plan event-driven workflows.
---

# Domain-Driven Design

> When this skill is active, use this knowledge as specialized reference for DDD strategic modeling, tactical implementation, and evented architecture patterns.

---

## Use this skill when

- You need to model a complex business domain with explicit boundaries.
- You want to decide whether full DDD is worth the added complexity.
- You need to connect strategic design decisions to implementation patterns.
- You are planning CQRS, event sourcing, sagas, or projections from domain needs.

## Do not use this skill when

- The problem is simple CRUD with low business complexity.
- You only need localized bug fixes.
- There is no access to domain knowledge and no proxy product expert.

---

## Instructions

1. Run a viability check before committing to full DDD.
2. Produce strategic artifacts first: subdomains, bounded contexts, language glossary.
3. Route to specialized patterns based on current task.
4. Define success criteria and evidence for each stage.

### Viability Check

Use full DDD only when at least two of these are true:

- Business rules are complex or fast-changing.
- Multiple teams are causing model collisions.
- Integration contracts are unstable.
- Auditability and explicit invariants are critical.

### Routing Map

- **Strategic model and boundaries**: DDD Strategic Design (subdomains, bounded contexts)
- **Cross-context integrations and translation**: Context Mapping patterns
- **Tactical code modeling**: DDD Tactical Patterns (aggregates, value objects, domain events)
- **Read/write separation**: CQRS Implementation
- **Event history as source of truth**: Event Sourcing
- **Long-running workflows**: Saga Orchestration
- **Read models**: Projection Patterns
- **Decision log**: Architecture Decision Records (ADRs)

---

## Output Requirements

Always return:

- Scope and assumptions
- Current stage (strategic, tactical, or evented)
- Explicit artifacts produced
- Open risks and next step recommendation

---

## Limitations

- This skill does not replace direct workshops with domain experts.
- It does not provide framework-specific code generation.
- It should not be used as a justification to over-engineer simple systems.

---

## DDD Deliverables Checklist

Use this checklist to keep DDD adoption practical and measurable.

### Strategic Deliverables

- [ ] Subdomain map (core, supporting, generic)
- [ ] Bounded context map and ownership
- [ ] Ubiquitous language glossary
- [ ] 1-2 ADRs documenting critical boundary decisions

### Tactical Deliverables

- [ ] Aggregate list with invariants
- [ ] Value object list
- [ ] Domain events list
- [ ] Repository contracts and transaction boundaries

### Evented Deliverables (only when required)

- [ ] Command and query separation rationale
- [ ] Event schema versioning policy
- [ ] Saga compensation matrix
- [ ] Projection rebuild strategy
