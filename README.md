# IF2050-2025-K3G-ME-SEUM
ME-SEUM - Museum Nusantara Management System

Pastikan memiliki software berikut terinstall:
- Docker Desktop (untuk Windows/Mac) atau Docker Engine (untuk Linux)
- Java 17 atau lebih tinggi
- Maven 3.6+
- Git

1. Clone Repository
```bash
git clone <repository-url>
cd IF2050_TB_K3G_ME-SEUM
```

2. Setup Database dengan Docker
```bash
# Start PostgreSQL database container
docker compose up -d

# Copy schema file to container
docker cp docker/init/02-schema.sql meseum_postgres:/tmp/schema.sql

# Copy data file to container  
docker cp docker/init/03-sample-data.sql meseum_postgres:/tmp/data.sql

# Execute schema
docker exec -it meseum_postgres psql -U meseum_user -d me_seum -f /tmp/schema.sql

# Execute sample data
docker exec -it meseum_postgres psql -U meseum_user -d me_seum -f /tmp/data.sql
```

3. Build dan Run Aplikasi
```bash
# Clean dan compile project
mvn clean compile

# Run aplikasi dengan JavaFX
mvn javafx:run
```

## Daftar Modul yang Diimplementasi

### 1. Modul Manajemen Artefak (Frontend + Backend)
**Pembagian Tugas**: Bryan P. Hutagalung
- **Model Layer**: [`Artefak.java`](src/main/model/entities/Artefak.java), [`StatusArtefak.java`](src/main/model/enums/StatusArtefak.java), [`ArtefakDto.java`](src/main/model/dto/ArtefakDto.java)
- **Controller**: [`ManajemenArtefakController.java`](src/main/controller/ManajemenArtefakController.java)
- **Service**: [`IArtefakService.java`](src/main/service/interfaces/IArtefakService.java), [`ArtefakServiceImpl.java`](src/main/service/impl/ArtefakServiceImpl.java)
- **Repository**: [`IArtefakRepository.java`](src/main/repository/interfaces/IArtefakRepository.java), Repository Implementation
- **UI Components**: [`ArtefakForm.java`](src/main/ui/components/forms/ArtefakForm.java)
- **UI Views**: [`ArtefakListPage.java`](src/main/ui/views/curator/ArtefakListPage.java), [`CollectionPage.java`](src/main/ui/views/customer/CollectionPage.java)
- **UI Interface**: [`ManajemenArtefakInterface.java`](src/main/ui/interfaces/ManajemenArtefakInterface.java)

**Fitur yang diimplementasi:**
- CRUD Artefak (Create, Read, Update, Delete)
- Pencarian data artefak dengan pagination
- Mengubah status artefak
- Tampilan koleksi untuk customer dengan filter
- Validasi data artefak

### 2. Modul Manajemen Pameran (Frontend + Backend)  
**Pembagian Tugas**: Ardra Rafif Sahasika
- **Model Layer**: [`Pameran.java`](src/main/model/entities/Pameran.java), [`PameranDto.java`](src/main/model/dto/PameranDto.java)
- **Controller**: [`PameranController.java`](src/main/controller/PameranController.java)
- **Service**: [`IPameranService.java`](src/main/service/interfaces/IPameranService.java), Service Implementation
- **Repository**: [`IPameranRepository.java`](src/main/repository/interfaces/IPameranRepository.java), Repository Implementation
- **UI Components**: [`PameranForm.java`](src/main/ui/components/forms/PameranForm.java)
- **UI Views**: [`EventArtefakPage.java`](src/main/ui/views/curator/EventArtefakPage.java), [`EventPage.java`](src/main/ui/views/customer/EventPage.java)

**Fitur yang diimplementasi:**
- CRUD Pameran/Exhibition
- Mengelola artefak dalam pameran (tambah/hapus)
- Mengakses informasi detail pameran
- Tampilan event untuk customer dengan pembelian tiket
- Manajemen jadwal pameran

