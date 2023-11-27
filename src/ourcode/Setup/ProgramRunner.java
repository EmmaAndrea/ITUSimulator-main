package ourcode.Setup;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.World;

import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;

import java.awt.*;
import java.io.File;

/**
 * A class that facilitates the creation and execution of simulations based on input files.
 */
public class ProgramRunner {
    // fields
    Program p;

    // constructor
    public ProgramRunner() {
        // constructor code
    }

    /**
     * Creates a simulation based on the information provided in an input file.
     * Adds actors to the simulation as dictated in the input file.
     * @throws Exception If an error occurs during the simulation setup or execution.
     */
    public void create() throws Exception {
        // create IDGenerator
        IDGenerator original_id_generator = new IDGenerator();

        // create scanner
        // Scanner scanner = new Scanner(System.in);

        // get the file
        // later: String fileName = scanner.nextLine();
        String fileName = "./data/t1-2cde.txt";
        File file = new File(fileName);

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
            Grass grass = new Grass(original_id_generator);
            grass.spawn(world);
        }

        // displays world
        p.show();

        System.out.println("");
        // run the simulation
        for (int j = 0; j < 200; j++) {
            p.simulate();
        }
    }

    /**
     * Runs the simulation for a specified number of steps.
     *
     * @param step_count The number of simulation steps to run.
     */
    /*
    public void runSimulation ( int step_count){
        // show the simulation
        p.show();

        // run the simulation
        for (int i = 0; i < 200; i++) {
            p.simulate();
        }


    }

     */
}

