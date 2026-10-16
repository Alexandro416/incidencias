CREATE DATABASE IF NOT EXISTS incidencias_lima
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE incidencias_lima;

CREATE TABLE IF NOT EXISTS usuario (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre         VARCHAR(100) NOT NULL,
  email          VARCHAR(120) NOT NULL UNIQUE,
  password       VARCHAR(255) NOT NULL,
  rol            VARCHAR(20)  NOT NULL DEFAULT 'USER',
  fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_usuario_rol CHECK (rol IN ('USER','ADMIN'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS distrito (
  id     BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS incidencia (
  id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
  titulo               VARCHAR(150) NOT NULL,
  descripcion          TEXT         NOT NULL,
  fecha                DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  latitud              DECIMAL(10,7) NOT NULL,
  longitud             DECIMAL(10,7) NOT NULL,
  url_imagen_evidencia VARCHAR(500),
  estado               VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE',
  usuario_id           BIGINT NOT NULL,
  distrito_id          BIGINT NOT NULL,
  CONSTRAINT fk_inc_usuario  FOREIGN KEY (usuario_id)  REFERENCES usuario(id),
  CONSTRAINT fk_inc_distrito FOREIGN KEY (distrito_id) REFERENCES distrito(id),
  CONSTRAINT chk_inc_estado CHECK (estado IN ('PENDIENTE','EN_REVISION','EN_PROCESO','RESUELTO','RECHAZADO')),
  INDEX idx_inc_estado (estado),
  INDEX idx_inc_distrito (distrito_id),
  INDEX idx_inc_usuario (usuario_id)
) ENGINE=InnoDB;

-- Los 43 distritos de Lima Metropolitana
INSERT IGNORE INTO distrito (nombre) VALUES
 ('Ancón'),('Ate'),('Barranco'),('Breña'),('Carabayllo'),('Chaclacayo'),('Chorrillos'),
 ('Cieneguilla'),('Comas'),('El Agustino'),('Independencia'),('Jesús María'),('La Molina'),
 ('La Victoria'),('Lima'),('Lince'),('Los Olivos'),('Lurigancho'),('Lurín'),
 ('Magdalena del Mar'),('Miraflores'),('Pachacámac'),('Pucusana'),('Pueblo Libre'),
 ('Puente Piedra'),('Punta Hermosa'),('Punta Negra'),('Rímac'),('San Bartolo'),('San Borja'),
 ('San Isidro'),('San Juan de Lurigancho'),('San Juan de Miraflores'),('San Luis'),
 ('San Martín de Porres'),('San Miguel'),('Santa Anita'),('Santa María del Mar'),
 ('Santa Rosa'),('Santiago de Surco'),('Surquillo'),('Villa El Salvador'),
 ('Villa María del Triunfo');

