package com.educollege.core.user.profile.controller;

import com.educollege.core.user.profile.dto.*;
import com.educollege.core.user.profile.service.UserProfileService;
import com.educollege.core.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User Profile Controller - REST endpoints for user profile management
 */
@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Profile", description = "User profile management endpoints")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    @Operation(
        summary = "Get current user profile",
        description = "Retrieve the complete profile information for the authenticated user.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
        }
    )
    public ResponseEntity<Map<String, Object>> getCurrentUserProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Getting current user profile for: {}", userDetails.getUsername());
        
        Long userId = getUserIdFromUserDetails(userDetails);
        UserProfileResponse profile = userProfileService.getUserProfile(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Profile retrieved successfully");
        response.put("data", profile);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Get user profile by ID",
        description = "Retrieve profile information for a specific user. Requires admin or faculty role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found")
        }
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<Map<String, Object>> getUserProfileById(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        log.info("Getting user profile for user ID: {}", userId);
        
        UserProfileResponse profile = userProfileService.getUserProfile(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Profile retrieved successfully");
        response.put("data", profile);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    @Operation(
        summary = "Update current user profile",
        description = "Update the complete profile information for the authenticated user.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    public ResponseEntity<Map<String, Object>> updateCurrentUserProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserProfileRequest request) {
        
        log.info("Updating current user profile for: {}", userDetails.getUsername());
        
        Long userId = getUserIdFromUserDetails(userDetails);
        UserProfileResponse profile = userProfileService.createOrUpdateProfile(userId, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Profile updated successfully");
        response.put("data", profile);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/basic")
    @Operation(
        summary = "Update basic user information",
        description = "Update basic profile information for the authenticated user.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Basic info updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    public ResponseEntity<Map<String, Object>> updateBasicInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserBasicInfoRequest request) {
        
        log.info("Updating basic info for: {}", userDetails.getUsername());
        
        Long userId = getUserIdFromUserDetails(userDetails);
        UserProfileResponse profile = userProfileService.updateBasicInfo(userId, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Basic information updated successfully");
        response.put("data", profile);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/academic")
    @Operation(
        summary = "Update academic information",
        description = "Update academic profile information for the authenticated user.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Academic info updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    public ResponseEntity<Map<String, Object>> updateAcademicInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserAcademicInfoRequest request) {
        
        log.info("Updating academic info for: {}", userDetails.getUsername());
        
        Long userId = getUserIdFromUserDetails(userDetails);
        UserProfileResponse profile = userProfileService.updateAcademicInfo(userId, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Academic information updated successfully");
        response.put("data", profile);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/professional")
    @Operation(
        summary = "Update professional information",
        description = "Update professional profile information for the authenticated user.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Professional info updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    public ResponseEntity<Map<String, Object>> updateProfessionalInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserProfessionalInfoRequest request) {
        
        log.info("Updating professional info for: {}", userDetails.getUsername());
        
        Long userId = getUserIdFromUserDetails(userDetails);
        UserProfileResponse profile = userProfileService.updateProfessionalInfo(userId, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Professional information updated successfully");
        response.put("data", profile);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/avatar")
    @Operation(
        summary = "Update user avatar",
        description = "Update avatar URL for the authenticated user.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Avatar updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    public ResponseEntity<Map<String, Object>> updateAvatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "Avatar URL") @RequestParam String avatarUrl) {
        
        log.info("Updating avatar for: {}", userDetails.getUsername());
        
        Long userId = getUserIdFromUserDetails(userDetails);
        UserProfileResponse profile = userProfileService.updateAvatar(userId, avatarUrl);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Avatar updated successfully");
        response.put("data", profile);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/department/{department}")
    @Operation(
        summary = "Get profiles by department",
        description = "Retrieve all user profiles in a specific department. Requires admin or faculty role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profiles retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<Map<String, Object>> getProfilesByDepartment(
            @Parameter(description = "Department name") @PathVariable String department) {
        
        log.info("Getting profiles by department: {}", department);
        
        List<UserProfileResponse> profiles = userProfileService.getProfilesByDepartment(department);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Profiles retrieved successfully");
        response.put("data", profiles);
        response.put("count", profiles.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/major/{major}")
    @Operation(
        summary = "Get profiles by major",
        description = "Retrieve all user profiles with a specific major. Requires admin or faculty role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profiles retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<Map<String, Object>> getProfilesByMajor(
            @Parameter(description = "Major name") @PathVariable String major) {
        
        log.info("Getting profiles by major: {}", major);
        
        List<UserProfileResponse> profiles = userProfileService.getProfilesByMajor(major);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Profiles retrieved successfully");
        response.put("data", profiles);
        response.put("count", profiles.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/faculty/department/{department}")
    @Operation(
        summary = "Get faculty profiles by department",
        description = "Retrieve all faculty profiles in a specific department.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Faculty profiles retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STAFF')")
    public ResponseEntity<Map<String, Object>> getFacultyByDepartment(
            @Parameter(description = "Department name") @PathVariable String department) {
        
        log.info("Getting faculty profiles by department: {}", department);
        
        List<UserProfileResponse> profiles = userProfileService.getFacultyByDepartment(department);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Faculty profiles retrieved successfully");
        response.put("data", profiles);
        response.put("count", profiles.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/students/year/{yearOfStudy}")
    @Operation(
        summary = "Get student profiles by year of study",
        description = "Retrieve all student profiles in a specific year of study. Requires admin or faculty role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Student profiles retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<Map<String, Object>> getStudentsByYearOfStudy(
            @Parameter(description = "Year of study") @PathVariable String yearOfStudy) {
        
        log.info("Getting student profiles by year of study: {}", yearOfStudy);
        
        List<UserProfileResponse> profiles = userProfileService.getStudentsByYearOfStudy(yearOfStudy);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Student profiles retrieved successfully");
        response.put("data", profiles);
        response.put("count", profiles.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(
        summary = "Search profiles by name",
        description = "Search user profiles by first name or last name. Requires admin or faculty role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<Map<String, Object>> searchProfilesByName(
            @Parameter(description = "Name to search for") @RequestParam String name) {
        
        log.info("Searching profiles by name: {}", name);
        
        List<UserProfileResponse> profiles = userProfileService.searchProfilesByName(name);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Search completed successfully");
        response.put("data", profiles);
        response.put("count", profiles.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public")
    @Operation(
        summary = "Get public profiles",
        description = "Retrieve all public user profiles.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Public profiles retrieved successfully")
        }
    )
    public ResponseEntity<Map<String, Object>> getPublicProfiles() {
        
        log.info("Getting public profiles");
        
        List<UserProfileResponse> profiles = userProfileService.getPublicProfiles();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Public profiles retrieved successfully");
        response.put("data", profiles);
        response.put("count", profiles.size());
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    @Operation(
        summary = "Delete current user profile",
        description = "Delete the profile of the authenticated user.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    public ResponseEntity<Map<String, Object>> deleteCurrentUserProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Deleting profile for: {}", userDetails.getUsername());
        
        Long userId = getUserIdFromUserDetails(userDetails);
        userProfileService.deleteProfile(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Profile deleted successfully");
        
        return ResponseEntity.ok(response);
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        try {
            // Get user entity from UserRepository
            var user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found: " + userDetails.getUsername()));
            return user.getId();
        } catch (Exception e) {
            log.error("Error getting user ID from UserDetails: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to determine user ID");
        }
    }
}
