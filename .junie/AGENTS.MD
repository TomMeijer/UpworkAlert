# AI Agent Instructions

## General
- Don't write unit tests unless explicitly asked for.
- Use a feature-based package structure.

## Backend (Spring Boot)
- Never directly return JPA entities from controllers, instead use mapper classes to map them to DTO's.
- Make DTO's immutable by default.
- Use Lombok for boilerplate code like getters, setters, constructors and builders. Don't use @Value and @Data.
- Use dedicated request objects to encapsulate request data received by controllers.
- Use Jakarta validation to validate request object fields.
- Don't pass request objects directly to services, instead use dedicated parameter objects to encapsulate service method parameters.
- Make parameter objects immutable by default.

## Frontend (Angular)
- Prefer SCSS to regular CSS.
- Separate HTML, TypeScript and CSS.
- Don't use deprecated NgIf and NgFor, instead use @if and @for blocks.
- Don't rely on ZoneJS for change detection, instead use Angular signals.