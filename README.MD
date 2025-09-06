# SkillForge API

<div align="center">
  <h3>🚀 Nền tảng học trực tuyến hiện đại</h3>
  <p>API backend cho hệ thống quản lý khóa học và học tập trực tuyến SkillForge</p>
</div>

## 📋 Mục lục

- [Giới thiệu](#giới-thiệu)
- [Tính năng chính](#tính-năng-chính)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
- [Cài đặt và chạy dự án](#cài-đặt-và-chạy-dự-án)
- [Cấu hình](#cấu-hình)
- [API Documentation](#api-documentation)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Đóng góp](#đóng-góp)
- [Liên hệ](#liên-hệ)

## 🎯 Giới thiệu

SkillForge API là backend service cho nền tảng học trực tuyến hiện đại, được xây dựng với Spring Boot 3.5.3 và Java 17. Hệ thống cung cấp các API RESTful để quản lý khóa học, người dùng, thanh toán và streaming video.

## ✨ Tính năng chính

### 🔐 Xác thực và Phân quyền
- **JWT Authentication**: Hệ thống xác thực dựa trên JWT token
- **Role-based Access Control**: Phân quyền theo vai trò (Admin, Instructor, Student)
- **OAuth2 Resource Server**: Tích hợp OAuth2 để bảo mật API
- **Refresh Token**: Tự động làm mới token

### 👥 Quản lý Người dùng
- Đăng ký, đăng nhập, đăng xuất
- Quản lý hồ sơ người dùng
- Phân quyền và vai trò
- Lọc và phân trang danh sách người dùng

### 📚 Quản lý Khóa học
- Tạo và quản lý khóa học
- Tổ chức theo danh mục (Category)
- Quản lý bài học (Lesson) và phần (Section)
- Hệ thống đánh giá và review
- Theo dõi tiến độ học tập

### 🎥 Streaming Video
- Tích hợp BunnyStream cho video streaming
- Upload video với hỗ trợ file lớn (500MB)
- Quản lý quyền truy cập video
- API phát video an toàn

### 🛒 Giỏ hàng và Thanh toán
- Quản lý giỏ hàng
- Tích hợp VNPay cho thanh toán
- Xử lý đơn hàng và hóa đơn
- Theo dõi lịch sử giao dịch

### 📊 Quản lý Nội dung
- Hệ thống phân cấp danh mục
- Quản lý kỹ năng (Skills)
- Theo dõi tiến độ học tập
- Hệ thống ghi danh (Enrollment)

## 🛠 Công nghệ sử dụng

### Backend Framework
- **Spring Boot 3.5.3** - Framework chính
- **Spring Security** - Bảo mật và xác thực
- **Spring Data JPA** - ORM và quản lý database
- **Spring Validation** - Validation dữ liệu

### Database
- **PostgreSQL** - Database chính
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

## 💻 Yêu cầu hệ thống

- **Java**: 17 hoặc cao hơn
- **PostgreSQL**: 12 hoặc cao hơn
- **Gradle**: 7.0 hoặc cao hơn (hoặc sử dụng Gradle Wrapper)
- **RAM**: Tối thiểu 2GB
- **Disk**: Tối thiểu 1GB trống

## 🚀 Cài đặt và chạy dự án

### 1. Clone repository
```bash
git clone <repository-url>
cd skill_forge_api
```

### 2. Cài đặt PostgreSQL
- Cài đặt PostgreSQL
- Tạo database tên `skillforge_db`
- Tạo user `postgres` với password `1084` (hoặc cập nhật trong application.yml)

### 3. Cấu hình environment variables
Tạo file `.env` trong thư mục gốc:
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

### 4. Chạy ứng dụng

#### Sử dụng Gradle Wrapper (Khuyến nghị)
```bash
# Windows
./gradlew bootRun

# Linux/Mac
./gradlew bootRun
```

#### Sử dụng Gradle
```bash
gradle bootRun
```

### 5. Kiểm tra ứng dụng
- Ứng dụng sẽ chạy tại: `http://localhost:8080`
- Health check: `http://localhost:8080/actuator/health` (nếu có actuator)

## ⚙️ Cấu hình

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
POST   /api/v1/auth/login     - Đăng nhập
GET    /api/v1/auth/account   - Thông tin tài khoản
POST   /api/v1/auth/logout    - Đăng xuất
```

### User Management
```
GET    /api/v1/users          - Danh sách người dùng (có phân trang)
GET    /api/v1/users/{id}     - Chi tiết người dùng
POST   /api/v1/users          - Tạo người dùng mới
PUT    /api/v1/users          - Cập nhật người dùng
DELETE /api/v1/users/{id}     - Xóa người dùng
```

### Course Management
```
GET    /api/v1/courses        - Danh sách khóa học
POST   /api/v1/courses        - Tạo khóa học mới
GET    /api/v1/courses/{id}   - Chi tiết khóa học
PUT    /api/v1/courses/{id}   - Cập nhật khóa học
DELETE /api/v1/courses/{id}   - Xóa khóa học
```

### Category Management
```
GET    /api/v1/categories     - Danh sách danh mục (dạng cây)
POST   /api/v1/categories     - Tạo danh mục mới
DELETE /api/v1/categories/{id} - Xóa danh mục
```

### Video Management (BunnyStream)
```
POST   /api/v1/videos/upload-complete - Upload video hoàn chỉnh
GET    /api/v1/videos/{videoId}/play  - Lấy thông tin phát video
GET    /api/v1/videos/{videoId}       - Chi tiết video
```

### Cart & Payment
```
GET    /api/v1/cart           - Xem giỏ hàng
POST   /api/v1/cart/add       - Thêm vào giỏ hàng
DELETE /api/v1/cart/remove    - Xóa khỏi giỏ hàng
POST   /api/v1/payment/vnpay  - Thanh toán VNPay
```

## 📁 Cấu trúc dự án

```
src/
├── main/
│   ├── java/com/skillforge/skillforge_api/
│   │   ├── config/              # Cấu hình ứng dụng
│   │   │   ├── security/        # Cấu hình bảo mật
│   │   │   ├── BunnyStreamConfig.java
│   │   │   ├── VNPayConfig.java
│   │   │   └── ...
│   │   ├── controller/          # REST Controllers
│   │   │   ├── AuthController.java
│   │   │   ├── UserController.java
│   │   │   ├── CourseController.java
│   │   │   └── ...
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── request/         # Request DTOs
│   │   │   ├── response/        # Response DTOs
│   │   │   └── mapper/          # DTO Mappers
│   │   ├── entity/              # JPA Entities
│   │   │   ├── User.java
│   │   │   ├── Course.java
│   │   │   ├── Category.java
│   │   │   └── ...
│   │   ├── repository/          # JPA Repositories
│   │   ├── service/             # Business Logic
│   │   ├── utils/               # Utility Classes
│   │   └── SkillForgeApiApplication.java
│   └── resources/
│       ├── application.yml      # Cấu hình ứng dụng
│       ├── static/              # Static resources
│       └── templates/           # Templates
└── test/                        # Unit tests
```

## 🔧 Development

### Chạy tests
```bash
./gradlew test
```

### Build project
```bash
./gradlew build
```

### Clean build
```bash
./gradlew clean build
```






<div align="center">
  <p>Made with ❤️ by SkillForge Team</p>
  <p>© 2025 SkillForge. All rights reserved.</p>
</div>
