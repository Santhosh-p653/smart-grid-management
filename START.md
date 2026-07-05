# 🚀 Smart Grid Management — Startup Guide

## Prerequisites
- ✅ XAMPP running with **MySQL on port 3307**
- ✅ Java 21 installed
- ✅ Node.js installed

---

## ▶️ Start Backend (Spring Boot)

Open a terminal in the project root and run:

```powershell
cd "C:\Users\Sav Ranjith\eclipse-workspace\smart-grid-management"
.\mvnw.cmd spring-boot:run
```

🌐 Backend runs at: **http://localhost:8080**

---

## ▶️ Start Frontend (React + Vite)

Open a **second terminal** and run:

```powershell
cd "C:\Users\Sav Ranjith\eclipse-workspace\smart-grid-management\frontend"
npm run dev
```

🌐 Frontend runs at: **http://localhost:5173**

---

## 🔐 Default Login Credentials

| Role     | Username    | Password      |
|----------|-------------|---------------|
| Admin    | `admin`     | `admin123`    |
| Operator | `operator1` | `operator123` |
| Operator | `operator2` | `operator123` |

---

## 📋 API Base URL

```
http://localhost:8080/api
```

### Key Endpoints:
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login & get JWT token |
| POST | `/api/auth/register` | Register new user |
| GET | `/api/grid/nodes` | Get all grid nodes |
| GET | `/api/grid/zones` | Get all zones |
| GET | `/api/analytics/dashboard` | Dashboard stats |
| GET | `/api/faults` | Get all faults |
| GET | `/api/outages` | Get all outages |
| GET | `/api/consumers` | Get all consumers |
| GET | `/api/faults/alerts` | Get recent alerts |

---

## ⚠️ Startup Order

1. **Start XAMPP MySQL** (port 3307) first
2. **Start Backend** — waits ~30 seconds to fully start
3. **Start Frontend** — then open http://localhost:5173

---

## 🛑 Stop

- Backend: Press `Ctrl + C` in the backend terminal
- Frontend: Press `Ctrl + C` in the frontend terminal
