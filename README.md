<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Redis-7-DC382D?style=for-the-badge&logo=redis&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" />
</p>

<h1 align="center">🛒 EcomStore</h1>
<p align="center">
  <strong>A production-ready e-commerce REST API powering everything from product browsing to order fulfillment.</strong>
</p>
<p align="center">
  Built with Spring Boot 3 · Secured with JWT · Cached with Redis · Containerized with Docker
</p>

---

## 🎯 What Is This?

EcomStore is a **complete backend** for an e-commerce platform. It handles:

> **User signs up → browses products → adds to cart → places order → gets email confirmation → admin ships it**

Every step above is a working API endpoint, secured, validated, and tested.

---

## ⚡ Quick Start (2 minutes)

```bash
git clone https://github.com/your-username/ecom-store.git
cd ecom-store/Ecommerce_SpringBoot_App/sb-ecom/sb-ecom

# Start PostgreSQL + Redis in Docker
docker-compose up postgres redis -d

# Run the app
./mvnw spring-boot:run
```

**That's it.** Open 👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 🔑 Default Accounts (auto-seeded)

| Username | Password | Role |
|:--------:|:--------:|:----:|
| `admin` | `adminPass` | 👑 ADMIN + SELLER + USER |
| `seller1` | `password2` | 🏪 SELLER |
| `user1` | `password1` | 🛍️ USER |

---

## 🏗️ Architecture at a Glance

```
                    ┌─────────────────────────┐
                    │      Client / Postman   │
                    └────────────┬────────────┘
                                 │ HTTPS
                                 ▼
┌────────────────────────────────────────────────────────┐
│                   SPRING SECURITY                      │
│              JWT Cookie Authentication                 │
│         ┌──────────────────────────────┐               │
│         │      AuthTokenFilter         │               │
│         │  Extract → Validate → Auth   │               │
│         └──────────────────────────────┘               │
├────────────────────────────────────────────────────────┤
│                                                        │
│   🔐 Auth    📦 Product   🗂️ Category   🛒 Cart      │
│   Controller  Controller   Controller    Controller    │
│                                                        │
│   📋 Order   📍 Address                               │
│   Controller  Controller                               │
│                                                        │
├────────────────────────────────────────────────────────┤
│                   SERVICE LAYER                        │
│         Business Logic · Validation · Mapping          │
│                                                        │
│   📧 EmailService (Async)   📁 FileService (Upload)   │
├────────────────────────────────────────────────────────┤
│                  REPOSITORY LAYER                      │
│            Spring Data JPA Repositories                │
├──────────────────────┬─────────────────────────────────┤
│                      │                                 │
│    ┌─────────────────▼──────────┐  ┌────────────────┐  │
│    │   🐘 PostgreSQL 16         │ │  🔴 Redis 7    │  │
│    │   (Flyway-managed schema)  │  │  (Cache layer) │  │
│    └────────────────────────────┘  └────────────────┘  │
└────────────────────────────────────────────────────────┘
```

---

## ✨ Features

### 🔐 Authentication & Security
- **JWT via HTTP-only cookies** — prevents JavaScript from directly accessing the token, reducing XSS-based token theft
- **3 roles**: `USER`, `SELLER`, `ADMIN` with method-level authorization
- **BCrypt** password hashing
- Auto-seeded roles & default users on first startup

### 📦 Product Catalog
- Full CRUD (Admin-only create/update/delete)
- **Search** by keyword with `LIKE` query
- **Filter** by category
- **Pagination + sorting** on all list endpoints
- **Image upload** to local filesystem
- **Redis caching** with automatic eviction on data changes
- **Optimistic locking** (`@Version`) to prevent race conditions on stock updates

### 🛒 Shopping Cart
- Auto-created per user on first add
- Add / remove / update quantity
- Validates stock availability
- **Auto-syncs** when admin updates or deletes a product

### 📋 Order Processing
- One-click order from cart contents
- **Stock validation** before order placement
- **Atomic stock reduction** with optimistic locking
- Payment record creation (gateway-ready structure)
- Order status lifecycle:

