CREATE TABLE IF NOT EXISTS tbl_tenants (
    tenant_id VARCHAR(50) PRIMARY KEY, -- Ej. 'KALLPA'
    nombre_comercial VARCHAR(100) NOT NULL,
    ruc VARCHAR(20) NOT NULL,
    dominio_personalizado VARCHAR(100),
    logo_url TEXT,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cat_tipos_contratista (
    tipo_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo VARCHAR(50) NOT NULL UNIQUE, -- 'PERMANENTE', 'EVENTUAL', 'HOST'
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS tbl_empresas (
    empresa_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL REFERENCES tbl_tenants(tenant_id),
    ruc VARCHAR(20) NOT NULL,
    razon_social VARCHAR(200) NOT NULL,
    tipo_id UUID REFERENCES cat_tipos_contratista(tipo_id),

    direccion VARCHAR(255),
    sitio_web VARCHAR(100),
    rubro_comercial VARCHAR(100),

    score_seguridad INT DEFAULT 100,
    estado_habilitacion VARCHAR(20) DEFAULT 'PENDIENTE',
    activo BOOLEAN DEFAULT true,
    es_host BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, ruc)
);

CREATE UNIQUE INDEX idx_one_host_per_tenant
ON tbl_empresas (tenant_id)    -- Único por tenant_id
WHERE es_host = TRUE;          -- Solo aplica cuando es_host es TRUE

CREATE TABLE IF NOT EXISTS tbl_empresa_contactos (
    contacto_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL,
    empresa_id UUID REFERENCES tbl_empresas(empresa_id),
    nombre_completo VARCHAR(150) NOT NULL,
    cargo VARCHAR(100),
    tipo_contacto VARCHAR(50) NOT NULL, -- 'REPRESENTANTE_LEGAL', 'RESPONSABLE_EHS', 'ADMIN_CONTRATO'
    email VARCHAR(150),
    telefono VARCHAR(20),
    es_principal BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tbl_contratos (
    contrato_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL,
    empresa_id UUID REFERENCES tbl_empresas(empresa_id), -- El Contratista

    numero_oc VARCHAR(50) NOT NULL, -- "NUMERO DE OC"
    descripcion_servicio TEXT, -- "SERVICIO"

    -- Vigencia del Contrato
    fecha_inicio DATE, -- "FECHA DE INICIO"
    fecha_fin DATE, -- Calculado según "DURACIÓN"

    -- Responsables y Riesgo
    nivel_riesgo VARCHAR(50), -- "NIVEL DE RIESGO DEL SERVICIO" (Alto/Medio/Bajo)
    admin_contrato_kallpa VARCHAR(150), -- "ADMINISTRADOR DE CONTRATO" (Nombre del responsable interno)

    estado VARCHAR(20) DEFAULT 'ACTIVO', -- 'ACTIVO', 'CERRADO', 'SUSPENDIDO'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, numero_oc)
);

CREATE TABLE IF NOT EXISTS tbl_personas (
    persona_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL,
    empresa_id UUID REFERENCES tbl_empresas(empresa_id),

    -- Opción: Vincular persona a un contrato específico si es necesario
    contrato_activo_id UUID REFERENCES tbl_contratos(contrato_id),

    dni_ce VARCHAR(20) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    cargo_puesto VARCHAR(100) NOT NULL,
    foto_perfil_url TEXT,
    es_conductor BOOLEAN DEFAULT FALSE,
    grupo_sanguineo VARCHAR(10),
    email VARCHAR(150),
    estado_global VARCHAR(20) DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, dni_ce)
);

