package com.educollege.auth.controller;

import com.educollege.auth.dto.LoginRequest;
import com.educollege.user.dto.StudentRegistrationRequest;
import com.educollege.user.dto.TeacherRegistrationRequest;
import com.educollege.user.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentRegistrationRequest studentRequest;
    private TeacherRegistrationRequest teacherRequest;

    @BeforeEach
    void setUp() {
        studentRequest = new StudentRegistrationRequest();
        studentRequest.setEmail("student@educollege.edu.vn");
        studentRequest.setUsername("student123");
        studentRequest.setPassword("SecurePass123!");
        studentRequest.setConfirmPassword("SecurePass123!");
        studentRequest.setVietnameseName("Nguyễn Văn A");
        studentRequest.setPhone("0912345678");
        studentRequest.setIdNumber("123456789");
        studentRequest.setAddress("123 Đường ABC, TP.HCM");
        studentRequest.setFacultyId(1L);
        studentRequest.setClassId(1L);
        studentRequest.setEnrollmentYear(2024);
        studentRequest.setAcademicLevel("DAIHOC");

        teacherRequest = new TeacherRegistrationRequest();
        teacherRequest.setEmail("teacher@educollege.edu.vn");
        teacherRequest.setUsername("teacher123");
        teacherRequest.setPassword("SecurePass123!");
        teacherRequest.setVietnameseName("Trần Thị B");
        teacherRequest.setFacultyId(1L);
        teacherRequest.setDepartmentId(1L);
        teacherRequest.setAcademicTitle("Giảng viên chính");
        teacherRequest.setSpecialization("Công nghệ phần mềm");
        teacherRequest.setHireDate(java.time.LocalDate.of(2024, 1, 1));
    }

    @Test
    void testRegisterStudent_Success() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("student@educollege.edu.vn"))
                .andExpect(jsonPath("$.username").value("student123"))
                .andExpect(jsonPath("$.idCategory").value("SINHVIEN"))
                .andExpect(jsonPath("$.vietnameseId").exists())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void testRegisterTeacher_Success() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register/teacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacherRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("teacher@educollege.edu.vn"))
                .andExpect(jsonPath("$.username").value("teacher123"))
                .andExpect(jsonPath("$.idCategory").value("GIAOVIEN"))
                .andExpect(jsonPath("$.vietnameseId").exists())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testRegisterStudent_InvalidEmail() throws Exception {
        studentRequest.setEmail("invalid-email");
        
        mockMvc.perform(post("/api/v1/auth/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterStudent_PasswordMismatch() throws Exception {
        studentRequest.setConfirmPassword("DifferentPass123!");
        
        mockMvc.perform(post("/api/v1/auth/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_WithUsername() throws Exception {
        // First register a student
        mockMvc.perform(post("/api/v1/auth/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk());

        // Then login with username
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrVietnameseId("student123");
        loginRequest.setPassword("SecurePass123!");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("student123"))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void testLogin_WithVietnameseId() throws Exception {
        // First register a student
        mockMvc.perform(post("/api/v1/auth/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk());

        // Get the Vietnamese ID from registration response
        // For this test, we'll use a known format
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrVietnameseId("SV24CNTT00001");
        loginRequest.setPassword("SecurePass123!");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrVietnameseId("student123");
        loginRequest.setPassword("WrongPassword123!");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRefreshToken_Success() throws Exception {
        // First register and login
        mockMvc.perform(post("/api/v1/auth/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrVietnameseId("student123");
        loginRequest.setPassword("SecurePass123!");

        String response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        UserResponse userResponse = objectMapper.readValue(response, UserResponse.class);
        String refreshToken = userResponse.getRefreshToken();

        // Refresh token
        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void testValidateToken_Success() throws Exception {
        // First register and login
        mockMvc.perform(post("/api/v1/auth/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrVietnameseId("student123");
        loginRequest.setPassword("SecurePass123!");

        String response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        UserResponse userResponse = objectMapper.readValue(response, UserResponse.class);
        String token = userResponse.getToken();

        // Validate token
        mockMvc.perform(post("/api/v1/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"" + token + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    void testGetCurrentUser_Success() throws Exception {
        // First register and login
        mockMvc.perform(post("/api/v1/auth/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrVietnameseId("student123");
        loginRequest.setPassword("SecurePass123!");

        String response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        UserResponse userResponse = objectMapper.readValue(response, UserResponse.class);
        String token = userResponse.getToken();

        // Get current user
        mockMvc.perform(get("/api/v1/auth/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("student123"));
    }

    @Test
    void testLogout_Success() throws Exception {
        // First register and login
        mockMvc.perform(post("/api/v1/auth/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrVietnameseId("student123");
        loginRequest.setPassword("SecurePass123!");

        String response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        UserResponse userResponse = objectMapper.readValue(response, UserResponse.class);
        String token = userResponse.getToken();

        // Logout
        mockMvc.perform(post("/api/v1/auth/logout")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));
    }

    @Test
    void testCheckVietnameseIdAvailability_Valid() throws Exception {
        mockMvc.perform(get("/api/v1/auth/check-vietnamese-id/SV24CNTT00101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vietnameseId").value("SV24CNTT00101"))
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    void testCheckVietnameseIdAvailability_Invalid() throws Exception {
        mockMvc.perform(get("/api/v1/auth/check-vietnamese-id/INVALID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }

    @Test
    void testCheckEmailAvailability_Valid() throws Exception {
        mockMvc.perform(get("/api/v1/auth/check-email/test@educollege.edu.vn"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@educollege.edu.vn"))
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    void testCheckEmailAvailability_Invalid() throws Exception {
        mockMvc.perform(get("/api/v1/auth/check-email/invalid-email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }
}
