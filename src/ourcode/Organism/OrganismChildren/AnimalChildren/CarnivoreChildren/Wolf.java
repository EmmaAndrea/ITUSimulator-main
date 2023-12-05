package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Cave;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Carnivore;
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
public class Wolf extends Carnivore implements DynamicDisplayInformationProvider {

    protected ArrayList<Wolf> pack; // The pack of wolves to which this wolf belongs.
    public boolean has_pack; // Indicates whether this wolf is part of a pack.

    private Wolf my_alpha; // The alpha wolf of the pack.
    private boolean alpha; // Indicates whether this wolf is the alpha of a pack.

    protected Cave my_cave;
    protected boolean has_cave;

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
        max_hunger = 28;
        has_pack = false;
        consumable_foods = new ArrayList<>(List.of("rabbit", "bear", "wolf"));
        alpha = false;
        has_cave = false;
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

        Location current_location = world.getLocation(this);

        if (world.getCurrentTime() == 1 && has_cave) {
            if (world.containsNonBlocking(current_location)) {
                if (world.getNonBlocking(current_location) == my_cave) {
                    enterCave(world);
                } else {
                    moveCloser(world, world.getLocation(my_cave));
                }
            }

        } else if (world.getCurrentTime() == 7) {
            in_hiding = false;
        }
<<<<<<< HEAD
        if (!is_sleeping && !in_hiding) {
=======

        if (!in_hiding) {
>>>>>>> ed4a653d6ba493bb754b66ca1743ed21b5fc243d
            if (!has_cave && age > 8) {
                if (alpha) createCave(world, id_generator);
            }
            if (!alpha && my_alpha != null) {
                if (distanceTo(world, world.getLocation(my_alpha)) > 2) {
                    moveCloser(world, world.getLocation(my_alpha)); // my alpha is null, so program crash
                }
            } else if (hunger > 15) {
                hunt(world);
            }
            else nextMove(world);
        }
    }

    @Override
    public void attack(World world, Animal animal) {
        if(alpha){
            for(Wolf wolf : pack){
                if (world.getEntities().containsKey(animal) && world.getEntities().get(animal) != null){
                    if(wolf != this) {
                        wolf.attack(world, animal);
                        if (world.getEntities().containsKey(animal) && world.getEntities().get(animal) != null) {
                            super.attack(world, animal);
                        }
                    }
                }
            }
        }
        if (world.getEntities().containsKey(animal) && world.getEntities().get(animal) != null){
            super.attack(world, animal);
        }
    }

    @Override
    public void hunt(World world){
        if(has_pack){
            if (alpha) {
                for (Wolf wolf : pack) {
                    if (wolf.findFood(world) != null){
                        attack(world, wolf.findFood(world));
                    }
                }
            } else {
                for (Wolf wolf : my_alpha.getPack()){
                    if (wolf.findFood(world) != null){
                        attack(world, wolf.findFood(world));
                    }
                }
            }
        } else {
            super.hunt(world);
        }
    }

    public Animal findFood(World world){
        Set<Location> surrounding_tiles = world.getSurroundingTiles(world.getLocation(this), 5);

        // First, check for blocking organisms.
        // Though, if there is an animal of higher trophic level, move away from this animal.
        for (Location location : surrounding_tiles) {

            // If the tile at given location isn't empty.
            if (!world.isTileEmpty(location)) {

                // Declares a new object at given location.
                Object object = world.getTile(location);

                // Casts object to Organism class and checks if the object is an Organism.
                if (object instanceof Animal animal) {
                    if (consumable_foods.contains(animal.getType())) {
                        return animal;
                    }
                }
            }
        }
        return null;
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
        if (!has_pack || has_cave) {
            return;
        }

        Location location = world.getLocation(this);
        Object tile = world.containsNonBlocking(location) ? world.getNonBlocking(location) : null;

        if (tile instanceof Grass) {
            world.delete(tile);
        }

        Cave cave = new Cave(id_generator);
        world.setTile(location, cave);
        has_cave = true;

        for (Wolf wolf : pack) {
            wolf.setCave(cave);
        }
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
    }

    /**
     * Removes a wolf from this wolf's pack. Adjusts the trophic level of the pack if necessary.
     *
     * @param thewolf The wolf to be removed from the pack.
     */
    public void removeWolfFromPack(Wolf thewolf) {
        if(pack.size() == 4) {
            for (Wolf wolf : pack) {
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
        if (has_cave) {
            if (my_cave.getResidents().isEmpty()) {
                if (my_cave == world.getNonBlocking(world.getLocation(this))) {
                    my_cave.addResident(this);
                    world.remove(this);
                    in_hiding = true;
                }
            }
        }
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
    public void deleteMe(World world) {
        if (my_alpha == this) {
            removeWolfFromPack(this);
            world.delete(this);

            if (!pack.isEmpty()) {
                Wolf next_alpha = pack.get(0);

                for (Wolf wolf : pack) {
                    wolf.setAlpha(next_alpha);
                }
            }

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
