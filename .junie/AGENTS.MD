# AI Agent Instructions

## General
- Don't write unit tests unless explicitly asked for.
- Use a feature-based package structure.

## Backend (Spring Boot)
- Never directly return JPA entities from controllers, instead use mapper classes to map them to DTO's.
- Use Lombok for boilerplate code like getters, setters and constructors.

## Frontend (Angular)
- Prefer SCSS to regular CSS.
- Separate HTML, TypeScript and CSS.
- Don't use deprecated NgIf and NgFor, instead use @if and @for blocks.