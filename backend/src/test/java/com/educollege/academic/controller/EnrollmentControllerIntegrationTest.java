package com.educollege.academic.controller;

import com.educollege.academic.dto.EnrollmentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EnrollmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private EnrollmentRequest enrollmentRequest;

    @BeforeEach
    void setUp() {
        enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setStudentId(1L);
        enrollmentRequest.setCourseOfferingId(1L);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testEnrollStudent_Success() throws Exception {
        mockMvc.perform(post("/api/v1/academic/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enrollmentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ENROLLED"))
                .andExpect(jsonPath("$.enrollmentDate").exists());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testGetStudentEnrollments_Success() throws Exception {
        mockMvc.perform(get("/api/v1/academic/enrollments/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testGetCourseEnrollments_Success() throws Exception {
        mockMvc.perform(get("/api/v1/academic/enrollments/course-offering/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testUpdateGrade_Success() throws Exception {
        mockMvc.perform(put("/api/v1/academic/enrollments/1/grade")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"grade\":8.5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(8.5));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testUpdateGrade_Forbidden() throws Exception {
        mockMvc.perform(put("/api/v1/academic/enrollments/1/grade")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"grade\":8.5}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetEnrollments_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/academic/enrollments/student/1"))
                .andExpect(status().isUnauthorized());
    }
}
