# 🛒 Professional E-Commerce API (Spring Boot)

This is a robust backend application for an E-Commerce platform built using **Java 21** and **Spring Boot 3.4.x**. It features a complete flow from user registration to product management, real-time cart handling, and secure order placement with inventory management.

---

## 🚀 Key Features

* **User Management:** Register and manage users with role-based attributes.
* **Product Catalog:** Complete CRUD operations for products including real-time stock tracking.
* **Smart Cart System:** Add/Update/Remove items in the cart with automatic price calculation.
* **Order Processing:** * Create orders directly from cart items.
    * Automated **Stock Deduction** upon successful order placement.
    * Transactional safety (Rollback if anything fails).
* **Order History:** Users can view their past orders with detailed item breakdowns.
* **Monitoring & Metrics:** Integrated **Spring Boot Actuator** for health checks and performance monitoring.

---

## 🛠️ Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.x
* **Database:** H2 (In-memory) for development / MySQL Ready
* **ORM:** Spring Data JPA (Hibernate)
* **Utility:** Lombok (for boilerplate-free code)
* **Architecture:** Layered Architecture (Controller -> Service -> Repository -> Model)

---

## 📂 Project Structure

```text
src/main/java/com/app/ecom/
├── Controller/   # REST Endpoints
├── Service/      # Business Logic
├── Repository/   # Database Access (JPA)
├── Model/        # Database Entities
├── DTO/          # Data Transfer Objects (Request/Response)
├── Enum/         # Order Status, User Roles
└── Transformer/  # Entity to DTO Mapping Logic