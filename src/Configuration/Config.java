package Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 *  Config class which contains system configuration data.
 *
 * @author M. Desantis
 * @version 20240213
 */
public class Config {

    /* Instance Variables */
    private int numFloors;
    private int numElevators;
    private long travelTime;
    private long loadTime;

    /* Constructors */

    /**
     * Default constructor for class Config.
     */
    public Config() {
        this.numFloors = 0;
        this.numElevators = 0;
        this.travelTime = 0;
        this.loadTime = 0;
    }

    /* Methods */

    public int getNumFloors() {
        return numFloors;
    }

    public int getNumElevators() {
        return numElevators;
    }

    public long getTravelTime() {
        return travelTime;
    }

    public long getLoadTime() {
        return loadTime;
    }

}
