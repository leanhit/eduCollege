# Test ID Keys for EduCollege

## Initial Test ID Keys

Khi ứng dụng khởi động, các ID keys sau sẽ được tạo tự động để test:

### Student ID Keys
- `STU20241235` - First student ID key
- `STU20241236` - Second student ID key
- `STU20241237` - Third student ID key

### Faculty ID Keys  
- `FAC0001001` - First faculty ID key
- `FAC0001002` - Second faculty ID key
- `FAC0001003` - Third faculty ID key

### Employee ID Keys
- `EMP0005001` - First employee ID key
- `EMP0005002` - Second employee ID key
- `EMP0005003` - Third employee ID key

### Staff ID Keys
- `STA0003001` - First staff ID key
- `STA0003002` - Second staff ID key
- `STA0003003` - Third staff ID key

## Cách sử dụng

### 1. Đăng ký user mới
Sử dụng các ID keys trên khi đăng ký user mới:

```json
POST /api/v1/users/register
{
  "username": "student1",
  "email": "student1@educollege.edu",
  "password": "Password123!",
  "confirmPassword": "Password123!",
  "fullName": "Student One",
  "phoneNumber": "0123456789",
  "idKey": "STU20241235"
}
```

### 2. Đăng nhập
Sau khi đăng ký, sử dụng username và password để đăng nhập:

```json
POST /api/v1/auth/login
{
  "username": "student1",
  "password": "Password123!"
}
```

### 3. Role Mapping
- `STU` prefix → STUDENT role
- `FAC` prefix → FACULTY role
- `EMP` prefix → EMPLOYEE role
- `STA` prefix → STAFF role

## Lưu ý
- Mỗi ID key chỉ được sử dụng một lần
- Role được tự động xác định dựa trên prefix của ID key
- Password phải đáp ứng yêu cầu: ít nhất 8 ký tự, chứa chữ hoa, chữ thường, số và ký tự đặc biệt
