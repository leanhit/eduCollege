# User Profile Feature

## Overview
This feature adds comprehensive user profile management to the EduCollege backend system, similar to the chatbot-saas-v2.1 backend structure. It provides detailed profile information management for students, faculty, staff, and employees.

## Features

### 📋 Profile Information Categories

#### Basic Information
- First Name & Last Name
- Date of Birth
- Gender
- Bio/Description
- Avatar URL
- Contact Information (Phone, Email, Address)

#### Academic Information
- Student ID / Faculty ID
- Department & Major
- Year of Study
- GPA
- Enrollment & Expected Graduation Dates

#### Professional Information
- Job Title
- Office Location & Hours
- Research Interests
- Publications
- LinkedIn & Personal Website URLs

#### Emergency Contact
- Contact Name, Phone, Relationship

#### Preferences
- Preferred Language
- Timezone
- Notification Preferences
- Privacy Settings

## API Endpoints

### 🔐 Authenticated User Operations

#### Get Current User Profile
```http
GET /api/v1/profiles/me
Authorization: Bearer {jwt_token}
```

#### Update Complete Profile
```http
PUT /api/v1/profiles/me
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "2000-01-01T00:00:00",
  "gender": "MALE",
  "bio": "Computer Science student",
  "phoneNumber": "+1234567890",
  "address": "123 Main St",
  "city": "New York",
  "country": "USA",
  "department": "Computer Science",
  "major": "Software Engineering",
  "yearOfStudy": "3rd Year",
  "gpa": 3.8,
  "isProfilePublic": true
}
```

#### Update Basic Information
```http
PUT /api/v1/profiles/me/basic
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890",
  "address": "123 Main St",
  "city": "New York",
  "country": "USA"
}
```

#### Update Academic Information
```http
PUT /api/v1/profiles/me/academic
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "department": "Computer Science",
  "major": "Software Engineering",
  "yearOfStudy": "3rd Year",
  "gpa": 3.8
}
```

#### Update Professional Information
```http
PUT /api/v1/profiles/me/professional
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "jobTitle": "Professor",
  "officeLocation": "Room 301",
  "officeHours": "Mon-Wed 2-4 PM",
  "researchInterests": "Machine Learning, AI",
  "linkedInUrl": "https://linkedin.com/in/johndoe"
}
```

#### Update Avatar
```http
PUT /api/v1/profiles/me/avatar?avatarUrl=https://example.com/avatar.jpg
Authorization: Bearer {jwt_token}
```

#### Delete Profile
```http
DELETE /api/v1/profiles/me
Authorization: Bearer {jwt_token}
```

### 👥 Admin/Faculty Operations

#### Get User Profile by ID
```http
GET /api/v1/profiles/user/{userId}
Authorization: Bearer {admin_or_faculty_token}
```

#### Get Profiles by Department
```http
GET /api/v1/profiles/department/{department}
Authorization: Bearer {admin_or_faculty_token}
```

#### Get Profiles by Major
```http
GET /api/v1/profiles/major/{major}
Authorization: Bearer {admin_or_faculty_token}
```

#### Get Faculty by Department
```http
GET /api/v1/profiles/faculty/department/{department}
Authorization: Bearer {admin_or_faculty_or_staff_token}
```

#### Get Students by Year of Study
```http
GET /api/v1/profiles/students/year/{yearOfStudy}
Authorization: Bearer {admin_or_faculty_token}
```

#### Search Profiles by Name
```http
GET /api/v1/profiles/search?name=John
Authorization: Bearer {admin_or_faculty_token}
```

### 🌐 Public Operations

#### Get Public Profiles
```http
GET /api/v1/profiles/public
```

## Database Schema

