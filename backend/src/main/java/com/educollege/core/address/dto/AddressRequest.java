package com.educollege.core.address.dto;

import com.educollege.core.address.model.OwnerType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {

    private OwnerType ownerType;
    private Long ownerId;

    // ===== Address Information =====
    private String houseNumber;
    private String street;
    private String ward;
    private String district;
    private String city;
    private String province;
    private String postalCode;
    private String country;

    // ===== Geographic Information =====
    private Double latitude;
    private Double longitude;

    // ===== Status Information =====
    @Builder.Default
    private String status = "ACTIVE";
    @Builder.Default
    private Boolean isPrimary = false;
}
