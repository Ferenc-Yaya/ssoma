-- =================================================================
-- SCRIPT DE INICIALIZACIÓN DE BASE DE DATOS MULTI-TENANT
-- =================================================================

-- -----------------------------------------------------------------
-- 1. CREAR ESQUEMAS PARA MULTI-TENENCIA
-- -----------------------------------------------------------------

CREATE SCHEMA IF NOT EXISTS tenant_a;
CREATE SCHEMA IF NOT EXISTS tenant_b;

-- -----------------------------------------------------------------
-- 2. CREAR TABLA DE ROLES EN TENANT_A
-- -----------------------------------------------------------------

CREATE TABLE IF NOT EXISTS tenant_a.tbl_roles (
    rol_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre_rol VARCHAR(100) NOT NULL,
    descripcion TEXT
);

-- -----------------------------------------------------------------
-- 3. CREAR TABLA DE ROLES EN TENANT_B
-- -----------------------------------------------------------------

CREATE TABLE IF NOT EXISTS tenant_b.tbl_roles (
    rol_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre_rol VARCHAR(100) NOT NULL,
    descripcion TEXT
);

-- -----------------------------------------------------------------
-- 4. CREAR TABLA GLOBAL DE USUARIOS (para auth-service)
-- -----------------------------------------------------------------

CREATE TABLE IF NOT EXISTS public.tbl_usuarios (
    usuario_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre_usuario VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    tenant_id VARCHAR(50) NOT NULL,
    rol VARCHAR(50) DEFAULT 'USER'
);

-- -----------------------------------------------------------------
-- 5. INSERTAR DATOS DE EJEMPLO
-- -----------------------------------------------------------------

-- Roles para tenant_a
INSERT INTO tenant_a.tbl_roles (nombre_rol, descripcion) VALUES
    ('Administrador Contrato', 'Responsable de la gestión y administración general del contrato'),
    ('Supervisor EHS', 'Supervisor de Medio Ambiente, Salud y Seguridad Ocupacional');

-- Roles para tenant_b
INSERT INTO tenant_b.tbl_roles (nombre_rol, descripcion) VALUES
    ('Contratista', 'Personal contratista con acceso limitado al sistema');

-- Usuarios de prueba
-- Password para todos: "123456" (hasheado con BCrypt)
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

INSERT INTO public.tbl_usuarios (nombre_usuario, password_hash, tenant_id, rol) VALUES
    ('admin@kallpa.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'tenant_a', 'ADMIN'),
    ('supervisor@kallpa.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'tenant_a', 'USER'),
    ('contratista@empresa.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'tenant_b', 'USER');

-- -----------------------------------------------------------------
-- 6. VERIFICACIÓN
-- -----------------------------------------------------------------

DO $$
BEGIN
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'BASE DE DATOS INICIALIZADA CORRECTAMENTE';
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'Schemas creados: tenant_a, tenant_b';
    RAISE NOTICE 'Roles en tenant_a: %', (SELECT COUNT(*) FROM tenant_a.tbl_roles);
    RAISE NOTICE 'Roles en tenant_b: %', (SELECT COUNT(*) FROM tenant_b.tbl_roles);
    RAISE NOTICE 'Usuarios creados: %', (SELECT COUNT(*) FROM public.tbl_usuarios);
    RAISE NOTICE '==============================================';
END $$;