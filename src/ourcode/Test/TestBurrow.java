package ourcode.Test;

import itumulator.world.World;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestBurrow {
    public ProgramRunner programRunner;
    public World world;
    /**
     * This test will provide an instance of a burrow based off of the requirements in 'K1-3a' from 'Tema 1'
     */
    public void testContructor() throws Exception {
        programRunner.create("./data/t1-3a.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(1);
        assertEquals(world.getEntities().size(), 1);
        assertTrue(world.getEntities().containsKey(programRunner.getObject()));
    }
}