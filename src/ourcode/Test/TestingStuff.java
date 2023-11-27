import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
/*
class TestingStuff {
    // Test for reading spawns with range
    public void testReadSpawnsRange(String[] args) throws IOException {
        // Setup: Choose file
        File file = new File("./data/t1-1b.txt");

        // Execute: Create InputReader and read world size
        InputReader reader = new InputReader(file.getAbsolutePath());
        reader.readSpawns();

        int delay = 1000;
        int display_size = 800;
        Program p = new Program(reader.readWorldSize(), display_size, delay);
        World world = p.getWorld();
        reader.spawnFromMap((HashMap<String, Integer>) reader.getMapOfSpawns(), world);
        DisplayInformation di = new DisplayInformation(Color.red);
        p.setDisplayInformation(Organism.class, di);

        p.show();
    }
}

 */
