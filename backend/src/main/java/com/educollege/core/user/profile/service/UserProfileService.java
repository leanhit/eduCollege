package com.educollege.core.user.profile.service;

import com.educollege.core.exception.EduCollegeException;
import com.educollege.core.user.model.User;
import com.educollege.core.user.profile.dto.*;
import com.educollege.core.user.profile.model.UserProfile;
import com.educollege.core.user.profile.repository.UserProfileRepository;
import com.educollege.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User Profile Service - Handles user profile operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        log.info("Getting user profile for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> EduCollegeException.notFound("User not found with ID: " + userId));

        UserProfile profile = userProfileRepository.findByUserIdWithUser(userId)
                .orElse(null);

        return UserProfileResponse.builder()
                .id(profile != null ? profile.getId() : null)
                .userId(userId)
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(profile != null ? profile.getFirstName() : null)
                .lastName(profile != null ? profile.getLastName() : null)
                .dateOfBirth(profile != null ? profile.getDateOfBirth() : null)
                .avatar(profile != null ? profile.getAvatar() : null)
                .gender(profile != null ? profile.getGender() : null)
                .bio(profile != null ? profile.getBio() : null)
                .phoneNumber(profile != null ? profile.getPhoneNumber() : null)
                .alternateEmail(profile != null ? profile.getAlternateEmail() : null)
                .address(profile != null ? profile.getAddress() : null)
                .city(profile != null ? profile.getCity() : null)
                .state(profile != null ? profile.getState() : null)
                .postalCode(profile != null ? profile.getPostalCode() : null)
                .country(profile != null ? profile.getCountry() : null)
                .studentId(profile != null ? profile.getStudentId() : null)
                .facultyId(profile != null ? profile.getFacultyId() : null)
                .department(profile != null ? profile.getDepartment() : null)
                .major(profile != null ? profile.getMajor() : null)
                .yearOfStudy(profile != null ? profile.getYearOfStudy() : null)
                .gpa(profile != null ? profile.getGpa() : null)
                .enrollmentDate(profile != null ? profile.getEnrollmentDate() : null)
                .expectedGraduationDate(profile != null ? profile.getExpectedGraduationDate() : null)
                .jobTitle(profile != null ? profile.getJobTitle() : null)
                .officeLocation(profile != null ? profile.getOfficeLocation() : null)
                .officeHours(profile != null ? profile.getOfficeHours() : null)
                .researchInterests(profile != null ? profile.getResearchInterests() : null)
                .publications(profile != null ? profile.getPublications() : null)
                .linkedInUrl(profile != null ? profile.getLinkedInUrl() : null)
                .personalWebsite(profile != null ? profile.getPersonalWebsite() : null)
                .emergencyContactName(profile != null ? profile.getEmergencyContactName() : null)
                .emergencyContactPhone(profile != null ? profile.getEmergencyContactPhone() : null)
                .emergencyContactRelationship(profile != null ? profile.getEmergencyContactRelationship() : null)
                .preferredLanguage(profile != null ? profile.getPreferredLanguage() : null)
                .timezone(profile != null ? profile.getTimezone() : null)
                .notificationPreferences(profile != null ? profile.getNotificationPreferences() : null)
                .privacySettings(profile != null ? profile.getPrivacySettings() : null)
                .profileStatus(profile != null ? profile.getProfileStatus() : "INACTIVE")
                .isProfilePublic(profile != null ? profile.getIsProfilePublic() : false)
                .lastProfileUpdate(profile != null ? profile.getLastProfileUpdate() : null)
                .createdAt(profile != null ? profile.getCreatedAt() : null)
                .updatedAt(profile != null ? profile.getUpdatedAt() : null)
                .build();
    }

    @Transactional
    public UserProfileResponse createOrUpdateProfile(Long userId, UserProfileRequest request) {
        log.info("Creating/updating user profile for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> EduCollegeException.notFound("User not found with ID: " + userId));

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElse(UserProfile.builder()
                        .user(user)
                        .build());

        // Update all fields
        updateProfileFields(profile, request);

        UserProfile savedProfile = userProfileRepository.save(profile);
        log.info("Successfully saved profile for user: {}", user.getUsername());

        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse updateBasicInfo(Long userId, UserBasicInfoRequest request) {
        log.info("Updating basic info for user ID: {}", userId);

        UserProfile profile = getOrCreateProfile(userId);

        // Update basic info fields
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGender(request.getGender());
        profile.setBio(request.getBio());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAlternateEmail(request.getAlternateEmail());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setPostalCode(request.getPostalCode());
        profile.setCountry(request.getCountry());
        profile.setEmergencyContactName(request.getEmergencyContactName());
        profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        profile.setEmergencyContactRelationship(request.getEmergencyContactRelationship());
        profile.setPreferredLanguage(request.getPreferredLanguage());
        profile.setTimezone(request.getTimezone());
        profile.setNotificationPreferences(request.getNotificationPreferences());
        profile.setPrivacySettings(request.getPrivacySettings());
        if (request.getIsProfilePublic() != null) {
            profile.setIsProfilePublic(request.getIsProfilePublic());
        }

        UserProfile savedProfile = userProfileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse updateAcademicInfo(Long userId, UserAcademicInfoRequest request) {
        log.info("Updating academic info for user ID: {}", userId);

        UserProfile profile = getOrCreateProfile(userId);

        // Update academic info fields
        profile.setStudentId(request.getStudentId());
        profile.setFacultyId(request.getFacultyId());
        profile.setDepartment(request.getDepartment());
        profile.setMajor(request.getMajor());
        profile.setYearOfStudy(request.getYearOfStudy());
        profile.setGpa(request.getGpa());
        profile.setEnrollmentDate(request.getEnrollmentDate());
        profile.setExpectedGraduationDate(request.getExpectedGraduationDate());

        UserProfile savedProfile = userProfileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse updateProfessionalInfo(Long userId, UserProfessionalInfoRequest request) {
        log.info("Updating professional info for user ID: {}", userId);

        UserProfile profile = getOrCreateProfile(userId);

        // Update professional info fields
        profile.setJobTitle(request.getJobTitle());
        profile.setOfficeLocation(request.getOfficeLocation());
        profile.setOfficeHours(request.getOfficeHours());
        profile.setResearchInterests(request.getResearchInterests());
        profile.setPublications(request.getPublications());
        profile.setLinkedInUrl(request.getLinkedInUrl());
        profile.setPersonalWebsite(request.getPersonalWebsite());

        UserProfile savedProfile = userProfileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    @Transactional
    public UserProfileResponse updateAvatar(Long userId, String avatarUrl) {
        log.info("Updating avatar for user ID: {}", userId);

        UserProfile profile = getOrCreateProfile(userId);
        profile.setAvatar(avatarUrl);

        UserProfile savedProfile = userProfileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getProfilesByDepartment(String department) {
        log.info("Getting profiles by department: {}", department);

        List<UserProfile> profiles = userProfileRepository.findByDepartment(department);
        return profiles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getProfilesByMajor(String major) {
        log.info("Getting profiles by major: {}", major);

        List<UserProfile> profiles = userProfileRepository.findByMajor(major);
        return profiles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getFacultyByDepartment(String department) {
        log.info("Getting faculty profiles by department: {}", department);

        List<UserProfile> profiles = userProfileRepository.findFacultyByDepartment(department);
        return profiles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getStudentsByYearOfStudy(String yearOfStudy) {
        log.info("Getting student profiles by year of study: {}", yearOfStudy);

        List<UserProfile> profiles = userProfileRepository.findStudentsByYearOfStudy(yearOfStudy);
        return profiles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> searchProfilesByName(String name) {
        log.info("Searching profiles by name: {}", name);

        List<UserProfile> profiles = userProfileRepository.searchByName(name);
        return profiles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getPublicProfiles() {
        log.info("Getting public profiles");

        List<UserProfile> profiles = userProfileRepository.findByIsProfilePublicTrue();
        return profiles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProfile(Long userId) {
        log.info("Deleting profile for user ID: {}", userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> EduCollegeException.notFound("Profile not found for user ID: " + userId));

        userProfileRepository.delete(profile);
        log.info("Successfully deleted profile for user ID: {}", userId);
    }

    private UserProfile getOrCreateProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> EduCollegeException.notFound("User not found with ID: " + userId));

        return userProfileRepository.findByUserId(userId)
                .orElse(UserProfile.builder()
                        .user(user)
                        .profileStatus("ACTIVE")
                        .isProfilePublic(false)
                        .build());
    }

    private void updateProfileFields(UserProfile profile, UserProfileRequest request) {
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setAvatar(request.getAvatar());
        profile.setGender(request.getGender());
        profile.setBio(request.getBio());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAlternateEmail(request.getAlternateEmail());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setPostalCode(request.getPostalCode());
        profile.setCountry(request.getCountry());
        profile.setStudentId(request.getStudentId());
        profile.setFacultyId(request.getFacultyId());
        profile.setDepartment(request.getDepartment());
        profile.setMajor(request.getMajor());
        profile.setYearOfStudy(request.getYearOfStudy());
        profile.setGpa(request.getGpa());
        profile.setEnrollmentDate(request.getEnrollmentDate());
        profile.setExpectedGraduationDate(request.getExpectedGraduationDate());
        profile.setJobTitle(request.getJobTitle());
        profile.setOfficeLocation(request.getOfficeLocation());
        profile.setOfficeHours(request.getOfficeHours());
        profile.setResearchInterests(request.getResearchInterests());
        profile.setPublications(request.getPublications());
        profile.setLinkedInUrl(request.getLinkedInUrl());
        profile.setPersonalWebsite(request.getPersonalWebsite());
        profile.setEmergencyContactName(request.getEmergencyContactName());
        profile.setEmergencyContactPhone(request.getEmergencyContactPhone());
        profile.setEmergencyContactRelationship(request.getEmergencyContactRelationship());
        profile.setPreferredLanguage(request.getPreferredLanguage());
        profile.setTimezone(request.getTimezone());
        profile.setNotificationPreferences(request.getNotificationPreferences());
        profile.setPrivacySettings(request.getPrivacySettings());
        if (request.getProfileStatus() != null) {
            profile.setProfileStatus(request.getProfileStatus());
        }
        if (request.getIsProfilePublic() != null) {
            profile.setIsProfilePublic(request.getIsProfilePublic());
        }
    }

    private UserProfileResponse mapToResponse(UserProfile profile) {
        return UserProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .username(profile.getUser().getUsername())
                .email(profile.getUser().getEmail())
                .role(profile.getUser().getRole())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .dateOfBirth(profile.getDateOfBirth())
                .avatar(profile.getAvatar())
                .gender(profile.getGender())
                .bio(profile.getBio())
                .phoneNumber(profile.getPhoneNumber())
                .alternateEmail(profile.getAlternateEmail())
                .address(profile.getAddress())
                .city(profile.getCity())
                .state(profile.getState())
                .postalCode(profile.getPostalCode())
                .country(profile.getCountry())
                .studentId(profile.getStudentId())
                .facultyId(profile.getFacultyId())
                .department(profile.getDepartment())
                .major(profile.getMajor())
                .yearOfStudy(profile.getYearOfStudy())
                .gpa(profile.getGpa())
                .enrollmentDate(profile.getEnrollmentDate())
                .expectedGraduationDate(profile.getExpectedGraduationDate())
                .jobTitle(profile.getJobTitle())
                .officeLocation(profile.getOfficeLocation())
                .officeHours(profile.getOfficeHours())
                .researchInterests(profile.getResearchInterests())
                .publications(profile.getPublications())
                .linkedInUrl(profile.getLinkedInUrl())
                .personalWebsite(profile.getPersonalWebsite())
                .emergencyContactName(profile.getEmergencyContactName())
                .emergencyContactPhone(profile.getEmergencyContactPhone())
                .emergencyContactRelationship(profile.getEmergencyContactRelationship())
                .preferredLanguage(profile.getPreferredLanguage())
                .timezone(profile.getTimezone())
                .notificationPreferences(profile.getNotificationPreferences())
                .privacySettings(profile.getPrivacySettings())
                .profileStatus(profile.getProfileStatus())
                .isProfilePublic(profile.getIsProfilePublic())
                .lastProfileUpdate(profile.getLastProfileUpdate())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