```
PENDING → ACCEPTED → PROCESSING → SHIPPED → DELIVERED
                                      ↘
                                   CANCELLED → REFUNDED
```

### 📧 Email Notifications (Async)
| Event | Email Sent |
|-------|-----------|
| User signs up | ✉️ Welcome email |
| Order placed | ✉️ Order confirmation with amount |
| Status updated | ✉️ Status change notification |

All emails run on background threads (`@Async`) — they never slow down API responses.

### 📍 Address Management
- Users manage multiple shipping addresses
- Link any address to an order at checkout

### 🛡️ Error Handling
- Global `@ControllerAdvice` exception handler
- Structured error responses with proper HTTP status codes
- Custom exceptions: `ResourceNotFoundException`, `APIExceptions`, `LogicException`

---

## 🔌 API Reference

> Full interactive docs at `/swagger-ui.html` when the app is running.

<details>
<summary><strong>🔐 Auth</strong> — <code>/api/auth</code></summary>

| Method | Endpoint | Description |
|:------:|----------|-------------|
| `POST` | `/api/auth/signup` | Register a new user |
| `POST` | `/api/auth/signin` | Login → returns JWT cookie |
| `POST` | `/api/auth/signout` | Logout → clears JWT cookie |
| `GET` | `/api/auth/user` | Get logged-in user details |
| `GET` | `/api/auth/username` | Get current username |

</details>

<details>
<summary><strong>📦 Products</strong> — <code>/api</code></summary>

| Method | Endpoint | Access | Description |
|:------:|----------|:------:|-------------|
| `GET` | `/api/public/products` | 🌐 Public | List products (paginated) |
| `GET` | `/api/public/products/keyword/{keyword}` | 🌐 Public | Search by keyword |
| `GET` | `/api/public/categories/{id}/products` | 🌐 Public | Filter by category |
| `POST` | `/api/admin/categories/{id}/product` | 👑 Admin | Create product |
| `PUT` | `/api/admin/products/{id}` | 👑 Admin | Update product |
| `DELETE` | `/api/admin/products/{id}` | 👑 Admin | Delete product |
| `PUT` | `/api/products/{id}/image` | 🔒 Auth | Upload product image |

</details>

<details>
<summary><strong>🗂️ Categories</strong> — <code>/api</code></summary>

| Method | Endpoint | Access | Description |
|:------:|----------|:------:|-------------|
| `GET` | `/api/public/categories` | 🌐 Public | List categories (paginated) |
| `GET` | `/api/public/categories/{id}` | 🌐 Public | Get by ID |
| `POST` | `/api/public/categories` | 🔒 Auth | Create category |
| `PUT` | `/api/public/categories/{id}` | 🔒 Auth | Update category |
| `DELETE` | `/api/admin/categories/{id}` | 👑 Admin | Delete category |

</details>

<details>
<summary><strong>🛒 Cart</strong> — <code>/api</code> (All Authenticated)</summary>

| Method | Endpoint | Description |
|:------:|----------|-------------|
| `POST` | `/api/carts/products/{productId}/quantity/{qty}` | Add product to cart |
| `GET` | `/api/carts/users/cart` | Get my cart |
| `GET` | `/api/carts` | Get all carts |
| `PUT` | `/api/cart/products/{productId}/quantity/{operation}` | +1 or -1 quantity |
| `DELETE` | `/api/carts/{cartId}/product/{productId}` | Remove product |

</details>

<details>
<summary><strong>📋 Orders</strong> — <code>/api</code></summary>

| Method | Endpoint | Access | Description |
|:------:|----------|:------:|-------------|
| `POST` | `/api/order/users/payments/{paymentMethod}` | 🔒 Auth | Place order |
| `GET` | `/api/order/users/orders` | 🔒 Auth | My orders |
| `GET` | `/api/admin/orders` | 👑 Admin | All orders |
| `PUT` | `/api/admin/orders/{id}/status` | 👑 Admin | Update order status |

</details>

<details>
<summary><strong>📍 Addresses</strong> — <code>/api</code> (All Authenticated)</summary>

