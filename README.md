# Leadly — Contact Management System

A full-stack contact management app built with React and Spring Boot. Manage contacts with multiple emails and phone numbers, search across all fields, and import/export via CSV.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=SafwanIrfan_10pearls-contact-management-system&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=SafwanIrfan_10pearls-contact-management-system)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=SafwanIrfan_10pearls-contact-management-system&metric=coverage&cacheBuster=1)](https://sonarcloud.io/summary/overall?id=SafwanIrfan_10pearls-contact-management-system)

---

## Stack

- **Frontend:** React + Vite (Node.js v24.15.0)
- **Backend:** Java 21 + Spring Boot + Spring Security + JWT
- **Database:** PostgreSQL

---

## Getting Started

### Prerequisites

- Node.js v24.15.0
- Java 21
- PostgreSQL
- Maven

### Clone

```bash
git clone https://github.com/SafwanIrfan/10pearls-contact-management-system.git
cd 10pearls-contact-management-system
```

### Database

```sql
CREATE DATABASE leadly;
```

---

### Backend

1. Set up environment variables:
   - Copy `src/main/resources/application.properties.example`
   - Fill in `src/main/resources/application.properties`

```properties
spring.application.name=
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
jwt.secret=
```

2. Run the project:

```bash
./mvnw spring-boot:run
```

Runs on `http://localhost:8080`

---

### Frontend

1. Set up environment variables:
   - Copy `.env.example`
   - Fill in `.env`

```env
VITE_API_BASE_URL=http://localhost:8080
```

2. Run the project:

```bash
npm install
npm run dev
```

Runs on `http://localhost:5173`

---

## API Docs

[View API Documentation](https://editor.swagger.io/?url=https://gist.githubusercontent.com/SafwanIrfan/ce224c43ccf135ded7fd891ba103040b/raw/gistfile1.txt)

> To test endpoints locally, run the backend and visit `http://localhost:8080/swagger-ui/index.html`

---

## Tests

```bash
./mvnw clean verify
```
