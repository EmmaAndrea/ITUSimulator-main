package ourcode.Setup;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.DinosaurEgg;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.*;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.Carcass;
import ourcode.Organism.OrganismChildren.Fungus;
import ourcode.Organism.OrganismChildren.PlantChildren.Bush;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;

import java.io.File;

/**
 * A class that facilitates the creation and execution of simulations based on input files.
 * This class is responsible for initializing the simulation environment, spawning entities,
 * and controlling the simulation flow based on user-defined parameters and input data.
 */
public class ProgramRunner {
    private Program program; // The Program instance for running the simulation.
    private IDGenerator id_generator; // IDGenerator for creating unique IDs for entities.
    private InputReader input_reader; // InputReader for reading and processing simulation setup files.
    private SocialPredator alpha; // Alpha wolf entity for wolf pack simulations.
    private int pack_number; // Counter to keep track of wolf packs.
    private int wolf_number; // Counter to keep track of individual wolves.
    private int wolf_place_in_pack; // Counter to keep track of a wolf's place within its pack.
    private int pack_size; // Size of the wolf pack.

    /**
     * Creates and initializes a simulation based on the specified input file.
     * Reads the file to set up the simulation environment and spawns entities as dictated by the file.
     *
     * @param file_name The name of the file containing simulation setup information.
     * @throws Exception If an error occurs during simulation setup or file reading.
     */
    public void create(String file_name) throws Exception {
        // create IDGenerator
        id_generator = new IDGenerator();

        File file = new File(file_name);

        // read file with input-reader
        input_reader = new InputReader(file.getAbsolutePath());

        int size = input_reader.readWorldSize();

        wolf_number = 0;
        wolf_place_in_pack = 0;
        pack_number = 0;
        pack_size = 0;

        // create world
        program = new Program(size, 1000, 800); // creates a new program
        World world = program.getWorld(); // pulls out the world where we can add things

        // Reads the input file.
        input_reader.readSpawns();

        // Spawns entities according to the input file.
        for (String type : input_reader.map_of_spawns.keySet()) {
            spawnEntity(world, type, input_reader.getAmount(type));
        }

        alpha = new Wolf(id_generator, false);
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

            if (entity instanceof TerritorialPredator) {
                setBearTerritory(entity, i + 1);
            }
            if (entity instanceof SocialPredator) {
                setPack(entity);
            }
        }
    }

    /**
     * Assigns the correct territory to a bear entity based on its order of creation.
     * Each bear is assigned a unique territory if specified in the input file.
     *
     * @param entity The bear entity to set the territory for.
     * @param i      The index of the bear entity, used to determine its territory.
     */
    public void setBearTerritory(Entity entity, int i) {
        String beartype = "bear" + i;
        if (input_reader.getMapOfBearTerritories().containsKey(beartype)) {
            TerritorialPredator bear = (TerritorialPredator) entity;
            bear.setTerritoryLocation(input_reader.getTerritory(beartype));
        }
    }

    /**
     * Assigns a wolf entity to a pack. If it's the first wolf in a pack, it becomes the alpha.
     * Subsequent wolves are added to the pack. Handles multiple packs if specified.
     *
     * @param entity The wolf entity to assign to a pack.
     */
    public void setPack(Entity entity) {

        SocialPredator predator = (Wolf) entity;

        if (input_reader.getMap_of_social_predator_packs().size() == 1) {
            if (wolf_number == 0) {
                predator.createPack();
                alpha = predator;
            }
            else {
                alpha.addToPack(predator);
            }
            wolf_number++;
        } else if (input_reader.getMap_of_social_predator_packs().size() > 1) {
            wolf_place_in_pack++;

                pack_size = input_reader.getMap_of_social_predator_packs().get(pack_number);

                if (wolf_place_in_pack > pack_size) {
                    wolf_place_in_pack = 1;
                    pack_number++;
                }

                if (wolf_place_in_pack == 1) {
                    predator.createPack();
                    alpha = predator;
                } else {
                    alpha.addToPack(predator);
                }
        }
    }

    /**
     * Spawns entities of a given type in the simulation world. The method determines the entity type,
     * applies any special conditions like cordyceps or fungi, and uses the appropriate factory method
     * to create and spawn entities.
     *
     * @param world       The simulation world where the entity will be spawned.
     * @param entityType  The type of entity to spawn (e.g., "rabbit", "grass", "burrow").
     * @param amount      The number of entities of the specified type to spawn.
     */
    public void spawnEntity(World world, String entityType, int amount) {
        boolean hasCordyceps = entityType.toLowerCase().startsWith("cordyceps ");
        String actualType = hasCordyceps ? entityType.substring(10) : entityType;
        boolean hasFungus;

        if (entityType.equalsIgnoreCase("carcass fungi")) {
            hasFungus = true;
            actualType = "carcass";
        } else {
            hasFungus = entityType.toLowerCase().contains("fungi");
        }

        switch (actualType) {
            case "grass":
                spawnEntities(world, amount, () -> new Grass(id_generator));
                break;
            case "berry":
                spawnEntities(world, amount, () -> new Bush(id_generator));
                break;
            case "burrow":
                spawnEntities(world, amount, () -> new Burrow(id_generator));
                break;
            case "rabbit":
                spawnEntities(world, amount, () -> new Rabbit(id_generator, hasCordyceps));
                break;
            case "wolf":
                spawnEntities(world, amount, () -> new Wolf(id_generator, hasCordyceps));
                break;
            case "bear":
                spawnEntities(world, amount, () -> new Bear(id_generator, hasCordyceps));
                break;
            case "carcass":
                spawnEntities(world, amount, () -> new Carcass(id_generator, 4, "carcass", hasFungus));
                break;
            case "fungi":
                spawnEntities(world, amount, () -> new Fungus(id_generator));
                break;
            case "dinosaur":
                spawnEntities(world, amount, () -> new TyrannosaurusRex(id_generator, false)); // Assuming Dinosaur is a class you have
                break;
            case "dinosaur egg":
                spawnEntities(world, amount, () -> new DinosaurEgg(id_generator, false)); // Assuming DinosaurEgg is a class you have
                break;
            // Include other cases as needed based on your entity types
            default:
                System.out.println("Unknown entity type: " + entityType);
        }
    }


    /**
     * Runs the simulation for a specified number of steps, advancing the state of the simulation
     * with each step.
     *
     * @param step_count The number of steps to run the simulation for.
     */
    public void runSimulation (int step_count) {
        // show the simulation
        program.show();

        for (int i = 0; i < step_count; i++) {
            program.simulate();
        }
        // for up to step count
    }

    /**
     * Retrieves the current state of the simulation world, providing information about all entities
     * and their locations within the simulation.
     *
     * @return The current simulation world object.
     */
    public World getWorld(){
        return program.getWorld();
    }

    /**
     * Retrieves the object located at the current position in the simulation world,
     * allowing for inspection or interaction with entities at that location.
     *
     * @return The object present at the current location in the simulation world, if any.
     */
    public Object getObject(){
        World world = program.getWorld();
        Location location = world.getCurrentLocation();
        return world.getEntities().get(location);
    }

    /**
     * Retrieves the Program instance used by this ProgramRunner, allowing access to
     * simulation controls and settings.
     *
     * @return The Program instance used by this ProgramRunner.
     */
    public Program getProgram() {
        return program;
    }

    /**
     * Retrieves a specific Organism entity located at the current position in the simulation world,
     * useful for interactions or observations involving Organism entities.
     *
     * @return The Organism entity at the current location, if present.
     */
    public Entity getOrganism(){
        return id_generator.getEntity(program.getWorld().getCurrentLocation());
    }

    /**
     * Provides access to the IDGenerator instance used by this ProgramRunner,
     * essential for generating unique identifiers for new entities in the simulation.
     *
     * @return The IDGenerator instance used by this ProgramRunner.
     */
    public IDGenerator getOriginal_id_generator() {
        return id_generator;
    }

    /**
     * Simulates a single step in the simulation, advancing the state of the world.
     */
    public void simulate() {
        // show the simulation
        program.show();
        program.simulate();
    }
}