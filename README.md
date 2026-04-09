# 🚑 ResQNet — Disaster Response Backend 

## 📌 Phase 1 Overview

ResQNet is a backend system designed to model real-world disaster response operations.
Phase 1 focuses on building a **strong domain foundation and REST API layer** that supports future scalability, geospatial intelligence, and AI-driven decision-making.

---

## 🎯 Phase 1 Goals

* Design a **clean and scalable domain model**
* Build **REST APIs for all core entities**
* Integrate **PostgreSQL + PostGIS** for geospatial data
* Implement **DTO validation and exception handling**
* Establish a **modular monolith architecture**

---

## 🧠 Core Domain Model

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

* Incident ↔ RescueTeam (Many-to-Many)
* Incident ↔ Volunteer (Many-to-Many)
* Incident → IncidentResource → Resource (One-to-Many mapping)
* Incident ↔ ReliefCamp (Many-to-Many)
* Volunteer → ReliefCamp (Many-to-One)

---

## ⚙️ Tech Stack

| Layer      | Technology                  |
| ---------- | --------------------------- |
| Backend    | Spring Boot                 |
| Database   | PostgreSQL                  |
| Geospatial | PostGIS                     |
| ORM        | Spring Data JPA (Hibernate) |
| Migration  | Flyway                      |
| API Docs   | OpenAPI / Swagger           |
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
