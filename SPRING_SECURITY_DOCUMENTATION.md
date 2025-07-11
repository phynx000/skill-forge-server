# Tài liệu Spring Security - Dự án SkillForge

## Tổng quan

Dự án SkillForge sử dụng Spring Security với JWT (JSON Web Token) để xác thực và phân quyền. Hệ thống được thiết kế theo mô hình stateless, không lưu trữ session trên server.

## Kiến trúc tổng thể

```
Client Request → JwtAuthenticationFilter → SecurityFilterChain → Controller
                       ↓
                 JWT Validation
                       ↓
                 Set Authentication Context
```

## Các thành phần chính

### 1. SecurityConfiguration.java
**Vai trò**: Cấu hình chính của Spring Security

**Chức năng chính**:
- Cấu hình JWT Encoder/Decoder
- Thiết lập SecurityFilterChain
- Cấu hình Authentication Manager
- Định nghĩa Password Encoder

**Chi tiết cấu hình**:
```java
// JWT Secret Key từ application.properties
@Value("${jwt.secret}")
private String jwtSecretKey;

// Thời gian hết hạn JWT (8640000 giây = 100 ngày)
@Value("${jwt.expiration}")
private Long jwtExpiration;
```

**Security Filter Chain**:
- Tắt CSRF (do sử dụng JWT)
- Cho phép truy cập tự do đến `/` và `/login`
- Yêu cầu xác thực cho tất cả request khác
- Sử dụng Custom JWT Filter
- Stateless session management

### 2. JwtAuthenticationFilter.java
**Vai trò**: Filter chính xử lý JWT trong mỗi request

**Luồng hoạt động**:
1. Trích xuất JWT token từ header `Authorization: Bearer <token>`
2. Validate và decode JWT token
3. Chuyển đổi JWT thành Authentication object
4. Set Authentication vào SecurityContext
5. Tiếp tục filter chain

**Xử lý lỗi**:
- Nếu JWT không hợp lệ: log lỗi và tiếp tục (để trả về 401 sau)
- Không throw exception để không ngắt filter chain

### 3. JwtAuthenticationConverter.java
**Vai trò**: Chuyển đổi JWT thành Spring Security Authentication

**Cơ chế**:
- Lấy username từ JWT subject
- Trích xuất roles từ JWT claim "roles"
- Tạo SimpleGrantedAuthority cho mỗi role
- Trả về UsernamePasswordAuthenticationToken

### 4. CustomAuthenticationEntryPoint.java
**Vai trò**: Xử lý lỗi xác thực (401 Unauthorized)

**Chức năng**:
- Trả về JSON response thay vì HTML mặc định
- Format lỗi theo cấu trúc RestResponse
- Thông báo lỗi tiếng Việt cho user

### 5. SecurityUtils.java
**Vai trò**: Utility class tạo JWT token

**Chức năng**:
- Tạo JWT token sau khi đăng nhập thành công
- Thiết lập thời gian hết hạn
- Thêm roles vào JWT claims
- Sử dụng HS512 algorithm

### 6. UserDetailCustom.java
**Vai trò**: Custom UserDetailsService

**Chức năng**:
- Load user từ database theo email
- Chuyển đổi User entity thành Spring Security UserDetails
- Thêm role prefix "ROLE_" cho Spring Security

## Luồng xác thực hoàn chỉnh

### 1. Đăng nhập (Login)
```
1. Client gửi POST /login với email/password
2. AuthController xác thực thông tin
3. Nếu hợp lệ → SecurityUtils tạo JWT token
4. Trả về JWT token cho client
```

### 2. Truy cập API được bảo vệ
```
1. Client gửi request với header: Authorization: Bearer <JWT>
2. JwtAuthenticationFilter nhận request
3. Trích xuất và validate JWT token
4. JwtAuthenticationConverter chuyển đổi JWT → Authentication
5. Set Authentication vào SecurityContext
6. Controller xử lý request với thông tin user đã xác thực
```

### 3. Xử lý lỗi xác thực
```
1. JWT không hợp lệ/hết hạn
2. CustomAuthenticationEntryPoint được kích hoạt
3. Trả về JSON response với status 401
4. Message lỗi tiếng Việt
```

## Cấu hình JWT

### application.properties
```properties
# Secret key cho JWT (Base64 encoded)
jwt.secret=L+OrcI8LddNx1t+TbvZo9ttaUiZbdVcWOFJXe5HBrgytYTgW8dQL8/aJXJvHoxiZ45zyNy2w+Iw1ntCXYB+U1A==

# Thời gian hết hạn: 8640000 giây = 100 ngày
jwt.expiration=8640000
```

### Cấu trúc JWT Token
```json
{
  "sub": "user@example.com",
  "roles": ["ROLE_USER"],
  "iat": 1625140800,
  "exp": 1625227200
}
```

## Bảo mật

### Điểm mạnh
1. **Stateless**: Không lưu session trên server
2. **JWT Signature**: Token được ký số với secret key
3. **Role-based Authorization**: Phân quyền theo role
4. **Password Encoding**: Sử dụng BCrypt
5. **CSRF Protection**: Tắt CSRF do sử dụng JWT

### Lưu ý bảo mật
1. **Secret Key**: Phải được bảo mật tuyệt đối
2. **HTTPS**: Nên sử dụng HTTPS trong production
3. **Token Expiration**: Thiết lập thời gian hết hạn hợp lý
4. **Token Storage**: Client nên lưu token an toàn

## Endpoints công khai

- `/` - Trang chủ
- `/login` - Đăng nhập

## Các endpoint khác đều yêu cầu JWT token hợp lệ

## Troubleshooting

### Lỗi thường gặp

1. **401 Unauthorized**
   - Token không có trong header
   - Token hết hạn
   - Token không hợp lệ
   - Secret key không đúng

2. **403 Forbidden**
   - User không có quyền truy cập resource
   - Role không phù hợp

3. **JWT Parsing Error**
   - Token format không đúng
   - Secret key khác với khi tạo token

### Debug
- Kiểm tra log JWT error trong console
- Verify JWT token tại jwt.io
- Kiểm tra header Authorization format

## Mở rộng

### Thêm role mới
1. Cập nhật Role entity
2. Thêm role vào database
3. Cấu hình authorization trong SecurityConfiguration

### Refresh Token
Hiện tại chưa implement refresh token. Có thể thêm:
1. Refresh token endpoint
2. Token blacklist
3. Sliding session

### OAuth2 Integration
Có thể tích hợp OAuth2 providers (Google, Facebook) bằng cách:
1. Thêm OAuth2 dependencies
2. Cấu hình OAuth2 trong SecurityConfiguration
3. Tạo OAuth2 success handler

---
*Tài liệu này mô tả cơ chế Spring Security của dự án SkillForge tại thời điểm hiện tại.*
