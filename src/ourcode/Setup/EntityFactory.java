package ourcode.Setup;

/**
 * Functional interface representing a factory for creating Entity objects.
 * This interface is used to define a method that can instantiate different types of entities
 * within the simulation environment. Implementations of this interface provide a way to generate
 * specific entity types dynamically.
 */
@FunctionalInterface
public interface EntityFactory {

    /**
     * Creates and returns a new instance of an Entity.
     * This method should be implemented to construct specific types of entities, depending on the context in which
     * the EntityFactory is used.
     *
     * @return A new instance of an Entity.
     */
    Entity create();
}