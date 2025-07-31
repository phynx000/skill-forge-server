Ư# Phân Tích Chức Năng Dự Án SkillForge API

Dựa trên việc phân tích các file controller trong thư mục `src/main/java/com/skillforge/skillforge_api/controller`, dự án đã hoàn thành các chức năng chính sau:

## 1. Quản lý Xác thực (Authentication) - `AuthController.java`

- **Đăng nhập:**
  - `POST /api/v1/auth/login`
  - Cho phép người dùng đăng nhập bằng `username` (email) và `password`.
  - Trả về `accessToken` và `refreshToken` (lưu trong cookie) sau khi xác thực thành công.

- **Lấy thông tin tài khoản hiện tại:**
  - `GET /api/v1/auth/account`
  - Lấy thông tin cơ bản của người dùng đang đăng nhập.

- **Đăng xuất:**
  - `POST /api/v1/auth/logout`
  - Xóa `refreshToken` và kết thúc phiên làm việc của người dùng.

## 2. Quản lý Video (BunnyStream) - `BunnyStreamController.java`

- **Tải lên video hoàn chỉnh:**
  - `POST /api/v1/videos/upload-complete`
  - API cho phép tạo và tải lên một video trong một lần gọi. Yêu cầu `title` và `file` video.

- **Lấy thông tin để phát video:**
  - `GET /api/v1/videos/{videoId}/play`
  - Trả về dữ liệu cần thiết để có thể phát một video, dựa trên `videoId`.

- **Lấy thông tin chi tiết của video:**
  - `GET /api/v1/videos/{videoId}`
  - Lấy thông tin chi tiết của một video từ BunnyStream.

## 3. Quản lý Danh mục (Category) - `CategoryController.java`

- **Tạo danh mục mới:**
  - `POST /api/v1/categories`
  - Cho phép tạo một danh mục mới.

- **Lấy danh mục dạng cây:**
  - `GET /api/v1/categories`
  - Trả về danh sách các danh mục được tổ chức theo cấu trúc cây (cha-con).

- **Xóa danh mục:**
  - `DELETE /api/v1/categories/{id}`
  - Xóa một danh mục dựa trên `id`.

## 4. Quản lý Khóa học (Course) - `CourseController.java`

- **Lấy danh sách khóa học (phân trang):**
  - `GET /api/v1/courses`
  - Trả về danh sách các khóa học có hỗ trợ phân trang.

- **Tạo khóa học mới:**
  - `POST /api/v1/courses`
  - Cho phép tạo một khóa học mới.

## 5. Quản lý Người dùng (User) - `UserController.java`

- **Lấy danh sách người dùng (phân trang và lọc):**
  - `GET /api/v1/users`
  - Trả về danh sách người dùng có hỗ trợ phân trang và bộ lọc động.

- **Lấy người dùng theo ID:**
  - `GET /api/v1/users/{id}`
  - Lấy thông tin chi tiết của một người dùng dựa trên `id`.

- **Tạo người dùng mới:**
  - `POST /api/v1/users`
  - Cho phép tạo một người dùng mới. Mật khẩu sẽ được mã hóa.

- **Cập nhật thông tin người dùng:**
  - `PUT /api/v1/users`
  - Cập nhật thông tin của một người dùng đã tồn tại.

- **Xóa người dùng:**
  - `DELETE /api/v1/users/{id}`
  - Xóa một người dùng dựa trên `id`.