| Method | Endpoint | Description |
|:------:|----------|-------------|
| `POST` | `/api/addresses` | Create address |
| `GET` | `/api/addresses` | List all addresses |
| `GET` | `/api/addresses/{id}` | Get by ID |
| `GET` | `/api/users/addresses` | My addresses |
| `PUT` | `/api/addresses/{id}` | Update |
| `DELETE` | `/api/addresses/{id}` | Delete |

</details>

---

## 🗄️ Database Design

```sql
┌──────────┐       ┌────────────┐       ┌──────────┐
│  users   │──M:N──│ user_roles │──M:N──│  roles   │
└────┬─────┘       └────────────┘       └──────────┘
     │ 1:N
     ├─────────────────┐
     ▼                 ▼
┌──────────┐     ┌──────────┐
│addresses │     │  carts   │
└──────────┘     └────┬─────┘
                      │ 1:N
                      ▼
                ┌────────────┐     ┌──────────────┐
                │ cart_items │───▶│   products    │
                └────────────┘     └──────┬───────┘
                                          │
┌──────────┐     ┌──────────────┐         │
│  orders  │───▶│ order_items   │─────────┘
└────┬─────┘     └──────────────┘
     │ 1:1
     ▼
┌──────────┐
│ payments │
└──────────┘
```

### Flyway Migrations
| Version | File | What It Does |
|:-------:|------|-------------|
| V1 | `V1__init_schema.sql` | Creates all tables, constraints, indexes |
| V2 | `V2__fix_orders_email_constraint.sql` | Fixes email constraint on orders |
| V3 | `V3__add_product_version.sql` | Adds version column for optimistic locking |

---

## 🐳 Docker

### Multi-Stage Build

The `Dockerfile` uses a **2-stage build** for minimal image size:

```
Stage 1 (Build)                    Stage 2 (Run)
┌─────────────────────┐           ┌──────────────────────┐
│ maven:3.9-temurin-21│           │ eclipse-temurin:21   │
│                     │           │       -jre-alpine    │
│ • Download deps     │    COPY   │                      │
│ • Compile           │ ───────▶ │ • Just the JAR       │
│ • Package JAR       │   .jar    │ • ~200MB final image │
│                     │           │                      │
│   (~800MB)          │           │   (Lean & fast)      │
└─────────────────────┘           └──────────────────────┘
```

### Run Everything in Docker

```bash
# One-time: create secrets file
echo "DB_PASSWORD=#Pubg8080" > .env

# Build & launch all 3 containers
docker-compose up --build

# Or run in background
docker-compose up --build -d
```

### Run Only DB + Redis (⭐ Best for Development)

```bash
docker-compose up postgres redis -d
# Then run app from your IDE with breakpoints, hot-reload, etc.
```

### Docker Commands Cheat Sheet

```bash
docker-compose ps                    # Check status
docker-compose logs -f app           # Stream app logs
docker-compose down                  # Stop everything
docker-compose down -v               # Stop + delete DB data
docker-compose up --build -d app     # Rebuild & restart app only
docker exec -it ecom-postgres psql -U postgres -d ecommerce_db  # DB shell
```

---

## ⚙️ Environment Profiles

| Profile | Activated By | Database | Redis | Logging |
|:-------:|-------------|:--------:|:-----:|:-------:|
| `dev` | Default in IDE | `localhost:5432` | `localhost:6379` | DEBUG |
| `prod` | `SPRING_PROFILES_ACTIVE=prod` | `postgres:5432` (Docker) | `redis:6379` | WARN |
| `aws` | `SPRING_PROFILES_ACTIVE=aws` | RDS endpoint | ElastiCache | INFO |

```
src/main/resources/
├── application.properties           ← Shared (JWT, mail, Flyway)
├── application-dev.properties       ← Local dev
├── application-prod.properties      ← Docker
└── application-aws.properties       ← AWS deployment
```

---

## 📁 Project Structure

