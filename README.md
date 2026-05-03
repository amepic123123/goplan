GoPlan Engine 🚀
GoPlan is a high-performance, dynamic project generation engine. It acts as an infrastructure orchestrator that allows developers to instantly scaffold customized backend architectures (like Spring Boot) with pre-configured templates, caching, and clean dependencies.

✨ Features
Dynamic Templating: Uses Apache FreeMarker to inject variables (like package names and artifact IDs) directly into raw source code and configuration files.

Remote Fetching: Pulls template structures directly from a remote GitHub repository in real-time using Spring WebFlux's non-blocking WebClient.

Lightning Fast Caching: Integrates a Dockerized Redis cache layer to bypass GitHub API rate limits and serve generated ZIP files in milliseconds.

Developer-First UI: A sleek, minimal React frontend tailored for engineering speed and usability.

🛠️ Tech Stack
Backend Engine:

Java 17+

Spring Boot 3.x (Web, WebFlux, Cache)

Apache FreeMarker

Redis (via Docker Compose)

Frontend Interface:

React (Functional Components & Hooks)

TailwindCSS (Zinc/Slate dark mode palette)

Vite

📋 Prerequisites
Before you begin, ensure you have the following installed on your machine:

Java 17+

Maven

Node.js (v18+ recommended)

Docker Desktop

🚀 Getting Started
1. Start the Redis Cache (Crucial)
GoPlan requires Redis to cache the remote GitHub templates. We use Docker Compose to make this painless.
Open your terminal in the root directory of the project and run:

Bash
docker compose up -d
(To shut down the cache later, run docker compose down)

2. Boot the Spring Boot Backend
Once Redis is running, start the backend orchestrator. Open a new terminal in the backend directory:

Bash
mvn clean install
mvn spring-boot:run
The engine will start listening on http://localhost:8080.

3. Launch the React Frontend
Open a separate terminal instance in your frontend directory:

Bash
# Install dependencies
npm install

# Start the development server
npm run dev
The UI will spin up (typically on http://localhost:5173). Open that URL in your browser.

🔌 API Reference
Generate Project
Creates a zipped software project based on the provided configuration.

Endpoint: POST /api/v1/projects/generate
Content-Type: application/json

Request Body:

JSON
{
  "projectName": "auth-service",
  "basePackage": "com.planagency.auth",
  "framework": "SPRING_BOOT"
}
Response:

200 OK: Returns a binary .zip file stream containing the generated project.

500 Internal Server Error: Engine failure (Check Redis connection or GitHub rate limits).

📂 Project Architecture Overview
Plaintext
/goplan-engine
├── docker-compose.yml       # Redis cache configuration
├── /backend
│   ├── /src/main/java/com/planagency/goplan
│   │   ├── /api             # REST Controllers
│   │   ├── /dto             # Data Transfer Objects
│   │   ├── /strategy        # Framework generation strategies
│   │   └── /service         # GitHub Fetcher & Template Renderer
│   └── pom.xml
└── /frontend
    ├── /src
    │   ├── App.jsx          # Main UI and API logic
    │   └── index.css        # Tailwind directives
    └── package.json
