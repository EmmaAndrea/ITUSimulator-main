package ourcode.Test.Theme3;

import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Organism.OrganismChildren.Fungus;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;
import itumulator.executable.Program;

import static org.junit.jupiter.api.Assertions.*;

public class FungusTest {
    public ProgramRunner programRunner;
    public World world;
    public IDGenerator idGenerator;

    public FungusTest() {
        programRunner = new ProgramRunner();
        idGenerator = new IDGenerator();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Fungus");
    }

    @BeforeEach
    public void startTest() throws Exception{
        System.out.println("Test started");
    }

    /**
     * Testing that fungus is spawned correctly into thw world.
     * The file should spawn five to eight fungi
     */
    @Test
    public void testFungusSpawnMethod() {
        world = new World(3);

        Fungus fungus = new Fungus(idGenerator);
        fungus.spawn(world);

        assertEquals(1, world.getEntities().size());
    }

    @Test
    public void testFungusCheckCarcassMethod() throws Exception {
        world = new World(3);

        Carcass carcass = new Carcass(idGenerator, 4, "bear", false);
        carcass.spawn(world);
        Fungus fungus = new Fungus(idGenerator);
        fungus.spawn(world);
        fungus.checkSurroundingCarcass(world);

        assertSame(fungus.checkSurroundingCarcass(world), carcass);
    }

    /**
     * This test checks if the spread method from Fungus class is working.
     */
    @Test
    public void testFungusSpreadMethod() {
        world = new World(3);

        Carcass carcass = new Carcass(idGenerator, 4, "bear", false);
        Fungus fungus = new Fungus(idGenerator);
        fungus.spawn(world);
        fungus.spread(carcass);

        assertTrue(carcass.hasFungus());
    }

    @Test
    public void testFungusSpreadRadius() {
        Program p = new Program(3,500,800);
        world = p.getWorld();

        // Adds fungus, carcass and specified location for fungus and carcass to spawn on.
        Fungus fungus = new Fungus(idGenerator);
        Carcass carcass = new Carcass(idGenerator,4,"bear",false);
        Location location_0 = new Location(0,2);
        Location location_1 = new Location(2,0);

        world.setTile(location_0,fungus);
        world.setTile(location_1,carcass);

        p.show();
        for (int i = 0; i < 20; i++) {
            p.simulate();
        }

        assertTrue(carcass.hasFungus());

    }

    /**
     * Tests that no fungus is left in the world after 6 steps
     * Since the only thing in the world is the fungus.
     */
    @Test
    public void testFungusDiesIfDoesntFindCarcass() throws Exception {
        programRunner.create("./data/fungi-test.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(6);

        assertEquals(0, world.getEntities().size());
    }

    @Test
    public void testFungusSimulation() throws Exception {
        programRunner.create("./data/test-fungus.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(10);

        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass) {
                assertTrue(((Carcass) o).hasFungus());
            }
        }
    }
}