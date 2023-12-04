package ourcode.Test.Theme2;


import itumulator.world.World;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ourcode.Obstacles.Cave;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.*;

public class CaveTest {
    public ProgramRunner programRunner;
    public World world;

    public CaveTest() {
        programRunner = new ProgramRunner();
    }

    /**
     *
     */
    @BeforeAll
    public void setUp() {
        System.out.println("Starting test for Cave");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
    }

    @Test
    public void testCaveDeclaration() {
        world = new World(3);
        IDGenerator id_generator = new IDGenerator();

        Cave cave1 = new Cave(id_generator);
        Cave cave2 = new Cave(id_generator);

        assertEquals(world.getEntities().size(), 2,
                "the amount of caves in the world should be 3, but it is: " + world.getEntities().size());
    }
}
