package Mains;

import Configuration.Config;
import Configuration.Configurator;
import Messaging.Transceivers.TransceiverDMAFactory;
import Messaging.Transceivers.TransceiverFactory;
import Subsystem.ElevatorSubsytem.Elevator;
import Subsystem.FloorSubsystem.Floor;
import Subsystem.SchedulerSubsystem.Scheduler;

/**
 * Mains.MainCombinedDMA initializes and maintains track of threads.
 *
 * @version Iteration-2
 */
public class MainCombinedDMA {
    /**
     * All subsystems combined start procedure (using DMA):
     * 1. Load numFloors, numElevators from config
     * 2. Create transceiver factory
     * 3. Create and start subsystems, (start Scheduler first).
     * 4. Start dispatcher.
     */
    public static void main(String[] args) {
        // 1. Configure system from JSON
        System.out.println("\n****** Configuring System ******\n");
        Config config = (new Configurator().getConfig());
        config.printConfig();

        // 2. Create factory
        TransceiverFactory dmaFactory = new TransceiverDMAFactory();

        // 3. Create and start Subsystem threads
        Scheduler scheduler = new Scheduler(config, dmaFactory.createServerReceiver(),
                dmaFactory.createServerTransmitter(),
                dmaFactory.createServerTransmitter());
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();

        // Floor and elevator thread creation
        for (int i = 1; i < config.getNumFloors() + 1; ++i) {
            new Thread(new Floor(config, i, dmaFactory.createClientReceiver(i),
                    dmaFactory.createClientTransmitter())).start();
        }
        //for (int i = 0; i < config.getNumElevators(); ++i) {
        for (int i = 1; i <= config.getNumElevators(); ++i) {
            new Thread(new Elevator(config, i, dmaFactory.createClientReceiver(i),
                    dmaFactory.createClientTransmitter())).start();
        }

        // Start dispatcher
        MainUtils.startDispatcher(dmaFactory);
    }
}