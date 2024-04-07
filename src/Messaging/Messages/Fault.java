package Messaging.Messages;

/**
 * Enum for fault types.
 *
 * @version iteration-4
 */
public enum Fault {
    NONE,
    TRANSIENT,
    HARD;

    public static Fault fromInt(int faultInt){
        Fault res;
        switch (faultInt){
            case 1 -> res = Fault.TRANSIENT;
            case 2 -> res = Fault.HARD;
            default -> res = Fault.NONE;
        }
        return res;
    }
}