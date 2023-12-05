package ourcode.Test.Theme2;

import itumulator.world.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.PlantChildren.Bush;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BushTest {
    public ProgramRunner programRunner;
    public World world;

    public BushTest() {
        programRunner = new ProgramRunner();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Starting test for Cave");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
        world = new World(3);
        IDGenerator id_generator = new IDGenerator();
        Bush bush1 = new Bush(id_generator);
        bush1.spawn(world);
    }

    @Test
    public void testSpawn() {
        assertEquals(world.getEntities().size(), 1,
                "the amount of bush in the world should be 1, but it is: " + world.getEntities().size());
    }

    @Test
    public void testGrowBerries() {
        Bush bush = null;
        for (Object object : world.getEntities().keySet()) {
            bush = (Bush) object;
            bush.grow();
        }
        Assertions.assertTrue(bush.getBerries() > 0, "the bush should have more berries after growing");
    }

    @Test
    public void testEatBerries() {
        Bush bush = null;
        for (Object object : world.getEntities().keySet()) {
            bush = (Bush) object;
            bush.grow();
        }
        bush.eatBerries();
        Assertions.assertTrue(bush.getBerries() < 3, "the bush should have less berries after eating");
    }

    @Test
    public void testBushAct() {
        Bush bush = null;
        for (Object object : world.getEntities().keySet()) {
            bush = (Bush) object;
            bush.act(world);
        }
        Assertions.assertTrue(bush.getBerries() < 3, "bushAct should grow the amount of berries");
    }
}
