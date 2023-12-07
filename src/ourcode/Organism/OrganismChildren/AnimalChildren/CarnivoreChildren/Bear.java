package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Territory;
import ourcode.Organism.Gender;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Bear extends Predator implements DynamicDisplayInformationProvider {

    protected Location territory_location;
    
    protected Territory my_territory;

    protected Bear mate;

    public Bear(IDGenerator idGenerator, boolean has_cordyceps) {
        super(idGenerator, has_cordyceps);
        trophic_level = 4;
        territory_location = null;
        type = "bear";
        max_age = 190;
        max_hunger = 30;
        power = 6;
        max_damage = 16;
        consumable_foods = new ArrayList<>(List.of("grass", "wolf", "bear", "rabbit", "bush"));
        this.has_cordyceps = has_cordyceps;
    }


    /**
     * Bears are extremely territorial, and therefore never wander outside their territory.
     * The omnivore act method ensures the bear has a territory and never goes too far away.
     * However, when the bear is old enough, it will leave its own territory in order to find a mate.
     * If the bear is very hungry, it will go further away from its territory than normal to hunt.
     * The bear will always sleep in its territory.
     * @param world The simulation world in which the omnivore exists.
     */
    @Override
    public void act(World world) {

        super.act(world);

        // if it's dusk, go to sleep
        if (world.getCurrentTime() == 12 && my_territory != null) {
            if (distanceTo(world, territory_location) < 1) {
                my_territory.addResident(this);
                in_hiding = true;
            } else {
                moveCloser(world, territory_location);
            }
        }
        // if it's dawn, wake up
        if (world.getCurrentTime() == 18 && my_territory != null) {
            my_territory.removeResident(this);
            in_hiding = false;
        }

        // if not sleeping
        if (!in_hiding) {
            // if ready to mate and if single, start finding a partner
            if (gender == Gender.Male && age > 19 && mate == null) {
                findMate(world);
            }

            // if very hungry, go hunt
            else if (hunger >= 20) {
                hunt(world);
            }

            // stay close to territory
            else if (distanceTo(world, territory_location) > 3) {
                moveCloser(world, territory_location);
            }

            // if it is still alive, and hasn't done anything else move randomly
            else if (world.getEntities().get(this) != null) {
                if (getRandomMoveLocation(world) != null) {
                    world.move(this, getRandomMoveLocation(world));
                }
            }
        }
    }

    @Override
    public void makeHabitat(World world) {
        if (territory_location == null) {
            territory_location = world.getLocation(this);
        }

        if (checkEmptySpace(world, territory_location)) {
            my_territory = new Territory(id_generator);
            world.setTile(territory_location, my_territory);
        }
                
    }

    /**
     * Searches for a mate within a specified range in the world. If a potential mate
     * is found, the bear moves closer and establishes a mating pair.
     *
     * @param world The simulation world where the bear searches for a mate.
     */
    public void findMate(World world){
        for (Location location: world.getSurroundingTiles(world.getLocation(this), 7)) {
            if (world.getTile(location) instanceof Bear potential_mate){
                if (potential_mate.getGender() == Gender.Female){
                    for(int i = 1 ; i < (distanceTo(world, world.getLocation(potential_mate))) ; i++) {
                        moveCloser(world, location);
                    }
                    if (maleSetMate(potential_mate, world)){
                        potential_mate.femaleSetMate(this);
                    }
                }
            }
        }
    }

    /**
     * Checks if the bear meets the conditions for breeding. If the bear is female
     * and has a mate within proximity, breeding may occur.
     *
     * @param world The simulation world where breeding is checked.
     * @return true if breeding conditions are met, false otherwise.
     */
    @Override
    public boolean checkBreed(World world) {
        if (gender == Gender.Female) {
            if (mate != null) {
                if (world.getEntities().containsKey(mate)) {
                    System.out.println("tries to breed");
                    if (distanceTo(world, world.getLocation(mate)) < 2) {
                        return true;
                    } else {
                        moveCloser(world, world.getLocation(mate));
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Sets the bear's mate to a specified bear and updates its territory
     * to match that of its mate.
     *
     * @param female_bear The bear to be set as the mate.
     */
    public boolean maleSetMate(Bear female_bear, World world) {
        mate = female_bear;
        friends.add(female_bear);
        for (Location location : world.getSurroundingTiles(female_bear.getTerritory())) {
            if (checkEmptySpace(world, location)) {
                setTerritory(world, location);
                System.out.println("BOY GOT MATE");
                return true;
            }
        } return false;
    }

    public void femaleSetMate(Bear male_bear){
        mate = male_bear;
        friends.add(male_bear);
        System.out.println("GIRL GOT MATE");
    }

    /**
     * Retrieves the territory of the bear.
     *
     * @return The current territory of the bear.
     */
    public Location getTerritory(){
        return territory_location;
    }

    /**
     * Retrieves the amount of damage this bear has taken.
     *
     * @return The damage taken.
     */
    public int getDamageTaken() {
        return damage_taken;
    }

    /**
     * Graphics for old, young and wounded bear.
     * @return the display information for the bear in its current state.
     */
    @Override
    public DisplayInformation getInformation() {
        if (age >= 11) {
            if (damage_taken > 0) {
                return new DisplayInformation(Color.yellow, "bear-large-wounded");
            } else if (in_hiding) {
                return new DisplayInformation(Color.yellow, "bear-large-sleeping");
            } else {
                return new DisplayInformation(Color.yellow, "bear-large");
            }
        } else {
            if (damage_taken > 0) {
                return new DisplayInformation(Color.yellow, "bear-small-wounded");
            } else if (in_hiding) {
                return new DisplayInformation(Color.yellow, "bear-small-sleeping");
            } else {
                return new DisplayInformation(Color.yellow, "bear-small");
            }
        }
    }

    /**
     * Sets the bear's territory to a specified location.
     *
     * @param territory The location to be set as the bear's new territory.
     */
    public void setTerritoryLocation(Location territory) {
        this.territory_location = territory;
    }

    public void setTerritory(World world, Location location){
        if (my_territory != null){
            world.delete(my_territory);
        }
        Territory new_territory = new Territory(id_generator);
        world.setTile(location, new_territory);
        my_territory = new_territory;
    }
}