CREATE TABLE IF NOT EXISTS tbl_roles (
    rol_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo_rol VARCHAR(50) NOT NULL UNIQUE,
    nombre_mostrar VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS tbl_usuarios (
    usuario_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL,
    persona_id UUID,
    empresa_id UUID,
    es_host BOOLEAN DEFAULT false,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol_id UUID NOT NULL REFERENCES tbl_roles(rol_id),
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tbl_activos (
    activo_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL,
    empresa_propietaria_id UUID REFERENCES tbl_empresas(empresa_id),

    codigo_identificacion VARCHAR(50) NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    categoria_activo VARCHAR(50) NOT NULL, -- 'VEHICULO', 'HERRAMIENTA'

    -- Datos Vehículos
    anio_fabricacion INT,
    kilometraje_actual INT,
    tiene_rops BOOLEAN DEFAULT FALSE,
    tiene_fops BOOLEAN DEFAULT FALSE,

    -- Datos Herramientas
    fecha_ultima_calibracion DATE,
    tiene_guardas_seguridad BOOLEAN DEFAULT FALSE,
    sistema_proteccion_fugas BOOLEAN DEFAULT FALSE,

    estado_operativo VARCHAR(20) DEFAULT 'OPERATIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, codigo_identificacion)
);

CREATE TABLE IF NOT EXISTS tbl_asignaciones (
    asignacion_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL,
    activo_id UUID REFERENCES tbl_activos(activo_id),
    persona_id UUID REFERENCES tbl_personas(persona_id),
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_fin TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'VIGENTE'
);

CREATE TABLE IF NOT EXISTS cat_sustancias_peligrosas (
    sustancia_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre_producto VARCHAR(200) NOT NULL,
    marca_fabricante VARCHAR(100) NOT NULL,
    estado_fisico VARCHAR(20),

    -- Rombo NFPA 704
    nfpa_salud INT DEFAULT 0,
    nfpa_inflamabilidad INT DEFAULT 0,
    nfpa_reactividad INT DEFAULT 0,
    nfpa_riesgo_especifico VARCHAR(10),

    numero_un VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tbl_inventario_matpel (
    inventario_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL,
    empresa_propietaria_id UUID REFERENCES tbl_empresas(empresa_id),
    sustancia_id UUID REFERENCES cat_sustancias_peligrosas(sustancia_id),

    descripcion_uso TEXT,
    ubicacion_almacenamiento VARCHAR(150),
    cantidad_estimada DECIMAL(10,2),
    unidad_medida VARCHAR(20),

    estado_autorizacion VARCHAR(20) DEFAULT 'PENDIENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cat_documentos_requeribles (
    doc_req_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo_interno VARCHAR(50) UNIQUE NOT NULL,
    nombre_mostrar VARCHAR(100) NOT NULL,
    categoria_agrupacion VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS tbl_reglas_negocio (
    regla_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL,
    aplicar_a_tipo_empresa UUID REFERENCES cat_tipos_contratista(tipo_id),
    aplicar_a_rol_o_tipo VARCHAR(100),
    entidad_objetivo VARCHAR(20) NOT NULL,
    doc_req_id UUID REFERENCES cat_documentos_requeribles(doc_req_id),
    dias_vigencia_minima INT DEFAULT 0,
    es_bloqueante BOOLEAN DEFAULT TRUE,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS tbl_documentos (
    documento_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL,
    entidad_id UUID NOT NULL,
    tipo_entidad VARCHAR(20) NOT NULL,
    doc_req_id UUID REFERENCES cat_documentos_requeribles(doc_req_id),
    numero_documento VARCHAR(100),
    fecha_emision DATE,
    fecha_vencimiento DATE,
    archivo_url TEXT NOT NULL,
    estado_validacion VARCHAR(20) DEFAULT 'PENDIENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tbl_estado_cumplimiento (
    estado_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(50) NOT NULL,
    entidad_id UUID NOT NULL,
    tipo_entidad VARCHAR(20) NOT NULL,
    es_apto BOOLEAN DEFAULT FALSE,
    detalle_json JSONB,
    ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, entidad_id, tipo_entidad)
);

INSERT INTO tbl_tenants (tenant_id, nombre_comercial, ruc)
VALUES ('KALLPA', 'Kallpa Generación S.A.', '20512836262') ON CONFLICT DO NOTHING;

INSERT INTO cat_tipos_contratista (codigo, nombre) VALUES
('VISITAS', 'Visitas'), ('PERMANENTE', 'Contratista Permanente'), ('EVENTUAL', 'Contratista Eventual')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO tbl_roles (codigo_rol, nombre_mostrar) VALUES
('SUPER_ADMIN', 'Super Administrador'),
('ADMIN_HOST', 'Administrador Host'),
('ADMIN_PROVEEDOR', 'Administrador Proveedor')
ON CONFLICT (codigo_rol) DO NOTHING;

INSERT INTO cat_documentos_requeribles (codigo_interno, nombre_mostrar, categoria_agrupacion) VALUES
('SCTR_PEN', 'SCTR Pensión', 'PERSONAL'),
('SOAT', 'SOAT Vigente', 'ACTIVO'),
('CERT_CALIB', 'Certificado de Calibración', 'ACTIVO'),
('MSDS_FDS', 'Hoja de Datos de Seguridad (MSDS)', 'MATERIAL')
ON CONFLICT (codigo_interno) DO NOTHING;
