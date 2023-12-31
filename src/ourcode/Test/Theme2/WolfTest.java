package ourcode.Test.Theme2;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Obstacles.Cave;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Bear;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.TerritorialPredator;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.SocialPredator;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Wolf;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rodent;

import static org.junit.jupiter.api.Assertions.*;

public class WolfTest {
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
        programRunner = new ProgramRunner();
    }

    public void countWolves(World world) {
        for (Object entity : world.getEntities().keySet()) {
            if (entity instanceof SocialPredator) {
                wolf_count++;
            }
        }
    }

    /**
     * Requirement a for wolves: spawn.
     * This test will check if a wolf can be spawned if dictated by an input file.
     * Also checks if the constructor of a Wolf functions
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
     * Requirement b for wolves: die.
     * Check is wolves die of hunger.
     */
    @Test
    public void testWolfDiesOfHUnger() throws Exception {
        programRunner.create("./data/t2-1ab.txt");

        programRunner.runSimulation(110);

        world = programRunner.getWorld();

        countWolves(world);

        assertEquals(0, wolf_count,
                "the amount of entities in the world should be 1 but is " + world.getEntities().size());
    }

    /**
     * Requirement for wolves: packs
     * This test will check the list 'pack' in the Wolf class along with some of the methods for retrieving
     * and adding a wolf to the list
     */
    @Test
    public void testWolfPackDeclarationMethod() {

        SocialPredator wolf1 = new Wolf(id_generator, false);
        wolf1.spawn(world);
        System.out.println("1 wolf has been spawned and the amount of entities is: " + world.getEntities().size());
        SocialPredator wolf2 = new Wolf(id_generator, false);
        wolf2.spawn(world);
        System.out.println("1 wolf has been spawned and the amount of entities is: " + world.getEntities().size());

        wolf1.createPack();
        System.out.println("a wolf pack has been made and has a size of: " + wolf1.getPack().size());
        wolf1.addToPack(wolf2);
        System.out.println("a wolf has been added to the pack");

        assertEquals(wolf1.getPack().size(), 2,
                "The size of the wolf pack list should be 2, but it is: " + wolf1.getPack().size());
        System.out.println("the amount of wolves in the pack is: " + wolf1.getPack().size());
    }

    /**
     * Requirement for wolves: packs
     * This test will check the 'removeWolfFromPack()' method from the Wolf class
     * This will ensure
     */
    @Test
    public void testRemovingWolfFromPackMethod() {

        SocialPredator wolf1 = new Wolf(id_generator, false);
        wolf1.spawn(world);
        System.out.println("a wolf has been added to the world, the amount of entities is: " + world.getEntities().size());
        SocialPredator wolf2 = new Wolf(id_generator, false);
        wolf2.spawn(world);
        System.out.println("a wolf has been added to the world, the amount of entities is: " + world.getEntities().size());

        wolf1.createPack();
        System.out.println("wolf1 created a pack with a size of: " + wolf1.getPack().size());
        wolf1.addToPack(wolf2);
        System.out.println("wolf1 added wolf2, the packs size is: " + wolf1.getPack().size());
        System.out.println("wolf2 is now in a pack and 'has_pack boolean is '" + wolf2.getHasPack() + "'");

        wolf1.removeFromPack(wolf2);
        System.out.println("wolf2 was removed by wolf1 from the pack and the size of the pack is: " + wolf1.getPack().size());
        System.out.println("wolf2 currently doesn't have a pack and 'has_pack' boolean is '" + wolf2.getHasPack() + "'");

        assertEquals(wolf1.getPack().size(), 1,
                "The size of the pack after removing a wolf should be 1, but is: " + wolf1.getPack().size());


    }

    /**
     * Optional requirement for wolves: hurt wolf can be overtaken
     * This test will check the 'OvertakePack()' method from the Wolf class to see if the correct wolf is the new 'alpha'
     * By checking this method the test will also use 'getMy_alpha()' method
     * to check if the correct information is displayed
     */
    @Test
    public void testOvertakePackMethod() {

        SocialPredator wolf1 = new Wolf(id_generator, false);
        wolf1.spawn(world);
        System.out.println("wolf1 added to the world, the amount of entities are: " + world.getEntities().size());
        SocialPredator wolf2 = new Wolf(id_generator, false);
        wolf2.spawn(world);
        System.out.println("wolf2 added to the world, the amount of entities are: " + world.getEntities().size());
        SocialPredator wolf3 = new Wolf(id_generator, false);
        wolf3.spawn(world);
        System.out.println("wolf3 added to the world, the amount of entities are: " + world.getEntities().size());

        System.out.println("wolf1 doesn't have a pack and 'has_pack' is " + wolf1.getHasPack());
        wolf1.createPack();
        System.out.println("wolf1 created a pack, wolf1's 'has_pack' boolean is " + wolf1.getHasPack());
        System.out.println("the size of wolf1's pack is " + wolf1.getPack().size());
        wolf1.addToPack(wolf2);

        wolf3.overtakePack(wolf1);

        assertSame(wolf1.getMyAlpha(), wolf3,
                "wolf2's alpha should be wolf3, but it is: " + wolf1.getMyAlpha());
    }

    /**
     * Requirement for wolves: becoming a pack based on an input file
     * Checks each wolf is in the same pack and has the same alpha
     */
    @Test
    public void TestingWolfPackSpawns() throws Exception{
        programRunner.create("./data/wolf-test4.txt");

        world = programRunner.getWorld();

        programRunner.runSimulation(1);

        boolean same_alpha = false;

        Wolf alpha = null;
        for(Object object: world.getEntities().keySet()){
            if (object instanceof Wolf wolf){
                alpha = (Wolf) wolf.getMyAlpha();
                break;
            }
        }
        for(Object object: world.getEntities().keySet()){
            if (object instanceof Wolf wolf){
                if (wolf.getMyAlpha() == alpha){
                    same_alpha= true;
                } else {
                    same_alpha = false;
                    break;
                }
            }
        }

        assertTrue(same_alpha, "each wolf should have the same alpha");

        assertEquals(6, alpha.getPack().size(), "there should be 6 wolves in the pack");
    }

    /**
     * Requirement for wolves: caves
     * Tests if wolves can create caves.
     * In a real simulation, only the alpha wolf will create a cave.
     */
    @Test
    public void testCreateCaveMethod() {

        SocialPredator wolf1 = new Wolf(id_generator, false);
        SocialPredator wolf2 = new Wolf(id_generator, false);

        wolf1.spawn(world);
        wolf2.spawn(world);
        wolf1.createPack();
        wolf1.addToPack(wolf2);

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

    /**
     * Requirement for wolves: caves
     */
    @Test
    public void testEnterCaveMethod() {

        SocialPredator wolf1 = new Wolf(id_generator, false);
        SocialPredator wolf2 = new Wolf(id_generator, false);

        wolf1.spawn(world);
        wolf2.spawn(world);

        wolf1.createPack();
        wolf1.addToPack(wolf2);

        wolf1.makeHabitat(world);

        wolf1.enterHabitat(world);
        wolf2.enterHabitat(world);

        assertEquals(wolf1.getMyHabitat().getResidents().size(), 2,
                "the amount of wolfs in the cave should be 2, but there is: "
                        + wolf1.getMyHabitat().getResidents().size());


    }

    /**
     * This test showcases the 'exitCave()' method from the 'Wolf' class. This test cooperates with the 'testEnterCave'
     * method, since it relies on the fact that a wolf should be able to enter the cave. Please check the
     * previous method before validating this one.
     */
    @Test
    public void testExitCave() {

        SocialPredator wolf1 = new Wolf(id_generator, false);
        SocialPredator wolf2 = new Wolf(id_generator, false);

        wolf1.spawn(world);
        wolf2.spawn(world);

        wolf1.createPack();
        wolf1.addToPack(wolf2);

        wolf1.makeHabitat(world);

        // should add two wolves to the cave's residents, works from previous testing
        wolf1.enterHabitat(world);
        wolf2.enterHabitat(world);

        wolf1.exitHabitat(world);
        wolf1.nextMove(world);
        wolf2.exitHabitat(world);

        assertEquals(wolf1.getMyHabitat().getResidents().size(), 0,
                "the amount of wolves in the cave should be 0, but is: "
                        + wolf1.getMyHabitat().getResidents().size());
    }

    /**
     * Requirement for wolves: breeding in caves (K2-3a)
     * This test checks if wolves breed in caves
     * Certain elements have been tampered to focus the test case.
     * Tampered elements are: setGender 'Male' and 'Female' for wolves,
     * The alpha will create a pack and then create a cave, which the pack then belongs to
     * Checks there are more than the original two wolves existing in the world.
     */
    @Test
    public void testWolfBreedingMethod() throws Exception {
        // simply creates a world from the input file
        programRunner.create("./data/wolf-test1.txt");

        world = programRunner.getWorld();

        SocialPredator wolf1 = new Wolf(id_generator, false);
        SocialPredator wolf2 = new Wolf(id_generator, false);

        wolf1.setGender("MALE");
        wolf2.setGender("FEMALE");

        wolf1.spawn(world);
        wolf2.spawn(world);

        wolf1.createPack();
        wolf1.addToPack(wolf2);

        programRunner.runSimulation(20);

        programRunner.runSimulation(15);

        countWolves(world);

        assertTrue(wolf_count > 2, "the amount of wolves should be 3 post breed");
    }


    /**
     * Requirement for wolves: wolves attack other wolves if not in same pack.
     * Testing that wolves attack lone wolves.
     * This test is dependent on circumstances, and which act happens first,
     * however the lone wolf should never be able to attack the pack wolves
     * Sometimes it must be run twice to succeed
     */
    @Test
    public void TestWolfPackAttacksLoneWolf() throws Exception {
        programRunner.create("./data/wolf-test2.txt");

        world = programRunner.getWorld();

        SocialPredator lone_wolf = null;
        for (Object object : world.getEntities().keySet()) {
            if (object instanceof SocialPredator wolf) {
                if (wolf.getMyAlpha().getPack().size() == 1) {
                    lone_wolf = wolf;
                }
            }
        }

        assertEquals(0, lone_wolf.getDamageTaken());

        programRunner.runSimulation(2);

        assertTrue(lone_wolf.getDamageTaken() > 3, "the lone wolf gets hurt twice and takes 4 damage, unless he moves away first");

    }


    /**
     * Optional requirement for wolves: hurt wolf can be overtaken
     * @throws Exception
     */
    @Test
    public void TestWolfPackTakesLoneWolf() throws Exception {
        programRunner.create("./data/wolf-test2.txt");

        world = programRunner.getWorld();

        SocialPredator lone_wolf = null;
        for (Object object : world.getEntities().keySet()) {
            if (object instanceof SocialPredator wolf) {
                if (wolf.getMyAlpha().getPack().size() == 1) {
                    lone_wolf = wolf;
                }
            }
        }

        world.setNight();

        assertEquals(0, lone_wolf.getDamageTaken());

        programRunner.runSimulation(2);

        assertTrue(lone_wolf.getDamageTaken() > 3, "the lone wolf gets hurt twice and takes 4 damage, unless he moves away first");

        programRunner.runSimulation(5);

        assertEquals(3, lone_wolf.getMyAlpha().getPack().size(), "the pack takes in the lone wolf");
    }

    /**
     * Checking wolf moves away from enemy cave
     */
    @Test
    public void TestWolfMovesAwayFromEnemyCave() throws Exception {
        programRunner.create("./data/wolf-test3.txt");

        world = programRunner.getWorld();

        SocialPredator wolf1 = new Wolf(programRunner.getOriginal_id_generator() , false);
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
         * Requirement for wolves: hunt together (share food)
         * Testing wolves eat together
         */
        @Test
        public void TestWolfPackSharesFoodWhenHungry () throws Exception {
            programRunner.create("./data/wolf-test4.txt");

            world = programRunner.getWorld();

            programRunner.runSimulation(11);

            TerritorialPredator bear = new Bear(programRunner.getOriginal_id_generator(), false);
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

            for (Object object : world.getEntities().keySet()) {
                if (object instanceof SocialPredator) {
                    break;
                }
            }


            programRunner.runSimulation(1);

            programRunner.runSimulation(1);
            programRunner.runSimulation(1);
            programRunner.runSimulation(1);

            System.out.println(bear_carcass.getEatenBy());
            assertTrue(bear_carcass.amountGottenEatenBy() > 1);

        }

    /**
     * Requirement: wolves don't like other packs
     * Testing that wolves move away from each other
     */

    @Test
    public void TestWolfPacksMoveAwayFromEachOther() throws Exception {
        programRunner.create("./data/wolf-test5.txt");

        world = programRunner.getWorld();

        world.setNight();

        programRunner.runSimulation(7);

        // visual check
    }

    /**
     * Requirement: pack animal
     * Testing wolves stay together
     * @throws Exception
     */
    @Test
    public void TestWolvesMoveCloserToTheirPack() throws Exception {
        programRunner.create("./data/wolf_test6.txt");

        world = programRunner.getWorld();

        world.setNight();

        programRunner.runSimulation(12);

        // visual check
    }

    /**
     * Requirement: wolves hunt together.
     * Testing if all wolves in the pack og into hunt mode together
     */
    @Test
    public void testWolvesHuntTogether() {
        Program p = new Program(4, 500, 2000);
        world = p.getWorld();
        IDGenerator idGenerator = new IDGenerator();

        // sets locations for wolves and rabbit to spawn on
        Location location = new Location(0,0);
        Location location1 = new Location(0,1);
        Location location2 = new Location(0,2);
        Location location3 = new Location(3,3);

        // added wolves to world
        SocialPredator wolf = new Wolf(idGenerator, false);
        SocialPredator wolf1 = new Wolf(idGenerator, false);
        SocialPredator wolf2 = new Wolf(idGenerator, false);

        // makes wolves appropriate age to hunt
        wolf.setAge(20);
        wolf1.setAge(20);
        wolf2.setAge(20);

        wolf.createPack();

        wolf.addToPack(wolf1);
        wolf.addToPack(wolf2);

        Rodent rabbit = new Rabbit(idGenerator, false);

        // spawns wolves at given locations
        world.setTile(location, wolf);
        world.setTile(location1, wolf1);
        world.setTile(location2, wolf2);

        // spawns rabbit at given location
        world.setTile(location3, rabbit);

        // display information
        p.show();
        for (int i = 0; i < 20; i++) {
            p.simulate();
        }

        assertTrue(wolf1.isWolfHunting());
        assertTrue(wolf2.isWolfHunting());
        assertTrue(wolf.isWolfHunting());
    }
}