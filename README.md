# 🚑 ResQNet — Disaster Response Backend 

## 📌 Overview

ResQNet is a backend system designed to model real-world disaster response operations.
It is divided into two main modules:
1. **resqnet-backend**: Core domain layer for disaster management.
2. **reqnet-security**: Security layer handling multi-role authentication and authorization.

---

## 🎯 Project Goals

* **Phase 1**: Design a clean and scalable domain model and build REST APIs for core entities.
* **Phase 2**: Implement a robust security layer with multi-role RBAC, JWT rotation, and hybrid authentication.

---

## 🧠 Core Domain Model (Phase 1)
... (existing content) ...

---

## 🔐 Security Architecture (Phase 2)

### Roles:
* **CITIZEN**: Report incidents, track own status.
* **VOLUNTEER / FIELD_RESCUE_TEAM**: Accept tasks, update progress.
* **DISTRICT_COORDINATOR**: Scoped access to district incidents and resources.
* **SUPER_ADMIN**: Full system control and cross-district analytics.

### Features:
* **Hybrid Auth**: LOCAL (Email/Pass), GOOGLE (OAuth2), and OTP (Phone).
* **JWT Rotation**: 25-minute Access Token + 7-day Refresh Token with DB-backed revocation.
* **Cookie-Based**: Tokens are delivered and validated via HttpOnly cookies for enhanced security.
* **Scoped Authorization**: District-based isolation for coordinators and teams.
* **Audit Logging**: Comprehensive tracking of all critical system actions.

---

## ⚙️ Tech Stack

The system revolves around **Incident** as the central entity.

### Entities:

* **Incident** – disaster event (type, severity, location, status)
* **RescueTeam** – response units with skills and availability
* **Volunteer** – individuals assisting in operations
* **Resource** – inventory (food, water, medicine, etc.)
* **ReliefCamp** – shelters for affected people
* **IncidentResource** – tracks resource allocation per incident

---

## 🔗 Key Relationships

* Incident ↔ RescueTeam via **IncidentAssignment** (assignment entity with status lifecycle)
* Incident ↔ Volunteer via **VolunteerAssignment** (assignment entity with role and status)
* Incident → **IncidentResource** → Resource (per-incident allocation; entity modeled, API planned)
* Incident → ReliefCamp (Many-to-One on camp; link via `POST /api/camp-assignments/admin/incidents/{incidentId}/camps/{campId}`)
* Resource ↔ ReliefCamp via **ResourceAssignment** and optional **camp_resources** join for inventory at camps

---

## ⚙️ Tech Stack

| Layer      | Technology                  |
| ---------- | --------------------------- |
| Backend    | Spring Boot                 |
| Database   | PostgreSQL                  |
| Geospatial | PostGIS                     |
| ORM        | Spring Data JPA (Hibernate) |
| Migration  | Flyway (dependency present; disabled — schema via Hibernate `ddl-auto: update`) |
| API Docs   | Planned (OpenAPI/Swagger not yet wired) |
| Build Tool | Maven                       |

---

## 🌍 Geospatial Support

* Uses **PostGIS POINT type** for location storage
* Enables future features:

    * Nearest responder search
    * Distance-based allocation
    * Geospatial analytics

---

## 🧱 Architecture

```
Controller → Service → Repository → Database
```

* **Controller**: API layer
* **Service**: Business logic
* **Repository**: Data access
* **Entity**: Domain model

---

## 🔐 Configuration

Sensitive data (DB credentials) is handled via **environment variables**:

```
DB_USERNAME
DB_PASSWORD
```

Example:

```yaml
spring:
  datasource:
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

---

## 🧪 Sample Flow (Postman)

1. Create Incident (with GPS coordinates)
2. Register Rescue Team
3. Assign Team to Incident
4. Add Volunteers
5. Allocate Resources
6. Link Relief Camp

---

## 👨‍💻 Author

**Mohammad Arsalan Rayeen**
Backend & AI Enthusiast

---

## ⭐ Note

This project is built with a focus on **system design, scalability, and real-world applicability**, not just CRUD operations.
