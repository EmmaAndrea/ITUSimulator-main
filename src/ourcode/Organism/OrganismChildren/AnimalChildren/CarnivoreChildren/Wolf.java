package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Cave;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Setup.IDGenerator;
import ourcode.Obstacles.Habitat;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Wolf entity in the simulated world, extending the Carnivore class.
 * Wolves have unique behaviors such as forming packs, having an alpha wolf,
 * and different interactions based on their pack status and trophic level.
 */
public class Wolf extends Predator implements DynamicDisplayInformationProvider {

    protected ArrayList<Wolf> pack; // The pack of wolves to which this wolf belongs.
    public boolean has_pack; // Indicates whether this wolf is part of a pack.

    private Wolf my_alpha; // The alpha wolf of the pack.
    private boolean alpha; // Indicates whether this wolf is the alpha of a pack.
    protected boolean has_cave;

    /**
     * Constructs a Wolf with specific characteristics and initializes its pack-related properties.
     *
     * @param idGenerator  The IDGenerator instance providing the unique identifier for the wolf.
     * @param has_cordyceps Boolean if it has cordyceps at birth or not.
     */
    public Wolf(IDGenerator idGenerator, boolean has_cordyceps) {
        super(idGenerator, has_cordyceps);
        type = "wolf";
        trophic_level = 3;
        max_age = 130;
        max_hunger = 28;
        has_pack = false;
        consumable_foods = new ArrayList<>(List.of("rabbit", "bear", "wolf", "carcass"));
        alpha = false;
        has_cave = false;
        power = 4;
        max_damage = 16;
        this.has_cordyceps = has_cordyceps;
        bedtime = 1;
        wakeup = 7;
    }

    /**
     * Defines the behavior of a wolf in each simulation step. This includes pack behavior,
     * nighttime activities such as sleeping, and movement during the day.
     *
     * @param world The simulation world in which the wolf exists.
     */

    @Override
    public void act(World world) {
        super.act(world);
        // if wolf has an alpha and the pack does not exist, delete pack
        if (my_alpha != null && my_alpha.getPack() == null) {
            deletePack();
        }

        if (in_hiding) return;
        if (isBedtime(world)) return;

        if (grace_period == 1) {
            grace_period = 0;
        }

        // if the wolf is the alpha and its hungry enough, set pack_hunting to false for all wolves in pack
        if (alpha) {
            if (hunger < 5) {
                for (Wolf wolf : pack) {
                    wolf.setPackHuntingFalse();
                }
            }
        }

        // pack hunting method
        if (pack_hunting && my_alpha != null){
            huntWithPack(world);
        }

        // sequence for alphas
        else if (alpha) {
            if (hunger > 8) {
                for (Wolf wolf : pack) {
                    wolf.setPackHuntingTrue();
                }
            } else nextMove(world);
        }

        // sequence for pack wolves
        else if (my_alpha != null && world.getEntities().get(my_alpha) != null) {
            if (hunger > 15) {
                for (Wolf wolf : my_alpha.getPack()) {
                    wolf.setPackHuntingTrue();
                }
            } else if (distanceTo(world, world.getLocation(my_alpha)) > 3) {
                moveCloser(world, world.getLocation(my_alpha));

            } else nextMove(world);

            // sequence for lone wolves
        } else if (hunger > 15) hunt(world);

        else nextMove(world);

    }

    public void huntWithPack(World world){
        if (my_alpha != null && world.getEntities().get(my_alpha) != null) {
            if (distanceTo(world, world.getLocation(my_alpha)) > 3) {
                moveCloser(world, world.getLocation(my_alpha));
            }
            hunt(world);
        }
    }

