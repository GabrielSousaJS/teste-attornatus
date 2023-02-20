package com.attornatus.attornatus.repositories;

import com.attornatus.attornatus.entities.Address;
import com.attornatus.attornatus.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT DISTINCT obj.addresses FROM User obj " +
            "INNER JOIN obj.addresses " +
            "WHERE obj.id = :id")
    List<Address> findAddressOfPerson(Long id);

    User findByName(String name);
}
