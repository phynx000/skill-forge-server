# SkillForge API

<div align="center">
  <h3>🚀 Modern Online Learning Platform</h3>
  <p>Backend API for SkillForge course management and online learning system</p>
</div>

## 📋 Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [System Requirements](#system-requirements)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Development](#development)
- [Contributing](#contributing)
- [Contact](#contact)

## 🎯 Overview

SkillForge API is a backend service for a modern online learning platform, built with Spring Boot 3.5.3 and Java 17. The system provides RESTful APIs for managing courses, users, payments, and video streaming.

## ✨ Key Features

### 🔐 Authentication & Authorization
- **JWT Authentication**: Token-based authentication system
- **Role-based Access Control**: Role-based permissions (Admin, Instructor, Student)
- **OAuth2 Resource Server**: OAuth2 integration for API security
- **Refresh Token**: Automatic token refresh mechanism

### 👥 User Management
- User registration, login, logout
- User profile management
- Role and permission management
- User listing with filtering and pagination

### 📚 Course Management
- Create and manage courses
- Organize by categories
- Manage lessons and sections
- Review and rating system
- Learning progress tracking

### 🎥 Video Streaming
- BunnyStream integration for video streaming
- Video upload with large file support (500MB)
- Video access control
- Secure video playback API

### 🛒 Cart & Payment
- Shopping cart management
- VNPay payment integration
- Order and invoice processing
- Transaction history tracking

### 📊 Content Management
- Hierarchical category system
- Skills management
- Learning progress tracking
- Enrollment system

## 🛠 Technology Stack

### Backend Framework
- **Spring Boot 3.5.3** - Main framework
- **Spring Security** - Security and authentication
- **Spring Data JPA** - ORM and database management
- **Spring Validation** - Data validation

### Database
- **PostgreSQL** - Primary database
- **Hibernate** - ORM mapping

### Security & Authentication
- **JWT (Nimbus JOSE)** - JSON Web Tokens
- **OAuth2 Resource Server** - OAuth2 integration
- **Spring Security** - Authentication & Authorization

### External Services
- **BunnyStream** - Video streaming service
- **VNPay** - Payment gateway
- **TUS Protocol** - Resumable file uploads

### Development Tools
- **Lombok** - Reduce boilerplate code
- **Spring Boot DevTools** - Development utilities
- **Gradle** - Build tool

### Additional Libraries
- **Jackson** - JSON processing
- **Apache HTTP Client** - HTTP requests
- **Spring Filter JPA** - Dynamic filtering
- **Commons Codec** - Encoding utilities

## 💻 System Requirements

- **Java**: 17 or higher
- **PostgreSQL**: 12 or higher
- **Gradle**: 7.0 or higher (or use Gradle Wrapper)
- **RAM**: Minimum 2GB
- **Disk**: Minimum 1GB free space

## 🚀 Installation & Setup

### 1. Clone repository
```bash
git clone <repository-url>
cd skill_forge_api
```

### 2. Setup PostgreSQL
- Install PostgreSQL
- Create database named `skillforge_db`
- Create user `postgres` with password `1084` (or update in application.yml)

### 3. Configure environment variables
Create `.env` file in root directory:
```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/skillforge_db
DB_USERNAME=postgres
DB_PASSWORD=1084

# JWT
JWT_SECRET=your-jwt-secret-key

# BunnyStream
BUNNY_STREAM_API_KEY=your-bunny-stream-api-key
BUNNY_STREAM_LIBRARY_ID=your-library-id

# VNPay
VNPAY_TMN_CODE=your-vnpay-tmn-code
VNPAY_HASH_SECRET=your-vnpay-hash-secret
```

### 4. Run the application

#### Using Gradle Wrapper (Recommended)
```bash
# Windows
./gradlew bootRun

# Linux/Mac
./gradlew bootRun
```

#### Using Gradle
```bash
gradle bootRun
```

### 5. Verify application
- Application will run at: `http://localhost:8080`
- Health check: `http://localhost:8080/actuator/health` (if actuator is enabled)

## ⚙️ Configuration

### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/skillforge_db
    username: postgres
    password: 1084
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### File Upload Configuration
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 500MB
      max-request-size: 500MB
```

### Server Configuration
```yaml
server:
  port: 8080
```

## 📖 API Documentation

### Authentication Endpoints
```
POST   /api/v1/auth/login     - User login
GET    /api/v1/auth/account   - Get account information
POST   /api/v1/auth/logout    - User logout
```

### User Management
```
GET    /api/v1/users          - List users (with pagination)
GET    /api/v1/users/{id}     - Get user details
POST   /api/v1/users          - Create new user
PUT    /api/v1/users          - Update user
DELETE /api/v1/users/{id}     - Delete user
```

### Course Management
```
GET    /api/v1/courses        - List courses
POST   /api/v1/courses        - Create new course
GET    /api/v1/courses/{id}   - Get course details
PUT    /api/v1/courses/{id}   - Update course
DELETE /api/v1/courses/{id}   - Delete course
```

### Category Management
```
GET    /api/v1/categories     - List categories (tree structure)
POST   /api/v1/categories     - Create new category
DELETE /api/v1/categories/{id} - Delete category
```

### Video Management (BunnyStream)
```
POST   /api/v1/videos/upload-complete - Complete video upload
GET    /api/v1/videos/{videoId}/play  - Get video playback info
GET    /api/v1/videos/{videoId}       - Get video details
```

### Cart & Payment
```
GET    /api/v1/cart           - View cart
POST   /api/v1/cart/add       - Add to cart
DELETE /api/v1/cart/remove    - Remove from cart
POST   /api/v1/payment/vnpay  - VNPay payment
```

### Enrollment Management
```
GET    /api/v1/enrollments    - List enrollments
POST   /api/v1/enrollments    - Create enrollment
DELETE /api/v1/enrollments/{id} - Cancel enrollment
```

### Learning Progress
```
GET    /api/v1/play-course/{courseId} - Get course playing info
POST   /api/v1/lessons/{lessonId}/complete - Mark lesson as complete
GET    /api/v1/sections/{sectionId}/progress - Get section progress
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/skillforge/skillforge_api/
│   │   ├── config/              # Application configuration
│   │   │   ├── security/        # Security configuration
│   │   │   ├── BunnyStreamConfig.java
│   │   │   ├── VNPayConfig.java
│   │   │   ├── DataInitializer.java
│   │   │   └── ...
│   │   ├── controller/          # REST Controllers
│   │   │   ├── AuthController.java
│   │   │   ├── UserController.java
│   │   │   ├── CourseController.java
│   │   │   ├── CartController.java
│   │   │   ├── PaymentController.java
│   │   │   └── ...
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── request/         # Request DTOs
│   │   │   ├── response/        # Response DTOs
│   │   │   └── mapper/          # DTO Mappers
│   │   ├── entity/              # JPA Entities
│   │   │   ├── User.java
│   │   │   ├── Course.java
│   │   │   ├── Category.java
│   │   │   ├── Cart.java
│   │   │   ├── Order.java
│   │   │   ├── Enrollments.java
│   │   │   └── ...
│   │   ├── repository/          # JPA Repositories
│   │   ├── service/             # Business Logic
│   │   ├── utils/               # Utility Classes
│   │   └── SkillForgeApiApplication.java
│   └── resources/
│       ├── application.yml      # Application configuration
│       ├── static/              # Static resources
│       └── templates/           # Templates
└── test/                        # Unit tests
    └── java/com/skillforge/skillforge_api/
        └── SkillForgeApiApplicationTests.java
```

## 🔧 Development

### Running Tests
```bash
./gradlew test
```

### Building Project
```bash
./gradlew build
```

### Clean Build
```bash
./gradlew clean build
```

### Development Mode
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## 🚨 Common Issues & Solutions

### Database Connection Issues
- Ensure PostgreSQL is running
- Check database credentials in `application.yml`
- Verify database `skillforge_db` exists

### Port Already in Use
- Change port in `application.yml`: `server.port: 8081`
- Or kill process using port 8080

### File Upload Issues
- Check file size limits in configuration
- Ensure sufficient disk space
- Verify BunnyStream API credentials

## 🔐 Security Considerations

- All API endpoints (except login) require JWT authentication
- Role-based access control is implemented
- Sensitive data is encrypted
- CORS is configured for frontend integration
- Input validation is enforced

## 📊 Performance & Monitoring

- Database queries are optimized with JPA
- Pagination is implemented for large datasets
- Connection pooling is configured
- Logging is structured for monitoring



### Development Guidelines
- Follow Java coding standards
- Write unit tests for new features
- Update documentation for API changes
- Use meaningful commit messages


## 🚀 Deployment

### Production Deployment
1. Build the application: `./gradlew build`
2. Configure production database
3. Set production environment variables
4. Deploy JAR file to server
5. Configure reverse proxy (Nginx/Apache)

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

---

<div align="center">
  <p>Made with ❤️ by SkillForge Team</p>
  <p>© 2025 SkillForge. All rights reserved.</p>
</div>
