# 🚀 Customers Management API (Production-Ready Template)

![CI/CD Pipeline](https://github.com/Rubenbc-lab/customers-api/actions/workflows/ci.yml/badge.svg)
![Java 21](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot 4](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-7-red?logo=redis)
![Docker](https://img.shields.io/badge/Docker-GHCR-2496ED?logo=docker)
![JaCoCo](https://img.shields.io/badge/Coverage-75%25%2B-green)

A production-grade RESTful API built with **Spring Boot 4** and **Java 21**. This project serves as an enterprise architectural foundation implementing stateless **JWT Authentication**, high-performance caching via **Redis**, robust database migrations with **Flyway**, automated integration testing with **Testcontainers**, and a strict **CI/CD pipeline** targeting GitHub Container Registry (GHCR).

---

## 🎯 Project Objective & Scope

The goal of this project is to implement an enterprise-standard backend architecture ready for production workloads:
* **Stateless Security Model:** Complete authentication flow using signed JSON Web Tokens (JWT) with BCrypt encryption.
* **Resilient Testing Strategy:** Elimination of in-memory mocks (H2) in favor of real, isolated runtime dependencies (**Testcontainers** for PostgreSQL and Redis).
* **Quality Gates:** Strict code coverage validation enforced via **JaCoCo** ($\ge 75\%$) preventing failing builds from reaching production.
* **Automated Delivery (DevOps):** End-to-end continuous integration and deployment pipeline that builds, tests, verifies coverage, and publishes production-ready multi-stage Docker images to **GHCR**.
* **Observability:** Health checks, liveness/readiness probes, and telemetry endpoints via **Spring Boot Actuator**.

---

## 🛠️ Tech Stack

* **Core:** Java 21 (LTS), Spring Boot 4
* **Data Persistence:** Spring Data JPA, Hibernate, PostgreSQL 16
* **Database Versioning:** Flyway
* **Caching Layer:** Redis 7 
* **Security:** Spring Security 7, JJWT (HMAC-SHA256)
* **Testing:** JUnit 5, AssertJ, Mockito, Testcontainers
* **Code Coverage:** JaCoCo (Enforced minimum threshold: 75%)
* **Observability:** Spring Boot Actuator
* **Containerization & CI/CD:** Docker (Multi-stage build), Docker Compose, GitHub Actions, GHCR

---

## 🏛️ Architecture Overview

```text
[ Client Requests ]
        │
        ▼
[ Security Filter Chain ] ── (JWT Verification)
        │
        ▼
[ Controller Layer ] ────── (REST / DTO Mapping)
        │
        ▼
[ Service Layer ] ───────── (Business Logic)
   │             │
   ▼             ▼
[ Redis Cache ]  [ Spring Data JPA ]
                       │
                       ▼
                 [ PostgreSQL 16 ]