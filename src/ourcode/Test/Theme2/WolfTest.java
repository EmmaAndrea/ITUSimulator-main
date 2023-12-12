package ourcode.Test.Theme2;

import itumulator.executable.Program;
import itumulator.world.Location;
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

        programRunner.runSimulation(0);

        world = programRunner.getWorld();
        assertEquals(world.getEntities().size(), 1,
                "the amount of entities in the world should be 1 but is " + world.getEntities().size());
    }

    /**
     * This test will check the list 'pack' in the Wolf class along with some of the methods for retrieving
     * and adding a wolf to the list
     */
    @Test
    public void testWolfPackDeclarationMethod() {
        world = new World(4);
        IDGenerator id = new IDGenerator();

        Wolf wolf1 = new Wolf(id, false);
        wolf1.spawn(world);
        System.out.println("1 wolf has been spawned and the amount of entities is: " + world.getEntities().size());
        Wolf wolf2 = new Wolf(id, false);
        wolf2.spawn(world);
        System.out.println("1 wolf has been spawned and the amount of entities is: " + world.getEntities().size());

        wolf1.createPack();
        System.out.println("a wolf pack has been made and has a size of: " + wolf1.getPack().size());
        wolf1.addWolfToPack(wolf2);
        System.out.println("a wolf has been added to the pack");

        assertEquals(wolf1.getPack().size(), 2,
                "The size of the wolf pack list should be 2, but it is: " + wolf1.getPack().size());
        System.out.println("the amount of wolves in the pack is: " + wolf1.getPack().size());
    }

    /**
     * This test will check the 'removeWolfFromPack()' method from the Wolf class
     * This will ensure
     */
    @Test
    public void testRemovingWolfFromPackMethod() {
        world = new World(3);
        IDGenerator idGenerator = new IDGenerator();

        Wolf wolf1 = new Wolf(idGenerator, false);
        wolf1.spawn(world);
        System.out.println("a wolf has been added to the world, the amount of entities is: " + world.getEntities().size());
        Wolf wolf2 = new Wolf(idGenerator, false);
        wolf2.spawn(world);
        System.out.println("a wolf has been added to the world, the amount of entities is: " + world.getEntities().size());

        wolf1.createPack();
        System.out.println("wolf1 created a pack with a size of: " + wolf1.getPack().size());
        wolf1.addWolfToPack(wolf2);
        System.out.println("wolf1 added wolf2, the packs size is: " + wolf1.getPack().size());
        System.out.println("wolf2 is now in a pack and 'has_pack boolean is '" + wolf2.getHasPack() + "'");

        wolf1.removeWolfFromPack(wolf2);
        System.out.println("wolf2 was removed by wolf1 from the pack and the size of the pack is: " + wolf1.getPack().size());
        System.out.println("wolf2 currently doesn't have a pack and 'has_pack' boolean is '" + wolf2.getHasPack() + "'");

        assertEquals(wolf1.getPack().size(), 1,
                "The size of the pack after removing a wolf should be 1, but is: " + wolf1.getPack().size());


    }

    /**
     * This test will check the 'OvertakePack()' method from the Wolf class to see if the correct wolf is the new 'alpha'
     * By checking this method the test will also use 'getMy_alpha()' method
     * to check if the correct information is displayed
     */
    @Test
    public void testOvertakePackMethod() {
        world = new World(3);
        IDGenerator idGenerator = new IDGenerator();

        Wolf wolf1 = new Wolf(idGenerator, false);
        wolf1.spawn(world);
        System.out.println("wolf1 added to the world, the amount of entities are: " + world.getEntities().size());
        Wolf wolf2 = new Wolf(idGenerator, false);
        wolf2.spawn(world);
        System.out.println("wolf2 added to the world, the amount of entities are: " + world.getEntities().size());
        Wolf wolf3 = new Wolf(idGenerator, false);
        wolf3.spawn(world);
        System.out.println("wolf3 added to the world, the amount of entities are: " + world.getEntities().size());

        System.out.println("wolf1 doesn't have a pack and 'has_pack' is " + wolf1.getHasPack());
        wolf1.createPack();
        System.out.println("wolf1 created a pack, wolf1's 'has_pack' boolean is " + wolf1.getHasPack());
        System.out.println("the size of wolf1's pack is " + wolf1.getPack().size());
        wolf1.addWolfToPack(wolf2);

        wolf3.overtakePack(wolf1);

        assertSame(wolf1.getMyAlpha(), wolf3,
                "wolf2's alpha should be wolf3, but it is: " + wolf1.getMyAlpha());
    }

    /**
     * Tests if wolves can create caves.
     */
    @Test
    public void testCreateCaveMethod() {
        world = new World(3);
        IDGenerator id_generator = new IDGenerator();

        Wolf wolf1 = new Wolf(id_generator, false);
        Wolf wolf2 = new Wolf(id_generator, false);

        wolf1.spawn(world);
        wolf2.spawn(world);
        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        wolf1.makeHabitat(world);

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
    public void testEnterCaveMethod() {
        world = new World(4);
        IDGenerator id_generator = new IDGenerator();

        Wolf wolf1 = new Wolf(id_generator, false);
        Wolf wolf2 = new Wolf(id_generator, false);

        wolf1.spawn(world);
        wolf2.spawn(world);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        wolf1.makeHabitat(world);

        wolf1.enterHabitat(world);
        wolf2.enterHabitat(world);

        assertEquals(wolf1.getMyCave().getResidents().size(), 2,
                "the amount of wolfs in the cave should be 2, but there is: "
                         + wolf1.getMyCave().getResidents().size());


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

        Wolf wolf1 = new Wolf(id_generator, false);
        Wolf wolf2 = new Wolf(id_generator, false);

        wolf1.spawn(world);
        wolf2.spawn(world);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        wolf1.makeHabitat(world);

        // should add two wolves to the cave's residents, works from previous testing
        wolf1.enterHabitat(world);
        wolf2.enterHabitat(world);

        wolf1.exitHabitat(world);
        wolf1.nextMove(world);
        wolf2.exitHabitat(world);

        assertEquals(wolf1.getMyCave().getResidents().size(), 0,
                "the amount of wolves in the cave should be 0, but is: "
                         + wolf1.getMyCave().getResidents().size());
    }

    @Test
    public void testWolves() throws Exception {
        programRunner.create("./data/t2-3a.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(10);

        for (int i = 0; i < 10; i++) {
            System.out.println(world.getEntities().size());
        }

    }

    @Test
    public void testWolvesBreedInCave() {
        Program p = new Program(3, 800, 500);
        world = p.getWorld();

        // Wolf

    }

    @Test
    public void testWolfGroupEatingBear() {
        Program p = new Program(3, 800, 500);
        world = p.getWorld();
    }

    @Test
    public void testWolfBreedingMethod() throws Exception {
        Program p = new Program(20,800,500);
        IDGenerator id_generator = new IDGenerator();
        world = p.getWorld();

        Wolf wolf1 = new Wolf(id_generator, false);
        Wolf wolf2 = new Wolf(id_generator, false);

        wolf1.setGender("MALE");
        System.out.println(wolf1.getGender());
        wolf2.setGender("FEMALE");

        wolf1.spawn(world);
        wolf2.spawn(world);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        wolf1.makeHabitat(world);
        wolf1.enterHabitat(world);
        wolf2.enterHabitat(world);
        int prev = wolf1.getMyCave().getResidents().size();
        wolf1.breed(world);
        int post = wolf1.getMyCave().getResidents().size();

        assertTrue(prev < post, "the amount of residents in the cave should increase post breed");
    }

    @Test
    public void testWolfBreedSimulation() {
        Program p = new Program(3,500,1000);
        world = p.getWorld();
        IDGenerator idGenerator = new IDGenerator();

        Location location0 = new Location(0,0);
        Location location1 = new Location(1,1);
        Wolf wolfMALE = new Wolf(idGenerator, false);
        wolfMALE.setGender("Male");
        Wolf wolfFEMALE = new Wolf(idGenerator, false);
        wolfFEMALE.setGender("Female");
        wolfMALE.createPack();
        wolfMALE.addWolfToPack(wolfFEMALE);
        System.out.println(wolfFEMALE.getGender());
        System.out.println(wolfMALE.getGender());

        wolfMALE.setAge(20);
        System.out.println(wolfMALE.getAge());
        wolfFEMALE.setAge(20);
        System.out.println(wolfFEMALE.getAge());

        world.setTile(location0, wolfMALE);
        world.setTile(location1, wolfFEMALE);

        p.show();
        for (int i = 0; i < 20; i++) {
            p.simulate();
            System.out.println(wolfMALE.isIn_hiding());
            System.out.println(wolfMALE.getMyCave().getResidents().size());
        }
    }
}