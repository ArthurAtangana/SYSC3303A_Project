package Messaging.Messages.Commands;

public record UnloadPassengersCommand(int elevNum) implements SystemCommand {
    /**
     * Match elevator number with the given key.
     *
     * @param key The elevator number of the elevator trying to process this event.
     * @return True if the elevator has the appropriate key, indicating this message is addressed to it.
     */
    @Override
    public boolean matchKey(int key) {
        return key == elevNum;
    }
}
