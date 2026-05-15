package com.educollege.core.controller;

import com.educollege.core.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Controller for file management (MinIO)
 */
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    /**
     * Upload a profile picture or document
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "uploads") String folder) {
        
        String fileName = fileStorageService.uploadFile(file, folder);
        return ResponseEntity.ok(Map.of("fileName", fileName));
    }

    /**
     * Get a presigned URL to view/download a file
     */
    @GetMapping("/url")
    public ResponseEntity<Map<String, String>> getFileUrl(@RequestParam String fileName) {
        String url = fileStorageService.getFileUrl(fileName);
        if (url == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("url", url));
    }
}
