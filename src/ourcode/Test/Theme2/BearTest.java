package ourcode.Test.Theme2;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.*;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rodent;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Organism.OrganismChildren.PlantChildren.Bush;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.AssertJUnit.assertSame;

/**
 *
 */
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
     * This test checks the declaration of a new Bear. Checks if a bear is spawned into the world.
     */
    @Test
    public void testBearDeclaration() {
        world = new World(3);
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);

        bear.spawn(world);

        assertEquals(world.getEntities().size(), 1,
                "the amount of bears in the world should be 1 but is " + world.getEntities().size());
    }

    /**
     * Requirement for bears: spawn.
     * This test will demonstrate if a bear can be spawned through an input file.
     */
    @Test
    public void testBearInput() throws Exception {
        programRunner.create("./data/t2-5a.txt");
        world = programRunner.getWorld();
        id_generator = new IDGenerator();
        programRunner.getProgram().setDelay(1000);

        programRunner.runSimulation(1);

        int counter = countBears(world);

        assertTrue(counter == 1);
    }

    /**
     * Method to count bears for any test.
     * @param world
     * @return bear count
     */
    public int countBears(World world){
        int counter = 0;

        for (Object o : world.getEntities().keySet()) {
            if (o instanceof TerritorialPredator) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * This test checks the 'setTerritory' method and the 'getTerritory' method from the Bear class.
     * This will check if the correct location is being declared as the bear's 'territory'.
     */
    @Test
    public void testBearTerritoryMethod() {
        world = new World(3);
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);
        bear.spawn(world);

        bear.setTerritoryLocation(world.getLocation(bear));

        Assertions.assertSame(bear.getTerritory(), world.getLocation(bear));
    }

    /**
     * Optional requirement for bears, they search for mates and don't attack the opposite gender
     * This test checks if bears, being spawned through an input file, can use the 'findMate' method correctly.
     * @throws Exception
     */
    @Test
    public void testBearFindMate() throws Exception {
        Program program = new Program(3, 500, 200);
        world = program.getWorld();
        id_generator = new IDGenerator();

        TerritorialPredator male_bear = new Bear(id_generator, false);
        male_bear.setGender("Male");

        TerritorialPredator female_bear = new Bear(id_generator, false);
        female_bear.setGender("Female");

        female_bear.spawn(world);
        male_bear.spawn(world);

        program.show();
        for (int i = 0; i < 30; i++) {
            program.simulate();
        }

        int counter = countBears(world);

        assertSame(female_bear.getMate(), male_bear);
        assertSame(male_bear.getMate(), female_bear);
    }

    /**
     * Requirement for bear: eat everything
     * This test will demonstrate a bears eating 'capabilities'. It will showcase if a bear can eat 'berries'
     */
    @Test
    public void testBearEatingBerries() {
        Program p = new Program(2, 800, 500);
        world = p.getWorld();
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);
        Bush bush = new Bush(id_generator);

        bear.spawn(world);
        bush.spawn(world);

        boolean hasEaten = false;

        p.show();
        for (int i = 0; i < 20; i++) {
            p.simulate();
            if (bush.getBerriesAmount() <= 2) {
                hasEaten = true;
            }
        }

        assertTrue(hasEaten);
    }

    /**
     * Requirement for bear: eat everything
     * This test will demonstrate a bears eating 'capabilities'. It will showcase if a bear can eat 'grass'
     */
    @Test
    public void testBearEatingGrass() throws Exception {
        Program p = new Program(2, 800, 500);
        world = p.getWorld();
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);
        Grass grass = new Grass(id_generator);

        bear.spawn(world);
        grass.spawn(world);
        boolean hasEaten = true;
        p.show();
        for (int i = 0; i < 20; i++) {
            p.simulate();
        }

        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Grass) {
                hasEaten = false;
            }
        }

        assertTrue(hasEaten);
    }

    /**
     * Requirement for bear: eat everything
     * This test will demonstrate a bears eating 'capabilities'. It will showcase if a bear can eat 'carcass'
     */
    @Test
    public void testBearEatingCarcass() {
        Program p = new Program(2, 800, 500);
        world = p.getWorld();
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);
        Carcass carcass = new Carcass(id_generator, 4, "wolf", false);

        bear.spawn(world);
        carcass.spawn(world);

        boolean hasEatenCarcass = true;
        p.show();
        for (int i = 0; i < 20; i++) {
            p.simulate();
        }
        // checks after the simulation if the bear has eaten the spawned carcass
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Carcass) {
                hasEatenCarcass = false;
            }
        }

        assertTrue(hasEatenCarcass, "The bear did 'in fact' not eat the placed carcass");
    }

    /**
     * Requirement for bear: can hunt
     * This test demonstrates a bears 'hunting' capabilities. For this test it will check if a bear can 'hunt' a rabbit
     * and then eat the rabbit.
     */
    @Test
    public void testBearHuntingRabbit() {
        Program p = new Program(2, 500, 800);
        world = p.getWorld();
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);
        Rodent rabbit = new Rabbit(id_generator, false);

        bear.spawn(world);
        rabbit.spawn(world);

        p.show();
        for (int i = 0; i < 20; i++) {
            p.simulate();
        }

        int animal_count = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Animal) {
                animal_count++;
            }
        }

        assertEquals(1, animal_count);
    }

    /**
     * Requirement for bear: eats everything
     * the following tests show bear eats all other animals
     * bear eats rabbit
     */
    @Test
    public void testBearEatingRabbit() {
        Program p = new Program(2, 500, 800);
        world = p.getWorld();
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);
        Rodent rabbit = new Rabbit(id_generator, false);

        bear.spawn(world);
        rabbit.spawn(world);
        bear.setAge(11);
        bear.setHunger(20);

        p.show();
        for (int i = 0; i < 10; i++) {
            p.simulate();
        }

        int x = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Animal) {
                x++;
            }
        }

        assertTrue(x == 1, "There should only be 1 animal in the world");
    }

    /**
     * bear eats wolf
     */
    @Test
    public void testBearEatingWolf() {
        Program p = new Program(2, 500, 800);
        world = p.getWorld();
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);
        SocialPredator wolf = new Wolf(id_generator, false);

        bear.spawn(world);
        wolf.spawn(world);
        bear.setAge(11);
        bear.setHunger(20);

        p.show();
        for (int i = 0; i < 20; i++) {
            p.simulate();
        }

        int x = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Animal) {
                x++;
            }
        }

        assertTrue(x == 1, "There should only be 1 animal in the world");
    }

    /**
     * bear eats dino
     */
    @Test
    public void testBearEatingDinosaur() {
        Program p = new Program(2, 500, 800);
        world = p.getWorld();
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);
        Dinosaur dino = new TyrannosaurusRex(id_generator, false);

        bear.spawn(world);
        dino.spawn(world);
        bear.setAge(11);
        bear.setHunger(20);

        boolean hasEaten = true;
        p.show();
        for (int i = 0; i < 20; i++) {
            p.simulate();
        }

        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Dinosaur) {
                hasEaten = false;
            }
        }
        assertTrue(hasEaten);
    }

    /**
     * Requirement: bear attacks bear near territory
     * Bears attack other bears that are not potential mates
     * bear eats bear
     */
    @Test
    public void testBearEatingBear() {
        Program p = new Program(2, 500, 800);
        world = p.getWorld();
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);
        bear.setGender("Male");
        TerritorialPredator bear2 = new Bear(id_generator, false);
        bear2.setGender("Male");

        bear.spawn(world);
        bear2.spawn(world);
        bear.setHunger(20);

        int bear_count = 0;
        p.show();
        for (int i = 0; i < 15; i++) {
            p.simulate();
        }

        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Bear) {
                bear_count++;
            }
        }

        assertTrue(bear_count < 2);
    }
    /**
     * Requirement for bear: stay near territory
     * This test will check if a bears walks outside its territory.
     * It will only leave its territory to hunt or find mate
     * Creates territory immediately after spawn
     */
    @Test
    public void testBearTerritory() {
        Program p = new Program(6, 800, 500);
        world = p.getWorld();
        id_generator = new IDGenerator();

        TerritorialPredator bear = new Bear(id_generator, false);
        bear.spawn(world);

        int counter = 0;

        p.show();

        p.simulate();
        assertTrue(bear.getTerritory() != null);

        for (int i = 0; i < 19; i++) {
            p.simulate();
            if (bear.distanceTo(world, bear.getTerritory()) > 3) {
                counter++;
            }
        }

        System.out.println(counter);
        assertTrue(counter < 3);
    }

    /**
     * This test will check bear territories can be determined by the input file.
     * The file dictates the territory should be at (12,15)
     */
    @Test
    public void testSpawnBearWithTerritory() throws Exception{
        programRunner.create("./data/t2-6a.txt");
        world = programRunner.getWorld();
        id_generator = new IDGenerator();

        programRunner.runSimulation(1);

        Location actual_territory_location = null;
        for (Object object: world.getEntities().keySet()){
            if (object instanceof Bear bear){
                actual_territory_location = bear.getTerritory();
            }
        }

        Location expected_territory_location = new Location(12,15);

        int expected_x = expected_territory_location.getX();
        int actual_x = actual_territory_location.getX();
        int expected_y = expected_territory_location.getY();
        int actual_y = actual_territory_location.getY();

        assertEquals(expected_x, actual_x);
        assertEquals(expected_y, actual_y);
    }

    /**
     * Optional requirement for bear
     * Tests the wolves will kill the bear if the pack is large enough
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
        assertEquals(12, bear_carcass.getNutritionalValue(), "program should stop when the bear is killed");
        }
    }
