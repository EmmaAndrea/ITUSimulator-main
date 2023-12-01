package ourcode.Setup;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;

import java.io.File;

/**
 * A class that facilitates the creation and execution of simulations based on input files.
 */
public class ProgramRunner {
    // fields
    private Program p;

    private IDGenerator original_id_generator;

    private Grass grass;

    private Rabbit rabbit;

    private Burrow burrow;

    // constructor for ProgramRunner
    public ProgramRunner() {
        // constructor code
    }

    /**
     * Creates a simulation based on the information provided in an input file.
     * Adds actors to the simulation as dictated in the input file.
     * @throws Exception If an error occurs during the simulation setup or execution.
     */
    public void create(String file_name) throws Exception {
        // create IDGenerator
        original_id_generator = new IDGenerator();

        // create scanner
        // Scanner scanner = new Scanner(System.in);

        // get the file
        // later: String fileName = scanner.nextLine();

        File file = new File(file_name);

        // read file with input-reader
        InputReader inputReader = new InputReader(file.getAbsolutePath());

        // get world size
        int size = inputReader.readWorldSize();

        // standard
        int delay = 300; // the delay between every step of the simulation (in ms)
        int display_size = 800; // screen resolution (i px)

        //create world
        p = new Program(size, display_size, delay); // creates a new program
        World world = p.getWorld(); // pulls out the world where we can add things

        // Reads the input file.
        inputReader.readSpawns();

        // Spawns entities according to the input file.
        for (String type : inputReader.map_of_spawns.keySet()) {
            spawnEntity(world, type, inputReader.getAmount(type));
        }

        //spawnEntity(world, "rabbit", 20);
        //spawnEntity(world, "grass", 50);
    }

    /**
     * Spawns as many of the given entity as stated by amount.
     */
    public void spawnEntities(World world, int amount, EntityFactory factory) {
        for (int i = 0; i < amount; i++) {
            Entity entity = factory.create();
            entity.spawn(world);
        }
    }

    /**
     * Spawns an entity based on its type via a switch case.
     */
    public void spawnEntity(World world, String entityType, int amount) {
        switch (entityType) {
            case "rabbit":
                spawnEntities(world, amount, () -> new Rabbit(original_id_generator));
                break;
            case "grass":
                spawnEntities(world, amount, () -> new Grass(original_id_generator));
                break;
            case "burrow":
                spawnEntities(world, amount, () -> new Burrow(original_id_generator));
                break;
            default:
                System.out.println("Unknown entity type: " + entityType);
        }
    }


    /**
     * Runs the simulation for a specified number of steps.
     *
     * @param step_count The number of simulation steps to run.
     */

    public void runSimulation (int step_count){
        // show the simulation
        p.show();

        for (int i = 0; i < 29; i++) {
            p.simulate();
        }

        p.simulate();

        p.simulate();

        p.simulate();

        // run the simulation
        for (int i = 0; i < step_count; i++) {
            p.simulate();
        }
    }

    public World getWorld(){
        return p.getWorld();
    }

    public Object getObject(){
        World world = p.getWorld();
        Location location = world.getCurrentLocation();
        return world.getEntities().get(location);
    }

    public Entity getOrganism(){
        return original_id_generator.getEntity(p.getWorld().getCurrentLocation());
    }

    public IDGenerator getOriginal_id_generator() {
        return original_id_generator;
    }

    public Grass getGrass() {
        return grass;
    }

    public Rabbit getRabbit() {
        return rabbit;
    }

    public Burrow getBurrow() { return burrow; }
}