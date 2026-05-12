-- EduCollege PostgreSQL Initialization Script

-- 1. Tạo User
CREATE USER educollege_user WITH PASSWORD 'educollege_Admin_2025';

-- 2. Tạo Database
CREATE DATABASE educollege_db OWNER educollege_user;

-- 3. Cấp quyền
GRANT ALL PRIVILEGES ON DATABASE educollege_db TO educollege_user;

-- 4. Enable pgvector extension cho vector search
\c educollege_db;
CREATE EXTENSION IF NOT EXISTS vector;

-- 5. Cấu hình additional settings
ALTER USER educollege_user CREATEDB;

-- 6. Grant connection privileges
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO educollege_user;

-- 7. Grant sequence privileges
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO educollege_user;

