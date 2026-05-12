package com.educollege.core.address.controller;

import com.educollege.core.address.dto.AddressRequest;
import com.educollege.core.address.model.Address;
import com.educollege.core.address.model.OwnerType;
import com.educollege.core.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAddress(@RequestBody AddressRequest request) {
        log.info("Creating address: {}", request);
        
        Address address = addressService.createAddress(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Address created successfully");
        response.put("data", address);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAddress(@PathVariable Long id) {
        log.info("Getting address by ID: {}", id);
        
        Address address = addressService.getAddressById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Address retrieved successfully");
        response.put("data", address);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/{ownerId}/type/{ownerType}")
    public ResponseEntity<Map<String, Object>> getAddressesByOwner(
            @PathVariable Long ownerId,
            @PathVariable String ownerType) {
        log.info("Getting addresses for owner: {} and type: {}", ownerId, ownerType);
        
        List<Address> addresses = addressService.getAddressesByOwner(ownerId, OwnerType.valueOf(ownerType));
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Addresses retrieved successfully");
        response.put("data", addresses);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/{ownerId}/type/{ownerType}/primary")
    public ResponseEntity<Map<String, Object>> getPrimaryAddress(
            @PathVariable Long ownerId,
            @PathVariable String ownerType) {
        log.info("Getting primary address for owner: {} and type: {}", ownerId, ownerType);
        
        var address = addressService.getPrimaryAddress(ownerId, OwnerType.valueOf(ownerType));
        
        Map<String, Object> response = new HashMap<>();
        if (address.isPresent()) {
            response.put("success", true);
            response.put("message", "Primary address retrieved successfully");
            response.put("data", address.get());
        } else {
            response.put("success", false);
            response.put("message", "Primary address not found");
            response.put("data", null);
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchAddresses(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String country) {
        log.info("Searching addresses with filters: city={}, district={}, province={}, country={}", city, district, province, country);
        
        List<Address> addresses = addressService.searchAddresses(city, district, province, country);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Addresses searched successfully");
        response.put("data", addresses);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAddress(@PathVariable Long id) {
        log.info("Deleting address: {}", id);
        
        addressService.deleteAddress(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Address deleted successfully");
        
        return ResponseEntity.ok(response);
    }
}
