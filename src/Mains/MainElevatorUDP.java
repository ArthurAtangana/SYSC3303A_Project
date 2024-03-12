package Mains;

import Configuration.Config;
import Configuration.Configurator;
import Messaging.Transceivers.TransceiverDMAFactory;
import Messaging.Transceivers.TransceiverFactory;
import Subsystem.ElevatorSubsytem.Elevator;

public class MainElevatorUDP {
    /**
     * All subsystems combined start procedure (using DMA):
     * 1. Load numElevators from config
     * 2. Create transceiver factory
     * 3. Create and start elevator subsystem.
     */
    public static void main(String[] args) {
        // 1. Configure system from JSON
        System.out.println("\n****** Configuring System ******\n");
        Config config = (new Configurator().getConfig());
        config.printConfig();

        // 2. Create factory
        TransceiverFactory dmaFactory = new TransceiverDMAFactory();

        // 3. Create and start elevator threads
        for (int i = 0; i < config.getNumElevators(); ++i) {
            new Thread(new Elevator(i, dmaFactory.createClientReceiver(i),
                    dmaFactory.createClientTransmitter())).start();
        }
    }
}
