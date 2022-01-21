/*
 * Andrew, Cole, Daniel
 * 2022-01-22
 * Class that represents a corner space on the board
 */
package andrewcoledaniel_monopoly;

/**
 *
 * @author anfeh1812
 */
public class CornerSpace implements Space {

    private int spaceType;
    private String name;

    public CornerSpace(String name, int type) {
        this.name = name;
        spaceType = type;
    }

    public String getName() {
        return name;
    }

    public int getSpaceType() {
        return spaceType;
    }
}
