package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Territory;
import ourcode.Organism.Gender;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Setup.IDGenerator;

import java.util.Arrays;
import java.util.Objects;

import static ourcode.Organism.Gender.Male;

/**
 * Represents a territorial predator in the simulated world, characterized by its territorial behavior.
 * This abstract class extends 'Predator', adding specific behaviors and interactions related to territory management and mating.
 */
public abstract class TerritorialPredator extends Predator {
    protected Location territory_location;
    protected TerritorialPredator mate;
    protected Animal my_cub;

    /**
     * Constructs a TerritorialPredator with specific characteristics and initializes its territorial properties.
     * @param idGenerator  The IDGenerator instance providing the unique identifier for the predator.
     * @param has_cordyceps Boolean indicating if the predator is born with cordyceps infection.
     */
    public TerritorialPredator(IDGenerator idGenerator, boolean has_cordyceps) {
        super(idGenerator, has_cordyceps);
        trophic_level = 3;
        territory_location = null;
        max_hunger = 60;
        power = 4;
        max_damage = 36;
        consumable_foods.addAll(Arrays.asList("bush", "tyrannosaurus", "grass"));
        nutritional_value = 12;
    }

    /**
     * Defines the behavior of a territorial predator in each simulation step. Includes territory management, hunting, and mating behaviors.
     * @param world The simulation world in which the territorial predator exists.
     */
    public void act(World world) {
        super.act(world);

        // bears are more dangerous and stronger as an adult
        if (age >= 11) {
            trophic_level = 4;
            power = 6;
        }

        // if not sleeping
        if (!is_hiding && !isBedtime(world)) {
            // if ready to mate and if single, start finding a partner
            if (gender == Male && age > 19 && mate == null) {
                System.out.println("Starts finding mate");
                if (findMate(world)) return;
            }

            // stay close to territory
            if (distanceTo(world, territory_location) > 1) {
                moveCloser(world, territory_location);
                // if very hungry, go hunt
            } else if (hunger >= 20) {
                if (hunt(world)){
                    return;
                }
            }

            if (!enemyHabitatNearby(world)) {
                if (getRandomMoveLocation(world) != null) {
                    world.move(this, getRandomMoveLocation(world));
                }
            }
        }
    }

    /**
     * Initiates an interaction with another animal of the same type. Adjusts behavior based on gender and health status.
     * @param world The simulation world.
     * @param animal The animal to interact with.
     */
    @Override
    public boolean sameTypeInteraction(World world, Animal animal){
        grace_period = 1;
        if (gender != animal.getGender()) return false;
        if (isCloseToDeath()) moveAway(world, world.getLocation(animal));
        else attack(world, animal);
        grace_period = 0;
        return true;
    }

    /**
     * Attacks a specified animal in the world. Adjusts behavior based on the type and gender of the animal.
     * @param world The simulation world where the attack happens.
     * @param animal The animal to be attacked.
     */
    @Override
    public void attack(World world, Animal animal){
        if (Objects.equals(type, animal.getType())) {
            if (!gender.equals(animal.getGender())) {
                return;
            }
        }
        super.attack(world, animal);
    }

    /**
     * Creates a habitat for the territorial predator. Establishes a territory at the current location.
     * @param world The simulation world where the habitat is created.
     */
    @Override
    public void makeHabitat(World world) {
        if (territory_location == null) {
            territory_location = world.getLocation(this);
        }
        if (checkEmptySpace(world, territory_location)) {
            habitat = new Territory(id_generator);
            world.setTile(territory_location, habitat);
        }
    }

    /**
     * Enters the habitat (territory) for resting or protection.
     * @param world The simulation world where the habitat is located.
     */
    @Override
    public void enterHabitat(World world) {
        moveCloser(world, world.getLocation(habitat));
        habitat.addResident(this);
        is_hiding = true;
    }

    /**
     * Exits the habitat (territory). Resumes active participation in the simulation world.
     * @param world The simulation world where the habitat is located.
     */
    @Override
    public void exitHabitat(World world){
        habitat.removeResident(this);
        is_hiding = false;
        grace_period = 0;
    }

    /**
     * Searches for a mate within the world. Establishes a mating pair if a suitable mate is found.
     * @param world The simulation world where the predator searches for a mate.
     */
    public boolean findMate(World world) {
        if (!world.contains(this)) return false;
        for (Location location: world.getSurroundingTiles(world.getLocation(this), 7)) {
            if (world.getTile(location) instanceof TerritorialPredator potential_mate) {
                if (potential_mate.getGender() == Gender.Female) {
                    for(int i = 1 ; i < (distanceTo(world, world.getLocation(potential_mate))) ; i++) {
                        moveCloser(world, location);
                    }
                    if (maleSetMate(potential_mate, world)) {
                        potential_mate.femaleSetMate(this);
                        return true;
                    }
                }
            }
        } return false;
    }

    /**
     * Checks for breeding conditions. Determines if breeding can occur based on the presence of a mate and other conditions.
     * @param world The simulation world where breeding conditions are checked.
     */
    @Override
    public boolean checkHasBreedMate(World world) {
        if (mate != null) {
            System.out.println("tries to breed");
                return true;
        }
        return false;
    }

    /**
     * Sets the mate for this predator and updates its territory to match that of its mate.
     * @param female_bear The predator to be set as the mate.
     * @param world The simulation world where the mating occurs.
     */
    public boolean maleSetMate(TerritorialPredator female_bear, World world) {
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

    /**
     * Sets this female predator's mate to the specified male predator.
     * @param male_bear The predator to be set as the mate for this female predator.
     */
    public void femaleSetMate(TerritorialPredator male_bear) {
        mate = male_bear;
        friends.add(male_bear);
        System.out.println("GIRL GOT MATE");
    }

    /**
     * Places the offspring of this predator in the world, specifically within its territory.
     * @param world The simulation world where the offspring is to be placed.
     * @param cub The offspring to be placed in the world.
     */
    @Override
    public boolean putCubInWorld(World world, Animal cub){
        cub.setHabitat(habitat);
        if(getSpawnLocation(world) == null) return false;
        world.setTile(getSpawnLocation(world), cub);
        cub.setHabitat(null);
        cub.setGracePeriod(1);
        setMyCub(cub);
        mate.setMyCub(cub);
        return true;
    }

    /**
     * Retrieves the current territory location of this predator.
     * @return The location of the predator's territory.
     */
    public Location getTerritory(){
        return territory_location;
    }

    /**
     * Retrieves the amount of damage this predator has taken.
     * @return The total damage taken by this predator.
     */
    public int getDamageTaken() {
        return damage_taken;
    }


    /**
     * Sets the territory location of this predator to a specified location.
     * @param territory The location to be set as the new territory.
     */
    public void setTerritoryLocation(Location territory) {
        this.territory_location = territory;
    }

    /**
     * Establishes a new territory for this predator at a specified location.
     * @param world The simulation world where the territory is to be established.
     * @param location The location at which the new territory is created.
     */
    public void setTerritory(World world, Location location) {
        if (habitat != null && world.contains(habitat)) {
            world.delete(habitat);
        }
        habitat = new Territory(id_generator);
        world.setTile(location, habitat);
    }

    /**
     * Sets the offspring of this predator, updating its relationship status within the pack or family.
     * @param cub The offspring to be associated with this predator.
     */
    public void setMyCub(Animal cub) {
        my_cub = cub;
        friends.add(cub);
    }

    public TerritorialPredator getMate(){
        return mate;
    }
}