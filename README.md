# 🚀 GoPlan Engine

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%2BBoot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io/)
[![React](https://img.shields.io/badge/React-UI-61DAFB?style=flat-square&logo=react&logoColor=black)](https://react.dev/)

GoPlan is a high-performance, dynamic project generation engine. It acts as an infrastructure orchestrator that allows developers to instantly scaffold customized backend architectures with pre-configured templates, caching, and clean dependencies.

## ✨ Features

- **Dynamic Templating:** Uses Apache FreeMarker to inject variables (like package names) directly into raw source code.
- **Remote Fetching:** Pulls templates directly from a remote GitHub repository in real-time using a non-blocking `WebClient`.
- **Lightning Fast Caching:** Integrates a Dockerized Redis cache layer to bypass API rate limits and serve generated ZIP files in milliseconds.
- **Developer-First UI:** A sleek, minimal React frontend tailored for engineering speed.

---

## 📋 Prerequisites

Before cloning, ensure you have the following installed:

- [Java 17+](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [Node.js](https://nodejs.org/) (v18+ recommended)
- [Docker](https://www.docker.com/) (Required for the caching layer)

---

## 🚀 Getting Started

### 1. Start the Redis Cache

GoPlan requires Redis to cache the remote templates. We use Docker Compose to make this painless. Run this in the root directory:

docker compose up -d

*(To shut down the cache later, run `docker compose down`)*

### 2. Boot the Spring Boot Backend

Start the backend orchestrator:

mvn clean install
mvn spring-boot:run

*The engine will start listening on `http://localhost:8080`.*

### 3. Launch the React Frontend

Open a new terminal and start the UI:

cd frontend
npm install
npm run dev

*Open `http://localhost:5173` in your browser to access the generator.*

---

## 🔌 API Reference

### Generate Project

Creates a zipped software project based on your configuration.

**Endpoint:** `POST /api/v1/projects/generate`  
**Content-Type:** `application/json`

**Payload:**
{
  "projectName": "auth-service",
  "basePackage": "com.planagency.auth",
  "framework": "SPRING_BOOT"
}

**Response:**
- `200 OK`: Returns a binary `.zip` file stream.
- `500 Internal Server Error`: Engine failure (Check your Redis container).

---

## 📂 Architecture Overview

/goplan-engine
├── docker-compose.yml       # Redis cache config
├── /src                     # Spring Boot Backend
│   ├── /api                 # REST Controllers
│   ├── /dto                 # Data Transfer Objects
│   ├── /strategy            # Framework generation strategies
│   └── /service             # Remote Fetcher & Renderer
└── /frontend                # React UI
    └── /src
        └── App.jsx          # Generator Form & API logic
