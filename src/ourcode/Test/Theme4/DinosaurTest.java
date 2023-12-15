package ourcode.Test.Theme4;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Obstacles.Fossil;
import ourcode.Organism.DinosaurEgg;
import ourcode.Organism.Footprint;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Bear;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.Dinosaur;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DinosaurTest {
    public ProgramRunner programRunner;
    public World world;

    public DinosaurTest() {
        programRunner = new ProgramRunner();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for dinosaur.");
    }

    @BeforeEach
    public void startTest() throws Exception {
        System.out.println("*** Test started ***");
    }

    /**
     * Testing that dinosaurs spawn in the correct amount.
     * @throws Exception skibob
     */
    @Test
    public void testDinosaurSpawn() throws Exception {
        programRunner.create("data/dinosaur-test-1.txt");
        world = programRunner.getWorld();
        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Dinosaur) {
                counter++;
            }
        }

        assertEquals(2, counter, "There should be 2 dinosaurs.");
    }

    /**
     * Testing that T. rex eat each other.
     * To date, only two other predatory dinosaurs—Tyrannosaurus and Majungasaurus—
     * have been shown to feed on the carcasses of their own species.
     * @throws Exception skibob
     */
    @Test
    public void testDinosaursEatEachOther() throws Exception {
        programRunner.create("data/dinosaur-test-1.txt");
        world = programRunner.getWorld();
        programRunner.runSimulation(27);
        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Dinosaur) {
                counter++;
            }
        }

        assertEquals(1, counter, "One of the dinosaurs should've eaten the other.");
    }

    /**
     * Testing that dinosaurs lay eggs as eggspected.
     */
    @Test
    public void testDinosaurLayEgg() {
        Program program = new Program(3, 500, 200);
        world = program.getWorld();
        IDGenerator id_generator = new IDGenerator();

        Dinosaur male_rex = new Dinosaur(id_generator, false);
        male_rex.setGender("Male");
        Dinosaur female_rex = new Dinosaur(id_generator, false);
        female_rex.setGender("Female");

        male_rex.spawn(world);
        female_rex.spawn(world);

        for (int i = 0; i < 50; i++) {
            program.simulate();
        }

        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof DinosaurEgg) {
                counter++;
            }
        }

        assertTrue(counter > 0, "The dinosaurs should have laid an egg.");
    }

    /**
     * Testing that eggs hatch into dinosaurs as expected.
     */
    @Test
    public void testEggHatch() {
        Program program = new Program(2, 500, 200);
        world = program.getWorld();
        IDGenerator id_generator = new IDGenerator();

        DinosaurEgg egg = new DinosaurEgg(id_generator, false);
        egg.spawn(world);

        for (int i = 0; i < 29; i++) {
            program.simulate();
        }

        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Dinosaur) {
                counter++;
            }
        }

        assertEquals(1, counter, "The egg should have become a dinosaur.");
    }

    /**
     * Testing that infected eggs hatch as infected dinosaur.
     */
    @Test
    public void testInfectedEggHatch() {
        Program program = new Program(2, 500, 200);
        world = program.getWorld();
        IDGenerator id_generator = new IDGenerator();

        DinosaurEgg egg = new DinosaurEgg(id_generator, true);
        egg.spawn(world);

        for (int i = 0; i < 29; i++) {
            program.simulate();
        }

        boolean infected = false;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Dinosaur dinosaur) {
                infected = dinosaur.hasCordyceps();
            }
        }

        assertTrue(infected, "The infected egg should have become an infected dinosaur.");
    }

    /**
     * Testing that a dinosaur leaves footprints as expected.
     */
    @Test
    public void testDinosaurFootprint() {
        Program program = new Program(2, 500, 200);
        world = program.getWorld();
        IDGenerator id_generator = new IDGenerator();

        Dinosaur dino = new Dinosaur(id_generator, false);
        dino.spawn(world);
        Location previous_location = world.getLocation(dino);
        program.simulate();

        boolean correct_footstep = world.getTile(previous_location) instanceof Footprint;

        assertTrue(correct_footstep, "The dinosaur should have left a footprint.");
    }

    /**
     * Testing that male dinosaur does not lay egg.
     */
    @Test
    public void testMaleEggLayingFalse() {
        Program program = new Program(2, 500, 200);
        world = program.getWorld();
        IDGenerator id_generator = new IDGenerator();

        Dinosaur dino = new Dinosaur(id_generator, false);
        dino.setGender("Male");
        dino.spawn(world);

        for (int i = 0; i < 48; i++) {
            program.simulate();
        }

        int counter = 0;
        for (Object o : world.getEntities().keySet()) {
            if (o instanceof DinosaurEgg) {
                counter++;
            }
        }

        assertEquals(0, counter, "The male should not have laid an agg.");
    }

    @Test
    public void testDinosaurTrophicLevel() {
        Program p = new Program(3,500,800);
        world = p.getWorld();
        IDGenerator idGenerator = new IDGenerator();

        Bear bear = new Bear(idGenerator, false);

        for (int i = 0; i < 20; i++) {
            p.simulate();
        }

        Dinosaur dino = new Dinosaur(idGenerator, false);

        bear.spawn(world);
        dino.spawn(world);

        p.show();
        for (int i = 0; i < 30; i++) {
            p.simulate();
            System.out.println(dino.getTrophicLevel());
        }

        int counter = 0;

        for (Object o : world.getEntities().keySet()) {
            if (o instanceof Dinosaur) {
                counter++;
            }
        }

        assertEquals(0, counter, "Dinosaur should have been eaten by bear.");
    }

    /**
     * Testing the process of dinosaur extinction.
     * @throws Exception skibob
     */
    @Test
    public void testDinosaurGoesExtinct() throws Exception {
        programRunner.create("data/dinosaur-test-meteor.txt");
        world = programRunner.getWorld();

        boolean fossilExists = false;

        for (int i = 0; i < 200; i++) {
            programRunner.simulate();

            for (Object o : world.getEntities().keySet()) {
                if (o instanceof Fossil) {
                    fossilExists = true;
                    break;
                }
            }
        }

        assertTrue(fossilExists, "There should (probably) be a fossil.");
    }

    /**
     * Testing that a meteor will turn into a fossil
     * @throws Exception skibob
     */
    @Test
    public void testMeteorBecomesFossil() throws Exception {
        programRunner.create("data/test-meteor.txt");
        world = programRunner.getWorld();

        boolean fossilExists = false;

        for (int i = 0; i < 6; i++) {
            programRunner.simulate();

            for (Object o : world.getEntities().keySet()) {
                if (o instanceof Dinosaur dino) {
                    dino.goExtinct(world);
                }
            }

            for (Object o : world.getEntities().keySet()) {
                if (o instanceof Fossil) {
                    fossilExists = true;
                    break;
                }
            }
        }

        assertTrue(fossilExists, "A meteor should turn into a fossil after 4 steps.");
    }
}