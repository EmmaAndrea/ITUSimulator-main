package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import ourcode.Organism.OrganismChildren.AnimalChildren.Carnivore;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Wolf entity in the simulated world, extending the Carnivore class.
 * Wolves have unique behaviors such as forming packs, having an alpha wolf,
 * and different interactions based on their pack status and trophic level.
 */
public class Wolf extends Carnivore implements DynamicDisplayInformationProvider {

    protected ArrayList<Wolf> pack; // The pack of wolves to which this wolf belongs.
    public boolean has_pack; // Indicates whether this wolf is part of a pack.

    private Wolf my_alpha; // The alpha wolf of the pack.
    private boolean alpha; // Indicates whether this wolf is the alpha of a pack.

    /**
     * Constructs a Wolf with specific characteristics and initializes its pack-related properties.
     *
     * @param idGenerator The IDGenerator instance providing the unique identifier for the wolf.
     */
    public Wolf(IDGenerator idGenerator) {
        super(idGenerator);
        type = "wolf";
        trophic_level = 3;
        max_age = 130;
        max_hunger = 21;
        has_pack = false;
        consumable_foods = new ArrayList<>(List.of("rabbit", "bear", "wolf"));
        alpha = false;
    }

    /**
     * Defines the behavior of a wolf in each simulation step. This includes pack behavior,
     * nighttime activities such as sleeping, and movement during the day.
     *
     * @param world The simulation world in which the wolf exists.
     */
    @Override
    public void carnivoreAct(World world) {
        super.carnivoreAct(world);
        if (pack != null) System.out.println("My pack: " + pack.size());

        if(timeToNight(world) == 7) {
            in_hiding = true;
            is_sleeping = true;
        } else if (timeToNight(world) == 3) {
            is_sleeping = false;
            in_hiding = false;
        }
        if (!is_sleeping && !in_hiding) {
            if (timeToNight(world) == 1) System.out.println("hooooooooowwwwwwwlllll");
            nextMove(world);
        }
    }

    /**
     * Creates a new pack with this wolf as the alpha. Initializes the pack and sets pack-related properties.
     */
    public void createPack() {
        pack = new ArrayList<>();
        pack.add(this);
        has_pack = true;
        alpha = true;
        my_alpha = this;
    }

    /**
     * Retrieves the pack of wolves to which this wolf belongs.
     *
     * @return The pack of wolves.
     */
    public ArrayList<Wolf> getPack() {
        return pack;
    }

    /**
     * Adds a wolf to this wolf's pack. Only the alpha wolf can add members to the pack.
     *
     * @param thewolf The wolf to be added to the pack.
     */
    public void addWolfToPack(Wolf thewolf) {
        if (!thewolf.getHasPack()) {
            pack.add(thewolf);
            thewolf.setAlpha(this);
            thewolf.setHasPack();
            if (pack.size() == 5) {
                for (Wolf wolf : pack) {
                    wolf.setTrophicLevel(5);
                }
            }
        }
    }

    /**
     * Retrieves a specific wolf from the pack.
     *
     * @param thisWolf The wolf to retrieve from the pack.
     * @return The wolf from the pack, if present.
     */
    public Wolf getWolfFromPack(Wolf thisWolf) {
        Wolf getwolf = null;
        for (Wolf wolf : pack) {
            if (wolf.equals(thisWolf)) {
                getwolf = wolf;
                break;
            }
        }
        return getwolf;
    }

    /**
     * Checks whether this wolf is part of a pack.
     *
     * @return true if the wolf is part of a pack, false otherwise.
     */
    public boolean getHasPack() {
        return has_pack;
    }

    /**
     * Sets the wolf's status to being part of a pack.
     */
    public void setHasPack() {
        has_pack = true;
    }

    /**
     * Sets the wolf's status to not being part of a pack and adjusts its trophic level.
     */
    public void setHasNotPack() {
        has_pack = false;
        alpha = false;
        trophic_level = 3;
    }

    /**
     * Removes a wolf from this wolf's pack. Adjusts the trophic level of the pack if necessary.
     *
     * @param thewolf The wolf to be removed from the pack.
     */
    public void removeWolfFromPack(Wolf thewolf) {
        if(pack.size() == 4){
            for (Wolf wolf : pack){
                wolf.setTrophicLevel(3);
            }
        }
        pack.remove(thewolf);
        thewolf.setHasNotPack();
    }

    /**
     * This wolf overtakes the pack of another wolf, becoming the new alpha.
     *
     * @param oldwolf The wolf whose pack is being overtaken.
     */
    public void overtakePack(Wolf oldwolf) {
        if (oldwolf.getPack() != null) {
            pack = new ArrayList<>();
            pack.addAll(oldwolf.getPack());
            has_pack = true;
            alpha = true;
            removeWolfFromPack(oldwolf);
            oldwolf.setHasNotPack();
            oldwolf.setAlpha(this);
        }
    }

    /**
     * Sets the trophic level of the wolf.
     *
     * @param i The new trophic level to be set.
     */
    public void setTrophicLevel(int i) {
        trophic_level = i;
    }

    /**
     * Retrieves the alpha wolf of the pack to which this wolf belongs.
     *
     * @return The alpha wolf of the pack.
     */
    public Wolf getMyAlpha() { return my_alpha; }


    /**
     * Determines the graphic of the wolf based on its current condition and age.
     * @return Returns the graphics information for the wolf.
     */
    @Override
    public DisplayInformation getInformation() {
        if (age >= 12) {
            if (is_sleeping) {
                return new DisplayInformation(Color.cyan, "wolf-large-sleeping");
            } else if (wounded) {
                return new DisplayInformation(Color.cyan, "wolf-large-wounded");
            } else {
                return new DisplayInformation(Color.cyan, "wolf-large");
            }
        } else {
            if (is_sleeping) {
                return new DisplayInformation(Color.cyan, "wolf-small-sleeping");
            } else if (wounded) {
                return new DisplayInformation(Color.cyan, "wolf-small-wounded");
            } else {
                return new DisplayInformation(Color.cyan, "wolf-small");
            }
        }
    }

    /**
     * Deletes this wolf from the simulation world and handles pack dynamics if this wolf is the alpha.
     *
     * @param world The simulation world from which the wolf is deleted.
     */
    public void deleteMe(World world){
        if (my_alpha == this) {
            pack.clear();
            has_pack = false;
        } else if (has_pack) {
            my_alpha.removeWolfFromPack(this);
        }
        world.delete(this);
    }

    /**
     * Retrieves the trophic level of the wolf.
     *
     * @return The trophic level of the wolf.
     */
    @Override
    public int getTrophicLevel(){
        return trophic_level;
    }

    public void setAlpha(Wolf wolf){
        my_alpha = wolf;
    }
}
