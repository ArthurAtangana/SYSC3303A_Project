package Configuration;

/**
 *  Config class which contains system configuration data.
 *
 * @author M. Desantis
 * @version Iteration-2
 */
public class Config {

    /* Instance Variables */
    private final int numFloors;
    private final int numElevators;
    private final long travelTime;
    private final long loadTime;
    private final String inputFilename;

    /* Constructors */

    /**
     * Default constructor for class Config.
     */
    public Config() {
        this.numFloors = 0;
        this.numElevators = 0;
        this.travelTime = 0;
        this.loadTime = 0;
        this.inputFilename = "";
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

    public String getInputFilename() {
        return inputFilename;
    }

    public void printConfig() {
        System.out.println("Launching system under the following configuration:");
        System.out.println("-- numFloors: " + getNumFloors());
        System.out.println("-- numElevators: " + getNumElevators());
        System.out.println("-- travelTime: " + getTravelTime() + " [ms]");
        System.out.println("-- loadTime: " + getLoadTime() + " [ms]");
        System.out.println("-- inputFilename: " + getInputFilename());
        System.out.println();
    }

}
