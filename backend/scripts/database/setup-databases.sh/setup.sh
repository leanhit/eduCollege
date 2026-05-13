#!/bin/bash

# Database Setup Script for eduCollege University System
# Creates single database for academic system

set -e

# Database connection parameters
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5432}
DB_USER=${DB_USER:-postgres}
DB_PASSWORD=${DB_PASSWORD:-password}

# Database name
EDUCOLLEGE_DB="educollege_db"

echo "🚀 Setting up eduCollege database..."

# Function to create database
create_database() {
    local db_name=$1
    echo "📦 Creating database: $db_name"
    
    PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d postgres -c "CREATE DATABASE $db_name;" || {
        echo "⚠️  Database $db_name already exists or failed to create"
    }
    
    echo "✅ Database $db_name ready"
}

# Create eduCollege database
create_database $EDUCOLLEGE_DB

# Grant permissions
echo "🔐 Setting up permissions..."
PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d postgres -c "
    GRANT ALL PRIVILEGES ON DATABASE $EDUCOLLEGE_DB TO $DB_USER;
"

echo "🎉 Database setup completed successfully!"
echo ""
echo "📋 Summary of created database:"
echo "  - $EDUCOLLEGE_DB (eduCollege University System)"
echo ""
echo "🔗 Connection details:"
echo "  Host: $DB_HOST"
echo "  Port: $DB_PORT"
echo "  User: $DB_USER"
