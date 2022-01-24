/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Interface for a Space object
 */
package andrewcoledaniel_monopoly;

/**
 *
 * @author anfeh1812
 */
public interface Space {
    
    public enum SpaceType {
        SPACE_GO,
        SPACE_JAIL,
        SPACE_PARKING,
        SPACE_GO_JAIL,
        SPACE_DEED,
        SPACE_TAX,
        SPACE_RAILROAD,
        SPACE_UTILITY,
        SPACE_CARD
    }
    
    /**
     * Get type of space
     * @return type of space
     */
    public SpaceType getType();
    
    /**
     * Get name of space
     * @return name of space
     */
    public String getName();
    
    /**
     * Return string representation of Space
     * @return string representation of Space
     */
    public String toString();

}
