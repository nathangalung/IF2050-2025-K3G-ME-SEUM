package test.repository;

import main.model.entities.Pameran;
import main.repository.impl.PameranRepositoryImpl;
import main.utils.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pameran Repository Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PameranRepositoryTest {

    private static PameranRepositoryImpl pameranRepository;
    private static DatabaseUtil testDbUtil;
    private Pameran testPameran;

    // Test database configuration
    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
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

                    CREATE TABLE IF NOT EXISTS artefak_pameran (
                        artefak_id BIGINT,
                        pameran_id BIGINT,
                        PRIMARY KEY (artefak_id, pameran_id)
                    );
                    """;

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
                System.out.println("✅ Test database schema created successfully");
            }
        }

        // Create test-specific DatabaseUtil
        testDbUtil = new DatabaseUtil() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASSWORD);
            }
        };

        // Create repository instance
        pameranRepository = new PameranRepositoryImpl();

        // Inject test DatabaseUtil using reflection
        try {
            java.lang.reflect.Field dbUtilField = PameranRepositoryImpl.class.getDeclaredField("dbUtil");
            dbUtilField.setAccessible(true);
            dbUtilField.set(pameranRepository, testDbUtil);
            System.out.println("✅ Test DatabaseUtil injected successfully");
        } catch (Exception e) {
            fail("Failed to inject test DatabaseUtil: " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        // Setup fresh test data for each test
        testPameran = new Pameran();
        testPameran.setNamaPameran("Test Exhibition " + System.currentTimeMillis()); // Make unique
        testPameran.setDeskripsiPameran("Test Description for integration testing");
        testPameran.setTanggalMulai(LocalDateTime.now());
        testPameran.setTanggalSelesai(LocalDateTime.now().plusDays(30));
        testPameran.setTanggalDibuat(LocalDateTime.now());
        testPameran.setIsActive(true);
        testPameran.setArtefakIds("1,2,3");

        // Clean up database before each test (except for ordered tests)
        cleanupDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Cleanup after each test to ensure isolation
        cleanupDatabase();
    }

    private void cleanupDatabase() throws SQLException {
        try (Connection conn = testDbUtil.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM artefak_pameran");
            stmt.execute("DELETE FROM pameran");
            stmt.execute("ALTER TABLE pameran ALTER COLUMN pameran_id RESTART WITH 1");
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should save pameran successfully")
    void testSave_Success() {
        // When
        Pameran result = pameranRepository.save(testPameran);

        // Then
        assertNotNull(result, "Saved pameran should not be null");
        assertNotNull(result.getPameranId(), "Pameran ID should be generated");
        assertTrue(result.getPameranId() > 0, "Pameran ID should be positive");
        assertEquals(testPameran.getNamaPameran(), result.getNamaPameran());
        assertEquals(testPameran.getDeskripsiPameran(), result.getDeskripsiPameran());
        assertEquals(testPameran.getIsActive(), result.getIsActive());
        assertNotNull(result.getArtefakIds(), "ArtefakIds should not be null");

        System.out.println("✅ Test Save Success - Pameran ID: " + result.getPameranId());
    }

    @Test
    @Order(2)
    @DisplayName("Should find pameran by ID successfully")
    void testFindById_Success() {
        // Given - save a pameran first
        Pameran savedPameran = pameranRepository.save(testPameran);
        assertNotNull(savedPameran.getPameranId(), "Saved pameran should have ID");

        // When
        Pameran result = pameranRepository.findById(savedPameran.getPameranId());

        // Then
        assertNotNull(result, "Found pameran should not be null");
        assertEquals(savedPameran.getPameranId(), result.getPameranId());
        assertEquals(testPameran.getNamaPameran(), result.getNamaPameran());
        assertEquals(testPameran.getDeskripsiPameran(), result.getDeskripsiPameran());

        System.out.println("✅ Test Find By ID Success - Found: " + result.getNamaPameran());
    }

    @Test
    @Order(3)
    @DisplayName("Should return null when pameran not found")
    void testFindById_NotFound() {
        // When - search for non-existent pameran
        Pameran result = pameranRepository.findById(999L);

        // Then
        assertNull(result, "Should return null for non-existent pameran");

        System.out.println("✅ Test Find By ID Not Found - Correctly returned null");
    }

    @Test
    @Order(4)
    @DisplayName("Should find all pamerans successfully")
    void testFindAll_Success() {
        // Given - save multiple pamerans
        Pameran pameran1 = pameranRepository.save(testPameran);

        Pameran pameran2 = new Pameran();
        pameran2.setNamaPameran("Second Exhibition " + System.currentTimeMillis());
        pameran2.setDeskripsiPameran("Second Description");
        pameran2.setTanggalMulai(LocalDateTime.now());
        pameran2.setTanggalSelesai(LocalDateTime.now().plusDays(30));
        pameran2.setTanggalDibuat(LocalDateTime.now());
        pameran2.setIsActive(true);
        pameran2.setArtefakIds("");
        Pameran savedPameran2 = pameranRepository.save(pameran2);

        // When
        List<Pameran> result = pameranRepository.findAll();

        // Then
        assertNotNull(result, "Result list should not be null");
        assertEquals(2, result.size(), "Should find exactly 2 pamerans");

        System.out.println("✅ Test Find All Success - Found " + result.size() + " pamerans");
    }

    @Test
    @Order(5)
    @DisplayName("Should find all pamerans ordered by ID successfully")
    void testFindAllOrderById_Success() {
        // Given - save multiple pamerans
        Pameran pameran1 = pameranRepository.save(testPameran);

        Pameran pameran2 = new Pameran();
        pameran2.setNamaPameran("Second Exhibition " + System.currentTimeMillis());
        pameran2.setDeskripsiPameran("Second Description");
        pameran2.setIsActive(true);
        pameran2.setArtefakIds("");
        Pameran savedPameran2 = pameranRepository.save(pameran2);

        // When
        List<Pameran> result = pameranRepository.findAllOrderById();

        // Then
        assertNotNull(result, "Result list should not be null");
        assertEquals(2, result.size(), "Should find exactly 2 pamerans");

        // Verify ordering (first pameran should have smaller ID)
        assertTrue(result.get(0).getPameranId() <= result.get(1).getPameranId(),
                "Results should be ordered by ID");

        System.out.println("✅ Test Find All Order By ID Success - Found " + result.size() + " pamerans");
    }

    @Test
    @Order(6)
    @DisplayName("Should update pameran successfully")
    void testUpdate_Success() {
        // Given - save a pameran first
        Pameran savedPameran = pameranRepository.save(testPameran);
        assertNotNull(savedPameran.getPameranId(), "Saved pameran should have ID");

        // Define the updated values BEFORE calling update
        String updatedName = "Updated Exhibition " + System.currentTimeMillis();
        String updatedDescription = "Updated Description";

        // When - update it
        savedPameran.setNamaPameran(updatedName);
        savedPameran.setDeskripsiPameran(updatedDescription);

        assertDoesNotThrow(() -> pameranRepository.update(savedPameran),
                "Update should not throw exception");

        // Then - verify the update using the SAME values we set
        Pameran updatedPameran = pameranRepository.findById(savedPameran.getPameranId());
        assertNotNull(updatedPameran, "Updated pameran should be found");
        assertEquals(updatedName, updatedPameran.getNamaPameran(), "Name should be updated");
        assertEquals(updatedDescription, updatedPameran.getDeskripsiPameran(), "Description should be updated");

        System.out.println("✅ Test Update Success - Updated to: " + updatedPameran.getNamaPameran());
    }

    @Test
    @Order(7)
    @DisplayName("Should delete pameran by ID successfully")
    void testDeleteById_Success() {
        // Given - save a pameran first
        Pameran savedPameran = pameranRepository.save(testPameran);
        Long pameranId = savedPameran.getPameranId();
        assertNotNull(pameranId, "Saved pameran should have ID");

        // Verify it exists first
        Pameran foundPameran = pameranRepository.findById(pameranId);
        assertNotNull(foundPameran, "Pameran should exist before deletion");
        assertTrue(pameranRepository.existsById(pameranId), "Pameran should exist in database");

        // When - delete the pameran
        assertDoesNotThrow(() -> pameranRepository.deleteById(pameranId),
                "Delete operation should not throw exception");

        // Then - verify it's deleted
        Pameran deletedPameran = pameranRepository.findById(pameranId);
        assertNull(deletedPameran, "Pameran should be null after deletion");
        assertFalse(pameranRepository.existsById(pameranId), "Pameran should not exist after deletion");

        System.out.println("✅ Test Delete Success - Pameran ID " + pameranId + " deleted");
    }

    @Test
    @Order(8)
    @DisplayName("Should check if pameran exists by ID")
    void testExistsById_Success() {
        // Given - save a pameran first
        Pameran savedPameran = pameranRepository.save(testPameran);
        Long pameranId = savedPameran.getPameranId();
        assertNotNull(pameranId, "Saved pameran should have ID");

        // When & Then
        assertTrue(pameranRepository.existsById(pameranId), "Should find existing pameran");
        assertFalse(pameranRepository.existsById(999L), "Should not find non-existent pameran");

        System.out.println("✅ Test Exists By ID Success");
    }

    @Test
    @Order(9)
    @DisplayName("Should count total pamerans successfully")
    void testCount_Success() {
        // Given - save some pamerans
        pameranRepository.save(testPameran);

        Pameran secondPameran = new Pameran();
        secondPameran.setNamaPameran("Second Exhibition " + System.currentTimeMillis());
        secondPameran.setIsActive(true);
        secondPameran.setArtefakIds("");
        pameranRepository.save(secondPameran);

        // When
        long count = pameranRepository.count();

        // Then
        assertEquals(2L, count, "Should count exactly 2 pamerans");

        System.out.println("✅ Test Count Success - Count: " + count);
    }

    @Test
    @Order(10)
    @DisplayName("Should search pamerans by name successfully")
    void testFindByNamaPameranContaining_Success() {
        // Given
        String uniqueName = "SearchableExhibition" + System.currentTimeMillis();
        testPameran.setNamaPameran(uniqueName);
        pameranRepository.save(testPameran);

        // When
        List<Pameran> result = pameranRepository.findByNamaPameranContaining("Searchable");

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find exactly 1 pameran");
        assertEquals(uniqueName, result.get(0).getNamaPameran());

        System.out.println("✅ Test Search By Name Success - Found: " + result.size());
    }

    @Test
    @Order(11)
    @DisplayName("Should find active pamerans successfully")
    void testFindByIsActive_Success() {
        // Given
        pameranRepository.save(testPameran);

        // When
        List<Pameran> result = pameranRepository.findByIsActive(true);

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should find exactly 1 active pameran");
        assertTrue(result.get(0).getIsActive(), "Found pameran should be active");

        System.out.println("✅ Test Find By Active Success - Found: " + result.size());
    }

    @Test
    @Order(12)
    @DisplayName("Should check if artifact is in pameran")
    void testIsArtefakInPameran_Success() {
        // Given - save a pameran first
        Pameran savedPameran = pameranRepository.save(testPameran);
        Long pameranId = savedPameran.getPameranId();

        // When & Then - check relationship (will be false initially)
        boolean result = pameranRepository.isArtefakInPameran(pameranId, 1L);
        assertFalse(result, "Artifact should not be in pameran initially");

        System.out.println("✅ Test Is Artifact In Pameran Success");
    }

    @Test
    @Order(13)
    @DisplayName("Should add artifact to pameran successfully")
    void testAddArtefakToPameran_Success() {
        // Given - save a pameran first
        Pameran savedPameran = pameranRepository.save(testPameran);
        Long pameranId = savedPameran.getPameranId();

        // When & Then
        assertDoesNotThrow(() -> pameranRepository.addArtefakToPameran(pameranId, 1L),
                "Adding artifact should not throw exception");

        // Verify relationship was created
        boolean exists = pameranRepository.isArtefakInPameran(pameranId, 1L);
        assertTrue(exists, "Artifact should be in pameran after adding");

        System.out.println("✅ Test Add Artifact To Pameran Success");
    }

    @Test
    @Order(14)
    @DisplayName("Should remove artifact from pameran successfully")
    void testRemoveArtefakFromPameran_Success() {
        // Given - save a pameran and add artifact first
        Pameran savedPameran = pameranRepository.save(testPameran);
        Long pameranId = savedPameran.getPameranId();
        pameranRepository.addArtefakToPameran(pameranId, 1L);

        // Verify it was added
        assertTrue(pameranRepository.isArtefakInPameran(pameranId, 1L),
                "Artifact should be in pameran before removal");

        // When
        assertDoesNotThrow(() -> pameranRepository.removeArtefakFromPameran(pameranId, 1L),
                "Removing artifact should not throw exception");

        // Then - verify it was removed
        boolean exists = pameranRepository.isArtefakInPameran(pameranId, 1L);
        assertFalse(exists, "Artifact should not be in pameran after removal");

        System.out.println("✅ Test Remove Artifact From Pameran Success");
    }

    @Test
    @Order(15)
    @DisplayName("Should handle database connection properly")
    void testFindById_ConnectionHandling() {
        // Given - save a pameran first
        Pameran savedPameran = pameranRepository.save(testPameran);
        Long pameranId = savedPameran.getPameranId();
        assertNotNull(pameranId, "Saved pameran should have ID");

        // When & Then - multiple calls should work without connection issues
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 3; i++) {
                Pameran found = pameranRepository.findById(pameranId);
                assertNotNull(found, "Should find pameran on call " + (i + 1));
                assertEquals(pameranId, found.getPameranId(), "Should find correct pameran");
            }
        }, "Multiple findById calls should not cause connection issues");

        System.out.println("✅ Test Connection Handling - Multiple calls successful");
    }

    @Test
    @Order(16)
    @DisplayName("Should handle SQL exceptions gracefully")
    void testSave_WithNullValues() {
        // Given - pameran with some null values
        Pameran pameranWithNulls = new Pameran();
        pameranWithNulls.setNamaPameran("Test Null Values");
        pameranWithNulls.setDeskripsiPameran(null); // This should be handled gracefully
        pameranWithNulls.setTanggalMulai(null);
        pameranWithNulls.setTanggalSelesai(null);
        pameranWithNulls.setIsActive(null);
        pameranWithNulls.setArtefakIds(null);

        // When & Then
        assertDoesNotThrow(() -> {
            Pameran result = pameranRepository.save(pameranWithNulls);
            assertNotNull(result, "Should save pameran even with null values");
            assertNotNull(result.getPameranId(), "Should generate ID");
            assertEquals("Test Null Values", result.getNamaPameran());
        }, "Should handle null values gracefully");

        System.out.println("✅ Test Handle Null Values Success");
    }

    @Test
    @Order(17)
    @DisplayName("Should handle edge cases properly")
    void testEdgeCases() {
        // Test with empty strings and edge values
        Pameran edgeCasePameran = new Pameran();
        edgeCasePameran.setNamaPameran("Edge Case Test " + System.currentTimeMillis());
        edgeCasePameran.setDeskripsiPameran("");
        edgeCasePameran.setIsActive(true);
        edgeCasePameran.setArtefakIds("");

        // Should save successfully
        Pameran saved = pameranRepository.save(edgeCasePameran);
        assertNotNull(saved);
        assertNotNull(saved.getPameranId());

        // Should find by empty name search
        List<Pameran> results = pameranRepository.findByNamaPameranContaining("");
        assertNotNull(results);
        assertTrue(results.size() >= 1);

        System.out.println("✅ Test Edge Cases Success");
    }
}