### 3. Modul Sistem Pemeliharaan (Frontend + Backend)
**Pembagian Tugas**: Fudhail Fayyadh
- **Model Layer**: [`Pemeliharaan.java`](src/main/model/entities/Pemeliharaan.java), [`StatusPemeliharaan.java`](src/main/model/enums/StatusPemeliharaan.java)
- **Controller**: [`PemeliharaanController.java`](src/main/controller/PemeliharaanController.java)
- **Service**: [`IPemeliharaanService.java`](src/main/service/interfaces/IPemeliharaanService.java), Service Implementation
- **Repository**: [`IPemeliharaanRepository.java`](src/main/repository/interfaces/IPemeliharaanRepository.java), Repository Implementation
- **UI Views**: [`MaintenanceListPage.java`](src/main/ui/views/curator/MaintenanceListPage.java), [`MaintenanceTaskPage.java`](src/main/ui/views/cleaner/MaintenanceTaskPage.java)
- **UI Interface**: [`PemeliharaanInterface.java`](src/main/ui/interfaces/PemeliharaanInterface.java)

**Fitur yang diimplementasi:**
- Mengajukan permintaan pemeliharaan artefak
- Mencatat aktivitas pemeliharaan
- Tracking status pemeliharaan dengan auto-refresh
- Dashboard untuk staff pemeliharaan (cleaner)
- Penjadwalan dan monitoring deadline

### 4. Modul User Management & Authentication + Infrastructure
**Pembagian Tugas**: Timothy Haposan Simanjuntak
- **Model Layer**: [`User.java`](src/main/model/entities/User.java), [`UserRole.java`](src/main/model/enums/UserRole.java)
- **Controller**: [`AuthController.java`](src/main/controller/AuthController.java)
- **Service**: [`IUserService.java`](src/main/service/interfaces/IUserService.java), Service Implementation
- **Repository**: [`IUserRepository.java`](src/main/repository/interfaces/IUserRepository.java), Repository Implementation
- **Database**: [`schema.sql`](docker/init/02-schema.sql), [`sample-data.sql`](docker/init/03-sample-data.sql)
- **UI Views**: [`LoginPage.java`](src/main/ui/views/auth/LoginPage.java), [`RegisterPage.java`](src/main/ui/views/auth/RegisterPage.java)
- **Utils**: [`DatabaseUtil.java`](src/main/utils/DatabaseUtil.java), [`ValidationUtil.java`](src/main/utils/ValidationUtil.java)

**Fitur yang diimplementasi:**
- Sistem autentikasi (login/logout)
- Manajemen user dan role (CURATOR, CLEANER, CUSTOMER)
- Database setup dan konfigurasi PostgreSQL/MySQL
- Validasi data dan keamanan
- Session management

### 5. Modul UI/UX Common Components + Customer Interface
**Pembagian Tugas**: Yusril Fazri Mahendra
- **Common UI Components**: 
  - [`NavigationBar.java`](src/main/ui/components/common/NavigationBar.java)
  - [`SideBar.java`](src/main/ui/components/common/SideBar.java)  
  - [`Footer.java`](src/main/ui/components/common/Footer.java)
- **Customer Views**: 
  - [`HomePage.java`](src/main/ui/views/customer/HomePage.java)
  - [`AboutUsPage.java`](src/main/ui/views/customer/AboutUsPage.java)
  - [`YourOrdersPage.java`](src/main/ui/views/customer/YourOrdersPage.java)
- **Dashboard**: [`CuratorDashboard.java`](src/main/ui/views/curator/CuratorDashboard.java)
- **Utils**: [`DateUtil.java`](src/main/utils/DateUtil.java), [`FileUtil.java`](src/main/utils/FileUtil.java)

**Fitur yang diimplementasi:**
- Komponen UI yang reusable dan responsive
- Customer interface dan sistem navigasi
- Dashboard layout untuk curator
- Asset management dan styling
- Order management untuk customer

## Daftar Tabel Basis Data

### 1. Table: `users`
**Deskripsi**: Menyimpan informasi pengguna sistem (Customer, Curator, Cleaner)
```sql
- user_id (SERIAL PRIMARY KEY)
- username (VARCHAR(50) UNIQUE NOT NULL)
- password (VARCHAR(255) NOT NULL) 
- email (VARCHAR(100) UNIQUE NOT NULL)
- role (VARCHAR(20) NOT NULL) -- 'CUSTOMER', 'CURATOR', 'CLEANER'
- full_name (VARCHAR(100) NOT NULL)
- created_at (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
- updated_at (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
```

