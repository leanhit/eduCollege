package com.educollege.core.address.repository;

import com.educollege.core.address.model.Address;
import com.educollege.core.address.model.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByOwnerIdAndOwnerTypeAndIsPrimary(Long ownerId, OwnerType ownerType, Boolean isPrimary);

    List<Address> findByOwnerIdAndOwnerType(Long ownerId, OwnerType ownerType);

    List<Address> findByCity(String city);

    List<Address> findByDistrict(String district);

    List<Address> findByProvince(String province);

    @Query("SELECT a FROM Address a WHERE " +
           "LOWER(a.city) LIKE LOWER(CONCAT('%', :city, '%')) OR " +
           "LOWER(a.district) LIKE LOWER(CONCAT('%', :district, '%')) OR " +
           "LOWER(a.province) LIKE LOWER(CONCAT('%', :province, '%')) OR " +
           "LOWER(a.country) LIKE LOWER(CONCAT('%', :country, '%'))" +
           "ORDER BY a.isPrimary DESC, a.createdAt DESC")
    List<Address> searchAddresses(@Param("city") String city,
                                @Param("district") String district,
                                @Param("province") String province,
                                @Param("country") String country);

    List<Address> findByOwnerTypeAndStatus(OwnerType ownerType, String status);
}
