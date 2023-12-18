package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class Wolf extends SocialPredator {

    public Wolf(IDGenerator idGenerator, Boolean has_cordyceps){
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