### UserProfile Table
```sql
CREATE TABLE user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL REFERENCES users(id),
    
    -- Basic Information
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    date_of_birth TIMESTAMP,
    avatar VARCHAR(500),
    gender VARCHAR(10),
    bio VARCHAR(1000),
    
    -- Contact Information
    phone_number VARCHAR(20),
    alternate_email VARCHAR(255),
    address VARCHAR(500),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    
    -- Academic Information
    student_id VARCHAR(50),
    faculty_id VARCHAR(50),
    department VARCHAR(100),
    major VARCHAR(100),
    year_of_study VARCHAR(20),
    gpa DECIMAL(3,2),
    enrollment_date TIMESTAMP,
    expected_graduation_date TIMESTAMP,
    
    -- Professional Information
    job_title VARCHAR(100),
    office_location VARCHAR(100),
    office_hours VARCHAR(200),
    research_interests VARCHAR(1000),
    publications VARCHAR(2000),
    linkedin_url VARCHAR(500),
    personal_website VARCHAR(500),
    
    -- Emergency Contact
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relationship VARCHAR(50),
    
    -- Preferences
    preferred_language VARCHAR(50),
    timezone VARCHAR(50),
    notification_preferences VARCHAR(500),
    privacy_settings VARCHAR(500),
    
    -- Status Information
    profile_status VARCHAR(20) DEFAULT 'ACTIVE',
    is_profile_public BOOLEAN DEFAULT FALSE,
    last_profile_update TIMESTAMP,
    
    -- Audit Fields
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_profile_user ON user_profiles(user_id);
```

## Response Format

All API responses follow this consistent format:

### Success Response
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {
    "id": 1,
    "userId": 123,
    "username": "johndoe",
    "email": "john@example.com",
    "role": "STUDENT",
    "firstName": "John",
    "lastName": "Doe",
    "department": "Computer Science",
    "major": "Software Engineering",
    "gpa": 3.8,
    "isProfilePublic": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-15T14:30:00"
  }
}
```

### List Response
```json
{
  "success": true,
  "message": "Profiles retrieved successfully",
  "data": [
    {
      "id": 1,
      "userId": 123,
      "username": "johndoe",
      "email": "john@example.com",
      "role": "STUDENT",
      "firstName": "John",
      "lastName": "Doe"
    }
  ],
  "count": 1
}
```

## Security Considerations

### Role-Based Access Control
- **STUDENT**: Can only view and update their own profile
- **FACULTY**: Can view student profiles, search by department/major
- **STAFF**: Can view faculty profiles by department
- **ADMIN**: Full access to all profiles and administrative operations

### Privacy Settings
- Profiles can be set as public or private
- Public profiles are accessible without authentication
- Private profiles require appropriate role permissions

### Data Validation
- All input fields are validated with appropriate constraints
- Email format validation for email fields
- Length constraints on all string fields
- GPA range validation (0.0 - 4.0)

## Integration Points

### User Entity Integration
The UserProfile entity has a bidirectional OneToOne relationship with the User entity:
```java
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private UserProfile userProfile;
```

### Security Integration
Profile endpoints are integrated with the existing JWT authentication system and role-based access control.

## Usage Examples

### Student Registration Flow
1. User registers with basic credentials
2. System creates User entity
3. Student can then create/update their detailed profile
4. Profile information is used for academic records and department management

### Faculty Profile Management
1. Faculty member logs in
2. Updates professional information (office hours, research interests)
3. Students can view faculty profiles for course selection
4. Admin can manage faculty profiles by department

### Public Directory
1. Public profiles are accessible via `/api/v1/profiles/public`
2. Useful for student directories, faculty listings
3. Respects individual privacy settings

## Error Handling

Common error responses:
```json
{
  "success": false,
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format"
    }
  ]
}
```

```json
{
  "success": false,
  "message": "User not found with ID: 123"
}
```

```json
{
  "success": false,
  "message": "Access denied: insufficient privileges"
}
```

## Future Enhancements

Potential future features:
- Profile picture upload with MinIO integration
- Profile completion tracking
- Bulk profile updates for administrators
- Profile export functionality (PDF, CSV)
- Profile history/audit trail
- Integration with external academic systems
- Profile analytics and reporting
