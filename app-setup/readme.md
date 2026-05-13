
# EduCollege App Setup

## 1. Khởi động services

```bash
docker compose up -d
```

## 2. Tắt services

```bash
docker compose down -v
```

## 3. Xóa toàn bộ (khi cần reset)
```bash
docker compose down -v --rmi all --remove-orphans
```

===================================================================

## Kết nối PostgreSQL

**Tên container:** `educollege_postgres`
**Database:** `educollege_db`
**User:** `educollege_user`
**Password:** `educollege_Admin_2025`

### Cách 1: Kết nối trực tiếp
```bash
docker exec -it educollege_postgres psql -U educollege_user -d educollege_db
```

### Cách 2: Kết nối từ local machine
```bash
psql -h localhost -p 5432 -U educollege_user -d educollege_db
```

### Cách 3: Dùng lệnh trực tiếp (không cần TTY)
```bash
# Xem tables
docker exec educollege_postgres psql -U educollege_user -d educollege_db -c "\dt"

# Xem schemas
docker exec educollege_postgres psql -U educollege_user -d educollege_db -c "\dn"

# Reset database
docker exec educollege_postgres psql -U educollege_user -d postgres -c "DROP DATABASE educollege_db; CREATE DATABASE educollege_db;"
```

## Service URLs

- 📊 MinIO Console: http://localhost:9090 (minioadmin/minioadmin)
- 📁 MinIO API: http://localhost:9000
- 🐰 RabbitMQ Management: http://localhost:15672 (admin/admin123)
- 🗄️ PostgreSQL: localhost:5432
- 🔴 Redis: localhost:6380

