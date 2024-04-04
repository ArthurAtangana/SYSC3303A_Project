package Mains;

import Configuration.Config;
import Configuration.Configurator;
import Messaging.Transceivers.TransceiverFactory;
import Messaging.Transceivers.TransceiverUDPFactory;
import Subsystem.FloorSubsystem.Floor;

public class MainFloorUDP {
    /**
     * Floor subsystem start procedure:
     * 1. Configure floor subsystem from JSON
     * 2. Create transceiver factory
     * 3. Create and start floor node instances
     * 4. Parse input
     * 5. Put input into dispatcher and start it
     */
    public static void main(String[] args) {
        // 1. Configure floor subsystem from JSON
        System.out.println("\n****** Configuring Floors ******\n");
        Config config = (new Configurator().getConfig());
        config.printConfig();

        // 2. Create transceiver factory
        TransceiverFactory udpFactory = new TransceiverUDPFactory();

        // 3. Create and start floors
        //for (int i = 0; i < (config.getNumFloors()+1); ++i) {
        for (int i = 1; i <= (config.getNumFloors()); ++i) {
            Floor floor = new Floor(config, i, udpFactory.createClientReceiver(i), udpFactory.createClientTransmitter());
            new Thread(floor).start();
        }

        // Start dispatcher
        MainUtils.startDispatcher(udpFactory);
    }
}
