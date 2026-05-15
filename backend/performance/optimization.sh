#!/bin/bash

echo "=== Performance Optimization Script for EduCollege University System ==="
echo "Applying database, caching, and application optimizations"
echo ""

# Configuration
POSTGRES_HOST="localhost"
POSTGRES_PORT="5432"
POSTGRES_USER="educollege_user"
REDIS_HOST="localhost"
REDIS_PORT="6380"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${BLUE}=== $1 ===${NC}"
}

print_success() {
    echo -e "${GREEN}SUCCESS: $1${NC}"
}

print_error() {
    echo -e "${RED}ERROR: $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}WARNING: $1${NC}"
}

# Check if running as root
if [[ $EUID -ne 0 ]]; then
   print_error "This script must be run as root (use sudo)"
   exit 1
fi

# ========================================
# 1. Database Optimization
# ========================================

print_status "Optimizing PostgreSQL databases"

# Apply optimization to EduCollege database
databases=("educollege_db")

for db in "${databases[@]}"; do
    print_status "Optimizing database: $db"
    
    # Apply optimization script
    if PGPASSWORD="${POSTGRES_PASSWORD:-educollege_Admin_2025}" psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -f performance/postgresql-optimization.sql; then
        print_success "Database $db optimized"
    else
        print_error "Failed to optimize database $db"
    fi
done

# ========================================
# 3. Application Performance Tuning
# ========================================

print_status "Optimizing application performance"

# Set system limits for application
cat > /etc/security/limits.d/educollege-performance.conf << 'EOF'
# Performance limits for EduCollege University System
* soft nofile 1048576
* hard nofile 1048576
EOF

# Configure kernel parameters for performance
cat > /etc/sysctl.d/99-educollege-performance.conf << 'EOF'
# Performance kernel parameters for EduCollege University System
net.core.rmem_max = 134217728
net.core.wmem_max = 134217728
vm.swappiness = 10
EOF

# Apply kernel parameters
sysctl -p /etc/sysctl.d/99-educollege-performance.conf

print_success "System performance limits configured"

# ========================================
# 6. JVM Performance Tuning
# ========================================

print_status "Creating JVM performance configuration"

# Create JVM options file
mkdir -p /opt/educollege
cat > /opt/educollege/jvm-options << 'EOF'
# JVM Performance Options for EduCollege University System
-Xms512m
-Xmx1g
-XX:+UseG1GC
-XX:+UseStringDeduplication
-XX:+UseContainerSupport
-Dspring.profiles.active=production
-Dlogging.file.name=/var/log/educollege/application.log
EOF

print_success "JVM performance configuration created"

# ========================================
# 7. Monitoring Performance Metrics
# ========================================

print_status "Setting up performance monitoring"

# Create performance monitoring script
cat > /usr/local/bin/educollege-monitor.sh << 'EOF'
#!/bin/bash
# Performance Monitoring Script for EduCollege University System

echo "=== System Metrics ==="
echo "CPU Usage: $(top -bn1 | grep "Cpu(s)" | awk '{print $2}' | cut -d'%' -f1)%"
echo "Memory Usage: $(free | grep Mem | awk '{printf("%.2f%%"), $3/$2 * 100.0}')"
EOF

chmod +x /usr/local/bin/educollege-monitor.sh

print_success "Performance monitoring configured"
