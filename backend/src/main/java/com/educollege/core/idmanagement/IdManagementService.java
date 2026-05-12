package com.educollege.core.idmanagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class IdManagementService {

    private final AtomicLong studentSequence = new AtomicLong(1234);
    private final AtomicLong facultySequence = new AtomicLong(1001);
    private final AtomicLong employeeSequence = new AtomicLong(5001);
    private final AtomicLong staffSequence = new AtomicLong(3001);

    public Map<String, Object> generateNextId(String idType) {
        log.info("Generating next ID for type: {}", idType);
        
        String generatedId;
        switch (idType) {
            case "STUDENT_ID":
                generatedId = "2024" + String.format("%04d", studentSequence.incrementAndGet());
                break;
            case "FACULTY_ID":
                generatedId = "FAC" + String.format("%04d", facultySequence.incrementAndGet());
                break;
            case "EMPLOYEE_ID":
                generatedId = "EMP" + String.format("%04d", employeeSequence.incrementAndGet());
                break;
            case "STAFF_ID":
                generatedId = "STA" + String.format("%04d", staffSequence.incrementAndGet());
                break;
            default:
                generatedId = "UNKNOWN" + System.currentTimeMillis();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "ID generated successfully");
        response.put("data", Map.of(
            "idType", idType,
            "generatedId", generatedId,
            "sequence", getCurrentSequence(idType)
        ));
        
        log.info("Generated ID: {} for type: {}", generatedId, idType);
        return response;
    }
    
    public Map<String, Object> validateId(String id) {
        log.info("Validating ID: {}", id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "ID validation completed");
        response.put("data", Map.of(
            "id", id,
            "valid", isValidIdFormat(id),
            "idType", determineIdType(id)
        ));
        
        return response;
    }
    
    public Map<String, Object> getCurrentSequences() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Current sequences retrieved");
        response.put("data", Map.of(
            "studentSequence", studentSequence.get(),
            "facultySequence", facultySequence.get(),
            "employeeSequence", employeeSequence.get(),
            "staffSequence", staffSequence.get()
        ));
        
        return response;
    }
    
    private boolean isValidIdFormat(String id) {
        if (id == null || id.isEmpty()) return false;
        
        // Student ID: 2024 + 8 digits
        if (id.matches("^2024\\d{8}$")) return true;
        
        // Faculty ID: FAC + 6 digits
        if (id.matches("^FAC\\d{6}$")) return true;
        
        // Employee ID: EMP + 6 digits
        if (id.matches("^EMP\\d{6}$")) return true;
        
        // Staff ID: STA + 6 digits
        if (id.matches("^STA\\d{6}$")) return true;
        
        return false;
    }
    
    private String determineIdType(String id) {
        if (id.matches("^2024\\d{8}$")) return "STUDENT_ID";
        if (id.matches("^FAC\\d{6}$")) return "FACULTY_ID";
        if (id.matches("^EMP\\d{6}$")) return "EMPLOYEE_ID";
        if (id.matches("^STA\\d{6}$")) return "STAFF_ID";
        return "UNKNOWN";
    }
    
    private long getCurrentSequence(String idType) {
        switch (idType) {
            case "STUDENT_ID": return studentSequence.get();
            case "FACULTY_ID": return facultySequence.get();
            case "EMPLOYEE_ID": return employeeSequence.get();
            case "STAFF_ID": return staffSequence.get();
            default: return 0;
        }
    }
}
