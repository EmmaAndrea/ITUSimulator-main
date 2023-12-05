package ourcode.Test.Theme2;

import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Obstacles.Cave;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Wolf;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.*;

public class WolfTest {
    public ProgramRunner programRunner;
    public World world;

    public WolfTest() { programRunner = new ProgramRunner(); }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Wolves");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
    }

    /**
     * this test will check if a wolf can be declared through the input of a file. Also checks if the
     * constructor of a Wolf functions
     */
    @Test
    public void testWolfDeclaration() throws Exception {
        programRunner.create("./data/t2-1ab.txt");

        programRunner.runSimulation(1);

        world = programRunner.getWorld();
        assertEquals(world.getEntities().size(), 1,
                "the amount of entities in the world should be 1 but is " + world.getEntities().size());
    }

    /**
     * This test will check the list 'pack' in the Wolf class along with some of the methods for retrieving
     * and adding a wolf to the list
     */
    @Test
    public void testWolfPackDeclaration() {
        world = new World(4);
        IDGenerator id = new IDGenerator();

        Wolf wolf1 = new Wolf(id);
        Wolf wolf2 = new Wolf(id);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        assertEquals(wolf1.getPack().size(), 2,
                "The size of the wolf pack list should be 2, but it is: " + wolf1.getPack().size());
    }

    /**
     * This test will check the 'removeWolfFromPack()' method from the Wolf class
     * This will ensure
     */
    @Test
    public void testRemovingWolfFromPack() {
        world = new World(3);
        IDGenerator idGenerator = new IDGenerator();

        Wolf wolf1 = new Wolf(idGenerator);
        Wolf wolf2 = new Wolf(idGenerator);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf1);

        wolf1.removeWolfFromPack(wolf2);

        assertEquals(wolf1.getPack().size(), 1,
                "The size of the pack after removing a wolf should be 1, but is: " + wolf1.getPack().size());


    }

    /**
     * This test will check the 'OvertakePack()' method from the Wolf class to see if the correct wolf is the new 'alpha'
     * By checking this method the test will also use 'getMy_alpha()' method
     * to check if the correct information is displayed
     */
    @Test
    public void testOvertakePack() {
        world = new World(3);
        IDGenerator idGenerator = new IDGenerator();

        Wolf wolf1 = new Wolf(idGenerator);
        Wolf wolf2 = new Wolf(idGenerator);
        Wolf wolf3 = new Wolf(idGenerator);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        wolf3.overtakePack(wolf1);

        assertSame(wolf1.getMyAlpha(), wolf3,
                "wolf2's alpha should be wolf3, but it is: " + wolf1.getMyAlpha());
    }

    /**
     * Tests if wolves can create caves.
     */
    @Test
    public void testCreateCave() {
        world = new World(3);
        IDGenerator id_generator = new IDGenerator();

        Wolf wolf1 = new Wolf(id_generator);
        Wolf wolf2 = new Wolf(id_generator);

        wolf1.spawn(world);
        wolf2.spawn(world);
        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        wolf1.createCave(world, id_generator);

        boolean isCave = false;

        for (Object object : world.getEntities().keySet()) {
            if (object instanceof Cave) {
                isCave = true;
                break;
            }
        }

        assertTrue(isCave, "There should be a cave in the world, but there isn't");
    }

    @Test
    public void testEnterCave() {
        world = new World(4);
        IDGenerator id_generator = new IDGenerator();

        Wolf wolf1 = new Wolf(id_generator);
        Wolf wolf2 = new Wolf(id_generator);

        wolf1.spawn(world);
        wolf2.spawn(world);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        wolf1.createCave(world, id_generator);

        wolf1.enterCave(world);
        wolf2.enterCave(world);

        assertEquals(wolf1.getMy_cave().getResidents().size(), 2,
                "the amount of wolfs in the cave should be 2, but there is: "
                         + wolf1.getMy_cave().getResidents().size());


    }

    /**
     * This test showcases the 'exitCave()' method from the 'Wolf' class. This test cooperates with the 'testEnterCave'
     * method, since it relies on the fact that a wolf should be able to enter the cave. Please check the
     * previous method before validating this one.
     */
    @Test
    public void testExitCave() {
        world = new World(5);
        IDGenerator id_generator = new IDGenerator();

        Wolf wolf1 = new Wolf(id_generator);
        Wolf wolf2 = new Wolf(id_generator);

        wolf1.spawn(world);
        wolf2.spawn(world);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        wolf1.createCave(world, id_generator);

        // should add two wolves to the cave's residents, works from previous testing
        wolf1.enterCave(world);
        wolf2.enterCave(world);

        wolf1.exitCave(world);
        wolf1.nextMove(world);
        wolf2.exitCave(world);

        assertEquals(wolf1.getMy_cave().getResidents().size(), 0,
                "the amount of wolves in the cave should be 0, but is: "
                         + wolf1.getMy_cave().getResidents().size());
    }
}
