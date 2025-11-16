package com.dataservicesperu.ssoma.empresas_service.repository;

import com.dataservicesperu.ssoma.empresas_service.entity.TipoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TipoEmpresaRepository extends JpaRepository<TipoEmpresa, UUID> {

    Optional<TipoEmpresa> findByCodigo(String codigo);

    List<TipoEmpresa> findByActivoTrue();

    @Query("SELECT te FROM TipoEmpresa te LEFT JOIN FETCH te.requisitos WHERE te.tipoId = :id")
    Optional<TipoEmpresa> findByIdWithRequisitos(@Param("id") UUID id);
}
