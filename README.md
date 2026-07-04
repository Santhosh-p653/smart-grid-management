# Smart Grid Management System

## Overview

The **Smart Grid Management System** is a full‑stack academic capstone project that provides a comprehensive solution for monitoring and managing electrical grid nodes, power consumption, faults, outages, and analytics.

It consists of:
- **Backend** – Spring Boot 3.x (Java 21) RESTful APIs with JWT authentication and role‑based access control (ADMIN, OPERATOR).
- **Frontend** – React 19 + Vite, using Axios for API calls, React Router for navigation, and Chart.js for visual analytics.
- **Database** – MySQL (accessed via Spring Data JPA).

## Prerequisites

- **JDK** 21 installed and `JAVA_HOME` set.
- **Maven** (comes with most IDEs such as Eclipse).
- **Node.js** 20.x (LTS) and npm.
- **MySQL** running on `localhost:3307` (XAMPP default). Default credentials:
  - Username: `root`
  - Password: `password`
  - Database: `smart_grid` (create if not present).

## Backend Setup

1. **Create the database** (if not already present):
   ```sql
   CREATE DATABASE smart_grid;
   ```
2. Open the project in Eclipse (or any IDE supporting Maven).
3. Ensure Lombok annotation processing is enabled in the IDE.
4. Verify `src/main/resources/application.properties` contains the correct connection string:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3307/smart_grid
   spring.datasource.username=root
   spring.datasource.password=password
   ```
5. Build the project:
   ```bash
   mvn clean install
   ```
6. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   The API will be available at **http://localhost:8080/api**.

## Frontend Setup

1. Navigate to the `frontend` directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
   The UI will be served at **http://localhost:5173** (Vite default).

## Running the Full Stack

1. Start the backend first (steps above).
2. Then start the frontend.
3. Open the UI in a browser and log in using a user created via the API (or via DB seed). The default admin credentials can be created manually in the `users` table with role `ADMIN`.

## Project Structure

```
smart-grid-management/
│
├─ backend/                # Spring Boot source
│   ├─ src/main/java/...   # Controllers, services, entities, repos, config
│   └─ src/main/resources  # application.properties, static resources
│
└─ frontend/               # React Vite app
    ├─ src/
    │   ├─ components/
    │   ├─ pages/
    │   └─ App.jsx
    └─ vite.config.js
```

---

**Happy coding!**
