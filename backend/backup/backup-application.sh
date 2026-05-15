#!/bin/bash

echo "=== Application Backup Script for EduCollege University System ==="
echo "Creating comprehensive application backups for academic system"
echo ""

# Configuration
BACKUP_DIR="/backups/educollege"
DATE=$(date +%Y%m%d_%H%M%S)
RETENTION_DAYS=30
APP_DIR="/home/ubuntu/ltanh/eduCollege/backend"
DOCKER_COMPOSE_FILE="docker-compose.production.yml"

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

# Create backup directory
mkdir -p "$BACKUP_DIR/$DATE/application"
mkdir -p "$BACKUP_DIR/logs"

print_status "Starting application backup for $DATE"

# ========================================
# 1. Source Code Backup
# ========================================

print_status "Backing up source code"

SOURCE_CODE_BACKUP="$BACKUP_DIR/$DATE/application/source_code_${DATE}.tar.gz"

if [[ -d "$APP_DIR" ]]; then
    # Create source code backup excluding build artifacts and logs
    tar -czf "$SOURCE_CODE_BACKUP" \
        --exclude="$APP_DIR/build" \
        --exclude="$APP_DIR/logs" \
        --exclude="$APP_DIR/.gradle" \
        -C "$(dirname "$APP_DIR")" "$(basename "$APP_DIR")"
    
    if [[ $? -eq 0 ]]; then
        print_success "Source code backed up: $SOURCE_CODE_BACKUP ($(du -h "$SOURCE_CODE_BACKUP" | cut -f1))"
    else
        print_error "Failed to backup source code"
    fi
else
    print_error "Application directory not found: $APP_DIR"
fi

# ========================================
# 2. Configuration Files Backup
# ========================================

print_status "Backing up configuration files"

CONFIG_BACKUP="$BACKUP_DIR/$DATE/application/config_${DATE}.tar.gz"
CONFIG_TEMP_DIR="$BACKUP_DIR/$DATE/application/config_temp"

mkdir -p "$CONFIG_TEMP_DIR"

# Copy configuration files
cp -r "$APP_DIR/.env.production.template" "$CONFIG_TEMP_DIR/" 2>/dev/null || true
cp -r "$APP_DIR/nginx.conf" "$CONFIG_TEMP_DIR/" 2>/dev/null || true
cp -r "$APP_DIR/k8s" "$CONFIG_TEMP_DIR/" 2>/dev/null || true
cp -r "$APP_DIR/monitoring" "$CONFIG_TEMP_DIR/" 2>/dev/null || true
cp -r "$APP_DIR/performance" "$CONFIG_TEMP_DIR/" 2>/dev/null || true

# Create configuration backup
if tar -czf "$CONFIG_BACKUP" -C "$CONFIG_TEMP_DIR" .; then
    print_success "Configuration files backed up: $CONFIG_BACKUP ($(du -h "$CONFIG_BACKUP" | cut -f1))"
else
    print_error "Failed to backup configuration files"
fi

# Clean up temp directory
rm -rf "$CONFIG_TEMP_DIR"

# ========================================
# 3. Docker Volumes Backup
# ========================================

print_status "Backing up Docker volumes"

# Check if Docker is running
if ! docker info &>/dev/null; then
    print_warning "Docker is not running - skipping volume backups"
else
    # List of important volumes to backup
    VOLUMES=(
        "educollege_postgres_data"
        "educollege_redis_data"
        "educollege_minio_data"
        "educollege_rabbitmq_data"
        "educollege_internal"
    )
    
    for volume in "${VOLUMES[@]}"; do
        if docker volume inspect "$volume" &>/dev/null; then
            print_status "Backing up Docker volume: $volume"
            
            VOLUME_BACKUP="$BACKUP_DIR/$DATE/application/volume_${volume}_${DATE}.tar.gz"
            TEMP_CONTAINER="backup_temp_$(date +%s)"
            
            # Create temporary container to access volume
            docker run --rm -d --name "$TEMP_CONTAINER" -v "$volume":/data alpine sleep 3600 2>/dev/null
            
            if [[ $? -eq 0 ]]; then
                # Wait for container to start
                sleep 2
                
                # Backup volume content
                if docker exec "$TEMP_CONTAINER" tar -czf - -C /data . > "$VOLUME_BACKUP"; then
                    print_success "Volume $volume backed up: $(du -h "$VOLUME_BACKUP" | cut -f1)"
                else
                    print_error "Failed to backup volume $volume"
                fi
                
                # Clean up temporary container
                docker stop "$TEMP_CONTAINER" 2>/dev/null || true
            else
                print_error "Failed to create temporary container for volume $volume"
            fi
        else
            print_warning "Volume $volume not found - skipping"
        fi
    done
fi

# ========================================
# 7. Create Backup Manifest
# ========================================

print_status "Creating backup manifest"

MANIFEST_FILE="$BACKUP_DIR/$DATE/application/MANIFEST.txt"

cat > "$MANIFEST_FILE" << EOF
EduCollege University System Application Backup Manifest
======================================================
Backup Date: $(date)
Backup Type: Full Application Backup
Server: $(hostname)
Operating System: $(uname -a)

Application Components Backed Up:
EOF

# Add backup files to manifest
for file in "$BACKUP_DIR/$DATE/application"/*.tar.gz; do
    if [[ -f "$file" ]]; then
        FILENAME=$(basename "$file")
        echo "- $FILENAME: $(du -h "$file" | cut -f1)" >> "$MANIFEST_FILE"
    fi
done

print_success "Application backup completed successfully!"
echo "Backup location: $BACKUP_DIR/$DATE/application"
echo "Manifest: $MANIFEST_FILE"
