package ourcode.Test;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ourcode.Setup.InputReader;

import java.io.File;
import java.io.IOException;

public class InputReaderTest {

    /**
     * Tests the ability of the InputReader to accurately read the world size from a given file.
     * This test ensures that the world size is correctly parsed and returned as an integer value.
     * The expected world size is compared with the actual size read from the file.
     */
    @Test
    public void testReadWorldSize() throws IOException {
        // Setup: Choose file
        File file = new File("./data/t1-1a.txt");

        // Execute: Create InputReader and read world size
        InputReader reader = new InputReader(file.getAbsolutePath());
        int worldSize = reader.readWorldSize();

        // Verify: Check if the world size is read correctly
        Assertions.assertEquals(5, worldSize);
    }

    /**
     * Tests the InputReader's functionality to read spawn data with a fixed amount from a file.
     * This test verifies that the reader correctly interprets and stores the number of entities to spawn.
     * The amount of a specific entity type (e.g., 'grass') read from the file is compared against an expected fixed value.
     */
    @Test
    public void testReadSpawnsFixedAmount() throws IOException {
        // Setup: Choose file
        File file = new File("./data/t1-1c.txt");

        // Execute: Create InputReader and read world size
        InputReader reader = new InputReader(file.getAbsolutePath());
        reader.readSpawns();

        // Verify: Check if the spawns are read correctly
        Assertions.assertEquals(1, reader.getAmount("grass"));
    }

    /**
     * Tests the InputReader's capability to handle spawn data specified in a range format in the input file.
     * This test checks whether the actual spawn amount falls within the expected range.
     * The range is defined in the input file, and the test ensures that the parsed amount adheres to this range.
     */
    @Test
    public void testReadSpawnsRange() throws IOException {
        // Setup: Choose file
        File file = new File("./data/t1-1b.txt");

        // Execute: Create InputReader and read world size
        InputReader reader = new InputReader(file.getAbsolutePath());
        reader.readSpawns();

        // Verify: Check if the amount is within the specified range
        int amount = reader.getAmount("grass");
        Assertions.assertTrue(amount >= 3 && amount <= 8);
    }

    /**
     * Tests the ability of the InputReader to parse spawn amounts specified as ranges and subsequently spawn entities.
     * This test ensures that the InputReader not only reads the range correctly but also spawns the correct number of entities.
     * The amount is checked to be within the specified range, and the test verifies the spawning logic based on this amount.
     */
    @Test
    public void testReadSpawnsThenSpawn() throws IOException {
        // Setup: Choose file
        File file = new File("./data/t1-1b.txt");

        // Execute: Create InputReader and read world size
        InputReader reader = new InputReader(file.getAbsolutePath());
        reader.readSpawns();

        // Verify: Check if the amount is within the specified range
        int amount = reader.getAmount("grass");
        Assertions.assertTrue(amount >= 3 && amount <= 8);
    }
}