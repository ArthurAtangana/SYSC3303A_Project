package Networking.Events;

/**
 * Interface definition for all commands passed in the system to inherit.
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
