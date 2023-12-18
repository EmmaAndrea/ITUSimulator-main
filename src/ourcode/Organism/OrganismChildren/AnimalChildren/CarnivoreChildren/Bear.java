package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import ourcode.Setup.IDGenerator;

import java.awt.*;

/**
 * Represents a Bear in the simulation environment.
 * Bears are territorial predators with specific behaviors such as a defined sleep cycle,
 * territorial dominance, and unique interactions based on their status in the environment.
 * This class extends from TerritorialPredator and implements DynamicDisplayInformationProvider
 * for graphical representation.
 */
public class Bear extends TerritorialPredator implements DynamicDisplayInformationProvider {

    /**
     * Constructs a Bear with specific characteristics, including type, maximum age, and sleep cycle.
     * Initializes the bear's attributes and sets up its basic behaviors.
     *
     * @param idGenerator The IDGenerator instance providing the unique identifier for the bear.
     * @param has_cordyceps A boolean indicating if the bear is born with cordyceps infection.
     */
    public Bear(IDGenerator idGenerator, Boolean has_cordyceps) {
        super(idGenerator, has_cordyceps);
        type = "bear";
        max_age = 190;
        bedtime = 12;
        wakeup = 18;
    }

    /**
     * Provides the display information for the bear. Changes the appearance of the bear based on its age,
     * sleeping status, and whether it is infected with cordyceps.
     *
     * @return DisplayInformation containing the color and icon representation of the bear.
     */
    @Override
    public DisplayInformation getInformation() {
        if (!has_cordyceps) {
            if (age >= 11) {
                if (is_hiding) {
                    return new DisplayInformation(Color.yellow, "bear-large-sleeping");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.yellow, "bear-large-wounded");
                } else {
                    return new DisplayInformation(Color.yellow, "bear-large");
                }
            } else {
                if (is_hiding) {
                    return new DisplayInformation(Color.yellow, "bear-small-sleeping");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.yellow, "bear-small-wounded");
                } else {
                    return new DisplayInformation(Color.yellow, "bear-small");
                }
            }
        } else {
            if (age >= 11) {
                if (is_hiding) {
                    return new DisplayInformation(Color.yellow, "bear-large-sleeping-cordyceps");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.yellow, "bear-large-wounded-cordyceps");
                } else {
                    return new DisplayInformation(Color.yellow, "bear-large-cordyceps");
                }
            } else {
                if (is_hiding) {
                    return new DisplayInformation(Color.yellow, "bear-small-sleeping-cordyceps");
                } else if (damage_taken > 0) {
                    return new DisplayInformation(Color.yellow, "bear-small-wounded-cordyceps");
                } else {
                    return new DisplayInformation(Color.yellow, "bear-small-cordyceps");
                }
            }
        }
    }
}
