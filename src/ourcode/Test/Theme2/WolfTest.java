package ourcode.Test.Theme2;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Obstacles.Cave;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Bear;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Wolf;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.*;

public class WolfTest {
    private Program p;

    private ProgramRunner programRunner;
    private World world;
    private int wolf_count;
    private IDGenerator id_generator;

    public WolfTest() {
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Wolves");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
        wolf_count = 0;
        world = new World(4);
        id_generator = new IDGenerator();
        p = new Program(4, 1000, 100);
        programRunner = new ProgramRunner();
    }

    public void countWolves(World world) {
        for (Object entity : world.getEntities().keySet()) {
            if (entity instanceof Wolf) {
                wolf_count++;
            }
        }
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

        countWolves(world);

        assertEquals(1, wolf_count,
                "the amount of entities in the world should be 1 but is " + world.getEntities().size());
    }

    /**
     * This test will check the list 'pack' in the Wolf class along with some of the methods for retrieving
     * and adding a wolf to the list
     */
    @Test
    public void testWolfPackDeclarationMethod() {

        Wolf wolf1 = new Wolf(id_generator, false);
        wolf1.spawn(world);
        System.out.println("1 wolf has been spawned and the amount of entities is: " + world.getEntities().size());
        Wolf wolf2 = new Wolf(id_generator, false);
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

        Wolf wolf1 = new Wolf(id_generator, false);
        wolf1.spawn(world);
        System.out.println("a wolf has been added to the world, the amount of entities is: " + world.getEntities().size());
        Wolf wolf2 = new Wolf(id_generator, false);
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

        Wolf wolf1 = new Wolf(id_generator, false);
        wolf1.spawn(world);
        System.out.println("wolf1 added to the world, the amount of entities are: " + world.getEntities().size());
        Wolf wolf2 = new Wolf(id_generator, false);
        wolf2.spawn(world);
        System.out.println("wolf2 added to the world, the amount of entities are: " + world.getEntities().size());
        Wolf wolf3 = new Wolf(id_generator, false);
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

    /**
     * This test checks if wolves breed in caves
     * Certain elements have been tampered to focus the test case.
     * Tampered elements are: setGender 'Male' and 'Female' for wolves,
     * Checks for three wolves existing in the world
     */
    @Test
    public void testWolfBreedingMethod() throws Exception {
        programRunner.create("./data/wolf-test1.txt");

        world = programRunner.getWorld();

        Wolf wolf1 = new Wolf(id_generator, false);
        Wolf wolf2 = new Wolf(id_generator, false);

        wolf1.setGender("MALE");
        wolf2.setGender("FEMALE");

        wolf1.spawn(world);
        wolf2.spawn(world);

        wolf1.createPack();
        wolf1.addWolfToPack(wolf2);

        programRunner.runSimulation(20);

        programRunner.runSimulation(7);

        countWolves(world);

        assertTrue(wolf_count == 3, "the amount of wolves should be 3 post breed");
    }


    /**
     * Testing that wolves attack lone wolves
     */
    @Test
    public void TestWolfPackAttacksLoneWolf() throws Exception {
        programRunner.create("./data/wolf-test2.txt");

        world = programRunner.getWorld();

        Wolf lone_wolf = null;
        for (Object object : world.getEntities().keySet()) {
            if (object instanceof Wolf wolf) {
                if (wolf.getMyAlpha().getPack().size() == 1) {
                    lone_wolf = wolf;
                }
            }
        }

        assertEquals(0, lone_wolf.getDamageTaken());

        programRunner.runSimulation(1);

        assertEquals(7, lone_wolf.getDamageTaken(), "the lone wolf gets hurt twice and takes 8 damage, then heals once");

    }


    @Test
    public void TestWolfPackTakesLoneWolf() throws Exception {
        programRunner.create("./data/wolf-test2.txt");

        world = programRunner.getWorld();

        Wolf lone_wolf = null;
        for (Object object : world.getEntities().keySet()) {
            if (object instanceof Wolf wolf) {
                if (wolf.getMyAlpha().getPack().size() == 1) {
                    lone_wolf = wolf;
                }
            }
        }

        programRunner.runSimulation(1);

        assertEquals(7, lone_wolf.getDamageTaken(), "the lone wolf gets hurt twice and takes 8 damage, then heals once");

        assertEquals(3, lone_wolf.getMyAlpha().getPack().size(), "now the wolf should become a part of the other wolves' pack");

        programRunner.runSimulation(1);

        assertEquals(6, lone_wolf.getDamageTaken(), "the wolves shouldn't attack him anymore");
    }

    /**
     * Checking wolf moves away from enemy cave
     */
    @Test
    public void TestWolfMovesAwayFromEnemyCave() throws Exception {
        programRunner.create("./data/wolf-test3.txt");

        world = programRunner.getWorld();

        Wolf wolf1 = new Wolf(programRunner.getOriginal_id_generator() , false);
        Location wolf_spawn_location = new Location(0,0);
        world.setTile(wolf_spawn_location, wolf1);

        Cave cave1 = new Cave(programRunner.getOriginal_id_generator());
        Location cave_spawn_location = new Location(3,3);
        world.setTile(cave_spawn_location, cave1);

        programRunner.runSimulation(10);

        programRunner.runSimulation(4);
        assertTrue(wolf1.distanceTo(world, cave_spawn_location) > 2, "the wolf should be far away from enemy cave");
    }


        /**
         * checking wolves eat together
         */
        @Test
        public void TestWolfPackSharesFoodWhenHungry () throws Exception {
            programRunner.create("./data/wolf-test4.txt");

            world = programRunner.getWorld();

            programRunner.runSimulation(7);

            Bear bear = new Bear(programRunner.getOriginal_id_generator(), false);
            bear.spawn(world);

            while (world.getEntities().containsKey(bear)) {
                programRunner.runSimulation(1);
            }

            Carcass bear_carcass = null;
            for (Object object : world.getEntities().keySet()) {
                if (object instanceof Carcass carcass) {
                    if (carcass.getType().equals("bear")) {
                        bear_carcass = carcass;
                    }
                }
            }

            assertEquals(12, bear_carcass.getNutritionalValue());

            Wolf wolf1 = null;
            for (Object object : world.getEntities().keySet()) {
                if (object instanceof Wolf wolf) {
                    wolf1 = wolf;
                    break;
                }
            }

            Wolf hungriest_wolf_before = wolf1.getHungriestWolf();

            /**
            int hungriest_wolf_hunger_before = hungriest_wolf.getHunger();

            if (hungriest_wolf == wolf1) {
                for (Object object : world.getEntities().keySet()) {
                    if (object instanceof Wolf wolf) {
                        if (wolf1 != wolf) {
                            wolf1 = wolf;
                            break;
                        }
                    }
                }
            }
            int wolf1_hunger_before = wolf1.getHunger();
             */


            programRunner.runSimulation(1);

            // int hungries_wolf_hunger_after = hungriest_wolf.getHunger();

            // int wolf1_hunger_after = wolf1.getHunger();

            Wolf hungriest_wolf_after = wolf1.getHungriestWolf();

            programRunner.runSimulation(1);
            programRunner.runSimulation(1);
            programRunner.runSimulation(1);

            System.out.println(bear_carcass.getEatenBy());
            assertTrue(bear_carcass.amountGottenEatenBy() > 1);

        }

    /**
     * Testing that wolves move away from each other
     */

    @Test
    public void TestWolfPacksMoveAwayFromEachOther() throws Exception{
        programRunner.create("./data/wolf-test5.txt");

        world = programRunner.getWorld();

        world.setNight();

        programRunner.runSimulation(7);

        // visual check
    }
}