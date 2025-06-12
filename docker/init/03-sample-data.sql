-- PostgreSQL-compatible sample data for ME_SEUM

-- Sample Users
INSERT INTO users (username, email, password, role, nama_lengkap, no_telepon) VALUES
('curator1', 'curator@museum.id', 'password123', 'CURATOR', 'Dr. Siti Nurhaliza', '081234567890'),
('curator2', 'curator2@museum.id', 'password123', 'CURATOR', 'Prof. Bambang Setiawan', '081234567891'),
('customer1', 'customer@email.com', 'password123', 'CUSTOMER', 'Budi Santoso', '081234567892'),
('customer2', 'customer2@email.com', 'password123', 'CUSTOMER', 'Andi Wijaya', '081234567893'),
('customer3', 'customer3@email.com', 'password123', 'CUSTOMER', 'Dewi Sartika', '081234567894'),
('cleaner1', 'cleaner@museum.id', 'password123', 'CLEANER', 'Ahmad Cleaning', '081234567895'),
('cleaner2', 'cleaner2@museum.id', 'password123', 'CLEANER', 'Sari Pembersih', '081234567896');

-- Sample Artefak
INSERT INTO artefak (nama_artefak, deskripsi_artefak, status, asal_daerah, periode, curator_id, gambar) VALUES
('Keris Pusaka Jawa', 'Keris pusaka dari Kerajaan Majapahit dengan ukiran halus dan motif naga', 'DIPAMERKAN', 'Jawa Timur', 'Abad 14', 1, 'keris_jawa.jpg'),
('Batik Tulis Yogya', 'Batik tulis tradisional dari Yogyakarta dengan motif parang rusak', 'DIPAMERKAN', 'DI Yogyakarta', 'Abad 18', 1, 'batik_yogya.jpg'),
('Ukiran Kayu Jepara', 'Ukiran kayu halus khas Jepara dengan motif flora fauna', 'TERSEDIA', 'Jawa Tengah', 'Abad 19', 2, 'ukiran_jepara.jpg'),
('Songket Palembang', 'Kain songket emas dari Palembang dengan motif bunga tanjung', 'TERSEDIA', 'Sumatera Selatan', 'Abad 17', 2, 'songket_palembang.jpg'),
('Tenun Ikat Flores', 'Tenun ikat tradisional dari Flores dengan pewarna alam', 'DIPAMERKAN', 'Nusa Tenggara Timur', 'Abad 16', 1, 'tenun_flores.jpg'),
('Gamelan Jawa', 'Seperangkat gamelan lengkap dari Jawa Tengah', 'TERSEDIA', 'Jawa Tengah', 'Abad 15', 2, 'gamelan_jawa.jpg'),
('Topeng Bali', 'Topeng tradisional Bali untuk tari Topeng dengan ukiran detail', 'DIPAMERKAN', 'Bali', 'Abad 18', 1, 'topeng_bali.jpg'),
('Wayang Kulit Jawa', 'Wayang kulit tradisional dengan cerita Ramayana', 'TERSEDIA', 'Jawa Tengah', 'Abad 16', 2, 'wayang_kulit.jpg'),
('Perhiasan Emas Sumatera', 'Perhiasan emas tradisional Sumatera dengan motif khas', 'DIPELIHARA', 'Sumatera Barat', 'Abad 17', 1, 'perhiasan_sumatera.jpg'),
('Sasando NTT', 'Alat musik tradisional dari Pulau Rote dengan senar lontar', 'TERSEDIA', 'Nusa Tenggara Timur', 'Abad 17', 1, 'sasando_ntt.jpg');

-- Sample Pameran
INSERT INTO pameran (nama_pameran, deskripsi_pameran, tanggal_mulai, tanggal_selesai, lokasi, status, curator_id) VALUES
('Warisan Nusantara 2024', 'Pameran koleksi artefak tradisional Indonesia dari berbagai daerah', '2024-01-01', '2024-06-30', 'Ruang Pamer Utama', 'AKTIF', 1),
('Kerajinan Tradisional', 'Pameran khusus kerajinan tangan tradisional Indonesia', '2024-07-01', '2024-12-31', 'Ruang Pamer 2', 'DIJADWALKAN', 2),
('Budaya Jawa Kuno', 'Pameran artefak dari masa Kerajaan Majapahit dan Mataram', '2024-03-15', '2024-09-15', 'Ruang Pamer 3', 'AKTIF', 1),
('Seni Musik Nusantara', 'Koleksi alat musik tradisional dari seluruh Indonesia', '2024-05-01', '2024-11-30', 'Ruang Pamer 4', 'DIJADWALKAN', 2),
('Tekstil Nusantara', 'Pameran kain dan tekstil tradisional Indonesia', '2024-02-01', '2024-08-31', 'Ruang Pamer 5', 'AKTIF', 1);

