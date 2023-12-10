package ourcode.Test.Theme3;

import itumulator.world.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Organism.OrganismChildren.Fungus;
import ourcode.Setup.IDGenerator;
import ourcode.Setup.ProgramRunner;

public class FungusTest {
    public ProgramRunner programRunner;
    public World world;

    public FungusTest() {
        programRunner = new ProgramRunner();
    }

    @BeforeAll
    public static void setUp() {
        System.out.println("Testing for Fungus");
    }

    @BeforeEach
    public void startTest() {
        System.out.println("Test started");
    }

    @Test
    public void testFungusDeclaration() {
        world = new World(3);
        IDGenerator idGenerator = new IDGenerator();

        Fungus fungus = new Fungus(idGenerator);
        fungus.spawn(world);
        System.out.println(world.getEntities());

        Assertions.assertSame(world.getEntities().size(), 1,
                "The amount of enities should be 1, but is: " + world.getEntities().size());
    }

    @Test
    public void testFungusSpread() {

        world = new World(4);
        IDGenerator idGenerator = new IDGenerator();

        Carcass carcass = new Carcass(idGenerator, 3, "bear", false);
        carcass.spawn(world);

        Fungus fungus = new Fungus(idGenerator);
        fungus.spawn(world);

        fungus.act(world);

        boolean state = carcass.getFungus().isInCarcass();

        System.out.println(state);
    }

    @Test
    public void testFungusSurrounding() {
        world = new World(3);
        IDGenerator idGenerator = new IDGenerator();

        Carcass carcass = new Carcass(idGenerator,3,"bear",false);
        carcass.spawn(world);

        Fungus fungus = new Fungus(idGenerator);
        fungus.spawn(world);

        fungus.checkSurroundingCarcass(world);
        if (fungus.checkSurroundingCarcass(world)) {
            System.out.println("true");
        }

    }
}