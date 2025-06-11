-- ME_SEUM Database Schema for MySQL
-- Museum Nusantara Management System

CREATE DATABASE IF NOT EXISTS ME_SEUM;
USE ME_SEUM;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('CURATOR', 'CUSTOMER', 'CLEANER') NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    no_telepon VARCHAR(20),
    tanggal_registrasi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('AKTIF', 'NONAKTIF') DEFAULT 'AKTIF'
);

-- Artefak table - Modified to match the UI in the image
CREATE TABLE IF NOT EXISTS artefak (
    artefak_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama_artefak VARCHAR(255) NOT NULL,     -- Name
    asal_daerah VARCHAR(100),               -- From
    deskripsi_artefak TEXT,                 -- Description
    gambar VARCHAR(255),                    -- Images
    periode VARCHAR(100),                   -- Additional fields kept for compatibility
    status ENUM('TERSEDIA', 'DIPAMERKAN', 'DIPELIHARA', 'RUSAK') DEFAULT 'TERSEDIA',
    tanggal_registrasi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    curator_id BIGINT,
    FOREIGN KEY (curator_id) REFERENCES users(user_id)
);

-- Pameran table
CREATE TABLE IF NOT EXISTS pameran (
    pameran_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nama_pameran VARCHAR(255) NOT NULL,
    deskripsi_pameran TEXT,
    tanggal_mulai DATE NOT NULL,
    tanggal_selesai DATE NOT NULL,
    lokasi VARCHAR(100),
    status ENUM('AKTIF', 'SELESAI', 'DIJADWALKAN') DEFAULT 'DIJADWALKAN',
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
    tiket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    jenis_tiket ENUM('REGULER', 'PREMIUM', 'GRUP') NOT NULL,
    harga DECIMAL(10,2) NOT NULL,
    tanggal_kunjungan DATE NOT NULL,
    status ENUM('AKTIF', 'DIGUNAKAN', 'EXPIRED') DEFAULT 'AKTIF',
    kode_tiket VARCHAR(20) UNIQUE NOT NULL,
    tanggal_pembelian TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Feedback table
CREATE TABLE IF NOT EXISTS feedback (
    feedback_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    komentar TEXT,
    tanggal_feedback TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Pemeliharaan table
CREATE TABLE IF NOT EXISTS pemeliharaan (
    pemeliharaan_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    artefak_id BIGINT,
    jenis_pemeliharaan ENUM('RUTIN', 'DARURAT', 'RESTORASI') NOT NULL,
    deskripsi_pemeliharaan TEXT,
    tanggal_mulai DATE NOT NULL,
    tanggal_selesai DATE,
    status ENUM('DIJADWALKAN', 'SEDANG_BERLANGSUNG', 'SELESAI') DEFAULT 'DIJADWALKAN',
    petugas_id BIGINT,
    catatan TEXT,
    FOREIGN KEY (artefak_id) REFERENCES artefak(artefak_id),
    FOREIGN KEY (petugas_id) REFERENCES users(user_id)
);

-- Indexes untuk optimasi query
CREATE INDEX idx_artefak_nama ON artefak(nama_artefak);
CREATE INDEX idx_artefak_asal_daerah ON artefak(asal_daerah);
CREATE INDEX idx_artefak_status ON artefak(status);
CREATE INDEX idx_artefak_periode ON artefak(periode);
CREATE INDEX idx_pameran_status ON pameran(status);
CREATE INDEX idx_pameran_tanggal ON pameran(tanggal_mulai, tanggal_selesai);
CREATE INDEX idx_tiket_tanggal_kunjungan ON tiket(tanggal_kunjungan);
CREATE INDEX idx_tiket_status ON tiket(status);
CREATE INDEX idx_feedback_rating ON feedback(rating);
CREATE INDEX idx_pemeliharaan_status ON pemeliharaan(status);

-- Views for MySQL (keeping these as they provide useful functionality)
-- View untuk artefak yang sedang dipamerkan
CREATE OR REPLACE VIEW v_artefak_dipamerkan AS
SELECT 
    a.artefak_id,
    a.nama_artefak,
    a.deskripsi_artefak,
    a.asal_daerah,
    a.periode,
    p.nama_pameran,
    p.lokasi,
    p.tanggal_mulai,
    p.tanggal_selesai
FROM artefak a
JOIN artefak_pameran ap ON a.artefak_id = ap.artefak_id
JOIN pameran p ON ap.pameran_id = p.pameran_id
WHERE a.status = 'DIPAMERKAN' AND p.status = 'AKTIF';

-- View untuk statistik museum
CREATE OR REPLACE VIEW v_statistik_museum AS
SELECT 
    (SELECT COUNT(*) FROM artefak) as total_artefak,
    (SELECT COUNT(*) FROM artefak WHERE status = 'TERSEDIA') as artefak_tersedia,
    (SELECT COUNT(*) FROM artefak WHERE status = 'DIPAMERKAN') as artefak_dipamerkan,
    (SELECT COUNT(*) FROM artefak WHERE status = 'DIPELIHARA') as artefak_dipelihara,
    (SELECT COUNT(*) FROM pameran WHERE status = 'AKTIF') as pameran_aktif,
    (SELECT COUNT(*) FROM users WHERE role = 'CUSTOMER') as total_customer,
    (SELECT AVG(rating) FROM feedback) as rata_rata_rating,
    (SELECT COUNT(*) FROM tiket WHERE status = 'AKTIF') as tiket_aktif;

-- View untuk laporan feedback
CREATE OR REPLACE VIEW v_laporan_feedback AS
SELECT 
    f.feedback_id,
    u.nama_lengkap,
    u.email,
    f.rating,
    f.komentar,
    f.tanggal_feedback
FROM feedback f
JOIN users u ON f.user_id = u.user_id
ORDER BY f.tanggal_feedback DESC;