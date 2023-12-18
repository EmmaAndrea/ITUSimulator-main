package ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren;

import itumulator.executable.DisplayInformation;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a rabbit in the simulation environment.
 * This class extends the 'Rodent' class, tailoring specific attributes and behaviors to represent a rabbit.
 * Rabbits are characterized by their age, sleep cycle, and potential infection status.
 */
public class Rabbit extends Rodent{

    /**
     * Constructs a new Rabbit with a unique identifier and cordyceps infection status.
     * Initializes specific properties of the rabbit, such as type, maximum age, bedtime, and wakeup time.
     *
     * @param original_id_generator The IDGenerator instance that provides the unique identifier for the rabbit.
     * @param has_cordyceps         Boolean indicating if the rabbit is infected with cordyceps.
     */
    public Rabbit(IDGenerator original_id_generator, Boolean has_cordyceps){
        super(original_id_generator, has_cordyceps);
        type = "rabbit";
        max_age = 100;  // Defines the lifespan of the rabbit in the simulation.
        bedtime = 9;    // The simulation step when the rabbit typically goes to sleep.
        wakeup = 19;    // The simulation step when the rabbit typically wakes up.
    }

    /**
     * Provides the visual representation of the rabbit in the simulation. Changes appearance based on the rabbit's age.
     *
     * @return DisplayInformation containing the color and icon representation of the rabbit.
     */
    @Override
    public DisplayInformation getInformation() {
        if (!has_cordyceps) {
            if (age >= 20) {
                return new DisplayInformation(Color.black, "rabbit-large");
            } else {
                return new DisplayInformation(Color.black, "rabbit-small");
            }
        } else {
            if (age >= 20) {
                return new DisplayInformation(Color.black, "rabbit-large-cordyceps");
            } else {
                return new DisplayInformation(Color.black, "rabbit-small-cordyceps");
            }
        }
    }
}
