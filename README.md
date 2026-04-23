# Smart Campus API

A high-performance, in-memory REST API built with JAX-RS (Jersey 2 / `javax.ws.rs`) and Grizzly.

## Run

```bash
mvn clean compile exec:java
```

Base URL: `http://localhost:8080/api/v1`

## Implemented Coursework Requirements (Part 1)

- Maven project bootstrapped with JAX-RS (Jersey) and an embedded lightweight server (Grizzly).
- Versioned entry point via `@ApplicationPath("/api/v1")` on a subclass of `javax.ws.rs.core.Application`.
- Root discovery endpoint at `GET /api/v1` returning metadata and hypermedia-style links.
- Core POJO models: `Room`, `Sensor`, and `SensorReading` with encapsulation.

## Short Report Answers

### 1) JAX-RS Resource Lifecycle and Concurrency Impact

By default, JAX-RS resource classes are request-scoped in most implementations, meaning a new resource instance is created for each request. Some runtimes or DI configurations can create singleton resources, but this is an explicit design choice.

Because request handling is concurrent regardless of resource lifecycle, shared in-memory data (maps/lists) must still be thread-safe. This project uses:

- `ConcurrentHashMap` for key-value collections.
- `CopyOnWriteArrayList` for lists that are read often and written less frequently.
- `synchronized` blocks for multi-structure updates that must be atomic across maps/lists.

This avoids race conditions, lost updates, and inconsistent cross-resource relationships.

### 2) Why Hypermedia (HATEOAS) Helps

Hypermedia lets clients discover valid next actions from API responses instead of hardcoding URL knowledge. That makes clients more resilient to API evolution, because navigation is driven by links returned by the server.

Compared to static documentation, hypermedia:

- Reduces tight coupling to fixed endpoint paths.
- Guides clients dynamically based on context/state.
- Improves discoverability for new consumers.
- Supports progressive API evolution with fewer breaking changes.
# smart_campus_api
