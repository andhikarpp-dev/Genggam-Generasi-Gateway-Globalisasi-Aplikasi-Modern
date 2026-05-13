-- =====================================================================
-- Genggam API - DDL & Seed Data (PostgreSQL)
-- Generasi Gateway Globalisasi Aplikasi Modern
-- =====================================================================
-- Cara pakai:
--   psql -U postgres -d genggam -f docs/ddl.sql
-- =====================================================================

-- Bersih-bersih (HATI-HATI di production)
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS services     CASCADE;
DROP TABLE IF EXISTS banners      CASCADE;
DROP TABLE IF EXISTS users        CASCADE;

-- ---------------------------------------------------------------------
-- 1. USERS
-- ---------------------------------------------------------------------
CREATE TABLE users (
    id             BIGSERIAL      PRIMARY KEY,
    email          VARCHAR(100)   NOT NULL UNIQUE,
    first_name     VARCHAR(50)    NOT NULL,
    last_name      VARCHAR(50)    NOT NULL,
    password       VARCHAR(100)   NOT NULL,                    -- BCrypt hash
    profile_image  TEXT           NULL,
    balance        NUMERIC(18,2)  NOT NULL DEFAULT 0
                   CHECK (balance >= 0),
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users (email);

-- ---------------------------------------------------------------------
-- 2. BANNERS
-- ---------------------------------------------------------------------
CREATE TABLE banners (
    id            BIGSERIAL    PRIMARY KEY,
    banner_name   VARCHAR(100) NOT NULL,
    banner_image  TEXT         NOT NULL,
    description   TEXT         NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- ---------------------------------------------------------------------
-- 3. SERVICES (Master layanan PPOB)
-- ---------------------------------------------------------------------
CREATE TABLE services (
    id              BIGSERIAL      PRIMARY KEY,
    service_code    VARCHAR(50)    NOT NULL UNIQUE,
    service_name    VARCHAR(100)   NOT NULL,
    service_icon    TEXT           NOT NULL,
    service_tariff  NUMERIC(18,2)  NOT NULL CHECK (service_tariff > 0),
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_services_code ON services (service_code);

-- ---------------------------------------------------------------------
-- 4. TRANSACTIONS (riwayat topup & payment)
-- ---------------------------------------------------------------------
CREATE TABLE transactions (
    id                BIGSERIAL      PRIMARY KEY,
    invoice_number    VARCHAR(30)    NOT NULL UNIQUE,
    user_id           BIGINT         NOT NULL,
    transaction_type  VARCHAR(10)    NOT NULL
                      CHECK (transaction_type IN ('TOPUP','PAYMENT')),
    service_code      VARCHAR(50)    NULL,
    description       TEXT           NOT NULL,
    total_amount      NUMERIC(18,2)  NOT NULL CHECK (total_amount > 0),
    created_on        TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_tx_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT fk_tx_service
        FOREIGN KEY (service_code) REFERENCES services (service_code)
        ON DELETE SET NULL,

    -- TOPUP tidak perlu service_code; PAYMENT wajib punya service_code
    CONSTRAINT chk_tx_service_code
        CHECK (
            (transaction_type = 'TOPUP'   AND service_code IS NULL) OR
            (transaction_type = 'PAYMENT' AND service_code IS NOT NULL)
        )
);

CREATE INDEX idx_tx_user_created ON transactions (user_id, created_on DESC);
CREATE INDEX idx_tx_invoice      ON transactions (invoice_number);

-- =====================================================================
-- SEED DATA
-- =====================================================================

-- ---- Banners ----
INSERT INTO banners (banner_name, banner_image, description) VALUES
('Banner 1', 'https://nutech-integrasi.app/dummy.jpg', 'Lerem Ipsum Dolor sit amet'),
('Banner 2', 'https://nutech-integrasi.app/dummy.jpg', 'Lerem Ipsum Dolor sit amet'),
('Banner 3', 'https://nutech-integrasi.app/dummy.jpg', 'Lerem Ipsum Dolor sit amet'),
('Banner 4', 'https://nutech-integrasi.app/dummy.jpg', 'Lerem Ipsum Dolor sit amet'),
('Banner 5', 'https://nutech-integrasi.app/dummy.jpg', 'Lerem Ipsum Dolor sit amet'),
('Banner 6', 'https://nutech-integrasi.app/dummy.jpg', 'Lerem Ipsum Dolor sit amet');

-- ---- Services (sesuai Swagger Nutech) ----
INSERT INTO services (service_code, service_name, service_icon, service_tariff) VALUES
('PAJAK',            'Pajak PBB',         'https://nutech-integrasi.app/dummy.jpg', 40000),
('PLN',              'Listrik',           'https://nutech-integrasi.app/dummy.jpg', 10000),
('PDAM',             'PDAM Berlangganan', 'https://nutech-integrasi.app/dummy.jpg', 40000),
('PULSA',            'Pulsa',             'https://nutech-integrasi.app/dummy.jpg', 40000),
('PGN',              'PGN Berlangganan',  'https://nutech-integrasi.app/dummy.jpg', 50000),
('MUSIK',            'Musik Berlangganan','https://nutech-integrasi.app/dummy.jpg', 50000),
('TV',               'TV Berlangganan',   'https://nutech-integrasi.app/dummy.jpg', 50000),
('PAKET_DATA',       'Paket data',        'https://nutech-integrasi.app/dummy.jpg', 50000),
('VOUCHER_GAME',     'Voucher Game',      'https://nutech-integrasi.app/dummy.jpg', 100000),
('VOUCHER_MAKANAN',  'Voucher Makanan',   'https://nutech-integrasi.app/dummy.jpg', 100000),
('QURBAN',           'Qurban',            'https://nutech-integrasi.app/dummy.jpg', 200000),
('ZAKAT',            'Zakat',             'https://nutech-integrasi.app/dummy.jpg', 300000);

-- =====================================================================
-- Selesai. Database siap digunakan.
-- =====================================================================