```
src/main/java/com/ecommerce/project/
│
├── 🔧 config/                  # App configuration
│   ├── AppConfig.java           # ModelMapper bean
│   ├── AppConstants.java        # Pagination defaults
│   └── RedisCacheConfig.java    # Cache configuration
│
├── 🌐 controller/              # REST endpoints
│   ├── AuthController.java      # Signup, signin, signout
│   ├── ProductController.java   # Product CRUD + search
│   ├── CategoryController.java  # Category CRUD
│   ├── CartController.java      # Cart operations
│   ├── OrderController.java     # Order placement + tracking
│   └── AddressController.java   # Address management
│
├── ⚠️ exeptions/               # Error handling
│   ├── MyGlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── APIExceptions.java
│   └── LogicException.java
│
├── 📊 model/                   # JPA entities
│   ├── User.java, Role.java, AppRole.java
│   ├── Product.java, Category.java
│   ├── Cart.java, CartItem.java
│   ├── Order.java, OrderItem.java, OrderStatus.java
│   ├── Payment.java
│   └── Address.java
│
├── 📨 payload/                 # DTOs & response wrappers
│   ├── ProductDTO.java, ProductResponse.java
│   ├── CategoryDTO.java, CategoryResponse.java
│   ├── CartDTO.java, CartItemDTO.java
│   ├── OrderDTO.java, OrderItemDTO.java, OrderRequestDTO.java
│   ├── PaymentDTO.java, AddressDTO.java
│   └── APIResponse.java
│
├── 🗃️ repositories/            # Data access
│   └── *Repository.java (10 repositories)
│
├── 🔐 security/
│   ├── WebSecurityConfig.java   # Security filter chain + data seeding
│   └── jwt/
│       ├── AuthTokenFilter.java # Intercepts every request
│       ├── JwtUtils.java        # Token create/validate/parse
│       ├── AuthEntryPointJwt.java # 401 handler
│       └── services/
│           └── UserDetailsServiceImpl.java
│
├── ⚙️ service/                 # Business logic
│   ├── ProductService[Impl].java
│   ├── CategoryService[Impl].java
│   ├── CartService[Impl].java
│   ├── OrderService[Impl].java
│   ├── AddressService[Impl].java
│   ├── FileService[Impl].java   # Image upload
│   └── EmailService.java        # Async email notifications
│
└── 🔧 util/
    └── AuthUtil.java            # Get logged-in user helper
```

---

## 🚀 Deployment to AWS

### Target Architecture


<img src="infrastructure-diagram.svg">

```
User → EC2 (Docker: Spring Boot + Redis) → RDS PostgreSQL
```

| Component | Setup |
|---|---|
| Compute | EC2, Amazon Linux 2023, t2.micro |
| Database | RDS PostgreSQL, ap-south-1 (Mumbai) |
| Cache | Redis 7 (Docker container, same EC2 instance) |
| Containerization | Docker + Docker Compose |
| Schema migrations | Flyway (versioned, auto-applied on startup) |
| Networking | Security groups — RDS accepts inbound `5432` only from EC2's specific security group |


### Deploy

```bash
# On EC2 instance
docker-compose -f docker-compose.aws.yml up --build -d
```

## 🛠️ Tech Decisions & Why

| Decision | Why |
|----------|-----|
| **JWT in cookies** (not headers) | Immune to XSS; browser manages token automatically |
| **Redis caching** | Product listings are read-heavy; cache reduces DB load by ~90% |
| **Flyway** (not `ddl-auto=update`) | Version-controlled, reproducible, safe for production |
| **Optimistic locking** on products | Prevents overselling during concurrent purchases |
| **Async emails** (`@Async`) | Email SMTP calls take 1-3s; async prevents blocking API response |
| **Multi-stage Docker build** | Final image is ~200MB instead of ~800MB |
| **ModelMapper** | Eliminates boilerplate entity↔DTO conversion code |
| **Spring Profiles** | Same codebase works in dev, Docker, and AWS without code changes |

---

## 📜 License

This project is built for learning and portfolio purposes.

---

<p align="center">
  <strong>⭐ Star this repo if you found it helpful!</strong>
</p>
