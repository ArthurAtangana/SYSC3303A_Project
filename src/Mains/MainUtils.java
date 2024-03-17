package Mains;

import Configuration.Config;
import Configuration.Configurator;
import Messaging.Messages.Events.FloorInputEvent;
import Messaging.Transceivers.TransceiverFactory;
import Subsystem.FloorSubsystem.DestinationDispatcher;
import Subsystem.FloorSubsystem.Floor;
import Subsystem.FloorSubsystem.Parser;

import java.util.ArrayList;

public class MainUtils {
    public static void startDispatcher(TransceiverFactory factory) {
        // 1. Instantiate Parser and parse input file to FloorInputEvents
        System.out.println("\n****** Generating System Input Events ******\n");
        Parser parser = new Parser();
        Config config = (new Configurator().getConfig());
        String inputFilename = config.getInputFilename();
        ArrayList<FloorInputEvent> inputEvents = parser.parse(inputFilename);

        // 2. Start dispatcher (want all systems to be ready before sending events)
        System.out.println("\n****** Begin Real-Time System Operation ******\n");

        new Thread(new DestinationDispatcher(inputEvents,
                factory.createClientTransmitter(),
                Floor.getFloorsTransmitter())).start();
    }
}
