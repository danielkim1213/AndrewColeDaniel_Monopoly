/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a corner space on the board
 */
package andrewcoledaniel_monopoly;

import andrewcoledaniel_monopoly.Space.SpaceType;

/**
 *
 * @author anfeh1812
 */
public class CornerSpace implements Space {

    private SpaceType type;
    private SpaceType cornerType;
    private String name;

    /**
     * Primary constructor, create CornerSpace with values
     *
     * @param name name of space
     * @param type type of space
     * @param cornerType type of CornerSpace
     */
    public CornerSpace(String name, SpaceType type, SpaceType cornerType) {
        this.name = name;
        this.type = type;
        this.cornerType = cornerType;
    }

    /**
     * Get name of space
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Perform space action
     *
     * @param p Player object
     */
    public void performSpaceAction(Player p) {
        switch (type) {
            // Go space
            case SPACE_GO:
                p.setMoney(p.getMoney() + 200);
                break;
            // Jail space
            case SPACE_JAIL: // This is handled in the playerTurn method
                break;
            // Free parking space
            case SPACE_PARKING:
                break;
            // Go to Jail space
            case SPACE_GO_JAIL:
                p.setPosition(10); // Position of Jail
                p.setJail(true);
                break;
        }
    }

    /**
     * Get type of space
     *
     * @return type of space
     */
    public SpaceType getType() {
        return type;
    }

    /**
     * Get type of CornerSpace
     *
     * @return type of CornerSpace
     */
    public SpaceType getCornerType() {
        return cornerType;
    }

    /**
     * Return string representation of object
     *
     * @return string representation of object
     */
    public String toString() {
        return "CornerSpace:\n Type: " + name + "\n";
    }
}
