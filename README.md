
# 🚀 Customers Management API (Production-Ready Template)

![CI/CD Pipeline](https://github.com/Rubenbc-lab/customers-api/actions/workflows/ci.yml/badge.svg)
![Java 21](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot 4](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-7-red?logo=redis)
![Docker](https://img.shields.io/badge/Docker-GHCR-2496ED?logo=docker)
![JaCoCo](https://img.shields.io/badge/Coverage-80%25%2B-green)

A production-grade RESTful API built with **Spring Boot 4** and **Java 21**. This project serves as an enterprise architectural foundation implementing **Clean Architecture**, secure authentication via **HttpOnly Cookies + JWT**, high-performance caching via **Redis**, robust database migrations with **Flyway**, automated integration testing with **Testcontainers**, and a strict **CI/CD pipeline** targeting GitHub Container Registry (GHCR).

---

## 🎯 Project Objective & Scope

The goal of this project is to implement an enterprise-standard backend architecture ready for production workloads:
- **Clean Architecture:** Strict separation of concerns (Domain, Application, Infrastructure, and Presentation layers) ensuring high maintainability and testability.
- **Secure Authentication Model:** Advanced security flow using **JWT stored in secure HttpOnly Cookies** alongside BCrypt encryption to prevent XSS and CSRF vulnerabilities.
- **Resilient Testing Strategy:** Elimination of in-memory mocks (H2) in favor of real, isolated runtime dependencies (**Testcontainers** for PostgreSQL and Redis).
- **Quality Gates:** Strict code coverage validation enforced via **JaCoCo** ($\ge 80\%$) preventing failing builds from reaching production.
- **Automated Delivery (DevOps):** End-to-end continuous integration and deployment pipeline that builds, tests, verifies coverage, and publishes production-ready multi-stage Docker images to **GHCR**.
- **Observability:** Health checks, liveness/readiness probes, and telemetry endpoints via **Spring Boot Actuator**.

---

## 🛠️ Tech Stack

- **Core:** Java 21 (LTS), Spring Boot 4
- **Architecture Pattern:** Clean Architecture (Domain-Centric)
- **Data Persistence:** Spring Data JPA, Hibernate, PostgreSQL 16
- **Database Versioning:** Flyway
- **Caching Layer:** Redis 7
- **Security:** Spring Security 7, JJWT (HMAC-SHA256), HttpOnly Cookies
- **Testing:** JUnit 5, AssertJ, Mockito, Testcontainers
- **Code Coverage:** JaCoCo (Enforced minimum threshold: 80%)
- **Observability:** Spring Boot Actuator
- **Containerization & CI/CD:** Docker (Multi-stage build), Docker Compose, GitHub Actions, GHCR

---

## 🏛️ Architecture Overview

```text
[ Client Requests ]
        │
        ▼
[ Presentation Layer ] ──── (Controllers / REST DTOs / Cookie Extractors)
        │
        ▼
[ Security Filter Chain ] ─ (JWT Verification via HttpOnly Cookies)
        │
        ▼
[ Application Layer ] ───── (Use Cases / Business Orchestration)
        │
        ▼
[ Domain Layer ] ────────── (Entities / Business Rules / Repository Interfaces)
        │
        ▼
[ Infrastructure Layer ] ── (Spring Data JPA / PostgreSQL / Redis Cache / External APIs)