### 2. Table: `artefaks` 
**Deskripsi**: Menyimpan koleksi artefak museum
```sql
- artefak_id (SERIAL PRIMARY KEY)
- nama_artefak (VARCHAR(200) NOT NULL)
- deskripsi_artefak (TEXT)
- asal_daerah (VARCHAR(100))
- periode (VARCHAR(100))
- status (VARCHAR(50)) -- 'TERSEDIA', 'DIPAMERKAN', 'DIPELIHARA', 'RUSAK'
- gambar (VARCHAR(500))
- curator_id (INTEGER REFERENCES users(user_id))
- tanggal_registrasi (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
```

### 3. Table: `pamerans`
**Deskripsi**: Menyimpan informasi pameran/exhibition museum  
```sql
- pameran_id (SERIAL PRIMARY KEY)
- nama_pameran (VARCHAR(200) NOT NULL)
- deskripsi_pameran (TEXT)
- tanggal_mulai (DATE NOT NULL)
- tanggal_selesai (DATE NOT NULL)
- lokasi_pameran (VARCHAR(100))
- harga_tiket (DECIMAL(10,2))
- curator_id (INTEGER REFERENCES users(user_id))
- status_pameran (VARCHAR(20)) -- 'PLANNED', 'ACTIVE', 'ENDED'
- artefak_ids (TEXT) -- JSON array of artifact IDs
```

### 4. Table: `pemeliharaans`
**Deskripsi**: Menyimpan jadwal dan riwayat pemeliharaan artefak
```sql
- pemeliharaan_id (SERIAL PRIMARY KEY)
- artefak_id (INTEGER REFERENCES artefaks(artefak_id))
- petugas_id (INTEGER REFERENCES users(user_id))
- jenis_pemeliharaan (VARCHAR(50)) -- 'CLEANING', 'REPAIR', 'INSPECTION'
- deskripsi_pemeliharaan (TEXT)
- tanggal_mulai (TIMESTAMP NOT NULL)
- tanggal_selesai (TIMESTAMP)
- status (VARCHAR(20)) -- 'DIJADWALKAN', 'BERLANGSUNG', 'SELESAI', 'DIBATALKAN'
```

### 5. Table: `tickets`
**Deskripsi**: Menyimpan informasi pembelian tiket pameran
```sql
- ticket_id (SERIAL PRIMARY KEY)
- pameran_id (INTEGER REFERENCES pamerans(pameran_id))
- user_id (INTEGER REFERENCES users(user_id))
- purchase_date (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
- visit_date (DATE NOT NULL)
- quantity (INTEGER NOT NULL)
- total_price (DECIMAL(10,2) NOT NULL)
- status (VARCHAR(20)) -- 'ACTIVE', 'USED', 'CANCELLED'
```

### 6. Table: `feedback`
**Deskripsi**: Menyimpan feedback dari pengunjung museum
```sql
- feedback_id (SERIAL PRIMARY KEY)
- user_id (INTEGER REFERENCES users(user_id))
- pameran_id (INTEGER REFERENCES pamerans(pameran_id))
- rating (INTEGER CHECK (rating >= 1 AND rating <= 5))
- comment (TEXT)
- response (TEXT)
- curator_id (INTEGER REFERENCES users(user_id))
- created_at (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
- responded_at (TIMESTAMP)
```

## Database Constraints & Indexes

- **Foreign Key Constraints**: Semua relasi antar tabel menggunakan foreign key constraints untuk menjaga integritas data
- **Check Constraints**: 
  - Rating feedback (1-5)
  - Status values untuk setiap enum
  - Validasi tanggal (tanggal_selesai >= tanggal_mulai)
- **Unique Constraints**: 
  - Username dan email pada table users
  - Nama artefak untuk mencegah duplikasi
- **Indexes**: 
  - Index pada `user_id` untuk performa query role-based
  - Index pada `artefak_id` dan `pameran_id` untuk join operations
  - Index pada `tanggal_registrasi` dan timestamps untuk sorting
  - Composite index pada (`status`, `tanggal_mulai`) untuk maintenance queries

## Teknologi yang Digunakan

- **Backend**: Java 17, Maven
- **Frontend**: JavaFX untuk desktop application
- **Database**: PostgreSQL (primary), MySQL (fallback)
- **Containerization**: Docker & Docker Compose
- **Architecture Pattern**: MVC (Model-View-Controller)
