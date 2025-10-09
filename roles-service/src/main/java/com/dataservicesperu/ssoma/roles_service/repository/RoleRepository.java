package com.dataservicesperu.ssoma.roles_service.repository;

import com.dataservicesperu.ssoma.roles_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
}
