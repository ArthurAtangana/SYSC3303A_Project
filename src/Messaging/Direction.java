/**
 * Enum to hold elevator state for directionality of travel.
 *
 * @version Iteration-2
 */

package Messaging;

public enum Direction {
    UP(1),
    DOWN(-1);

    private final int displacement;

    Direction(int displacement) {
        this.displacement = displacement;
    }

    public int getDisplacement() {
        return displacement;
    }
}
