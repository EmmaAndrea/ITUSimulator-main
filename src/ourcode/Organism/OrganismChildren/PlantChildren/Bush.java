package ourcode.Organism.OrganismChildren.PlantChildren;

import ourcode.Organism.OrganismChildren.Plant;
import ourcode.Setup.IDGenerator;

public class Bush extends Plant {
    protected int berries;
    public Bush(IDGenerator idGenerator){
        super(idGenerator);
        berries = 0;
        nutritional_value = 3;
    }

    public void grow(){
        berries =+ 3;
    }

    public int getBerries(){
        return berries;
    }

    public void eatBerries(){
        berries =-3;
    }
}
