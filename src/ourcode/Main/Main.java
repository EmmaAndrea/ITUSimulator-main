package ourcode.Main;

import ourcode.Setup.ProgramRunner;

/**
 * Main class for the simulation program.
 * This class serves as the entry point for running the simulation, initializing the simulation environment,
 * and starting the simulation process.
 */
public class Main {

    /**
     * The main method that starts the simulation. It creates an instance of ProgramRunner,
     * sets up the simulation based on the specified input file, and runs the simulation for a set number of steps.
     *
     * @param args Command line arguments, not used in this program.
     * @throws Exception If there are any issues in setting up or running the simulation.
     */
    public static void main(String[] args) throws Exception {
        // Sets up the simulation environment based on the contents of the input file.
        ProgramRunner programRunner = new ProgramRunner();
        programRunner.create("./data/tf4-2a.txt");

        // Runs the simulation for a specified number of steps.
        programRunner.runSimulation(200);
    }
}