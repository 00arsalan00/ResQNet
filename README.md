# 🚑 ResQNet — Integrated Disaster Response Platform

ResQNet is a high-resilience, full-stack disaster management ecosystem. It bridges geospatial intelligence, multi-modal security, and high-fidelity visualization to provide real-time situational awareness for citizens and responders.

---

## 📌 Architectural Overview

ResQNet is built as a **Modular Monolith** consisting of three specialized pillars:
1.  **`resqnet-backend`**: The operational "Brain" powered by Java 21, Spring Boot 3.2, and PostGIS.
2.  **`resqnet-security`**: A high-security Identity Provider (IdP) managing access and accountability.
3.  **`resqnet-ui`**: A modern React-based "Command Center" with 3D/2D visualization capabilities.

---

## 🚀 Advanced Production Features

### **1. Intelligent Geospatial Layer (Backend)**
*   **PostGIS Integration**: Native storage of disaster locations as geometric points.
*   **Hybrid Geocoding Engine**: Real-time integration with **OpenStreetMap (Nominatim)** to convert text addresses into mathematical coordinates automatically.
*   **Fail-Safe Logic**: Automatic fallback to 0,0 and logging if external geocoding providers are unreachable, ensuring system uptime during crises.

### **2. Multi-Modal Identity Gateway (Security)**
*   **Hybrid Authentication**: Support for **Google OAuth2** (Citizens), **SMS OTP** (Field Teams), and **Local Password** (Admins).
*   **Verified Auto-Registration**: Reporting an incident triggers a "Silent Path" where users are automatically registered and verified via OTP, creating a zero-friction entry for victims.
*   **Traffic Enforcement (Rate Limiting)**: Integrated **Bucket4j Token Bucket** algorithm to throttle requests per IP, preventing DoS attacks and API cost overruns.
*   **Real Communication**: Backend integration with **Spring Mail** for asynchronous secure key delivery.

### **3. Operational Command Console (Frontend)**
*   **High-Fidelity Visuals**: Immersive **Three.js 3D Globe** for global threat perception and **2D Radar Map** for localized operational reach.
*   **Personalized Progress Tracking**: Dedicated "My Activity" hub for citizens to track their complaints through the `Reported -> Dispatched -> Resolved` lifecycle.
*   **Glassmorphic Tactical UI**: Professional UI using an optimized 5-color palette, supporting full theme synchronization across 2D and 3D layers.

---

## 🛠️ Technical Stack & Concepts

| Domain | Technology / Concept |
| :--- | :--- |
| **Backend** | Spring Boot, JPA, PostGIS, JTS (Java Topology Suite) |
| **Security** | Spring Security 6, JWT Rotation, OAuth2, Rate Limiting (Bucket4j) |
| **Communication** | Asynchronous SMTP (Spring Mail), REST (RestTemplate) |
| **Frontend** | React, Vite, Tailwind CSS, Three.js, D3-Geo |
| **Architecture** | Normalization (3NF), Idempotency, Transactional Integrity, ABAC |

---

## 👨‍💻 Developer Guide

1.  **Database**: PostgreSQL with PostGIS extension.
2.  **Environment Variables**:
    *   `DB_USERNAME`, `DB_PASSWORD`: Database credentials.
    *   `MAIL_USERNAME`, `MAIL_PASSWORD`: SMTP Credentials (use App Password for Gmail).
    *   `JWT_SECRET`: 256-bit key for token signing.
3.  **Startup**: Launch Backend (8071), Security (8072), and Frontend (3000).

---

## 👨‍💻 Author
**Mohammad Arsalan Rayeen**
*Backend & AI Enthusiast*
