# GoPlan ⚡

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io/)
[![React](https://img.shields.io/badge/React-19-61DAFB?style=flat-square&logo=react&logoColor=black)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-Frontend-646CFF?style=flat-square&logo=vite&logoColor=white)](https://vitejs.dev/)

GoPlan is a project scaffolding generator that lets you instantly bootstrap a structured backend project for your chosen framework. Fill in your project details, pick a framework, select the features you need, and download a ready-to-go ZIP in seconds.

> ⚠️ **Note:** The frontend UI was entirely AI-generated.

---

## ✨ Features

- Scaffold projects for **Spring Boot**, **ASP.NET Core**, **FastAPI**, **Django**, and more
- Choose which layers to generate: Models, Controllers, Services, Repositories
- Instant ZIP download — no sign-up required
- Redis-backed caching for fast repeated generation
- Clean React + Tailwind CSS frontend

---

## 🛠️ Tech Stack

| Layer     | Technology                        |
|-----------|-----------------------------------|
| Backend   | Java 21, Spring Boot 4, Maven     |
| Frontend  | React 19, Vite, Tailwind CSS      |
| Cache     | Redis (via Docker)                |
| Templates | Apache FreeMarker                 |

---

## 📋 Prerequisites

Make sure the following are installed before you begin:

- [Java 21+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/download.cgi) *(or use the included `./mvnw` wrapper)*
- [Node.js 20+ & npm](https://nodejs.org/)
- [Docker](https://www.docker.com/products/docker-desktop/) *(required for Redis)*

---

## 🚀 Setup

### 1. Clone the repository

```bash
git clone https://github.com/amepic123123/goplan.git
cd goplan
```

---

### 2. Start Redis with Docker

GoPlan requires a running Redis instance on **port 6379**. A `docker-compose.yml` is included in the root of the repository.

```bash
docker compose up -d
```

This spins up a Redis container named `goplan-redis` and exposes it on `localhost:6379`.

To confirm it is running:

```bash
docker ps
```

You should see `goplan-redis` listed with status **Up**.

To stop Redis when you're done:

```bash
docker compose down
```

---

### 3. Start the Backend

From the **root of the repository**, run the Spring Boot application using the Maven wrapper:

```bash
# macOS / Linux
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The backend starts on **http://localhost:8080**.

---

### 4. Start the Frontend

The frontend lives in the `goplan-frontend/` directory.

```bash
cd goplan-frontend
npm install
npm run dev
```

Vite will print the local URL in the terminal — typically **http://localhost:5173**. Open that in your browser to use the app.

---

## 🖥️ Usage

1. Open **http://localhost:5173** in your browser.
2. Enter a **Project Name** — lowercase letters, numbers, and hyphens only (e.g. `my-awesome-api`).
3. Enter a **Base Package** — valid Java-style package (e.g. `com.example.api`).
4. Select a **Framework** from the available cards.
5. Choose the **Features** you want included (Models, Controllers, Services, Repositories).
6. Click **Generate Project** — your scaffolded project ZIP will download automatically.

---

## 📂 Project Structure

```
goplan/
├── src/                          # Spring Boot backend source
│   └── main/
│       ├── java/                 # Controllers, services, strategies, DTOs
│       └── resources/
│           └── application.properties
├── goplan-frontend/              # React + Vite frontend (AI-generated)
│   └── src/
│       ├── App.jsx
│       └── components/ui/
├── docker-compose.yml            # Redis service definition
├── pom.xml                       # Maven build configuration
└── mvnw / mvnw.cmd               # Maven wrapper scripts
```

---

## ⚙️ Configuration

Backend configuration is in `src/main/resources/application.properties`:

```properties
spring.application.name=goplan

spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379
```

If you need Redis on a different host or port, update those values **and** the corresponding port mapping in `docker-compose.yml`.

---

## 🔌 API Reference

### `POST /api/v1/projects/generate`

Generates and returns a scaffolded project as a ZIP file.

**Request body (JSON):**

```json
{
  "projectName": "my-awesome-api",
  "basePackage": "com.example.api",
  "framework": "SPRING_BOOT",
  "features": ["MODELS", "CONTROLLERS", "SERVICES", "REPOSITORIES"]
}
```

| Field         | Type   | Validation                                          |
|---------------|--------|-----------------------------------------------------|
| `projectName` | string | Lowercase letters, numbers, and hyphens; max 255 chars |
| `basePackage` | string | Valid Java package name (e.g. `com.example.api`)   |
| `framework`   | string | One of the supported framework IDs (see below)      |
| `features`    | array  | Non-empty list of feature IDs                       |

**Supported framework IDs:**

| ID           | Framework     |
|--------------|---------------|
| `SPRING_BOOT` | Spring Boot  |
| `ASP_NET`     | ASP.NET Core |
| `FASTAPI`     | FastAPI      |
| `DJANGO`      | Django       |

**Response:** `application/zip` — the scaffolded project as a downloadable ZIP file.

**Error responses:**

| Status | Meaning                                    |
|--------|--------------------------------------------|
| `400`  | Validation failed (check request payload)  |
| `500`  | Server error (check Redis is running)      |
