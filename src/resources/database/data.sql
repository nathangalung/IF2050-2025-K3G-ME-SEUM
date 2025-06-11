-- ME_SEUM Sample Data
-- Museum Nusantara Management System

USE ME_SEUM;

-- Sample Users
INSERT INTO users (username, email, password, role, nama_lengkap, no_telepon) VALUES
('curator1', 'curator@museum.id', 'password123', 'CURATOR', 'Dr. Siti Nurhaliza', '081234567890'),
('curator2', 'curator2@museum.id', 'password123', 'CURATOR', 'Prof. Bambang Setiawan', '081234567891'),
('customer1', 'customer@email.com', 'password123', 'CUSTOMER', 'Budi Santoso', '081234567892'),
('customer2', 'customer2@email.com', 'password123', 'CUSTOMER', 'Andi Wijaya', '081234567893'),
('customer3', 'customer3@email.com', 'password123', 'CUSTOMER', 'Dewi Sartika', '081234567894'),
('cleaner1', 'cleaner@museum.id', 'password123', 'CLEANER', 'Ahmad Cleaning', '081234567895'),
('cleaner2', 'cleaner2@museum.id', 'password123', 'CLEANER', 'Sari Pembersih', '081234567896');

-- Sample Artefak (10 items)
INSERT INTO artefak (nama_artefak, deskripsi_artefak, status, asal_daerah, periode, curator_id, gambar) VALUES
('Keris Pusaka Jawa', 'Keris pusaka dari Kerajaan Majapahit dengan ukiran halus dan motif naga', 'DIPAMERKAN', 'Jawa Timur', 'Abad 14', 1, 'keris_jawa.jpg'),
('Batik Tulis Yogyakarta', 'Batik tulis tradisional dengan motif klasik Parang Rusak', 'DIPAMERKAN', 'Yogyakarta', 'Abad 18', 1, 'batik_yogya.jpg'),
('Ulos Batak Toba', 'Kain tradisional Batak Toba untuk upacara adat pernikahan', 'TERSEDIA', 'Sumatra Utara', 'Abad 19', 1, 'ulos_batak.jpg'),
('Topeng Barong Bali', 'Topeng Barong untuk upacara keagamaan Hindu Bali', 'DIPAMERKAN', 'Bali', 'Abad 17', 2, 'topeng_barong.jpg'),
('Mandau Dayak', 'Senjata tradisional suku Dayak Kalimantan dengan ukiran khas', 'DIPELIHARA', 'Kalimantan Tengah', 'Abad 16', 2, 'mandau_dayak.jpg'),
('Tifa Papua', 'Alat musik tradisional Papua untuk upacara adat', 'TERSEDIA', 'Papua', 'Abad 15', 1, 'tifa_papua.jpg'),
('Songket Palembang', 'Kain tenun emas dari Palembang dengan motif bunga cempaka', 'DIPAMERKAN', 'Sumatra Selatan', 'Abad 19', 2, 'songket_palembang.jpg'),
('Wayang Kulit Jawa', 'Wayang kulit dengan lakon Ramayana, karya Ki Dalang Surakarta', 'TERSEDIA', 'Jawa Tengah', 'Abad 16', 1, 'wayang_kulit.jpg'),
('Rumah Gadang Miniatur', 'Miniatur rumah adat Minangkabau dengan atap tanduk kerbau', 'DIPAMERKAN', 'Sumatra Barat', 'Abad 18', 2, 'rumah_gadang.jpg'),
('Sasando NTT', 'Alat musik tradisional dari Pulau Rote dengan senar lontar', 'TERSEDIA', 'Nusa Tenggara Timur', 'Abad 17', 1, 'sasando_ntt.jpg');

-- Sample Pameran
INSERT INTO pameran (nama_pameran, deskripsi_pameran, tanggal_mulai, tanggal_selesai, lokasi, status, curator_id) VALUES
('Warisan Nusantara 2024', 'Pameran koleksi artefak tradisional Indonesia dari berbagai daerah', '2024-01-01', '2024-06-30', 'Ruang Pamer Utama', 'AKTIF', 1),
('Kerajinan Tradisional', 'Pameran khusus kerajinan tangan tradisional Indonesia', '2024-07-01', '2024-12-31', 'Ruang Pamer 2', 'DIJADWALKAN', 2),
('Budaya Jawa Kuno', 'Pameran artefak dari masa Kerajaan Majapahit dan Mataram', '2024-03-15', '2024-09-15', 'Ruang Pamer 3', 'AKTIF', 1),
('Seni Musik Nusantara', 'Koleksi alat musik tradisional dari seluruh Indonesia', '2024-05-01', '2024-11-30', 'Ruang Pamer 4', 'DIJADWALKAN', 2),
('Tekstil Nusantara', 'Pameran kain dan tekstil tradisional Indonesia', '2024-02-01', '2024-08-31', 'Ruang Pamer 5', 'AKTIF', 1);

-- Sample Pameran-Artefak Relations
INSERT INTO artefak_pameran (pameran_id, artefak_id) VALUES
-- Warisan Nusantara 2024 (5 artefak)
(1, 1), (1, 2), (1, 4), (1, 7), (1, 9),
-- Kerajinan Tradisional (3 artefak)
(2, 3), (2, 5), (2, 10),
-- Budaya Jawa Kuno (3 artefak)
(3, 1), (3, 2), (3, 8),
-- Seni Musik Nusantara (2 artefak)
(4, 6), (4, 10),
-- Tekstil Nusantara (3 artefak)
(5, 2), (5, 3), (5, 7);

