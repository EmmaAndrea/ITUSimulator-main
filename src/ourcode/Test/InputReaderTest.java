/*
import org.junit.Test;

import java.io.*;

public class InputReaderTest {

    // Test for reading world size
    @Test
    public void testReadWorldSize() throws IOException {
        // Setup: Choose file
        File file = new File("./data/t1-1a.txt");

        // Execute: Create InputReader and read world size
        InputReader reader = new InputReader(file.getAbsolutePath());
        int worldSize = reader.readWorldSize();

        // Verify: Check if the world size is read correctly
        //Assertions.assertEquals(5, worldSize);
    }

    // Test for reading spawns with fixed amount
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

    // Test for reading spawns with range
    @Test
    public void testReadSpawnsRange() throws IOException {
        // Setup: Choose file
        File file = new File("./data/t1-1b.txt");

        // Execute: Create InputReader and read world size
        InputReader reader = new InputReader(file.getAbsolutePath());
        reader.readSpawns();

        // Verify: Check if the amount is within the specified range
        int amount = reader.getAmount("grass");
        //Assertions.assertTrue(amount >= 3 && amount <= 8);
    }

    // Test for reading spawns with range
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

 */