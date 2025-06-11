-- ME_SEUM Database Schema for PostgreSQL
-- Museum Nusantara Management System

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('CURATOR', 'CUSTOMER', 'CLEANER')) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    no_telepon VARCHAR(20),
    tanggal_registrasi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('AKTIF', 'NONAKTIF')) DEFAULT 'AKTIF'
);

-- Artefak table - Modified to match the UI in the image
CREATE TABLE IF NOT EXISTS artefak (
    artefak_id BIGSERIAL PRIMARY KEY,
    nama_artefak VARCHAR(255) NOT NULL,     -- Name
    asal_daerah VARCHAR(100),               -- From
    deskripsi_artefak TEXT,                 -- Description
    gambar VARCHAR(255),                    -- Images
    periode VARCHAR(100),                   -- Additional fields kept for compatibility
    status VARCHAR(20) CHECK (status IN ('TERSEDIA', 'DIPAMERKAN', 'DIPELIHARA', 'RUSAK')) DEFAULT 'TERSEDIA',
    tanggal_registrasi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    curator_id BIGINT,
    FOREIGN KEY (curator_id) REFERENCES users(user_id)
);

-- Pameran table
CREATE TABLE IF NOT EXISTS pameran (
    pameran_id BIGSERIAL PRIMARY KEY,
    nama_pameran VARCHAR(255) NOT NULL,
    deskripsi_pameran TEXT,
    tanggal_mulai DATE NOT NULL,
    tanggal_selesai DATE NOT NULL,
    lokasi VARCHAR(100),
    status VARCHAR(20) CHECK (status IN ('AKTIF', 'SELESAI', 'DIJADWALKAN')) DEFAULT 'DIJADWALKAN',
    curator_id BIGINT,
    FOREIGN KEY (curator_id) REFERENCES users(user_id)
);

-- Artefak_Pameran (Many-to-Many relationship)
CREATE TABLE IF NOT EXISTS artefak_pameran (
    artefak_id BIGINT,
    pameran_id BIGINT,
    PRIMARY KEY (artefak_id, pameran_id),
    FOREIGN KEY (artefak_id) REFERENCES artefak(artefak_id) ON DELETE CASCADE,
    FOREIGN KEY (pameran_id) REFERENCES pameran(pameran_id) ON DELETE CASCADE
);

-- Tiket table
CREATE TABLE IF NOT EXISTS tiket (
    tiket_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    jenis_tiket VARCHAR(20) CHECK (jenis_tiket IN ('REGULER', 'PREMIUM', 'GRUP')) NOT NULL,
    harga DECIMAL(10,2) NOT NULL,
    tanggal_kunjungan DATE NOT NULL,
    status VARCHAR(20) CHECK (status IN ('AKTIF', 'DIGUNAKAN', 'EXPIRED')) DEFAULT 'AKTIF',
    kode_tiket VARCHAR(20) UNIQUE NOT NULL,
    tanggal_pembelian TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Feedback table
CREATE TABLE IF NOT EXISTS feedback (
    feedback_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    komentar TEXT,
    tanggal_feedback TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Pemeliharaan table
CREATE TABLE IF NOT EXISTS pemeliharaan (
    pemeliharaan_id BIGSERIAL PRIMARY KEY,
    artefak_id BIGINT,
    jenis_pemeliharaan VARCHAR(20) CHECK (jenis_pemeliharaan IN ('RUTIN', 'DARURAT', 'RESTORASI')) NOT NULL,
    deskripsi_pemeliharaan TEXT,
    tanggal_mulai DATE NOT NULL,
    tanggal_selesai DATE,
    status VARCHAR(20) CHECK (status IN ('DIJADWALKAN', 'SEDANG_BERLANGSUNG', 'SELESAI')) DEFAULT 'DIJADWALKAN',
    petugas_id BIGINT,
    catatan TEXT,
    FOREIGN KEY (artefak_id) REFERENCES artefak(artefak_id),
    FOREIGN KEY (petugas_id) REFERENCES users(user_id)
);

-- Indexes untuk optimasi query
CREATE INDEX IF NOT EXISTS idx_artefak_nama ON artefak(nama_artefak);
CREATE INDEX IF NOT EXISTS idx_artefak_asal_daerah ON artefak(asal_daerah);
CREATE INDEX IF NOT EXISTS idx_artefak_status ON artefak(status);
CREATE INDEX IF NOT EXISTS idx_artefak_periode ON artefak(periode);
CREATE INDEX IF NOT EXISTS idx_pameran_status ON pameran(status);
CREATE INDEX IF NOT EXISTS idx_pameran_tanggal ON pameran(tanggal_mulai, tanggal_selesai);
CREATE INDEX IF NOT EXISTS idx_tiket_tanggal_kunjungan ON tiket(tanggal_kunjungan);
CREATE INDEX IF NOT EXISTS idx_tiket_status ON tiket(status);
CREATE INDEX IF NOT EXISTS idx_feedback_rating ON feedback(rating);
CREATE INDEX IF NOT EXISTS idx_pemeliharaan_status ON pemeliharaan(status);