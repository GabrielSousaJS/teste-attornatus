package com.attornatus.attornatus.repositories;

import com.attornatus.attornatus.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
