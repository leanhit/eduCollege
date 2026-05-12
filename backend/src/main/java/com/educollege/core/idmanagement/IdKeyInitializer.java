package com.educollege.core.idmanagement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IdKeyInitializer implements CommandLineRunner {

    private final IdManagementService idManagementService;

    @Override
    public void run(String... args) throws Exception {
        log.info("=== Initializing Test ID Keys ===");
        
        // Generate initial ID keys for testing
        var studentResult = generateAndLogId("STUDENT_ID");
        var facultyResult = generateAndLogId("FACULTY_ID");
        var employeeResult = generateAndLogId("EMPLOYEE_ID");
        var staffResult = generateAndLogId("STAFF_ID");
        
        log.info("=== Current Sequences ===");
        var sequences = idManagementService.getCurrentSequences();
        log.info("Sequences: {}", sequences.get("data"));
        
        log.info("=== Test ID Keys Ready ===");
        log.info("Use these ID keys for testing registration:");
        
        // Extract the generated IDs from the results
        String studentId = (String) ((java.util.Map<?, ?>) studentResult.get("data")).get("generatedId");
        String facultyId = (String) ((java.util.Map<?, ?>) facultyResult.get("data")).get("generatedId");
        String employeeId = (String) ((java.util.Map<?, ?>) employeeResult.get("data")).get("generatedId");
        String staffId = (String) ((java.util.Map<?, ?>) staffResult.get("data")).get("generatedId");
        
        log.info("Student: {}", studentId);
        log.info("Faculty: {}", facultyId);
        log.info("Employee: {}", employeeId);
        log.info("Staff: {}", staffId);
    }
    
    private java.util.Map<String, Object> generateAndLogId(String idType) {
        var result = idManagementService.generateNextId(idType);
        log.info("Generated {}: {}", idType, result.get("data"));
        return result;
    }
}
