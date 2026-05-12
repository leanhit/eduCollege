package com.educollege.core.address.service;

import com.educollege.core.address.dto.AddressRequest;
import com.educollege.core.address.model.Address;
import com.educollege.core.address.model.OwnerType;
import com.educollege.core.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    @Transactional
    public Address createAddress(AddressRequest request) {
        log.info("Creating address: {}", request);
        
        Address address = Address.builder()
                .ownerType(request.getOwnerType())
                .ownerId(request.getOwnerId())
                .houseNumber(request.getHouseNumber())
                .street(request.getStreet())
                .ward(request.getWard())
                .district(request.getDistrict())
                .city(request.getCity())
                .province(request.getProvince())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .isPrimary(request.getIsPrimary())
                .status(request.getStatus())
                .build();
        
        // Set as primary if this is first address for this owner
        if (address.getOwnerId() != null && address.getOwnerType() != null) {
            List<Address> existingAddresses = addressRepository.findByOwnerIdAndOwnerType(
                address.getOwnerId(), 
                address.getOwnerType()
            );
            
            if (existingAddresses.isEmpty()) {
                address.setIsPrimary(true);
            } else {
                address.setIsPrimary(false);
            }
        }
        
        return addressRepository.save(address);
    }

    @Transactional(readOnly = true)
    public Optional<Address> getPrimaryAddress(Long ownerId, OwnerType ownerType) {
        log.info("Getting primary address for owner: {} and type: {}", ownerId, ownerType);
        return addressRepository.findByOwnerIdAndOwnerTypeAndIsPrimary(ownerId, ownerType, true);
    }

    @Transactional(readOnly = true)
    public List<Address> getAddressesByOwner(Long ownerId, OwnerType ownerType) {
        log.info("Getting addresses for owner: {} and type: {}", ownerId, ownerType);
        return addressRepository.findByOwnerIdAndOwnerType(ownerId, ownerType);
    }

    @Transactional(readOnly = true)
    public List<Address> searchAddresses(String city, String district, String province, String country) {
        log.info("Searching addresses with filters: city={}, district={}, province={}, country={}", city, district, province, country);
        return addressRepository.searchAddresses(city, district, province, country);
    }

    @Transactional
    public void deleteAddress(Long id) {
        log.info("Deleting address: {}", id);
        addressRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Address getAddressById(Long id) {
        log.info("Getting address by ID: {}", id);
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }
}
