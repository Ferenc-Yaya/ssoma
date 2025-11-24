package com.dataservicesperu.ssoma.auth_service.repository;

import com.dataservicesperu.ssoma.auth_service.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, UUID> {
    Optional<RolEntity> findByCodigoRol(String codigoRol);
}