    /**
     * Attacks a specified animal in the world. If the target is a wolf with significant damage,
     * and this wolf is the alpha, it overtakes the target wolf's pack. Otherwise, performs
     * a standard attack.
     *
     * @param world  The simulation world where the attack happens.
     * @param animal The animal to be attacked.
     */
    @Override
    public void attack(World world, Animal animal) {
        if (world.getEntities().containsKey(animal) && world.getEntities().get(animal) != null) {
            if (animal instanceof Wolf wolf) {
                if(wolf.getDamageTaken() > 7) {
                    if (alpha) this.overtakePack(wolf);
                }
            }
            else super.attack(world, animal);
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
     * Creates a cave in the world at the current location if certain conditions are met.
     * If the object has a pack (has_pack is true) and doesn't already have a cave (has_cave is false),
     * this method will attempt to create a new Cave. If the current location in the world contains
     * Grass, the grass will be removed.
     * A new cave is then created and set at this location.
     * Finally, if a cave is created, all wolves in the pack are assigned this cave.
     *
     * @param world         The world in which the cave is to be created.
     */
    @Override
    public void makeHabitat(World world) {
        if (has_pack && !alpha) return;

        Location location = world.getLocation(this);

        if (checkEmptySpace(world, location)) {
            habitat = new Cave(id_generator);
            world.setTile(location, habitat);
            has_cave = true;

            if(alpha) {
                for (Wolf wolf : pack) {
                    wolf.setHabitat(habitat);
                }
            }
        }
    }

    /**
     * Retrieves the pack to which this wolf belongs.
     *
     * @return The pack of wolves, or null if this wolf is not part of any pack.
     */
    public ArrayList<Wolf> getPack() {
        return pack;
    }

    /**
     * Retrieves the cave associated with this wolf.
     *
     * @return The cave where this wolf resides, or null if no cave is associated.
     */
    public Habitat getMyCave() {
        return habitat;
    }

    /**
     * Adds a wolf to this wolf's pack. Only the alpha wolf can add members to the pack.
     *
     * @param new_wolf The wolf to be added to the pack.
     */
    public void addWolfToPack(Wolf new_wolf) {
        if (!new_wolf.getHasPack()) {
            pack.add(new_wolf);
            new_wolf.setAlpha(this);
            new_wolf.setHasPack();
            for (Wolf wolf : pack) {
                wolf.setFriends(new_wolf);
                if (pack.size() == 5) {
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
        pack = null;
    }

    /**
     * Removes a wolf from this wolf's pack. Adjusts the trophic level of the pack if necessary.
     *
     * @param thewolf The wolf to be removed from the pack.
     */
    public void removeWolfFromPack(Wolf thewolf) {
        if (pack.size() == 4) {
            for (Wolf wolf : pack) {
                wolf.setTrophicLevel(3);
                wolf.setPackHuntingFalse();
            }
        }
        pack.remove(thewolf);
        thewolf.setHasNotPack();
        thewolf.setAlpha(null);
    }

    /**
     * Overtakes the pack of another wolf, becoming the new alpha. The existing pack members
     * are transferred to this wolf's pack, and the former alpha is removed from the pack.
     *
     * @param oldwolf The alpha wolf of the pack being overtaken.
     */
    public void overtakePack(Wolf oldwolf) {
        if (oldwolf.getPack() != null) {
            pack = new ArrayList<>();
            for (Wolf wolf : oldwolf.getPack()) {
                addWolfToPack(wolf);
            }
            addWolfToPack(this);
            has_pack = true;
            alpha = true;
            removeWolfFromPack(oldwolf);
            oldwolf.setHasNotPack();
            oldwolf.setAlpha(this);
        }
    }

    /**
     * Sets the trophic level of this wolf.
     *
     * @param i The new trophic level to be set for the wolf.
     */
    public void setTrophicLevel(int i) {
        trophic_level = i;
    }

    /**
     * Retrieves the alpha wolf of the pack to which this wolf belongs.
     *
     * @return The alpha wolf of the pack, or null if this wolf has no pack.
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
    }

    /**
     * Removes this wolf from the simulation world. If this wolf is the alpha, pack dynamics
     * are adjusted accordingly.
     *
     * @param world The simulation world from which the wolf is removed.
     */
    public void deleteMe(World world) {
        if(alpha){
            if (!pack.isEmpty()) {
                int i = 0;
                while (pack.get(i) == this) {
                    i++;
                }
                Wolf next_alpha = pack.get(i);

                for (Wolf wolf : pack) {
                    wolf.setAlpha(next_alpha);
                    wolf.overtakePack(this);
                }
            }
        } else if (has_pack && pack != null) {
            my_alpha.removeWolfFromPack(this);
        }
        world.delete(this);
    }

    /**
     * Deletes the pack associated with this wolf, resetting pack-related properties.
     */
    public void deletePack(){
        pack = null;
        has_pack = false;
        alpha = false;
        my_alpha = null;
    }

    /**
     * Sets the alpha wolf for this wolf.
     *
     * @param wolf The wolf to be set as alpha.
     */
    public void setAlpha(Wolf wolf){
        my_alpha = wolf;
    }

    /**
     * Retrieves the amount of damage this wolf has taken.
     *
     * @return The damage taken.
     */
    public int getDamageTaken() {
        return damage_taken;
    }

    /**
     * Sets the pack hunting status of this wolf to true, indicating it is part of a hunting pack.
     */
    public void setPackHuntingTrue(){
        pack_hunting = true;
    }

    /**
     * Sets the pack hunting status of this wolf to false, indicating it is not part of a hunting pack.
     */
    public void setPackHuntingFalse(){
        pack_hunting = false;
    }

    /**
     * Checks if this wolf is the alpha of its pack.
     *
     * @return true if this wolf is the alpha, false otherwise.
     */
    public boolean isAlpha(){
        return alpha;
    }
}
