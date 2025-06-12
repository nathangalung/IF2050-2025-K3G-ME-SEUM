-- PostgreSQL-compatible sample data for ME_SEUM
-- Handle duplicates with ON CONFLICT

-- Clear existing data (optional - remove if you want to keep existing data)
-- DELETE FROM pemeliharaan;
-- DELETE FROM feedback;
-- DELETE FROM tiket;
-- DELETE FROM artefak_pameran;
-- DELETE FROM pameran;
-- DELETE FROM artefak;
-- DELETE FROM users;

-- Sample Users - Use ON CONFLICT to handle duplicates
INSERT INTO users (username, email, password, role, nama_lengkap, no_telepon) VALUES
('curator1', 'curator@museum.id', 'password123', 'CURATOR', 'Dr. Siti Nurhaliza', '081234567890'),
('curator2', 'curator2@museum.id', 'password123', 'CURATOR', 'Prof. Bambang Setiawan', '081234567891'),
('customer1', 'customer@email.com', 'password123', 'CUSTOMER', 'Budi Santoso', '081234567892'),
('customer2', 'customer2@email.com', 'password123', 'CUSTOMER', 'Andi Wijaya', '081234567893'),
('customer3', 'customer3@email.com', 'password123', 'CUSTOMER', 'Dewi Sartika', '081234567894'),
('cleaner1', 'cleaner@museum.id', 'password123', 'CLEANER', 'Ahmad Cleaning', '081234567895'),
('cleaner2', 'cleaner2@museum.id', 'password123', 'CLEANER', 'Sari Pembersih', '081234567896')
ON CONFLICT (username) DO NOTHING;

-- Sample Artefak
INSERT INTO artefak (nama_artefak, deskripsi_artefak, status, asal_daerah, periode, curator_id, gambar) VALUES
('Keris Pusaka Jawa', 'Keris pusaka dari Kerajaan Majapahit dengan ukiran halus dan motif naga', 'DIPAMERKAN', 'Jawa Timur', 'Abad 14', 1, 'keris_jawa.jpg'),
('Batik Tulis Yogya', 'Batik tulis motif klasik dari Yogyakarta dengan pewarna alami', 'DIPAMERKAN', 'Yogyakarta', 'Abad 19', 1, 'batik_yogya.jpg'),
('Ukiran Kayu Jepara', 'Ukiran kayu tradisional dari Jepara dengan motif flora dan fauna', 'TERSEDIA', 'Jawa Tengah', 'Abad 18', 2, 'ukiran_jepara.jpg'),
('Songket Palembang', 'Kain songket dengan benang emas dari Palembang', 'DIPAMERKAN', 'Sumatera Selatan', 'Abad 17', 1, 'songket_palembang.jpg'),
('Tenun Ikat Flores', 'Tenun ikat tradisional dengan motif khas Flores', 'TERSEDIA', 'Nusa Tenggara Timur', 'Abad 16', 2, 'tenun_flores.jpg'),
('Gamelan Jawa', 'Set gamelan lengkap dari Jawa Tengah dengan tuning slendro', 'DIPAMERKAN', 'Jawa Tengah', 'Abad 15', 1, 'gamelan_jawa.jpg'),
('Topeng Bali', 'Topeng tari tradisional Bali dari kayu pule dengan cat alami', 'DIPAMERKAN', 'Bali', 'Abad 19', 2, 'topeng_bali.jpg'),
('Wayang Kulit Jawa', 'Wayang kulit dengan cerita Ramayana dari Jawa Tengah', 'TERSEDIA', 'Jawa Tengah', 'Abad 18', 1, 'wayang_jawa.jpg'),
('Perhiasan Emas Sumatera', 'Perhiasan emas tradisional Minangkabau dengan ukiran halus', 'DIPAMERKAN', 'Sumatera Barat', 'Abad 16', 2, 'perhiasan_sumatra.jpg'),
('Sasando NTT', 'Alat musik tradisional dari Pulau Rote dengan senar lontar', 'TERSEDIA', 'Nusa Tenggara Timur', 'Abad 17', 1, 'sasando_ntt.jpg')
ON CONFLICT DO NOTHING;

-- Sample Pameran
INSERT INTO pameran (nama_pameran, deskripsi_pameran, tanggal_mulai, tanggal_selesai, lokasi, status, curator_id, artefak_ids, is_active, tanggal_dibuat) VALUES
('Warisan Nusantara 2024', 'Pameran koleksi artefak tradisional Indonesia dari berbagai daerah', '2024-01-01 08:00:00', '2024-06-30 18:00:00', 'Ruang Pamer Utama', 'AKTIF', 1, '1,2,4,7,9', true, '2024-01-01 08:00:00'),
('Kerajinan Tradisional', 'Pameran khusus kerajinan tangan tradisional Indonesia', '2024-07-01 09:00:00', '2024-12-31 17:00:00', 'Ruang Pamer 2', 'DIJADWALKAN', 2, '3,5,10', true, '2024-06-01 10:00:00'),
('Budaya Jawa Kuno', 'Pameran artefak dari masa Kerajaan Majapahit dan Mataram', '2024-03-15 10:00:00', '2024-09-15 16:00:00', 'Ruang Pamer 3', 'AKTIF', 1, '1,2,8', true, '2024-02-15 14:00:00'),
('Seni Musik Nusantara', 'Koleksi alat musik tradisional dari seluruh Indonesia', '2024-05-01 09:00:00', '2024-11-30 17:00:00', 'Ruang Pamer 4', 'DIJADWALKAN', 2, '6,10', true, '2024-04-01 09:00:00'),
('Tekstil Nusantara', 'Pameran kain dan tekstil tradisional Indonesia', '2024-02-01 10:00:00', '2024-08-31 16:00:00', 'Ruang Pamer 5', 'AKTIF', 1, '2,4,5', true, '2024-01-15 11:00:00')
ON CONFLICT DO NOTHING;

