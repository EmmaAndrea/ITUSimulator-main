package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a Wolf in the simulation environment.
 * Wolves are social predators that exhibit pack behavior and have specific characteristics
 * such as a defined sleep cycle, pack hunting, and alpha status within the pack.
 * This class extends from SocialPredator and implements DynamicDisplayInformationProvider
 * for graphical representation.
 */
public class Wolf extends SocialPredator implements DynamicDisplayInformationProvider {

    /**
     * Constructs a Wolf with specific characteristics, including type, maximum age, and sleep cycle.
     * Initializes the wolf's attributes and sets up its basic behaviors, especially related to pack dynamics.
     *
     * @param idGenerator The IDGenerator instance providing the unique identifier for the wolf.
     * @param has_cordyceps A boolean indicating if the wolf is born with cordyceps infection.
     */
    public Wolf(IDGenerator idGenerator, Boolean has_cordyceps) {
        super(idGenerator, has_cordyceps);
        type = "wolf";
        max_age = 131;
        bedtime = 1;
        wakeup = 7;
    }

    /**
     * Determines the graphic of the wolf based on its current condition and age.
     * @return Returns the graphics information for the wolf.
     */
    @Override
    public DisplayInformation getInformation() {
        if (!has_cordyceps) {
            if (age >= 12) {
                if (is_sleeping) {
                    return new DisplayInformation(Color.cyan, "wolf-large-sleeping");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.cyan, "wolf-large-wounded");
                } else {
                    return new DisplayInformation(Color.cyan, "wolf-large");
                }
            } else {
                if (is_sleeping) {
                    return new DisplayInformation(Color.cyan, "wolf-small-sleeping");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.cyan, "wolf-small-wounded");
                } else {
                    return new DisplayInformation(Color.cyan, "wolf-small");
                }
            }
        } else {
            if (age >= 12) {
                if (is_sleeping) {
                    return new DisplayInformation(Color.cyan, "wolf-large-sleeping-cordyceps");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.cyan, "wolf-large-wounded-cordyceps");
                } else {
                    return new DisplayInformation(Color.cyan, "wolf-large-cordyceps");
                }
            } else {
                if (is_sleeping) {
                    return new DisplayInformation(Color.cyan, "wolf-small-sleeping-cordyceps");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.cyan, "wolf-small-wounded-cordyceps");
                } else {
                    return new DisplayInformation(Color.cyan, "wolf-small-cordyceps");
                }
            }
        }
    }
}
