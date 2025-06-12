package test.integration;

import main.controller.PameranController;
import main.model.dto.PameranDto;
import main.repository.impl.ArtefakRepositoryImpl;
import main.repository.impl.PameranRepositoryImpl;
import main.service.impl.ArtefakServiceImpl;
import main.service.impl.PameranServiceImpl;
import main.utils.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pameran Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PameranIntegrationTest {

    private static PameranController pameranController;
    private static PameranDto testPameranDto;
    private static DatabaseUtil testDbUtil;

    // Test database configuration
    private static final String TEST_DB_URL = "jdbc:h2:mem:integrationdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    private static final String TEST_DB_USER = "sa";
    private static final String TEST_DB_PASSWORD = "";

    @BeforeAll
    static void setUpClass() throws SQLException {
        // Create H2 in-memory database schema
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD)) {
            String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS pameran (
                        pameran_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nama_pameran VARCHAR(255) NOT NULL,
                        deskripsi_pameran TEXT,
                        tanggal_mulai TIMESTAMP,
                        tanggal_selesai TIMESTAMP,
                        tanggal_dibuat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        is_active BOOLEAN DEFAULT TRUE,
                        artefak_ids TEXT DEFAULT ''
                    );

                    CREATE TABLE IF NOT EXISTS artefak (
                        artefak_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nama_artefak VARCHAR(255) NOT NULL,
                        deskripsi_artefak TEXT,
                        asal_daerah VARCHAR(255),
                        tahun_ditemukan INTEGER,
                        status_artefak VARCHAR(50) DEFAULT 'TERSEDIA'
                    );

                    CREATE TABLE IF NOT EXISTS artefak_pameran (
                        artefak_id BIGINT,
                        pameran_id BIGINT,
                        PRIMARY KEY (artefak_id, pameran_id)
                    );
                    """;

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
                System.out.println("✅ Integration test database schema created successfully");
            }
        }

        // Create test-specific DatabaseUtil
        testDbUtil = new DatabaseUtil() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
            }
        };

        // Initialize real services for integration testing
        PameranRepositoryImpl pameranRepository = new PameranRepositoryImpl();
        ArtefakRepositoryImpl artefakRepository = new ArtefakRepositoryImpl();

        // Inject test DatabaseUtil using reflection
        try {
            java.lang.reflect.Field pameranDbUtilField = PameranRepositoryImpl.class.getDeclaredField("dbUtil");
            pameranDbUtilField.setAccessible(true);
            pameranDbUtilField.set(pameranRepository, testDbUtil);

            java.lang.reflect.Field artefakDbUtilField = ArtefakRepositoryImpl.class.getDeclaredField("dbUtil");
            artefakDbUtilField.setAccessible(true);
            artefakDbUtilField.set(artefakRepository, testDbUtil);

            System.out.println("✅ Integration test DatabaseUtil injected successfully");
        } catch (Exception e) {
            fail("Failed to inject test DatabaseUtil: " + e.getMessage());
        }

        PameranServiceImpl pameranService = new PameranServiceImpl(pameranRepository);
        ArtefakServiceImpl artefakService = new ArtefakServiceImpl(artefakRepository);

        pameranController = new PameranController(pameranService, artefakService);

        // Setup test data
        testPameranDto = new PameranDto();
        testPameranDto.setNamaPameran("Integration Test Exhibition");
        testPameranDto.setDeskripsiPameran("Integration Test Description");
        testPameranDto.setTanggalMulai(LocalDateTime.now());
        testPameranDto.setTanggalSelesai(LocalDateTime.now().plusDays(30));
        testPameranDto.setArtefakIds(Arrays.asList(1L, 2L));
    }

    @Test
    @Order(1)
    @DisplayName("Should create pameran in integration test")
    void testCreatePameran_Integration() {
        // When
        PameranDto result = pameranController.createPameran(testPameranDto);

        // Then
        assertNotNull(result);
        assertNotNull(result.getPameranId());
        assertEquals(testPameranDto.getNamaPameran(), result.getNamaPameran());

        // Store the ID for subsequent tests
        testPameranDto.setPameranId(result.getPameranId());

        System.out.println("✅ Integration Test - Pameran created with ID: " + result.getPameranId());
    }

    @Test
    @Order(2)
    @DisplayName("Should retrieve pameran by ID in integration test")
    void testGetPameranById_Integration() {
        // When
        PameranDto result = pameranController.getPameranById(testPameranDto.getPameranId());

        // Then
        assertNotNull(result);
        assertEquals(testPameranDto.getNamaPameran(), result.getNamaPameran());
        assertEquals(testPameranDto.getDeskripsiPameran(), result.getDeskripsiPameran());

        System.out.println("✅ Integration Test - Pameran retrieved: " + result.getNamaPameran());
    }

    @Test
    @Order(3)
    @DisplayName("Should get all pamerans in integration test")
    void testGetAllPamerans_Integration() {
        // When
        List<PameranDto> result = pameranController.getAllPamerans();

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Find our test pameran
        boolean found = result.stream()
                .anyMatch(p -> p.getPameranId().equals(testPameranDto.getPameranId()));
        assertTrue(found, "Test pameran should be found in all pamerans list");

        System.out.println("✅ Integration Test - Found " + result.size() + " pamerans");
    }

    @Test
    @Order(4)
    @DisplayName("Should search pamerans in integration test")
    void testSearchPamerans_Integration() {
        // When
        List<PameranDto> result = pameranController.searchPamerans("Integration");

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Should find our test pameran
        boolean found = result.stream()
                .anyMatch(p -> p.getNamaPameran().contains("Integration"));
        assertTrue(found, "Should find pamerans containing 'Integration'");

        System.out.println("✅ Integration Test - Search found " + result.size() + " pamerans");
    }

    @Test
    @Order(5)
    @DisplayName("Should update pameran in integration test")
    void testUpdatePameran_Integration() {
        // Given
        testPameranDto.setNamaPameran("Updated Integration Test Exhibition");
        testPameranDto.setDeskripsiPameran("Updated Description");

        // When
        PameranDto result = pameranController.updatePameran(testPameranDto.getPameranId(), testPameranDto);

        // Then
        assertNotNull(result);
        assertEquals("Updated Integration Test Exhibition", result.getNamaPameran());
        assertEquals("Updated Description", result.getDeskripsiPameran());

        System.out.println("✅ Integration Test - Pameran updated successfully");
    }

    @Test
    @Order(6)
    @DisplayName("Should handle full pameran lifecycle in integration test")
    void testFullPameranLifecycle_Integration() {
        // Create
        PameranDto newPameran = new PameranDto();
        newPameran.setNamaPameran("Lifecycle Test Exhibition");
        newPameran.setDeskripsiPameran("Lifecycle Test");
        newPameran.setTanggalMulai(LocalDateTime.now());
        newPameran.setTanggalSelesai(LocalDateTime.now().plusDays(30));

        PameranDto created = pameranController.createPameran(newPameran);
        assertNotNull(created.getPameranId());

        // Read
        PameranDto retrieved = pameranController.getPameranById(created.getPameranId());
        assertEquals(created.getNamaPameran(), retrieved.getNamaPameran());

        // Update
        retrieved.setNamaPameran("Updated Lifecycle Test");
        PameranDto updated = pameranController.updatePameran(retrieved.getPameranId(), retrieved);
        assertEquals("Updated Lifecycle Test", updated.getNamaPameran());

        // Delete
        assertDoesNotThrow(() -> pameranController.deletePameran(created.getPameranId()));

        // Verify Deletion
        assertThrows(RuntimeException.class, () -> {
            pameranController.getPameranById(created.getPameranId());
        });

        System.out.println("✅ Integration Test - Full lifecycle completed successfully");
    }

    @Test
    @Order(7)
    @DisplayName("Should clean up test data")
    void testCleanup_Integration() {
        // Clean up our main test pameran
        if (testPameranDto.getPameranId() != null) {
            assertDoesNotThrow(() -> pameranController.deletePameran(testPameranDto.getPameranId()));
            System.out.println("✅ Integration Test - Test data cleaned up");
        }
    }
}
