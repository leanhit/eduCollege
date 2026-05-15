#!/bin/bash

echo "=== Backup Schedule Setup Script for EduCollege University System ==="
echo "Configuring automated backup schedules for academic data"
echo ""

# Configuration
BACKUP_SCRIPT_DIR="/home/ubuntu/ltanh/eduCollege/backend/backup"
NOTIFICATION_EMAIL="${NOTIFICATION_EMAIL:-admin@yourdomain.com}"
REMOTE_BACKUP_ENABLED="${REMOTE_BACKUP_ENABLED:-false}"

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
# 1. Create Backup User
# ========================================

print_status "Creating backup user"

# Create backup user if it doesn't exist
if ! id "backup" &>/dev/null; then
    useradd -r -m -s /bin/bash backup
    print_success "Backup user created"
else
    print_warning "Backup user already exists"
fi

# Add backup user to required groups
usermod -a -G docker backup 2>/dev/null || true
usermod -a -G sudo backup 2>/dev/null || true

# ========================================
# 2. Setup Backup Directories
# ========================================

print_status "Setting up backup directories"

# Create backup directories
mkdir -p /backups/educollege/logs
mkdir -p /backups/educollege/temp
mkdir -p /var/log/educollege/backup

# Set permissions
chown -R backup:backup /backups/educollege
chmod -R 755 /backups/educollege
chown -R backup:backup /var/log/educollege/backup
chmod -R 755 /var/log/educollege/backup

print_success "Backup directories created and configured"

# ========================================
# 3. Setup Cron Jobs
# ========================================

print_status "Setting up cron jobs"

# Create backup cron file
cat > /etc/cron.d/educollege-backup << EOF
# EduCollege University System Backup Schedule
# ===========================================

# Daily full backup at 2:00 AM
0 2 * * * backup /bin/bash $BACKUP_SCRIPT_DIR/automated-backup.sh >> /var/log/educollege/backup/daily_backup.log 2>&1

# Weekly database verification on Sunday at 3:00 AM
0 3 * * 0 backup /bin/bash $BACKUP_SCRIPT_DIR/backup-databases.sh >> /var/log/educollege/backup/weekly_verification.log 2>&1

# Monthly application backup on 1st at 4:00 AM
0 4 1 * * backup /bin/bash $BACKUP_SCRIPT_DIR/backup-application.sh >> /var/log/educollege/backup/monthly_backup.log 2>&1

# Backup cleanup daily at 5:00 AM
0 5 * * * backup find /backups/educollege -type d -name "*" -mtime +30 -exec rm -rf {} + >> /var/log/educollege/backup/cleanup.log 2>&1
EOF

# Reload cron service
systemctl reload cron

print_success "Cron jobs configured"

# ========================================
# 7. Setup Environment Variables
# ========================================

print_status "Setting up environment variables"

# Create backup environment file
cat > /etc/environment.d/educollege-backup << EOF
# EduCollege Backup Environment Variables
NOTIFICATION_EMAIL=$NOTIFICATION_EMAIL
REMOTE_BACKUP_ENABLED=$REMOTE_BACKUP_ENABLED
BACKUP_DIR=/backups/educollege
BACKUP_SCRIPT_DIR=$BACKUP_SCRIPT_DIR
EOF

print_success "Environment variables configured"

# ========================================
# 9. Create Backup Documentation
# ========================================

print_status "Creating backup documentation"

cat > /backups/educollege/README.md << EOF
# EduCollege University System Backup System

## Overview

This directory contains automated backups for EduCollege University System.

## Backup Schedule

- **Daily Full Backup**: 2:00 AM
- **Weekly Database Verification**: Sunday 3:00 AM
- **Monthly Application Backup**: 1st of month 4:00 AM
- **Backup Cleanup**: Daily 5:00 AM

## Backup Structure

\`\`\`
/backups/educollege/
|-- YYYYMMDD_HHMMSS/
|   |-- educollege_db_YYYYMMDD_HHMMSS.sql.gz
|   |-- application/
|   |   |-- source_code_YYYYMMDD_HHMMSS.tar.gz
|   |   |-- config_YYYYMMDD_HHMMSS.tar.gz
|   |   |-- volume_*.tar.gz
|   |   |-- MANIFEST.txt
|   |-- backup_summary.txt
|-- logs/
|   |-- automated_backup_YYYYMMDD_HHMMSS.log
|   |-- daily_backup.log
|   |-- weekly_verification.log
|   |-- monthly_backup.log
|   |-- cleanup.log
\`\`\`

## Manual Backup Commands

### Full Automated Backup
\`\`\`bash
sudo $BACKUP_SCRIPT_DIR/automated-backup.sh
\`\`\`

## Restoration Commands

### Database Restoration
\`\`\`bash
# Extract backup
gunzip /backups/educollege/YYYYMMDD_HHMMSS/educollege_db_YYYYMMDD_HHMMSS.sql.gz

psql -U educollege_user -h localhost -d educollege_db < educollege_db_YYYYMMDD_HHMMSS.sql
\`\`\`

## Contact Information
- **Log Location**: /var/log/educollege/backup/

Last Updated: $(date)
EOF

print_success "Backup documentation created"

# ========================================
# 11. Summary
# ========================================

print_status "Backup Schedule Summary"
echo "==========================="
echo "Backup User: backup"
echo "Backup Directory: /backups/educollege"
echo "Log Directory: /var/log/educollege/backup"
echo ""
echo "Scheduled Backups configured in /etc/cron.d/educollege-backup"
echo ""
echo "Documentation: /backups/educollege/README.md"

print_success "Backup schedule setup completed!"
echo "Your EduCollege University System is now protected with automated backups"
