package ourcode.Organism.OrganismChildren.AnimalChildren.CarnivoreChildren;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Obstacles.Territory;
import ourcode.Organism.Gender;
import ourcode.Organism.OrganismChildren.Animal;
import ourcode.Organism.OrganismChildren.AnimalChildren.Predator;
import ourcode.Setup.IDGenerator;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

import static ourcode.Organism.Gender.Male;

public abstract class TerritorialPredator extends Predator implements DynamicDisplayInformationProvider {
    protected Location territory_location;
    protected TerritorialPredator mate;
    protected Animal my_cub;

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
     * Bears are extremely territorial, and therefore rarely wander outside their territory.
     * If the bear is very hungry, it will go further away from its territory than normal to hunt.
     * The bear will always sleep in its territory.
     * @param world The simulation world in which the omnivore exists.
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
            if (distanceTo(world, territory_location) > 2) {
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

    @Override
    public boolean sameTypeInteraction(World world, Animal animal){
        grace_period = 1;
        if (gender != animal.getGender()) return false;
        if (isCloseToDeath()) moveAway(world, world.getLocation(animal));
        else attack(world, animal);
        grace_period = 0;
        return true;
    }

    @Override
    public void attack(World world, Animal animal){
        if (Objects.equals(type, animal.getType())) {
            if (!gender.equals(animal.getGender())) {
                return;
            }
        }
        super.attack(world, animal);
    }

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

    @Override
    public void enterHabitat(World world) {
        moveCloser(world, world.getLocation(habitat));
        habitat.addResident(this);
        is_hiding = true;
    }

    @Override
    public void exitHabitat(World world){
        habitat.removeResident(this);
        is_hiding = false;
        grace_period = 0;
    }

    /**
     * Searches for a mate within a specified range in the world. If a potential mate
     * is found, the bear moves closer and establishes a mating pair.
     *
     * @param world The simulation world where the bear searches for a mate.
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
     * Checks if the bear meets the conditions for breeding. If the bear is female
     * and has a mate within proximity, breeding may occur.
     *
     * @param world The simulation world where breeding is checked.
     * @return true if breeding conditions are met, false otherwise.
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
     * Sets the bear's mate to a specified bear and updates its territory
     * to match that of its mate.
     *
     * @param female_bear The bear to be set as the mate.
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

    public void femaleSetMate(TerritorialPredator male_bear) {
        mate = male_bear;
        friends.add(male_bear);
        System.out.println("GIRL GOT MATE");
    }

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
     * Sets the bear's territory to a specified location.
     *
     * @param territory The location to be set as the bear's new territory.
     */
    public void setTerritoryLocation(Location territory) {
        this.territory_location = territory;
    }

    public void setTerritory(World world, Location location) {
        if (habitat != null && world.contains(habitat)) {
            world.delete(habitat);
        }
        habitat = new Territory(id_generator);
        world.setTile(location, habitat);
    }

    public void setMyCub(Animal cub) {
        my_cub = cub;
        friends.add(cub);
    }
}