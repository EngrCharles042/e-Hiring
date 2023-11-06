package com.swiftselect.repositories;

import com.swiftselect.domain.entities.Roles;
import com.swiftselect.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(Role name);
}