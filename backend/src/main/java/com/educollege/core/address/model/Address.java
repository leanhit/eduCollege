package com.educollege.core.address.model;

import com.educollege.core.shared.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends AuditableEntity {

    // ===== Owner Information =====
    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false, length = 30)
    private OwnerType ownerType;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    // ===== Address Fields =====
    @Column(name = "house_number", length = 255)
    private String houseNumber;

    @Column(name = "street", length = 255)
    private String street;

    @Column(name = "ward", length = 255)
    private String ward;

    @Column(name = "district", length = 255)
    private String district;

    @Column(name = "city", length = 255)
    private String city;

    @Column(name = "province", length = 255)
    private String province;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "country", length = 255)
    private String country;

    // ===== Geographic Information =====
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    // ===== Status Information =====
    @Column(name = "is_primary", nullable = false)
    @Builder.Default
    private Boolean isPrimary = false;

    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE";

    // ===== Helper Methods =====
    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder();
        
        if (houseNumber != null && !houseNumber.trim().isEmpty()) {
            fullAddress.append(houseNumber).append(", ");
        }
        
        if (street != null && !street.trim().isEmpty()) {
            fullAddress.append(street).append(", ");
        }
        
        if (ward != null && !ward.trim().isEmpty()) {
            fullAddress.append(ward).append(", ");
        }
        
        if (district != null && !district.trim().isEmpty()) {
            fullAddress.append(district).append(", ");
        }
        
        if (city != null && !city.trim().isEmpty()) {
            fullAddress.append(city).append(", ");
        }
        
        if (province != null && !province.trim().isEmpty()) {
            fullAddress.append(province).append(", ");
        }
        
        if (postalCode != null && !postalCode.trim().isEmpty()) {
            fullAddress.append(postalCode).append(" ");
        }
        
        if (country != null && !country.trim().isEmpty()) {
            fullAddress.append(country);
        }
        
        return fullAddress.toString().trim();
    }
}
