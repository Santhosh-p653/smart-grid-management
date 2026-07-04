# Architecture Overview

## High‑Level Diagram (textual)
```
+-------------------+          +-------------------+          +-------------------+
|   Frontend (UI)   |  <--->  |   Backend API     |  <--->  |   MySQL Database |
|   React 19 + Vite|  HTTP    | Spring Boot 3.x   |  JDBC   |   (smart_grid)   |
+-------------------+          +-------------------+          +-------------------+
```

## Components

### Backend (Spring Boot)
- **Controllers** – expose REST endpoints under `/api/**`.
- **Services** – business logic (e.g., `AnalyticsService`, `UserService`).
- **Repositories** – Spring Data JPA interfaces for entity persistence.
- **Entities** – JPA‑mapped domain models (`GridNode`, `PowerReading`, `Alert`, `Fault`, `Report`, `User`, `Role`, `Zone`).
- **DTOs** – data transfer objects for API payloads (`DashboardStatsDto`, `AlertDto`, `ReportDto`).
- **Security** – JWT authentication (`JwtAuthenticationFilter`, `SecurityConfig`), role‑based access (`ADMIN`, `OPERATOR`).
- **Exception Handling** – global `@ControllerAdvice` to translate exceptions to proper HTTP responses.

### Frontend (React 19)
- **Pages** – Dashboard, Reports, Grid Management, Authentication.
- **Components** – reusable UI widgets (charts, tables, forms) using **Bootstrap 5** and **Chart.js**.
- **State Management** – React Context for auth state; local component state for UI.
- **Routing** – `react-router-dom` for navigation between pages.
- **API Layer** – Axios instance with JWT interceptor for authenticated calls.

### Database (MySQL)
- Schemas for each entity with foreign‑key relationships (e.g., `grid_nodes` ↔ `zones`).
- Indexes on primary keys and foreign keys for performance.
- Sample seed data can be inserted via SQL scripts or application bootstrap.

## Interaction Flow
1. **User logs in** – credentials sent to `/api/auth/login`, receives JWT.
2. **Frontend stores JWT** in memory and attaches it to subsequent Axios requests.
3. **API endpoints** validate JWT, enforce roles, and perform CRUD operations.
4. **AnalyticsService** aggregates data from multiple repositories to supply dashboard statistics.
5. **Reports** are generated on‑demand, persisted, and can be retrieved via `/api/reports`.

---

*All package names follow the `com.smartgrid` namespace.*
