package ourcode.Setup;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;
import itumulator.world.Location;

import ourcode.Organism.Organism;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;

import java.awt.*;
import java.io.File;

/**
 * A class that facilitates the creation and execution of simulations based on input files.
 */
public class ProgramRunner {
    // fields
    private Program p;

    private IDGenerator original_id_generator;

    private Grass grass;
    // constructor
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
        int delay = 1000; // the delay between every step of the simulation (in ms)
        int display_size = 800; // screen resolution (i px)

        //create world
        p = new Program(size, display_size, delay); // creates a new program
        World world = p.getWorld(); // pulls out the world where we can add things

        //design world
        DisplayInformation rabbit_color = new DisplayInformation(Color.black);
        p.setDisplayInformation(Rabbit.class, rabbit_color);
        DisplayInformation grass_color = new DisplayInformation(Color.green);
        p.setDisplayInformation(Grass.class, grass_color);

        // run inputReader
        inputReader.readSpawns(); // interprets the input file

        // fix this part with a switch case (when iterating over spawn_map
        // spawns rabbits

        int amountOfRabbits = inputReader.getAmount("rabbit");

        for (int i = 0; i < amountOfRabbits; i++) {
            int id = original_id_generator.getID();
            Rabbit rabbit = new Rabbit(original_id_generator);
            rabbit.spawn(world);
        }

        // spawns grass
        int amountOfGrass = inputReader.getAmount("grass");
        for (int i = 0; i < amountOfGrass; i++) {
            int id = original_id_generator.getID();
            grass = new Grass(original_id_generator);
            grass.spawn(world);
            world.setCurrentLocation(world.getLocation(grass));
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

    public Organism getOrganism(){
        return original_id_generator.getOrganism(p.getWorld().getCurrentLocation());
    }

    public IDGenerator getOriginal_id_generator(){
        return original_id_generator;
    }

    public Grass getGrass(){
        return grass;
    }
}

