# Disaster Recovery Plan for EduCollege University Management System

## Overview

This document outlines the comprehensive disaster recovery procedures for EduCollege University Management System, ensuring business continuity and minimal downtime in case of system failures. The plan prioritizes academic data integrity and student record protection.

## Recovery Objectives

### Recovery Time Objective (RTO)
- **Critical Services**: 4 hours
- **All Services**: 8 hours
- **Full System**: 24 hours

### Recovery Point Objective (RPO)
- **Academic Database**: 15 minutes (student records, grades)
- **Application Data**: 1 hour (course data, schedules)
- **File Storage**: 4 hours (documents, transcripts)
- **Configuration**: 24 hours

## Recovery Procedures

### Phase 1: Assessment (0-30 minutes)

#### 1.1 Initial Assessment
```bash
# Check system status
systemctl status docker
systemctl status nginx
systemctl status postgresql
systemctl status redis

# Check application health
curl -f http://localhost:8080/actuator/health

# Check database connectivity
pg_isready -h localhost -U educollege_user
redis-cli -p 6380 ping
```

### Phase 3: Recovery (1-4 hours)

#### 3.1 Database Recovery
```bash
# Identify latest backup
LATEST_BACKUP=$(ls -t /backups/educollege/*/educollege_db_*.sql.gz | head -1)

# Restore database
gunzip -c "$LATEST_BACKUP" | psql -h localhost -U educollege_user -d educollege_db
```

#### 3.2 Application Recovery
```bash
# Restore application files
LATEST_APP_BACKUP=$(ls -t /backups/educollege/*/application/source_code_*.tar.gz | head -1)
tar -xzf "$LATEST_APP_BACKUP" -C /home/ubuntu/ltanh/

# Restore configuration
LATEST_CONFIG_BACKUP=$(ls -t /backups/educollege/*/application/config_*.tar.gz | head -1)
tar -xzf "$LATEST_CONFIG_BACKUP" -C /home/ubuntu/ltanh/eduCollege/backend/

# Restore Docker volumes
for volume in educollege_postgres_data educollege_redis_data educollege_minio_data educollege_rabbitmq_data; do
    LATEST_VOLUME_BACKUP=$(ls -t /backups/educollege/*/application/volume_${volume}_*.tar.gz | head -1)
    if [[ -f "$LATEST_VOLUME_BACKUP" ]]; then
        docker run --rm -v "$volume":/data -v "$(dirname "$LATEST_VOLUME_BACKUP")":/backup alpine tar -xzf /backup/$(basename "$LATEST_VOLUME_BACKUP") -C /data
    fi
done
```

### Phase 4: Validation (4-6 hours)

#### 4.1 Service Validation
```bash
# Start application
cd /home/ubuntu/ltanh/eduCollege/backend
./gradlew bootRun
```

#### 4.2 Data Validation
```bash
# Validate database data
psql -h localhost -U educollege_user -d educollege_db -c "SELECT COUNT(*) FROM users;"
```

## Backup Locations

### Local Backups
- **Path**: /backups/educollege/
- **Retention**: 30 days
- **Types**: Database, Application, Configuration

### Remote Backups
- **Provider**: AWS S3
- **Path**: s3://educollege-backups/
- **Retention**: 90 days
- **Types**: Full system backups
