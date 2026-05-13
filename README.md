# ỨNG DỤNG QUẢN LÝ RẠP PHIM

# Hướng dẫn chạy dự án

## Yêu cầu hệ thống

| Công cụ | Phiên bản tối thiểu |
|---|---|
| JDK | 17 trở lên |
| Maven | 3.6 trở lên (hoặc dùng Maven Wrapper `./mvnw`) |
| MySQL Server | 8.0 trở lên |

## Bước 1: Cài đặt và khởi động MySQL

Đảm bảo MySQL Server đã được cài đặt và đang chạy trên máy (cổng mặc định `3306`).

## Bước 2: Tạo Database

Mở MySQL client (MySQL Workbench, DBeaver, hoặc terminal) và chạy lệnh:

```sql
CREATE DATABASE IF NOT EXISTS defaultdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> **Lưu ý:** Ứng dụng sử dụng Spring JPA với `ddl-auto=update`, các bảng sẽ được **tự động tạo** khi khởi chạy lần đầu.

## Bước 3: Cấu hình kết nối (nếu cần)

Mở file `CinemaManagementApplication/complete/src/main/resources/application.properties` và chỉnh lại thông tin nếu MySQL của bạn có cấu hình khác:

```properties
my.database.url=jdbc:mysql://localhost:3306/defaultdb?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true
my.database.username=root
my.database.password=      # Điền mật khẩu MySQL của bạn vào đây
```

## Bước 4: Chạy ứng dụng

### Cách 1: Dùng Maven Wrapper (khuyến nghị)

```bash
cd CinemaManagementApplication/complete
./mvnw spring-boot:run
```

> Trên Windows: `mvnw.cmd spring-boot:run`

### Cách 2: Dùng Maven đã cài sẵn

```bash
cd CinemaManagementApplication/complete
mvn spring-boot:run
```

### Cách 3: Chạy từ IDE (IntelliJ IDEA / Eclipse)

1. Mở project `CinemaManagementApplication/complete` trong IDE.
2. Tìm class `ServingWebContentApplication.java`.
3. Click **Run** (hoặc nhấn `Shift + F10` trong IntelliJ).

## Bước 5: Truy cập ứng dụng

Sau khi ứng dụng khởi động thành công, mở trình duyệt và truy cập:

```
http://localhost:8080
```

---


