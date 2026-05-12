package com.educollege.core.user.profile.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Professional Info Request DTO - For updating professional information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfessionalInfoRequest {

    @Size(max = 100, message = "Job title must not exceed 100 characters")
    private String jobTitle;

    @Size(max = 100, message = "Office location must not exceed 100 characters")
    private String officeLocation;

    @Size(max = 200, message = "Office hours must not exceed 200 characters")
    private String officeHours;

    @Size(max = 1000, message = "Research interests must not exceed 1000 characters")
    private String researchInterests;

    @Size(max = 2000, message = "Publications must not exceed 2000 characters")
    private String publications;

    @Size(max = 500, message = "LinkedIn URL must not exceed 500 characters")
    private String linkedInUrl;

    @Size(max = 500, message = "Personal website must not exceed 500 characters")
    private String personalWebsite;
}
