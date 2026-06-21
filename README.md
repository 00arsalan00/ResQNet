# 🚑 ResQNet — Integrated Disaster Response Platform

ResQNet is a sophisticated, full-stack disaster management ecosystem designed to model real-world emergency operations. The system bridge geospatial intelligence, multi-modal security, and high-fidelity visualization to create a unified command and control center.

---

## 📌 Project Overview

ResQNet is structured into three specialized modules:
1.  **`resqnet-backend`**: The core operational engine handling incidents, resources, and geospatial logic.
2.  **`resqnet-security`**: A dedicated identity provider managing hybrid authentication and scoped authorization.
3.  **`resqnet-ui`**: A high-impact frontend delivering real-time situational awareness.

---

## 🧠 Phase 1: Core Domain Foundation (Backend)

The system revolves around the **Incident** as the central entity, coordinating human and material resources.

### **Core Entities:**
*   **Incident**: Disaster events with type, narrative description, and geospatial location.
*   **RescueTeam**: Professional response units with specialized skills and deployment status.
*   **Volunteer**: Individuals assisting in local operations.
*   **Resource**: Inventory management (food, medical supplies, equipment).
*   **ReliefCamp**: Safe zones and distribution hubs for affected populations.

### **Geospatial Intelligence:**
*   Uses **PostgreSQL + PostGIS** for storing location as native `POINT` geometry.
*   **Hybrid Geocoding**: Automatically converts text addresses into GPS coordinates for 100% database coordinate coverage.

---

## 🔐 Phase 2: Security & Scoped Authorization

Designed for high-stress environments, Phase 2 implements a multi-tenant security architecture.

### **Hybrid Authentication:**
*   **Citizen Access**: Instant registration/login via **Google OAuth2**.
*   **Field Operations**: Passwordless **OTP-based login** for responders using mobile devices.
*   **Command Level**: Traditional **Email/Password** for coordinators and admins.

### **Authorization & Accountability:**
*   **District Scoping**: District Coordinators are locked to their specific `districtId`, preventing unauthorized cross-district data access.
*   **JWT Rotation**: 25-minute Access Tokens + 7-day Refresh Tokens with database-backed revocation.
*   **Audit Logging**: Every dispatch, status change, and resource allocation is logged with the actor's identity, role, and district.

---

## 🖥️ Phase 3: Command Center (Frontend)

The frontend delivers a "War Room" experience, emphasizing clarity and visual impact.

### **High-Fidelity Visualizations:**
*   **3D Global Threat Monitor**: An interactive **Three.js globe** with dynamic country borders and spherical-to-flat "Unroll" animations.
*   **2D Operational Reach**: A detailed map grid using `react-simple-maps` with animated **Amaranth Pins** marking active ResQNet nodes.
*   **NLP Narrative Portal**: A full-page reporting interface designed to capture descriptive disaster narratives for future AI analysis.

### **UX & Design:**
*   **Fresh Palette**: Orchestrated use of **Amaranth**, **Peppermint**, **Aqua Island**, **Wedgewood**, and **Cello**.
*   **Dynamic Theming**: Full synchronization between Light and Dark modes across all 2D and 3D components.
*   **Glassmorphism**: Translucent, blurred UI layers for a modern, tactical feel.

---

## ⚙️ Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Backend** | Spring Boot 3.2, Java 21, Maven |
| **Security** | Spring Security 6, JWT (JJWT), OAuth2 Client |
| **Database** | PostgreSQL, PostGIS (Spatial Data) |
| **Frontend** | React, Vite, Tailwind CSS, Lucide Icons |
| **Graphics** | Three.js, React Three Fiber, D3-Geo |

---

## 🧪 Quick Start

1.  **Configure DB**: Ensure PostgreSQL is running with a `resqnet` database.
2.  **Environment**: Set `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET`.
3.  **Launch Backend**: Run `ResqnetBackendApplication` (Port 8071).
4.  **Launch Security**: Run `SecurityApplication` (Port 8072).
5.  **Launch UI**: `cd resqnet-frontend/resqnet-ui && npm install && npm run dev`.

---

## 👨‍💻 Author
**Mohammad Arsalan Rayeen**
*Backend & AI Enthusiast*
