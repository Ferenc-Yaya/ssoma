-- =================================================================
-- SCRIPT DE INICIALIZACIÓN DE BASE DE DATOS MULTI-TENANT SSOMA
-- =================================================================

-- -----------------------------------------------------------------
-- 1. CREAR ESQUEMAS PARA MULTI-TENENCIA
-- -----------------------------------------------------------------

CREATE SCHEMA IF NOT EXISTS tenant_a;
CREATE SCHEMA IF NOT EXISTS tenant_b;
CREATE SCHEMA IF NOT EXISTS public;

-- -----------------------------------------------------------------
-- 2. TABLAS DEL SISTEMA DE AUTENTICACIÓN (SCHEMA PUBLIC)
-- -----------------------------------------------------------------

CREATE TABLE IF NOT EXISTS public.tbl_roles (
    rol_id UUID PRIMARY KEY,
    nombre_rol VARCHAR(100) NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS public.tbl_usuarios (
    usuario_id UUID PRIMARY KEY,
    nombre_usuario VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    tenant_id VARCHAR(50) NOT NULL,
    persona_id UUID,
    rol_id UUID REFERENCES public.tbl_roles(rol_id),
    is_super_admin BOOLEAN NOT NULL DEFAULT FALSE
);

-- -----------------------------------------------------------------
-- 3. TABLAS PARA EMPRESAS-SERVICE (POR TENANT)
-- -----------------------------------------------------------------

-- Tenant A: Empresas
--/////////////////////////////////////////////////////////////

-- ============================================
-- 1. CATÁLOGO: TIPOS DE EMPRESA
-- ============================================
CREATE TABLE IF NOT EXISTS tenant_a.cat_tipos_empresa (
tipo_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
codigo VARCHAR(50) NOT NULL UNIQUE,
nombre VARCHAR(100) NOT NULL,
activo BOOLEAN DEFAULT true
);

-- ============================================
-- 2. CATÁLOGO: REQUISITOS
-- ============================================
CREATE TABLE IF NOT EXISTS tenant_a.cat_requisitos (
categoria_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
codigo VARCHAR(50) NOT NULL UNIQUE,
nombre VARCHAR(100) NOT NULL,
activo BOOLEAN DEFAULT true
);

-- ============================================
-- 3. CONFIGURACIÓN: TIPO - REQUISITOS
-- ============================================
CREATE TABLE IF NOT EXISTS tenant_a.cat_tipo_requisitos (
tipo_categoria_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
tipo_id UUID NOT NULL REFERENCES tenant_a.cat_tipos_empresa(tipo_id) ON DELETE CASCADE,
categoria_id UUID NOT NULL REFERENCES tenant_a.cat_requisitos(categoria_id) ON DELETE CASCADE,
activo BOOLEAN DEFAULT true,
UNIQUE(tipo_id, categoria_id)
);

-- ============================================
-- 4. TABLA PRINCIPAL: EMPRESAS
-- ============================================
CREATE TABLE IF NOT EXISTS tenant_a.tbl_empresas (
empresa_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
ruc VARCHAR(20) NOT NULL UNIQUE,
razon_social VARCHAR(200) NOT NULL,
direccion VARCHAR(255),
sector VARCHAR(100),
score_seguridad INT,
fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
activo BOOLEAN DEFAULT true
);

-- ============================================
-- 5. CONTACTOS DE EMPRESA
-- ============================================
CREATE TABLE IF NOT EXISTS tenant_a.tbl_empresa_contactos (
contacto_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
empresa_id UUID NOT NULL REFERENCES tenant_a.tbl_empresas(empresa_id) ON DELETE CASCADE,
nombre_contacto VARCHAR(200),
telefono VARCHAR(20),
email VARCHAR(100),
es_principal BOOLEAN DEFAULT false,
activo BOOLEAN DEFAULT true
);

-- ============================================
-- 6. SERVICIOS DE EMPRESA
-- ============================================
CREATE TABLE IF NOT EXISTS tenant_a.tbl_empresa_servicios (
servicio_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
empresa_id UUID NOT NULL REFERENCES tenant_a.tbl_empresas(empresa_id) ON DELETE CASCADE,
descripcion_servicio TEXT NOT NULL,
nivel_riesgo VARCHAR(50), -- 'ALTO', 'MEDIO', 'BAJO', 'ALTO_ESPECIALIZADO'
activo BOOLEAN DEFAULT true
);

-- ============================================
-- 7. RELACIÓN: EMPRESA - TIPO
-- ============================================
CREATE TABLE IF NOT EXISTS tenant_a.tbl_empresa_tipos (
empresa_tipo_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
empresa_id UUID NOT NULL REFERENCES tenant_a.tbl_empresas(empresa_id) ON DELETE CASCADE,
tipo_id UUID NOT NULL REFERENCES tenant_a.cat_tipos_empresa(tipo_id),
activo BOOLEAN DEFAULT true,
UNIQUE(empresa_id, tipo_id)
);

-- ============================================
-- 8. EXCEPCIONES/REGLAS: EMPRESA - REQUISITOS
-- ============================================
CREATE TABLE IF NOT EXISTS tenant_a.tbl_empresa_requisitos (
empresa_requisito_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
empresa_tipo_id UUID NOT NULL REFERENCES tenant_a.tbl_empresa_tipos(empresa_tipo_id) ON DELETE CASCADE,
categoria_id UUID NOT NULL REFERENCES tenant_a.cat_requisitos(categoria_id),
aplica BOOLEAN DEFAULT true,
activo BOOLEAN DEFAULT true,
UNIQUE(empresa_tipo_id, categoria_id)
);

-- ============================================
-- DATOS INICIALES
-- ============================================

-- Tipos de empresa
INSERT INTO tenant_a.cat_tipos_empresa (codigo, nombre) VALUES
('PERMANENTE', 'Contratistas Permanentes'),
('EVENTUAL', 'Contratistas Eventuales'),
('VISITA', 'Visitas');

-- Requisitos (antes "categorías")
INSERT INTO tenant_a.cat_requisitos (codigo, nombre) VALUES
('GENERALES', 'Generales'),
('PERSONAL', 'Personal'),
('CONDUCTOR', 'Conductor'),
('VEHICULO', 'Vehículo'),
('HERRAMIENTAS', 'Herramientas'),
('OPERATIVOS', 'Operativos'),
('OTROS', 'Otros');

-- Configuración DEFAULT (según imagen 3)
-- PERMANENTE: GENERALES, PERSONAL, CONDUCTOR, VEHICULO
INSERT INTO tenant_a.cat_tipo_requisitos (tipo_id, categoria_id)
SELECT t.tipo_id, r.categoria_id
FROM tenant_a.cat_tipos_empresa t
CROSS JOIN tenant_a.cat_requisitos r
WHERE t.codigo = 'PERMANENTE'
AND r.codigo IN ('GENERALES', 'PERSONAL', 'CONDUCTOR', 'VEHICULO');

-- EVENTUAL: TODAS
INSERT INTO tenant_a.cat_tipo_requisitos (tipo_id, categoria_id)
SELECT t.tipo_id, r.categoria_id
FROM tenant_a.cat_tipos_empresa t
CROSS JOIN tenant_a.cat_requisitos r
WHERE t.codigo = 'EVENTUAL';

-- VISITA: PERSONAL, CONDUCTOR, VEHICULO
INSERT INTO tenant_a.cat_tipo_requisitos (tipo_id, categoria_id)
SELECT t.tipo_id, r.categoria_id
FROM tenant_a.cat_tipos_empresa t
CROSS JOIN tenant_a.cat_requisitos r
WHERE t.codigo = 'VISITA'
AND r.codigo IN ('PERSONAL', 'CONDUCTOR', 'VEHICULO');

--//////////////////////////////////////////////////////////////////////////

-- Tenant B: Empresas
CREATE TABLE IF NOT EXISTS tenant_b.tbl_empresas (
    empresa_id UUID PRIMARY KEY,
    ruc VARCHAR(20) NOT NULL,
    razon_social VARCHAR(200) NOT NULL,
    direccion VARCHAR(255),
    sector VARCHAR(100),
    score_seguridad INT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT true
);

-- -----------------------------------------------------------------
-- 4. TABLAS PARA PERSONAS-SERVICE (POR TENANT)
-- -----------------------------------------------------------------

-- Tenant A: Personas
CREATE TABLE IF NOT EXISTS tenant_a.tbl_personas (
    persona_id UUID PRIMARY KEY,
    empresa_id UUID REFERENCES tenant_a.tbl_empresas(empresa_id),
    tipo_persona VARCHAR(50) NOT NULL,
    nombre_completo VARCHAR(200) NOT NULL,
    dni VARCHAR(20) NOT NULL,
    fecha_nacimiento DATE,
    genero VARCHAR(20),
    telefono VARCHAR(20),
    correo VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS tenant_a.tbl_salud_personas (
    salud_id UUID PRIMARY KEY,
    persona_id UUID REFERENCES tenant_a.tbl_personas(persona_id),
    grupo_sanguineo VARCHAR(10),
    alergias TEXT,
    historial_medico TEXT
);

CREATE TABLE IF NOT EXISTS tenant_a.tbl_certificaciones (
    certificacion_id UUID PRIMARY KEY,
    persona_id UUID REFERENCES tenant_a.tbl_personas(persona_id),
    nombre_certificacion VARCHAR(100) NOT NULL,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE
);

CREATE TABLE IF NOT EXISTS tenant_a.tbl_score_seguridad (
    score_id UUID PRIMARY KEY,
    persona_id UUID REFERENCES tenant_a.tbl_personas(persona_id),
    puntuacion INT NOT NULL,
    fecha_evaluacion DATE NOT NULL,
    motivo_cambio TEXT
);

-- Tenant B: Personas
CREATE TABLE IF NOT EXISTS tenant_b.tbl_personas (
    persona_id UUID PRIMARY KEY,
    empresa_id UUID REFERENCES tenant_b.tbl_empresas(empresa_id),
    tipo_persona VARCHAR(50) NOT NULL,
    nombre_completo VARCHAR(200) NOT NULL,
    dni VARCHAR(20) NOT NULL,
    fecha_nacimiento DATE,
    genero VARCHAR(20),
    telefono VARCHAR(20),
    correo VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS tenant_b.tbl_salud_personas (
    salud_id UUID PRIMARY KEY,
    persona_id UUID REFERENCES tenant_b.tbl_personas(persona_id),
    grupo_sanguineo VARCHAR(10),
    alergias TEXT,
    historial_medico TEXT
);

CREATE TABLE IF NOT EXISTS tenant_b.tbl_certificaciones (
    certificacion_id UUID PRIMARY KEY,
    persona_id UUID REFERENCES tenant_b.tbl_personas(persona_id),
    nombre_certificacion VARCHAR(100) NOT NULL,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE
);

CREATE TABLE IF NOT EXISTS tenant_b.tbl_score_seguridad (
    score_id UUID PRIMARY KEY,
    persona_id UUID REFERENCES tenant_b.tbl_personas(persona_id),
    puntuacion INT NOT NULL,
    fecha_evaluacion DATE NOT NULL,
    motivo_cambio TEXT
);

-- -----------------------------------------------------------------
-- 5. TABLAS PARA ACTIVOS-SERVICE (POR TENANT)
-- -----------------------------------------------------------------

-- Tenant A: Activos
CREATE TABLE IF NOT EXISTS tenant_a.tbl_activos (
    activo_id UUID PRIMARY KEY,
    empresa_id UUID REFERENCES tenant_a.tbl_empresas(empresa_id),
    nombre_activo VARCHAR(100) NOT NULL,
    tipo_activo VARCHAR(50) NOT NULL,
    codigo_activo VARCHAR(50),
    estado VARCHAR(50) NOT NULL,
    tiene_rops_fops BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tenant_a.tbl_mantenimientos (
    mantenimiento_id UUID PRIMARY KEY,
    activo_id UUID REFERENCES tenant_a.tbl_activos(activo_id),
    fecha_mantenimiento DATE NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS tenant_a.tbl_asignaciones_operativas (
    asignacion_id UUID PRIMARY KEY,
    activo_id UUID REFERENCES tenant_a.tbl_activos(activo_id),
    persona_id UUID REFERENCES tenant_a.tbl_personas(persona_id),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    estado VARCHAR(20) NOT NULL,
    observaciones TEXT
);

-- Tenant B: Activos
CREATE TABLE IF NOT EXISTS tenant_b.tbl_activos (
    activo_id UUID PRIMARY KEY,
    empresa_id UUID REFERENCES tenant_b.tbl_empresas(empresa_id),
    nombre_activo VARCHAR(100) NOT NULL,
    tipo_activo VARCHAR(50) NOT NULL,
    codigo_activo VARCHAR(50),
    estado VARCHAR(50) NOT NULL,
    tiene_rops_fops BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tenant_b.tbl_mantenimientos (
    mantenimiento_id UUID PRIMARY KEY,
    activo_id UUID REFERENCES tenant_b.tbl_activos(activo_id),
    fecha_mantenimiento DATE NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS tenant_b.tbl_asignaciones_operativas (
    asignacion_id UUID PRIMARY KEY,
    activo_id UUID REFERENCES tenant_b.tbl_activos(activo_id),
    persona_id UUID REFERENCES tenant_b.tbl_personas(persona_id),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    estado VARCHAR(20) NOT NULL,
    observaciones TEXT
);

-- -----------------------------------------------------------------
-- 6. TABLAS PARA DOCUMENTAL-SERVICE (POR TENANT)
-- -----------------------------------------------------------------

-- Tenant A: Documentos
CREATE TABLE IF NOT EXISTS tenant_a.tbl_documentos (
    documento_id UUID PRIMARY KEY,
    nombre_archivo VARCHAR(255) NOT NULL,
    tipo_archivo VARCHAR(50) NOT NULL,
    version INT DEFAULT 1,
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    documento_url VARCHAR(255) NOT NULL,
    persona_id UUID REFERENCES tenant_a.tbl_personas(persona_id),
    activo_id UUID REFERENCES tenant_a.tbl_activos(activo_id)
);

CREATE TABLE IF NOT EXISTS tenant_a.tbl_documentos_empresa (
    documento_empresa_id UUID PRIMARY KEY,
    empresa_id UUID REFERENCES tenant_a.tbl_empresas(empresa_id),
    nombre_documento VARCHAR(200) NOT NULL,
    fecha_vencimiento DATE,
    documento_url VARCHAR(255)
);

-- Tenant B: Documentos
CREATE TABLE IF NOT EXISTS tenant_b.tbl_documentos (
    documento_id UUID PRIMARY KEY,
    nombre_archivo VARCHAR(255) NOT NULL,
    tipo_archivo VARCHAR(50) NOT NULL,
    version INT DEFAULT 1,
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    documento_url VARCHAR(255) NOT NULL,
    persona_id UUID REFERENCES tenant_b.tbl_personas(persona_id),
    activo_id UUID REFERENCES tenant_b.tbl_activos(activo_id)
);

CREATE TABLE IF NOT EXISTS tenant_b.tbl_documentos_empresa (
    documento_empresa_id UUID PRIMARY KEY,
    empresa_id UUID REFERENCES tenant_b.tbl_empresas(empresa_id),
    nombre_documento VARCHAR(200) NOT NULL,
    fecha_vencimiento DATE,
    documento_url VARCHAR(255)
);

-- -----------------------------------------------------------------
-- 7. TABLAS PARA BPM-SERVICE (POR TENANT)
-- -----------------------------------------------------------------

-- Tenant A: Permisos de Trabajo y Riesgos
CREATE TABLE IF NOT EXISTS tenant_a.tbl_riesgos (
    riesgo_id UUID PRIMARY KEY,
    nombre_riesgo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    medidas_control TEXT
);

CREATE TABLE IF NOT EXISTS tenant_a.tbl_permisos_trabajo (
    permiso_id UUID PRIMARY KEY,
    empresa_id UUID REFERENCES tenant_a.tbl_empresas(empresa_id),
    solicitado_por_persona_id UUID REFERENCES tenant_a.tbl_personas(persona_id),
    estado_aprobacion VARCHAR(50) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    tipo_trabajo VARCHAR(50),
    ubicacion VARCHAR(100),
    aprobador_principal_id UUID REFERENCES tenant_a.tbl_personas(persona_id),
    fecha_aprobacion TIMESTAMP
);

-- Tenant B: Permisos de Trabajo y Riesgos
CREATE TABLE IF NOT EXISTS tenant_b.tbl_riesgos (
    riesgo_id UUID PRIMARY KEY,
    nombre_riesgo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    medidas_control TEXT
);

CREATE TABLE IF NOT EXISTS tenant_b.tbl_permisos_trabajo (
    permiso_id UUID PRIMARY KEY,
    empresa_id UUID REFERENCES tenant_b.tbl_empresas(empresa_id),
    solicitado_por_persona_id UUID REFERENCES tenant_b.tbl_personas(persona_id),
    estado_aprobacion VARCHAR(50) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    tipo_trabajo VARCHAR(50),
    ubicacion VARCHAR(100),
    aprobador_principal_id UUID REFERENCES tenant_b.tbl_personas(persona_id),
    fecha_aprobacion TIMESTAMP
);

-- -----------------------------------------------------------------
-- 8. TABLAS PARA COMPLIANCE-SERVICE (POR TENANT)
-- -----------------------------------------------------------------

-- Tenant A: Reglas y Validaciones
CREATE TABLE IF NOT EXISTS tenant_a.tbl_reglas_cumplimiento (
    regla_id UUID PRIMARY KEY,
    ambito VARCHAR(50) NOT NULL,
    tipo_elemento VARCHAR(50) NOT NULL,
    sede_condicion VARCHAR(50),
    campo_validacion_1 VARCHAR(50) NOT NULL,
    valor_max_1 INT,
    documento_requerido VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tenant_a.tbl_historial_validacion (
    historial_id UUID PRIMARY KEY,
    referencia_id UUID NOT NULL,
    tipo_referencia VARCHAR(20) NOT NULL,
    valido BOOLEAN NOT NULL,
    motivos_incumplimiento JSONB,
    fecha_validacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tenant B: Reglas y Validaciones
CREATE TABLE IF NOT EXISTS tenant_b.tbl_reglas_cumplimiento (
    regla_id UUID PRIMARY KEY,
    ambito VARCHAR(50) NOT NULL,
    tipo_elemento VARCHAR(50) NOT NULL,
    sede_condicion VARCHAR(50),
    campo_validacion_1 VARCHAR(50) NOT NULL,
    valor_max_1 INT,
    documento_requerido VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tenant_b.tbl_historial_validacion (
    historial_id UUID PRIMARY KEY,
    referencia_id UUID NOT NULL,
    tipo_referencia VARCHAR(20) NOT NULL,
    valido BOOLEAN NOT NULL,
    motivos_incumplimiento JSONB,
    fecha_validacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------
-- 9. TABLAS PARA NOTIFICATIONS-SERVICE (POR TENANT)
-- -----------------------------------------------------------------

-- Tenant A: Notificaciones
CREATE TABLE IF NOT EXISTS tenant_a.tbl_log_notificaciones (
    log_id UUID PRIMARY KEY,
    referencia_id UUID NOT NULL,
    destinatario_email VARCHAR(100) NOT NULL,
    tipo_alerta VARCHAR(50) NOT NULL,
    estado_final_esp VARCHAR(20) NOT NULL,
    fecha_hora_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    contenido TEXT
);

-- Tenant B: Notificaciones
CREATE TABLE IF NOT EXISTS tenant_b.tbl_log_notificaciones (
    log_id UUID PRIMARY KEY,
    referencia_id UUID NOT NULL,
    destinatario_email VARCHAR(100) NOT NULL,
    tipo_alerta VARCHAR(50) NOT NULL,
    estado_final_esp VARCHAR(20) NOT NULL,
    fecha_hora_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    contenido TEXT
);

-- -----------------------------------------------------------------
-- 10. TABLAS PARA AUDITORIA-SERVICE (POR TENANT)
-- -----------------------------------------------------------------

-- Tenant A: Auditoria
CREATE TABLE IF NOT EXISTS tenant_a.tbl_auditoria (
    auditoria_id UUID PRIMARY KEY,
    usuario_id UUID,
    accion VARCHAR(50) NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    detalles_json JSONB
);

-- Tenant B: Auditoria
CREATE TABLE IF NOT EXISTS tenant_b.tbl_auditoria (
    auditoria_id UUID PRIMARY KEY,
    usuario_id UUID,
    accion VARCHAR(50) NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    detalles_json JSONB
);

-- -----------------------------------------------------------------
-- 11. CREAR USUARIOS INICIALES PARA PRUEBAS
-- -----------------------------------------------------------------

-- Roles iniciales
INSERT INTO public.tbl_roles (rol_id, nombre_rol, descripcion)
VALUES
(gen_random_uuid(), 'SUPER_ADMIN', 'Administrador con acceso a todos los tenants'),
(gen_random_uuid(), 'ADMIN', 'Administrador de un tenant específico'),
(gen_random_uuid(), 'USER', 'Usuario regular');

-- -----------------------------------------------------------------
-- 12. VERIFICACIÓN
-- -----------------------------------------------------------------

DO $$
BEGIN
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'BASE DE DATOS INICIALIZADA CORRECTAMENTE';
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'Schemas creados: tenant_a, tenant_b';
    RAISE NOTICE 'Tablas creadas por microservicio:';
    RAISE NOTICE '- auth-service: tbl_usuarios, tbl_roles';
    RAISE NOTICE '- empresas-service: tbl_empresas';
    RAISE NOTICE '- personas-service: tbl_personas, tbl_salud_personas, tbl_certificaciones, tbl_score_seguridad';
    RAISE NOTICE '- activos-service: tbl_activos, tbl_asignaciones_operativas, tbl_mantenimientos';
    RAISE NOTICE '- documental-service: tbl_documentos, tbl_documentos_empresa';
    RAISE NOTICE '- bpm-service: tbl_permisos_trabajo, tbl_riesgos';
    RAISE NOTICE '- compliance-service: tbl_reglas_cumplimiento, tbl_historial_validacion';
    RAISE NOTICE '- notifications-service: tbl_log_notificaciones';
    RAISE NOTICE '- auditoria-service: tbl_auditoria';
    RAISE NOTICE '==============================================';
END $$;