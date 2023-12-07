package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Cave;
import ourcode.Organism.Gender;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Organism.OrganismChildren.PlantChildren.NonBlockingPlantChildren.Grass;
import ourcode.Setup.IDGenerator;

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

    protected Cave my_cave;
    protected boolean has_cave;

    protected boolean pack_hunting;

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
        consumable_foods = new ArrayList<>(List.of("rabbit", "bear", "wolf"));
        alpha = false;
        has_cave = false;
        power = 4;
        max_damage = 16;
        pack_hunting = false;
        this.has_cordyceps = has_cordyceps;
    }

    /**
     * Defines the behavior of a wolf in each simulation step. This includes pack behavior,
     * nighttime activities such as sleeping, and movement during the day.
     *
     * @param world The simulation world in which the wolf exists.
     */
    @Override
    public void carnivoreAct(World world) {
        // if wolf has an alpha and the pack does not exist, delete pack
        if (my_alpha != null && my_alpha.getPack() == null) {
            deletePack();
        }

        // if the wolf is the alpha and its hungry enough, set pack_hunting to false for all wolves in pack
        if (alpha) {
            if (hunger < 5) {
                for (Wolf wolf : pack) {
                    wolf.setPackHuntingFalse();
                }
            }
        }

        // if the current time is 1, wolf has a cave, pack isn't hunting, and wolf is not in cave
            // if cave is nearby, enter
            // else move closer to cave
        // else, exit cave if time is 7
        // else, exit if in cave and the pack is hunting
        // else, if checkBreed, breed
        if (world.getCurrentTime() == 1 && has_cave && !pack_hunting && !in_hiding) {
            if (distanceTo(world, world.getLocation(my_cave)) <= 1) {
                enterCave(world);
            } else {
                moveCloser(world, world.getLocation(my_cave));
            }
        } else if (in_hiding && world.getCurrentTime() == 7) {
            exitCave(world);
        } else if (in_hiding && pack_hunting) {
            exitCave(world);
        } else if (in_hiding) {
            if (checkBreedWolf()) {
                breedWolf(id_generator, my_cave.getResidents());
            }
        }

        // if not in cave:
            // move closer to alpha, if the packing is hunting and alpha is not sleeping and alpha is not null
            // if this is alpha
                // and doesn't have cave and is older than 8, make cave
                // if hungry enough, set all wolves in pack in hunting mode
            // if not alpha
                // and is pretty hungry, set all wolves in pack in hunting mode
                //
        if (!in_hiding) {
            if (pack_hunting && world.getEntities().containsKey(my_alpha)){
                if (my_alpha != null) {
                    if (distanceTo(world, world.getLocation(my_alpha)) > 3) {
                        moveCloser(world, world.getLocation(my_alpha));
                    }
                    hunt(world);
                    nextMove(world);
                    return;
                }
            }
            if (alpha) {
                if (!has_cave && age > 8) {
                    createCave(world, id_generator);
                }
                if (hunger > 8) {
                    for (Wolf wolf : pack) {
                        wolf.setPackHuntingTrue();
                    }
                } else nextMove(world);
            }
            if (!alpha) {
                if (world.getEntities().containsKey(my_alpha)) {
                    if (hunger > 15) {
                        for (Wolf wolf : my_alpha.getPack()) {
                            wolf.setPackHuntingTrue();
                        }
                    } else if (distanceTo(world, world.getLocation(my_alpha)) > 3) {
                        moveCloser(world, world.getLocation(my_alpha));
                    }
                } else nextMove(world);
            }
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
     * @param id_generator  An IDGenerator instance for generating unique IDs for the new cave.
     */
    public void createCave(World world, IDGenerator id_generator) {
        Location location = world.getLocation(this);

        if (world.containsNonBlocking(location)) {
            if (world.getNonBlocking(location) instanceof Grass grass) {
                //grass.setExists(false)
                world.delete(grass);
            }
            else return;
        }

        Cave cave = new Cave(id_generator);
        world.setTile(location, cave);
        has_cave = true;

        for (Wolf wolf : pack) {
            wolf.setCave(cave);
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
    public Cave getMyCave() {
        return my_cave;
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
     * Assigns a given cave to a wolf.
     * @param cave The cave which is to be assigned a new wolf.
     */
    public void setCave(Cave cave) {
        my_cave = cave;
        has_cave = true;
    }

    /**
     * Enters the cave that the wolf is standing on, if it is empty, and it's their cave.
     * @param world The world in which the current events are happening.
     */
    public void enterCave(World world) {
        my_cave.addResident(this);
        world.remove(this);
        in_hiding = true;
    }

    /**
     * Exits the cave in which this wolf is currently hiding.
     *
     * @param world The world where the cave is located.
     */
    public void exitCave(World world) {
        my_cave.removeResident(this);
        world.setTile(world.getLocation(my_cave), this);
        in_hiding = false;
    }

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
     * Checks whether the conditions for wolf breeding are met. Breeding occurs during the day, inside a cave.
     * The method ensures that the wolf is male, has a cave, and finds a female wolf in the cave who hasn't bred recently.
     * Additionally, it checks that the age of the wolves is within a certain range of their maximum age.
     *
     * @return true if all breeding conditions are met, false otherwise.
     */
    public boolean checkBreedWolf() {
        if (gender == Gender.Male) {
            if (has_cave) {
                List<Animal> residents = my_cave.getResidents();

                if (residents.size() >= 2) {
                    for (Animal resident : residents) {
                        if (resident.getGender() == Gender.Female) {
                            if (resident.getStepsSinceLastBirth() <= 12 && steps_since_last_birth <= 12) {
                                if (age >= max_age * 0.15 && age <= max_age * 0.85) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        // If only one or two or three or four or five... or none of the conditions are met, return false.
        return false;
    }

    /**
     * Creates a new wolf cub and adds it to the pack and the cave's residents.
     * This method is invoked when breeding conditions are met.
     * It generates a new Wolf instance (cub), adds it to the list of residents in the cave,
     * and adds it to the alpha wolf's pack. The cub is also assigned the same cave as its parents.
     *
     * @param id_generator The ID generator used for creating the unique ID of the new wolf cub.
     * @param residents The list of animals residing in the cave, to which the new cub will be added.
     */
    public void breedWolf(IDGenerator id_generator, List<Animal> residents) {
        Wolf cub = new Wolf(id_generator, false);
        residents.add(cub);
        my_alpha.addWolfToPack(cub);
        cub.setCave(my_cave);
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