-- Sample Feedback
INSERT INTO feedback (user_id, rating, komentar, tanggal_feedback) VALUES
(3, 5, 'Museum yang sangat bagus! Koleksi artefaknya lengkap dan menarik. Saya sangat terkesan dengan pameran Warisan Nusantara.', '2024-01-15 10:30:00'),
(4, 4, 'Pemandu museumnya sangat informatif, tapi fasilitas toilet perlu diperbaiki. Overall bagus sekali!', '2024-01-20 14:45:00'),
(5, 5, 'Luar biasa! Anak-anak saya sangat suka dengan koleksi wayang kulitnya. Terima kasih Museum Nusantara!', '2024-02-05 09:15:00'),
(3, 4, 'Pameran Budaya Jawa Kuno sangat edukatif. Sayang parkiran agak sempit untuk weekend.', '2024-02-10 16:20:00'),
(4, 5, 'Koleksi batiknya menakjubkan! Saya belajar banyak tentang sejarah batik Indonesia.', '2024-02-25 11:00:00'),
(5, 3, 'Bagus tapi agak mahal tiketnya. Mungkin bisa ada diskon untuk pelajar?', '2024-03-01 13:30:00'),
(3, 5, 'Museum terbaik di Indonesia! Staff ramah dan knowledgeable. Pasti akan datang lagi.', '2024-03-10 15:45:00'),
(4, 4, 'Sangat informatif dan interaktif. Anak-anak tidak bosan sama sekali.', '2024-03-15 10:20:00');

-- Sample Tiket
INSERT INTO tiket (user_id, jenis_tiket, harga, tanggal_kunjungan, status, kode_tiket, tanggal_pembelian) VALUES
(3, 'REGULER', 25000.00, '2024-01-15', 'DIGUNAKAN', 'TKT20240115001', '2024-01-14 20:30:00'),
(4, 'PREMIUM', 50000.00, '2024-01-20', 'DIGUNAKAN', 'TKT20240120001', '2024-01-19 15:45:00'),
(5, 'GRUP', 200000.00, '2024-02-05', 'DIGUNAKAN', 'TKT20240205001', '2024-02-04 10:15:00'),
(3, 'REGULER', 25000.00, '2024-02-10', 'DIGUNAKAN', 'TKT20240210001', '2024-02-09 16:20:00'),
(4, 'PREMIUM', 50000.00, '2024-02-25', 'DIGUNAKAN', 'TKT20240225001', '2024-02-24 11:00:00'),
(5, 'REGULER', 25000.00, '2024-03-01', 'DIGUNAKAN', 'TKT20240301001', '2024-02-28 13:30:00'),
(3, 'PREMIUM', 50000.00, '2024-03-10', 'DIGUNAKAN', 'TKT20240310001', '2024-03-09 15:45:00'),
(4, 'REGULER', 25000.00, '2024-03-15', 'DIGUNAKAN', 'TKT20240315001', '2024-03-14 10:20:00'),
(5, 'REGULER', 25000.00, '2024-04-01', 'AKTIF', 'TKT20240401001', '2024-03-31 14:30:00'),
(3, 'PREMIUM', 50000.00, '2024-04-15', 'AKTIF', 'TKT20240415001', '2024-04-14 09:45:00');

-- Sample Pemeliharaan
INSERT INTO pemeliharaan (artefak_id, jenis_pemeliharaan, deskripsi_pemeliharaan, tanggal_mulai, tanggal_selesai, status, petugas_id, catatan) VALUES
(5, 'DARURAT', 'Pembersihan mandau dari karat dan restorasi ukiran yang pudar', '2024-01-10', '2024-01-25', 'SELESAI', 6, 'Pemeliharaan berhasil, ukiran kembali terlihat jelas'),
(1, 'RUTIN', 'Pembersihan rutin keris dan perawatan sarung', '2024-02-01', '2024-02-05', 'SELESAI', 6, 'Pembersihan rutin berjalan lancar'),
(8, 'RESTORASI', 'Restorasi wayang kulit yang robek dan pudar warnanya', '2024-02-15', '2024-03-15', 'SELESAI', 7, 'Restorasi berhasil, wayang kembali dalam kondisi prima'),
(3, 'RUTIN', 'Pembersihan dan penyimpanan ulos dalam kondisi optimal', '2024-03-01', '2024-03-03', 'SELESAI', 6, 'Ulos dibersihkan dan disimpan dengan pengatur kelembaban'),
(6, 'RUTIN', 'Perawatan tifa dan pengecekan kondisi kulit', '2024-03-10', '2024-03-12', 'SELESAI', 7, 'Kulit tifa dalam kondisi baik, dilakukan pembersihan ringan'),
(10, 'DARURAT', 'Perbaikan senar sasando yang putus', '2024-03-20', NULL, 'SEDANG_BERLANGSUNG', 6, 'Proses penggantian senar lontar sedang berlangsung'),
(2, 'RUTIN', 'Pembersihan batik dan pengecekan kondisi kain', '2024-04-01', NULL, 'DIJADWALKAN', 7, NULL),
(4, 'RUTIN', 'Perawatan topeng barong dan pembersihan debu', '2024-04-15', NULL, 'DIJADWALKAN', 6, NULL);