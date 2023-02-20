package com.attornatus.attornatus.repositories;

import com.attornatus.attornatus.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT obj FROM Address obj " +
            "WHERE obj.user.id = :userId AND " +
            "obj.mainAddress = true")
    Address findByMainAddress(Long userId);

}
