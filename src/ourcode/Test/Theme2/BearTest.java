package ourcode.Test.Theme2;

import itumulator.world.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.Gender;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Bear;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BearTest {
    public ProgramRunner programRunner;
    public World world;
    public IDGenerator id_generator;

    public BearTest() { programRunner = new ProgramRunner(); }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Bears");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
    }

    /**
     * This test checks the declaration of a 'new Bear()'. Checks if a bear is being spawned into the world.
     */
    @Test
    public void testBearDeclaration() {
        world = new World(3);
        id_generator = new IDGenerator();

        Bear bear = new Bear(id_generator, false);

        bear.spawn(world);

        assertEquals(world.getEntities().size(), 1,
                "the amount of bears in the world should be 1 but is " + world.getEntities().size());
    }

    /**
     * This test checks the 'setTerritory' method and the 'getTerritory' method from the Bear class.
     * This will check if the correct location is being declared as the bear's 'territory'.
     */
    @Test
    public void testBearTerritoryMethod() {
        world = new World(3);
        id_generator = new IDGenerator();

        Bear bear = new Bear(id_generator, false);
        bear.spawn(world);

        bear.setTerritoryLocation(world.getLocation(bear));

        Assertions.assertSame(bear.getTerritory(), world.getLocation(bear));
    }

    /**
     * This test checks if bears, being spawned through an input file, can use the 'findMate' method correctly.
     * @throws Exception
     */
    @Test
    public void testBearFindMate() throws Exception {
        programRunner.create("./data/bear-test.txt");
        world = programRunner.getWorld();

        programRunner.getP().setDelay(1000);
        programRunner.runSimulation(20);

        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Bear) {
                if (((Bear) o).getGender() == Gender.Male) {
                    assertTrue(((Bear) o).findMate(world));
                }
            }
        }


    }
}
