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

    public CornerSpace(String name, SpaceType type, SpaceType cornerType) {
        this.name = name;
        this.type = type;
        this.cornerType = cornerType;
    }

    public String getName() {
        return name;
    }
    
    public void performSpaceAction(Player p) {
        switch (type) {
            case SPACE_GO:
                p.setMoney(p.getMoney() + 200);
                break;
            case SPACE_JAIL: // This is handled in the playerTurn method
                break;
            case SPACE_PARKING:
                break;
            case SPACE_GO_JAIL:
                p.setPosition(10); // Position of Jail
                p.setJail(true);
                break;
        }
    }
    
    public SpaceType getType() {
        return type;
    }
    
    public SpaceType getCornerType() {
        return cornerType;
    }
}
