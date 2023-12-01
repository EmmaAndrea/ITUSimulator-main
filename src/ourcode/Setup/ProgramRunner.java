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
 * This class is responsible for initializing the simulation environment, spawning entities,
 * and controlling the simulation flow based on user-defined parameters and input data.
 */
public class ProgramRunner {
    // fields
    private Program p;

    private IDGenerator original_id_generator;

    // constructor for ProgramRunner
    public ProgramRunner() {
        // constructor code
    }

    /**
     * Creates and initializes a simulation based on the specified input file.
     * Reads the file to set up the simulation environment and spawns entities as dictated by the file.
     *
     * @param file_name The name of the file containing simulation setup information.
     * @throws Exception If an error occurs during simulation setup or file reading.
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
     * Spawns a specified number of entities in the world as per the given factory method.
     *
     * @param world The simulation world where entities are to be spawned.
     * @param amount The number of entities to spawn.
     * @param factory A factory method to create instances of the entity.
     */
    public void spawnEntities(World world, int amount, EntityFactory factory) {
        for (int i = 0; i < amount; i++) {
            Entity entity = factory.create();
            entity.spawn(world);
        }
    }

    /**
     * Spawns an entity based on its type via a switch case. This method determines the type of entity to be
     * spawned and calls the appropriate factory method to create and spawn the specified number of entities in the world.
     *
     * @param world The simulation world where the entity will be spawned.
     * @param entityType The type of entity to spawn (e.g., "rabbit", "grass", "burrow").
     * @param amount The number of entities of the specified type to spawn.
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
     * Runs the simulation for a specified number of steps. This involves executing a series of simulation cycles,
     * where each cycle advances the state of the simulation by one step.
     *
     * @param step_count The number of steps to run the simulation for.
     */
    public void runSimulation (int step_count){
        // show the simulation
        p.show();

        for (int i = 0; i < step_count; i++) {
            p.simulate();
        }
    }

    /**
     * Retrieves the current state of the simulation world. This method allows access to the world object,
     * which contains information about all entities and their locations within the simulation.
     *
     * @return The current simulation world object.
     */
    public World getWorld(){
        return p.getWorld();
    }

    /**
     * Retrieves the object located at the current position in the simulation world. This can be used to inspect
     * or interact with entities at a specific location.
     *
     * @return The object present at the current location in the simulation world, if any.
     */
    public Object getObject(){
        World world = p.getWorld();
        Location location = world.getCurrentLocation();
        return world.getEntities().get(location);
    }

    /**
     * Retrieves the specific Organism entity located at the current position in the simulation world. This is
     * particularly useful for scenarios where interactions or observations of Organism entities are required.
     *
     * @return The Organism entity at the current location, if present.
     */
    public Entity getOrganism(){
        return original_id_generator.getEntity(p.getWorld().getCurrentLocation());
    }

    /**
     * Provides access to the IDGenerator instance used by this ProgramRunner. This is essential for generating
     * unique identifiers for new entities within the simulation.
     *
     * @return The IDGenerator instance used by this ProgramRunner.
     */
    public IDGenerator getOriginal_id_generator() {
        return original_id_generator;
    }
}