-- Sample Artefak-Pameran Relations
INSERT INTO artefak_pameran (pameran_id, artefak_id) VALUES
(1, 1), (1, 2), (1, 4), (1, 7), (1, 9),
(2, 3), (2, 5), (2, 10),
(3, 1), (3, 2), (3, 8),
(4, 6), (4, 10),
(5, 2), (5, 3), (5, 7);

-- Sample Tiket
INSERT INTO tiket (user_id, jenis_tiket, harga, tanggal_kunjungan, status, kode_tiket, tanggal_pembelian) VALUES
(3, 'REGULER', 25000.00, '2024-01-15', 'DIGUNAKAN', 'TKT20240115001', '2024-01-14 09:30:00'),
(4, 'PREMIUM', 50000.00, '2024-01-20', 'DIGUNAKAN', 'TKT20240120001', '2024-01-19 15:45:00'),
(5, 'GRUP', 200000.00, '2024-02-05', 'DIGUNAKAN', 'TKT20240205001', '2024-02-04 10:15:00'),
(3, 'REGULER', 25000.00, '2024-02-10', 'DIGUNAKAN', 'TKT20240210001', '2024-02-09 16:20:00'),
(4, 'PREMIUM', 50000.00, '2024-02-25', 'DIGUNAKAN', 'TKT20240225001', '2024-02-24 11:00:00'),
(5, 'REGULER', 25000.00, '2024-03-01', 'DIGUNAKAN', 'TKT20240301001', '2024-02-28 13:30:00'),
(3, 'PREMIUM', 50000.00, '2024-03-10', 'DIGUNAKAN', 'TKT20240310001', '2024-03-09 15:45:00'),
(4, 'REGULER', 25000.00, '2024-03-15', 'DIGUNAKAN', 'TKT20240315001', '2024-03-14 10:20:00'),
(5, 'REGULER', 25000.00, '2024-04-01', 'AKTIF', 'TKT20240401001', '2024-03-31 14:30:00'),
(3, 'PREMIUM', 50000.00, '2024-04-05', 'AKTIF', 'TKT20240405001', '2024-04-04 16:45:00');

-- Sample Feedback
INSERT INTO feedback (user_id, rating, komentar, tanggal_feedback) VALUES
(3, 5, 'Museum yang sangat bagus! Koleksi artefaknya lengkap dan menarik. Saya sangat terkesan dengan pameran Warisan Nusantara.', '2024-01-15 10:30:00'),
(4, 4, 'Pemandu museumnya sangat informatif, tapi fasilitas toilet perlu diperbaiki. Overall bagus sekali!', '2024-01-20 14:45:00'),
(5, 5, 'Luar biasa! Anak-anak saya sangat suka dengan koleksi wayang kulitnya. Terima kasih Museum Nusantara!', '2024-02-05 09:15:00'),
(3, 4, 'Pameran Budaya Jawa Kuno sangat edukatif. Sayang parkiran agak sempit untuk weekend.', '2024-02-10 16:20:00'),
(4, 5, 'Koleksi batiknya menakjubkan! Saya belajar banyak tentang sejarah batik Indonesia.', '2024-02-25 11:00:00');

-- Sample Pemeliharaan
INSERT INTO pemeliharaan (artefak_id, jenis_pemeliharaan, deskripsi_pemeliharaan, tanggal_mulai, tanggal_selesai, status, petugas_id, catatan) VALUES
(1, 'RUTIN', 'Pembersihan dan perawatan rutin keris pusaka', '2024-01-10', '2024-01-12', 'SELESAI', 6, 'Pembersihan dilakukan dengan hati-hati menggunakan kain khusus'),
(9, 'RESTORASI', 'Restorasi perhiasan emas yang mengalami oksidasi ringan', '2024-02-01', NULL, 'SEDANG_BERLANGSUNG', 6, 'Proses restorasi memerlukan keahlian khusus'),
(3, 'RUTIN', 'Perawatan rutin ukiran kayu Jepara', '2024-02-15', '2024-02-16', 'SELESAI', 6, 'Aplikasi pelindung kayu natural'),
(6, 'DARURAT', 'Perbaikan gamelan yang mengalami kerusakan minor', '2024-03-05', NULL, 'DIJADWALKAN', 6, 'Memerlukan koordinasi dengan ahli gamelan');
