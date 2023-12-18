package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import ourcode.Setup.IDGenerator;

import java.awt.*;

public class Bear extends TerritorialPredator{

    public Bear(IDGenerator idGenerator, Boolean has_cordyceps){
        super(idGenerator, has_cordyceps);
        type = "bear";
        max_age = 190;
        bedtime = 12;
        wakeup = 18;
    }

    /**
     * Graphics for old, young and wounded bear.
     * @return the display information for the bear in its current state.
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
