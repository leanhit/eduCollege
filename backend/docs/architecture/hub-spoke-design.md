# eduCollege Architecture Design

## Overview
The eduCollege University System follows a simplified monolithic architecture pattern optimized for academic operations:
- **Simplicity**: Single database and application for easier management
- **Performance**: Optimized for academic workloads and data patterns
- **Maintainability**: Clear domain boundaries within single application
- **Scalability**: Designed for university-scale operations

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    API Gateway                              │
│                 (Load Balancer)                            │
└─────────────────────┬───────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
┌───────▼──────┐ ┌────▼────┐ ┌─────▼──────┐
│  Web Client  │ │ Mobile  │ │ External   │
│              │ │ Client  │ │ Systems    │
└──────────────┘ └─────────┘ └────────────┘
                      │
        ┌─────────────▼─────────────────┐
        │      eduCollege Application   │
        │    (Single Academic System)   │
        └─────────────┬─────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
┌───────▼──────┐ ┌────▼────┐ ┌─────▼──────┐
│ Academic Core│ User Mgmt│ File Storage│
└──────────────┘ └────────┘ └────────────┘
        │             │             │
        └─────────────┼─────────────┘
                      │
        ┌─────────────▼─────────────────┐
        │         Shared Services       │
        │  (Security, Utils, Cache, etc) │
        └─────────────┬─────────────────┘
                      │
        ┌─────────────▼─────────────────┐
        │          External Services      │
        │    (MinIO, Email, Payment)     │
        └──────────────────────────────┘
```

## Core Modules

### Academic Core
- **Purpose**: Academic operations management
- **Database**: `educollege_db`
- **Key Features**: 
  - Student management and enrollment
  - Course catalog and scheduling
  - Grade management and transcripts
  - Academic advising system
  - Graduation management

### User Management
- **Database**: `educollege_db`
- **Key Features**:
  - Authentication and authorization
  - User profiles and roles
  - Permission management
  - Vietnamese ID validation

### File Storage
- **Service**: MinIO integration
- **Key Features**:
  - Document storage for academic files
  - Avatar management
  - Transcript PDF storage
  - Academic resource files

## External Services

### MinIO Storage
- **Purpose**: File storage service
- **Features**: Document storage, academic files, avatars
- **Communication**: S3-compatible API

### Email Service
- **Purpose**: Email notifications
- **Features**: Academic notifications, system alerts
- **Communication**: SMTP integration

### Payment Service (Optional)
- **Purpose**: Tuition fee processing
- **Features**: Payment processing, fee management
- **Communication**: Payment gateway API

# Legacy Spokes (REMOVED for eduCollege)
# External integrations have been simplified to essential services only

## Communication Patterns

### Synchronous Communication
- **gRPC**: Inter-hub communication
- **REST API**: External client communication
- **Response Time**: < 100ms for intra-hub calls

### Asynchronous Communication
- **Message Queues**: Event-driven architecture
- **Saga Pattern**: Distributed transactions
- **Event Sourcing**: Audit trails and replay capability

## Data Flow

1. **Request Ingestion**: API Gateway routes to application
2. **Authentication**: User Management validates JWT token
3. **Authorization**: Role-based access control checks permissions
4. **Processing**: Business logic in appropriate module
5. **Integration**: External services handle system calls
6. **Response**: Results flow back through application layer

## Scaling Strategy

### Vertical Scaling
- Single application instance with optimized database
- Connection pooling and caching strategies
- Load balancing at application level

### Horizontal Scaling
- Multiple application instances behind load balancer
- Read replicas for database scaling
- Microservice-ready architecture for future expansion

## Fault Tolerance

### Circuit Breakers
- External service protection
- Automatic failover mechanisms
- Graceful degradation

### Data Consistency
- ACID compliance for academic data
- Transactional integrity
- Backup and recovery procedures
## Security Model

### Authentication
- JWT-based authentication
- Token refresh mechanism
- Multi-factor authentication support

### Authorization
- Role-based access control (RBAC)
- Department-level permissions
- Academic data access controls

### Data Protection
- Encryption at rest and in transit
- PII masking and anonymization
- Audit logging for compliance

## Monitoring & Observability

### Metrics
- Application performance metrics
- Database connection pool metrics
- API response times
- Academic operation statistics
- Error rates and alerts tracking

### Logging
- Structured logging with correlation IDs
- Centralized log aggregation
- Real-time log analysis

### Tracing
- Distributed tracing across hubs
- Request flow visualization
- Performance bottleneck identification

## Deployment Architecture

### Container Strategy
- One container per hub
- Sidecar patterns for cross-cutting concerns
- Health checks and readiness probes

### Orchestration
- Kubernetes for container orchestration
- Helm charts for deployment management
- GitOps for deployment automation

### Environment Management
- Separate namespaces per environment
- Configuration externalization
- Blue-green deployment strategy
