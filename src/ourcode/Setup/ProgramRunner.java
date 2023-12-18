package ourcode.Setup;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Burrow;
import ourcode.Organism.DinosaurEgg;
import ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren.*;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rodent;
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
    private Program p;
    private IDGenerator id_generator;
    private Grass grass;
    private Rodent rabbit;
    private Burrow burrow;
    private InputReader input_reader;
    private SocialPredator alpha;
    private int pack_number;

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

        // create world
        p = new Program(size, 1000, 800); // creates a new program
        World world = p.getWorld(); // pulls out the world where we can add things

        // Reads the input file.
        input_reader.readSpawns();

        // Spawns entities according to the input file.
        for (String type : input_reader.map_of_spawns.keySet()) {
            spawnEntity(world, type, input_reader.getAmount(type));
        }

        alpha = new Wolf(id_generator, false);
        pack_number = 0;
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

            if (entity instanceof Bear) {
                setBearTerritory(entity, i + 1);
            }
            if (entity instanceof SocialPredator) {
                setPack(entity, i);
            }
        }
    }

    /**
     * find the correct bear territory and assign it to the bear which has just been spawned
     * @param entity
     * @param i
     */
    public void setBearTerritory(Entity entity, int i) {
        String beartype = "bear" + i;
        if (input_reader.getMap_of_bear_territories().containsKey(beartype)) {
            Bear bear = (Bear) entity;
            bear.setTerritoryLocation(input_reader.getTerritory(beartype));
        }
    }

    /**
     * If the wolf is the first of its pack to be spawned, it becomes the alpha
     * Else find the correct wolf pack and assign it to the wolf which has just been spawned
     * @param entity
     * @param i
     */
    public void setPack(Entity entity, int i) {
        int packsize = 0;

        SocialPredator predator = (SocialPredator) entity;
        if (input_reader.getMap_of_social_predator_packs().size() == 1) {
            if (i == 0) {
                predator.createPack();
                alpha = predator;
            }
            if (i > 0) {
                alpha.addToPack(predator);
            }
        } else if (input_reader.getMap_of_social_predator_packs().size() > 1) {
            packsize = input_reader.getMap_of_social_predator_packs().get(pack_number);

            if (i != 0 && i % packsize == 0) pack_number++;

            packsize = input_reader.getMap_of_social_predator_packs().get(pack_number);

            if (i % packsize == 0){
                predator.createPack();
                alpha = predator;
            } else {
                alpha.addToPack(predator);
            }
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
     * Runs the simulation for a specified number of steps. This involves executing a series of simulation cycles,
     * where each cycle advances the state of the simulation by one step.
     *
     * @param step_count The number of steps to run the simulation for.
     */
    public void runSimulation (int step_count) {
        // show the simulation
        p.show();

        for (int i = 0; i < step_count; i++) {
            p.simulate();
        }
        // for up to step count
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

    public Program getP() {
        return p;
    }

    /**
     * Retrieves the specific Organism entity located at the current position in the simulation world. This is
     * particularly useful for scenarios where interactions or observations of Organism entities are required.
     *
     * @return The Organism entity at the current location, if present.
     */
    public Entity getOrganism(){
        return id_generator.getEntity(p.getWorld().getCurrentLocation());
    }

    /**
     * Provides access to the IDGenerator instance used by this ProgramRunner. This is essential for generating
     * unique identifiers for new entities within the simulation.
     *
     * @return The IDGenerator instance used by this ProgramRunner.
     */
    public IDGenerator getOriginal_id_generator() {
        return id_generator;
    }

    public Grass getGrass() {
        return grass;
    }

    public Rodent getRabbit() {
        return rabbit;
    }

    public void simulate() {
        // show the simulation
        p.show();
        p.simulate();
    }
}