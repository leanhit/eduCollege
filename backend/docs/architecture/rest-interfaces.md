# REST API Interfaces Documentation

## Overview

The eduCollege University System uses REST APIs for communication with simplified architecture optimized for academic operations. This provides easy-to-understand interfaces with standard HTTP protocols and excellent tooling support.

## REST API Architecture

### Service Endpoints
```
┌─────────────────────────────────────────────────────────────┐
│                    eduCollege Application                    │
├─────────────────────────────────────────────────────────────┤
│  /api/v1/academic/*  │  /api/v1/users/*     │  /api/v1/files/*      │
│  /api/v1/auth/*      │  /api/v1/admin/*     │  /api/v1/health       │
└─────────────────────────────────────────────────────────────┘
```

### Communication Patterns
- **RESTful APIs**: Standard HTTP methods
- **JSON Payloads**: Easy serialization
- **HTTP Status Codes**: Standard error handling
- **Timeouts**: Configurable request timeouts

## API Documentation

### Common DTOs
```json
// Common Response Structure
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { ... },
  "timestamp": "2024-01-01T00:00:00Z"
}

// Pagination
{
  "content": [ ... ],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}

// Error Response
{
  "success": false,
  "message": "Validation failed",
  "errorCode": "VALIDATION_ERROR",
  "timestamp": "2024-01-01T00:00:00Z"
}
```

## Academic API Endpoints

### Academic Management
```
/api/v1/academic/advisors
/api/v1/academic/sessions
/api/v1/academic/enrollments
/api/v1/academic/transcripts
/api/v1/academic/graduation
```

### User Management
```
/api/v1/users/profile
/api/v1/users/roles
/api/v1/users/permissions
```

### File Management
```
/api/v1/files/upload
/api/v1/files/download/{id}
/api/v1/files/metadata/{id}
```

### System Management
```
/api/v1/health
/api/v1/admin/system
/api/v1/admin/logs
```

## Implementation Examples

### Academic Controller Implementation
```java
@RestController
@RequestMapping("/api/v1/academic")
@RequiredArgsConstructor
public class AcademicController {
    
    private final AcademicAdvisingService advisingService;
    
    @PostMapping("/advisors")
    public ResponseEntity<ApiResponse<AcademicAdvisorResponse>> createAdvisor(
            @Valid @RequestBody AcademicAdvisorRequest request) {
        AcademicAdvisorResponse advisor = advisingService.createAdvisor(request);
        return ResponseEntity.ok(ApiResponse.success("Advisor created successfully", advisor));
    }
    
    @GetMapping("/advisors/available")
    public ResponseEntity<ApiResponse<List<AcademicAdvisorResponse>>> getAvailableAdvisors() {
        List<AcademicAdvisorResponse> advisors = advisingService.getAvailableAdvisors();
        return ResponseEntity.ok(ApiResponse.success("Available advisors retrieved", advisors));
    }
}
```

### User Controller Implementation
```java
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(
            @PathVariable Long userId) {
        UserProfileResponse profile = userService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved", profile));
    }
    
    @PutMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserProfileRequest request) {
        UserProfileResponse profile = userService.updateUserProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated", profile));
    }
}
```

### File Controller Implementation
```java
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    
    private final FileStorageService fileStorageService;
    
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileMetadataResponse>> uploadFile(
            @RequestParam("file") MultipartFile file) {
        FileMetadataResponse metadata = fileStorageService.storeFile(file);
        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", metadata));
    }
    
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        Resource file = fileStorageService.getFile(fileId);
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }
}
```

## API Security

### Authentication
- JWT-based authentication
- Token refresh mechanism
- Multi-factor authentication support

### Authorization
- Role-based access control (RBAC)
- Department-level permissions
- Academic data access controls

### Data Protection
- HTTPS encryption
- Input validation
- SQL injection prevention
- XSS protection

## API Performance

### Caching
- Redis caching for frequently accessed data
- HTTP caching headers
- Database query optimization

### Rate Limiting
- Request rate limiting per user
- API key throttling
- Distributed rate limiting

### Monitoring
- Request/response logging
- Performance metrics
- Error tracking
- API health checks

## API Documentation

### OpenAPI/Swagger
- Interactive API documentation
- Request/response examples
- Authentication examples
- Error code reference

### Postman Collection
- Pre-configured API requests
- Environment variables
- Authentication setup
- Test scripts

## API Versioning

### Version Strategy
- URL-based versioning (/api/v1/)
- Backward compatibility
- Deprecation notices
- Migration guides

### Version Lifecycle
- Current version: v1.0
- Support period: 12 months
- Deprecation period: 6 months
- End-of-life notification
