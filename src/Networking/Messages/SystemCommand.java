package Networking.Messages;

/**
 * Interface definition for all commands passed in the system to inherit.
 * Commands are defined as messages used to refer to a SPECIFIC instance of a subsystem (through key matching).
 *
 * Information needed to execute the command activity/action is passed, along with a key identifier.
 * Commands usually trigger more complex actions, often with a significant processing time.
 *
 * @author Alexandre Marques
 * @version Iteration 2
 */
public interface SystemCommand extends SystemMessage {
    /**
     * Matches the key provided against the intended key (stored in the command)
     * to make sure the correct instance processes this command.
     * @param key The instance's key to match against.
     * @return True IFF the keys match and the instance should be processing the command.
     */
    boolean matchKey(int key);
}
