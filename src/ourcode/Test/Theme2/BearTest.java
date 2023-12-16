package ourcode.Test.Theme2;

import itumulator.executable.Program;
import itumulator.world.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Bear;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Dinosaur;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Wolf;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Organism.OrganismChildren.PlantChildren.Bush;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     * This test will demonstrate if a bear can be spawned through an input file.
     */
    @Test
    public void testBearInput() throws Exception {
        programRunner.create("./data/test-bear_2.txt");
        world = programRunner.getWorld();
        id_generator = new IDGenerator();
        programRunner.getP().setDelay(1000);

        programRunner.runSimulation(10);
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
        Program program = new Program(3, 500, 200);
        world = program.getWorld();
        id_generator = new IDGenerator();

        Bear male_bear = new Bear(id_generator, false);
        male_bear.setGender("Male");

        Bear female_bear = new Bear(id_generator, false);
        female_bear.setGender("Female");

        female_bear.spawn(world);
        male_bear.spawn(world);

        program.show();
        for (int i = 0; i < 40; i++) {
            program.simulate();
        }

        int counter = 0;

        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Bear) {
                counter++;
            }
        }

        assertTrue(counter > 2, "The bears should have mated.");
    }

    /**
     * This test will demonstrate a bears eating 'capabilities'. It will showcase if a bear can eat 'berries'
     */
    @Test
    public void testBearEatingBerries() {
        Program p = new Program(2, 800, 500);
        world = p.getWorld();
        id_generator = new IDGenerator();

        Bear bear = new Bear(id_generator, false);
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
     * This test will demonstrate a bears eating 'capabilities'. It will showcase if a bear can eat 'grass'
     */
    @Test
    public void testBearEatingGrass() throws Exception {
        Program p = new Program(2, 800, 500);
        world = p.getWorld();
        id_generator = new IDGenerator();

        Bear bear = new Bear(id_generator, false);
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
     * This test will demonstrate a bears eating 'capabilities'. It will showcase if a bear can eat 'carcass'
     */
    @Test
    public void testBearEatingCarcass() {
        Program p = new Program(2, 800, 500);
        world = p.getWorld();
        id_generator = new IDGenerator();

        Bear bear = new Bear(id_generator, false);
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
     * This test demonstrates a bears 'hunting' capabilities. For this test it will check if a bear can 'hunt' a rabbit
     * and then eat the rabbit.
     */
    @Test
    public void testBearHuntingRabbit() {
        Program p = new Program(2, 500, 800);
        world = p.getWorld();
        id_generator = new IDGenerator();

        Bear bear = new Bear(id_generator, false);
        Rabbit rabbit = new Rabbit(id_generator, false);

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
     * This test will check if a bears walks outside its territory. The test will fail if the bear walks outside the
     * territory too much
     */
    @Test
    public void testBearTerritory() {
        Program p = new Program(6, 800, 500);
        world = p.getWorld();
        id_generator = new IDGenerator();

        Bear bear = new Bear(id_generator, false);
        bear.spawn(world);

        int counter = 0;

        p.show();
        for (int i = 0; i < 40; i++) {
            p.simulate();
            if (bear.distanceTo(world, bear.getTerritory()) > 3) {
                counter++;
            }
        }

        System.out.println(counter);
        assertTrue(counter <= 7);
    }

    @Test
    public void testBearEatingRabbit() {
        Program p = new Program(2, 500, 800);
        world = p.getWorld();
        id_generator = new IDGenerator();

        Bear bear = new Bear(id_generator, false);
        Rabbit rabbit = new Rabbit(id_generator, false);

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

    @Test
    public void testBearEatingWolf() {
        Program p = new Program(2, 500, 800);
        world = p.getWorld();
        id_generator = new IDGenerator();

        Bear bear = new Bear(id_generator, false);
        Wolf wolf = new Wolf(id_generator, false);

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

    @Test
    public void testBearEatingDinosaur() {
        Program p = new Program(2, 500, 800);
        world = p.getWorld();
        id_generator = new IDGenerator();

        Bear bear = new Bear(id_generator, false);
        Dinosaur dino = new Dinosaur(id_generator, false);

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
        System.out.println("the dinosaur was hit for " + dino.getDamageTaken() + " damage");
        System.out.println(bear.getHunger());
        System.out.println("in hiding: " + bear.isHiding());
        assertTrue(hasEaten);
    }
}