-- Sample Artefak-Pameran Relations
INSERT INTO artefak_pameran (pameran_id, artefak_id) VALUES
-- Warisan Nusantara 2024 (pameran_id: 1)
(1, 1), -- Keris Pusaka Jawa
(1, 2), -- Batik Tulis Yogya  
(1, 4), -- Songket Palembang
(1, 7), -- Topeng Bali
(1, 9), -- Perhiasan Emas Sumatera

-- Kerajinan Tradisional (pameran_id: 2)
(2, 3), -- Ukiran Kayu Jepara
(2, 5), -- Tenun Ikat Flores
(2, 10), -- Sasando NTT

-- Budaya Jawa Kuno (pameran_id: 3)
(3, 1), -- Keris Pusaka Jawa
(3, 2), -- Batik Tulis Yogya
(3, 8), -- Wayang Kulit Jawa

-- Seni Musik Nusantara (pameran_id: 4)
(4, 6), -- Gamelan Jawa
(4, 10), -- Sasando NTT

-- Tekstil Nusantara (pameran_id: 5)
(5, 2), -- Batik Tulis Yogya
(5, 4), -- Songket Palembang
(5, 5) -- Tenun Ikat Flores
ON CONFLICT DO NOTHING;

-- Sample Tiket
INSERT INTO tiket (user_id, jenis_tiket, harga, tanggal_kunjungan, status, kode_tiket, tanggal_pembelian) VALUES
(3, 'REGULER', 25000.00, '2024-01-15', 'DIGUNAKAN', 'TKT20240115001', '2024-01-14 09:30:00'),
(4, 'PREMIUM', 50000.00, '2024-01-20', 'DIGUNAKAN', 'TKT20240120001', '2024-01-19 15:45:00'),
(5, 'REGULER', 25000.00, '2024-02-05', 'DIGUNAKAN', 'TKT20240205001', '2024-02-04 11:15:00'),
(3, 'PREMIUM', 50000.00, '2024-03-10', 'DIGUNAKAN', 'TKT20240310001', '2024-03-09 15:45:00'),
(4, 'REGULER', 25000.00, '2024-03-15', 'DIGUNAKAN', 'TKT20240315001', '2024-03-14 10:20:00'),
(5, 'REGULER', 25000.00, '2024-04-01', 'AKTIF', 'TKT20240401001', '2024-03-31 14:30:00'),
(3, 'PREMIUM', 50000.00, '2024-04-05', 'AKTIF', 'TKT20240405001', '2024-04-04 16:45:00')
ON CONFLICT (kode_tiket) DO NOTHING;

-- Sample Feedback (SINGLE SET - REMOVED DUPLICATES)
INSERT INTO feedback (user_id, rating, komentar, tanggal_feedback) VALUES
(3, 5, 'Museum yang sangat bagus! Koleksi artefaknya lengkap dan menarik. Saya sangat terkesan dengan pameran Warisan Nusantara.', '2024-01-15 10:30:00'),
(4, 4, 'Pemandu museumnya sangat informatif, tapi fasilitas toilet perlu diperbaiki. Overall bagus sekali!', '2024-01-20 14:45:00'),
(5, 5, 'Luar biasa! Anak-anak saya sangat suka dengan koleksi wayang kulitnya. Terima kasih Museum Nusantara!', '2024-02-05 09:15:00'),
(3, 4, 'Pameran Budaya Jawa Kuno sangat edukatif. Sayang parkiran agak sempit untuk weekend.', '2024-02-10 16:20:00'),
(4, 5, 'Koleksi batiknya menakjubkan! Saya belajar banyak tentang sejarah batik Indonesia.', '2024-02-25 11:00:00'),
(3, 4, 'Great collection, would love to see more interactive displays.', '2024-01-16 14:20:00'),
(5, 5, 'The event was fantastic! Learned so much about Indonesian culture.', '2024-01-17 16:45:00'),
(4, 3, 'Good but could be improved with better lighting in some areas.', '2024-01-18 11:15:00'),
(3, 5, 'Outstanding! The staff was very knowledgeable and helpful.', '2024-01-19 13:30:00')
ON CONFLICT DO NOTHING;

-- Sample Pemeliharaan
INSERT INTO pemeliharaan (artefak_id, jenis_pemeliharaan, deskripsi_pemeliharaan, tanggal_mulai, tanggal_selesai, status, petugas_id, catatan) VALUES
(1, 'RUTIN', 'Pembersihan dan perawatan rutin keris pusaka', '2024-01-10', '2024-01-12', 'SELESAI', 6, 'Pembersihan dilakukan dengan hati-hati menggunakan kain khusus'),
(2, 'RESTORASI', 'Restorasi bagian batik yang mulai pudar', '2024-01-20', '2024-01-25', 'SELESAI', 7, 'Menggunakan pewarna alami sesuai teknik tradisional'),
(9, 'RESTORASI', 'Restorasi perhiasan emas yang mengalami oksidasi ringan', '2024-02-01', NULL, 'SEDANG_BERLANGSUNG', 6, 'Proses restorasi memerlukan keahlian khusus'),
(3, 'RUTIN', 'Perawatan rutin ukiran kayu Jepara', '2024-02-15', '2024-02-16', 'SELESAI', 6, 'Aplikasi pelindung kayu natural'),
(6, 'DARURAT', 'Perbaikan gamelan yang mengalami kerusakan minor', '2024-03-05', NULL, 'DIJADWALKAN', 6, 'Memerlukan koordinasi dengan ahli gamelan')
ON CONFLICT DO NOTHING;