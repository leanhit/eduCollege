#!/bin/bash

echo "=== Automated Backup Script for EduCollege University System ==="
echo "Running comprehensive automated backup system for academic data"
echo ""

# Configuration
BACKUP_DIR="/backups/educollege"
DATE=$(date +%Y%m%d_%H%M%S)
LOG_FILE="$BACKUP_DIR/logs/automated_backup_$DATE.log"
RETENTION_DAYS=30
REMOTE_BACKUP_ENABLED="${REMOTE_BACKUP_ENABLED:-false}"
REMOTE_BACKUP_PATH="${REMOTE_BACKUP_PATH:-s3://educollege-backups/}"
NOTIFICATION_EMAIL="${NOTIFICATION_EMAIL:-}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${BLUE}=== $1 ===${NC}"
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] === $1 ===" >> "$LOG_FILE"
}

print_success() {
    echo -e "${GREEN}SUCCESS: $1${NC}"
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] SUCCESS: $1" >> "$LOG_FILE"
}

print_error() {
    echo -e "${RED}ERROR: $1${NC}"
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] ERROR: $1" >> "$LOG_FILE"
}

print_warning() {
    echo -e "${YELLOW}WARNING: $1${NC}"
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] WARNING: $1" >> "$LOG_FILE"
}

# Initialize log
mkdir -p "$BACKUP_DIR/logs"
echo "=== Automated Backup Session Started: $(date) ===" >> "$LOG_FILE"

# Check if running as root
if [[ $EUID -ne 0 ]]; then
   print_error "This script must be run as root (use sudo)"
   exit 1
fi

# ========================================
# 1. Pre-Backup Checks
# ========================================

print_status "Running pre-backup checks"

# Check disk space
AVAILABLE_SPACE=$(df "$BACKUP_DIR" | awk 'NR==2 {print $4}')
REQUIRED_SPACE=5242880  # 5GB in KB

if [[ $AVAILABLE_SPACE -lt $REQUIRED_SPACE ]]; then
    print_error "Insufficient disk space for backup. Available: ${AVAILABLE_SPACE}KB, Required: ${REQUIRED_SPACE}KB"
    exit 1
fi

# Check service status
SERVICES_OK=true

if ! pg_isready -h localhost -U educollege_user; then
    print_error "PostgreSQL is not ready"
    SERVICES_OK=false
fi

if ! redis-cli -p 6380 ping &>/dev/null; then
    print_error "Redis is not responding"
    SERVICES_OK=false
fi

if ! curl -f http://localhost:8080/actuator/health &>/dev/null; then
    print_warning "Application is not responding - continuing with backup"
fi

if [[ "$SERVICES_OK" == "false" ]]; then
    print_error "Critical services are not ready - aborting backup"
    exit 1
fi

print_success "Pre-backup checks completed"

# ========================================
# 2. Database Backup
# ========================================

print_status "Starting database backup"

DATABASE_BACKUP_SUCCESS=true

# Run database backup script
if ./backup/backup-databases.sh >> "$LOG_FILE" 2>&1; then
    print_success "Database backup completed successfully"
else
    print_error "Database backup failed"
    DATABASE_BACKUP_SUCCESS=false
fi

# ========================================
# 3. Application Backup
# ========================================

print_status "Starting application backup"

APPLICATION_BACKUP_SUCCESS=true

# Run application backup script
if ./backup/backup-application.sh >> "$LOG_FILE" 2>&1; then
    print_success "Application backup completed successfully"
else
    print_error "Application backup failed"
    APPLICATION_BACKUP_SUCCESS=false
fi

# ========================================
# 5. Backup Verification
# ========================================

print_status "Verifying backup integrity"

BACKUP_VERIFICATION_SUCCESS=true

# Verify database backups
for db in educollege_db; do
    BACKUP_FILE="$BACKUP_DIR/$DATE/${db}_${DATE}.sql.gz"
    if [[ -f "$BACKUP_FILE" ]]; then
        if gunzip -t "$BACKUP_FILE" 2>/dev/null; then
            print_success "Database backup integrity verified: $db"
        else
            print_error "Database backup integrity check failed: $db"
            BACKUP_VERIFICATION_SUCCESS=false
        fi
    else
        print_error "Database backup file not found: $db"
        BACKUP_VERIFICATION_SUCCESS=false
    fi
done

# Verify application backups
APP_BACKUP_FILES=(
    "source_code_${DATE}.tar.gz"
    "config_${DATE}.tar.gz"
)

