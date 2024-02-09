package Networking.Messages;

import java.util.ArrayList;

public record PassengerLoadEvent(ArrayList<DestinationEvent> passengers) implements SystemEvent {
}
