# Archify AI Java Backend

Spring Boot backend for auth and 3D visualization project management.

## Features

- JWT based auth (`signup`, `signin`, `me`)
- MongoDB persistence for visualizations
- Project CRUD
- Share / unshare project URLs
- Owner-only delete and render updates

## Run Locally

1. Set environment variables:

- `MONGODB_URI` (default: `mongodb://localhost:27017/archify-ai`)
- `JWT_SECRET` (required in production, long random string)
- `JWT_EXPIRATION_MS` (default: 7 days)
- `FRONTEND_ORIGIN` (default: `http://localhost:5173`)

2. Start API:

```bash
.\mvnw spring-boot:run
```

Server runs on `http://localhost:8080`.

## API Endpoints

- `POST /api/auth/signup`
- `POST /api/auth/signin`
- `GET /api/auth/me`
- `GET /api/projects`
- `POST /api/projects`
- `GET /api/projects/{id}`
- `PATCH /api/projects/{id}/render`
- `POST /api/projects/{id}/share`
- `POST /api/projects/{id}/unshare`
- `DELETE /api/projects/{id}`
- `GET /api/public/projects/{id}`