for file in "${APP_BACKUP_FILES[@]}"; do
    BACKUP_FILE="$BACKUP_DIR/$DATE/application/$file"
    if [[ -f "$BACKUP_FILE" ]]; then
        if tar -tzf "$BACKUP_FILE" &>/dev/null; then
            print_success "Application backup integrity verified: $file"
        else
            print_error "Application backup integrity check failed: $file"
            BACKUP_VERIFICATION_SUCCESS=false
        fi
    else
        print_error "Application backup file not found: $file"
        BACKUP_VERIFICATION_SUCCESS=false
    fi
done

# ========================================
# 7. Cleanup Old Backups
# ========================================

print_status "Cleaning up old backups"

# Clean up local backups older than retention period
find "$BACKUP_DIR" -type d -name "*" -mtime +$RETENTION_DAYS -exec rm -rf {} + 2>/dev/null

if [[ $? -eq 0 ]]; then
    print_success "Old backups cleaned up successfully"
else
    print_warning "Some old backups could not be removed"
fi

# ========================================
# 8. Backup Summary
# ========================================

print_status "Generating backup summary"

# Calculate backup sizes
TOTAL_SIZE=$(du -sh "$BACKUP_DIR/$DATE" 2>/dev/null | cut -f1 | head -1)

# Create summary report
cat > "$BACKUP_DIR/$DATE/backup_summary.txt" << EOF
EduCollege University System Automated Backup Summary
==================================================
Backup Date: $(date)
Backup Duration: $SECONDS seconds
Server: $(hostname)

Backup Results:
- Database Backup: $DATABASE_BACKUP_SUCCESS
- Application Backup: $APPLICATION_BACKUP_SUCCESS
- Backup Verification: $BACKUP_VERIFICATION_SUCCESS

Backup Total Size: $TOTAL_SIZE
Backup Location: $BACKUP_DIR/$DATE
Log File: $LOG_FILE

Next Scheduled Backup: $(date -d "+1 day" +"%Y-%m-%d %H:%M:%S")

Backup Retention: $RETENTION_DAYS days
EOF

print_success "Backup summary created: $BACKUP_DIR/$DATE/backup_summary.txt"

# ========================================
# 9. Health Check
# ========================================

print_status "Running post-backup health check"

# Check if services are still running
HEALTH_CHECK_PASSED=true

if ! pg_isready -h localhost -U educollege_user; then
    print_error "PostgreSQL health check failed"
    HEALTH_CHECK_PASSED=false
fi

if ! redis-cli -p 6380 ping &>/dev/null; then
    print_error "Redis health check failed"
    HEALTH_CHECK_PASSED=false
fi

if [[ "$HEALTH_CHECK_PASSED" == "true" ]]; then
    print_success "Post-backup health check passed"
else
    print_error "Post-backup health check failed"
fi

# ========================================
# 10. Notification
# ========================================

if [[ -n "$NOTIFICATION_EMAIL" ]]; then
    print_status "Sending backup notification"
    
    # Determine notification status
    if [[ "$DATABASE_BACKUP_SUCCESS" == "true" && "$APPLICATION_BACKUP_SUCCESS" == "true" && "$BACKUP_VERIFICATION_SUCCESS" == "true" && "$HEALTH_CHECK_PASSED" == "true" ]]; then
        SUBJECT="EduCollege Backup SUCCESS - $DATE"
        STATUS="SUCCESS"
    else
        SUBJECT="EduCollege Backup FAILURE - $DATE"
        STATUS="FAILURE"
    fi
    
    # Create notification email
    cat > /tmp/backup_notification.txt << EOF
Subject: $SUBJECT

EduCollege University System Automated Backup $STATUS
====================================================

Backup Details:
- Date: $(date)
- Server: $(hostname)
- Total Size: $TOTAL_SIZE

Results:
- Database Backup: $DATABASE_BACKUP_SUCCESS
- Application Backup: $APPLICATION_BACKUP_SUCCESS
- Backup Verification: $BACKUP_VERIFICATION_SUCCESS
- Health Check: $HEALTH_CHECK_PASSED

Backup Location: $BACKUP_DIR/$DATE
Log File: $LOG_FILE

This is an automated notification from EduCollege backup system.
EOF
    
    # Send email
    if command -v mail &> /dev/null; then
        mail -s "$SUBJECT" "$NOTIFICATION_EMAIL" < /tmp/backup_notification.txt
        print_success "Backup notification sent to $NOTIFICATION_EMAIL"
    else
        print_warning "Mail command not available - notification not sent"
    fi
    
    rm -f /tmp/backup_notification.txt
fi

print_success "Automated backup completed successfully!"
echo "Backup location: $BACKUP_DIR/$DATE"
echo "Total size: $TOTAL_SIZE"
echo "Log file: $LOG_FILE"
