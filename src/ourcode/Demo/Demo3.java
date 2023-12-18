package ourcode.Demo;

import ourcode.Setup.ProgramRunner;

public class Demo3 {
    public static void main(String[] args) throws Exception {
        // Sets up the simulation environment based on the contents of the input file.
        ProgramRunner programRunner = new ProgramRunner();
        programRunner.create("./data/demo-test-3.txt");

        // Runs the simulation for a specified number of steps.
        programRunner.runSimulation(200);
    }
}