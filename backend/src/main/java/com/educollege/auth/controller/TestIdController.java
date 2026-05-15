package com.educollege.auth.controller;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility Controller for generating test Vietnamese IDs
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestIdController {

    @GetMapping("/generate-ids")
    public ResponseEntity<TestIdsResponse> generateTestIds() {
        List<String> studentIds = new ArrayList<>();
        studentIds.add("SV24CNTT00001");
        studentIds.add("SV24CNTT00002");
        studentIds.add("SV24TOAN00001");
        studentIds.add("SV24LY00001");
        studentIds.add("SV24HOA00001");

        List<String> teacherIds = new ArrayList<>();
        teacherIds.add("GVCNPM0001");
        teacherIds.add("GVMMTT0002");
        teacherIds.add("GVKHMT0003");
        teacherIds.add("GVHTTT0004");

        List<String> staffIds = new ArrayList<>();
        staffIds.add("NVCNTTHC0001");
        staffIds.add("NVTOANVT0002");

        TestIdsResponse response = TestIdsResponse.builder()
                .studentIds(studentIds)
                .teacherIds(teacherIds)
                .staffIds(staffIds)
                .build();

        return ResponseEntity.ok(response);
    }

    @Data
    @Builder
    public static class TestIdsResponse {
        private List<String> studentIds;
        private List<String> teacherIds;
        private List<String> staffIds;
    }
}
