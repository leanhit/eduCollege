package com.educollege.core.idmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/id-management")
public class IdManagementController {

    private final IdManagementService idManagementService;

    public IdManagementController(IdManagementService idManagementService) {
        this.idManagementService = idManagementService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateNextId(@RequestParam String idType) {
        Map<String, Object> response = idManagementService.generateNextId(idType);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateId(@RequestParam String id) {
        Map<String, Object> response = idManagementService.validateId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sequences")
    public ResponseEntity<Map<String, Object>> getCurrentSequences() {
        Map<String, Object> response = idManagementService.getCurrentSequences();
        return ResponseEntity.ok(response);
    }
}
