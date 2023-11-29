package ourcode.Organism.OrganismChildren;

import itumulator.world.Location;
import itumulator.world.World;
import ourcode.Organism.Organism;
import ourcode.Organism.OrganismChildren.AnimalChildren.HerbivoreChildren.Rabbit;
import ourcode.Setup.IDGenerator;

/**
 * The Animal class gives the abstraction of an Animal. An animal inherits from the Organism
 *  class and has the fields hunger and max hunger.
 *  The animal class controls the movement of an animal
 *  the eat method is here, which gets the correct nutritional value of the organism being eaten
 */
public abstract class Animal extends Organism {
    public int hunger;
    public int max_hunger;
    public int days_since_last_birth;

    /**
     * The constructor of an Animal.
     */
    public Animal(IDGenerator original_id_generator) {
        super(original_id_generator); // life_counter = 1;
        hunger = 1;
        max_hunger = 1; // this value is random and will get initialized to another value in the children classes.
        days_since_last_birth = 0;
    }

    /**
     * Returns the nutritional value of the organism of the current location.
     */
    public int getStandingOnNutritionalValue(World world) {
        Organism organism = id_generator.getOrganism(world.getLocation(world.getNonBlocking(world.getCurrentLocation())));
        return organism.getNutritionalValue();
    }

    /**
     * A method for giving animals the ability to eat. They decrease their 'hunger' by a certain amount of 'nutritional value'
     *  and the object the animal is standing on will be 'eaten' by calling the 'delete()' method from the World class
     */
    public void eat(World world) {
        // Deducts the animal's hunger with the nutritional value of the eaten organism.
        hunger -= getStandingOnNutritionalValue(world);

        // Deletes the eaten organism from the world.
        world.delete(world.getNonBlocking(world.getLocation(this)));
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
        days_since_last_birth++;

        // Checks if it dies of hunger; if not, move, breed if possible, and go to next step in act process: herbivoreAct.
        if (checkHunger(world)) {
            nextMove(world);
            checkBreed(world);
            herbivoreAct(world);
        }
    }

    /**
     * explain here why we chose to have an herbivoreAct method
     */
    public void herbivoreAct(World world) {

    }

    /**
     * Makes a baby of the animal in the parameter.
     * Creates a new instance of that type of animal.
     * Spawns at a random possible location.
     */
    public void breed(World world, Animal animal) {
        // Retrieve current location
        Location random_location = getRandomSurroundingFreeLocation(world);

        // Checks if there is a possible spawn location.
        if (random_location != null) {

            // Finds which type of animal to make baby of.
            Animal offspring = null;
            String animal_type = animal.getType();
            switch (animal_type) {
                case "rabbit":
                    offspring = new Rabbit(id_generator);
                    break;
                case "wolf":
                    //baby = new Wolf(id_generator);
                    break;
                default:
                    //baby = new Bear(id_generator);
            }

            // Spawns baby and adds to location and id maps.
            world.setTile(random_location, offspring);
            assert offspring != null; // We promise that a baby exists.
            id_generator.addAnimalToIdMap(offspring.getId(), offspring);
            id_generator.addLocationToIdMap(random_location, offspring.getId());
        }
    }

    /**
     * Checks if the circumstances for breeding are met:
     * If there is a nearby same animal type;
     * If animal is older than 20;
     * If there is space for one more animal.
     * ERROR: SOMETIMES BREEDS ITSELF
     */
    public void checkBreed(World world) {
        // eventually check if the mating animal is of opposite gender
        // eventually check if mating animal also fulfills criteria.

        // If there is another one of its type in the surrounding tiles.
        for (Location location : world.getSurroundingTiles(world.getLocation(this), 1)) {
            if (location != world.getLocation(this)) { // this part is redundant as get surrounding doesn't include center

                if (id_generator.getOrganism(location) != null && id_generator.getOrganism(location).getType().equals(type)) {

                    // If the animal is in breeding age (older than 10% and younger than 90% of its age).
                    if (age >= max_age * 0.1) {
                        if (age <= max_age * 0.9) {

                            // If there is space for one more animal.
                            if (!world.getEmptySurroundingTiles().isEmpty()) {

                                // If they haven't bred in 10 steps.
                                if (days_since_last_birth >= 5) {
                                    breed(world, this);
                                    days_since_last_birth = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Animal dies of hunger if it is hungrier than its max hunger.
     * If the animal dies from hunger, method returns false.
     */
    public boolean checkHunger(World world) {
        if (hunger > max_hunger) {
            return false;
        } return true;
    }

    /**
     * Moves to new tile, if there is a free surrounding tile.
     */
    public void nextMove(World world) {
        // Gets random location.
        Location random_location = getRandomSurroundingFreeLocation(world);

        // Moves to new location, if location isn't null.
        if (random_location != null) {
            world.move(this, getRandomSurroundingFreeLocation(world));
        }
    }
}
