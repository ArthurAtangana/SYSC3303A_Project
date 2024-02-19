package Subsystem;

import Configuration.Config;
import Configuration.Configurator;
import Messaging.Receivers.DMA_Receiver;
import Messaging.Receivers.Receiver;
import StatePatternLib.Context;

public abstract class SubsystemContext extends Context {

    // Static config loaded and shared across all subsystems
    private static final String JSON_FILEPATH = "res/system-config-00.json";
    protected static final Config CONFIG = (new Configurator(JSON_FILEPATH).getConfig());

    // Fields common in each subsystem
    protected final Receiver rx;
    protected final int key;

    protected SubsystemContext(int key) {
        super();
        this.rx = new DMA_Receiver(key); // TODO: Replace by UDP receiver later
        this.key = key;
    }

    public Receiver getRx() {
        return rx;
    }
}
