package ourcode.Organism.OrganismChildren;

import itumulator.world.World;
import itumulator.world.Location;
import ourcode.Organism.Organism;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Setup.IDGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Animal class gives the abstraction of an Animal. An animal inherits from the Organism
 *  class and has the fields hunger and max hunger.
 *  The animal class controls the movement of an animal
 *  the eat method is here, which gets the correct nutritional value of the organism being eaten
 */
public abstract class Animal extends Organism {
    public int hunger;
    public int max_hunger;

    /**
     * The constructor of an Animal.
     */
    public Animal(IDGenerator original_id_generator) {
        super(original_id_generator); // life_counter = 1;
        hunger = 1;
        max_hunger = 1;
    }

    /**
     * Finds nutritional value of the object we are standing on
     */
    public int getStandingOnNutritionalValue(World world) {
        // Deducts hunger by the nutritional value of the eaten organism.
        Organism organism = id_generator.getOrganism(world.getLocation(world.getNonBlocking(world.getCurrentLocation())));
        return organism.getNutritionalValue();
    }

    /**
     * A method for giving animals the ability to eat. They decrease their 'hunger' by a certain amount of 'nutritional value'
     *  and the object the animal is standing on will be 'eaten' by calling the 'delete()' method from the World class
     */
    public void eat(World world) {
        hunger -= getStandingOnNutritionalValue(world);
        world.delete(world.getNonBlocking(world.getCurrentLocation()));
    }

    /**
     * Calls the act method from Organism.
     * Increases hunger by 1.
     * Dies, if max hunger is reached.
     * Moves, if possible.
     * Breeds, if circumstances are met.
     * Calls herbivoreAct().
     */
    public void animalAct(World world) {
        super.animalAct(world);

        hunger++;

        checkHunger(world);

        nextMove(world);

        //checkBreed(world);

        herbivoreAct(world);
    }

    /**
     * explain here why we chose to have an herbivoreAct method
     */
    public void herbivoreAct(World world) {

    }

    /**
     * Gives the ability to breed with a partner, if there exists another one of its kind in surrounding tiles
     */
    public void breed(World world, Animal animal) {
        // calls spawn() method from Organism class
        // not sure if this works yet
        // super.spawn(world); // should make new rabbit with new id, but doesn't

        // Retrieve current location
        Location currentLocation = world.getLocation(this);

        // Find a random suitable location to spawn baby
        ArrayList<Location> possibleSpawnLocations = new ArrayList<>();

        for (Location surroundingTile : world.getSurroundingTiles(currentLocation, 1)) {
            if (world.isTileEmpty(surroundingTile)) {
                possibleSpawnLocations.add(surroundingTile);
            }
        }

        Random random = new Random();
        int random_index = random.nextInt(0, possibleSpawnLocations.size()-1);


        String animal_type = animal.getType();
        switch (animal_type) {
            case "rabbit":
                //Animal baby = new Rabbit(IDGenerato);
                break;
            case "wolf":
                // Animal baby_wolf = new Wolf();
                break;
            default:
                // Animal baby_wolf = new W:

        }

        //world.setTile(location, this); // If it's empty, spawn organism into this location.

        //id_generator.addLocationToIdMap(location, id);
        id_generator.addAnimalToIdMap(id, this);





        /*
        // If a suitable location is found, spread the grass
        if (spreadLocation != null) {
            Grass spreadedgrass = new Grass(id_generator);
            world.setTile(spreadLocation, spreadedgrass);
        }
        id_generator.addAnimalToIdMap(id, this);
        id_generator.addLocationToIdMap(spreadLocation, id);
         */
    }

    /**
     * Checks if the circumstances for breeding are met:
     * If there is a nearby same animal type;
     * If animal is older than 20;
     * If there is space for one more animal.
     */
    public void checkBreed(World world, Animal animal) {
        // only if there is another one of its type in the surrounding tiles
            // only if the animal is 20 steps old
                // only if there is space for one more rabbit
                    // only if they haven't bred in 10 steps
                        breed(world, animal);
    }

    /**
     * A method for creating a new burrow from the Burrow class.
     */
    public void makeBurrow() {

    }

    /**
     * Animal dies of hunger if it is hungrier than its max hunger.
     */
    public void checkHunger(World world) {
        if (hunger > max_hunger) {
            world.delete(this);
        }
    }

    /**
     * Moves to new tile, if there is a free surrounding tile.
     */
    public void nextMove(World world) {
        // If there are no free surrounding tiles, then animal stays in position.
        Location new_location = world.getCurrentLocation();

        // Finds a random free tile.
        for (Location location : world.getEmptySurroundingTiles(world.getCurrentLocation())) {
            new_location = location;
        }
        // Moves animal to this new tile
        world.move(this, new_location);
    }
}
