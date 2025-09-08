**Features (MVP)**

* Partner/Inventory: manage theatres, screens, shows (performances).
* Seat layout stored as JSON and cached in Redis.
* Booking: seat locking (TTL), booking creation, notification after commit.
* AI Recommendations: in-memory movie catalog + Spring AI embeddings (OpenAI or Ollama).
* Notification: publishes booking emails (stub for MVP).
* Observability: Spring Boot Actuator (health, liveness, readiness).
* API docs: Swagger/OpenAPI enabled in each service.


**Quick Start (Local)**

**Install Ollama**

Download & install for macOS / Windows / Linux from the official site. Once installed, Ollama runs a local service listening on port 11434.

bash
```
ollama serve
ollama pull nomic-embed-text
```

**Infra with Docker**

```bash
docker-compose up -d
```

Run services (in order)

```bash
# from repo root
./gradlew :services:partner-inventory-service:bootRun
./gradlew :services:booking-service:bootRun
./gradlew :services:ai-recommendation-service:bootRun
./gradlew :services:notification-service:bootRun
```
