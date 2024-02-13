import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 *  Configurator class which parses a given JSON file and makes its data
 *  publicly available in a constructed Config Java object.
 *
 * @author M. Desantis
 * @version 20240213
 */
public class Configurator {

    /* Instance Variables */

    // Container for ingested JSON configuration data
    private Config config;

    /**
     * Parametric constructor for Configurator class.
     *
     * @param jsonFilename The JSON file to read config from. Should be pathed
     *                     from the top-level project directory.
     */
    public Configurator (String jsonFilename) {

        // Get the system config JSON file
        File jsonFile = new File(jsonFilename);

        // Create ObjectMapper to map JSON fields to class instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Read in the JSON file and map it to static inner class Config
        try {
            this.config = objectMapper.readValue(jsonFile, Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return;
    }

    /* Methods */
    public Config getConfig() {
        return this.config;
    }

    /**
     * Main method for this class. Can be run as a quick manual test of this
     * class's functionality and JSON file validity.
     *
     * @param args CLI arguments.
     */
    public static void main(String[] args) {
        // Ingest config
        String jsonFilename = "res/system-config-00.json";
        Configurator configurator = new Configurator(jsonFilename);
        Config config = configurator.getConfig();

        // Print
        System.out.println();
        System.out.println("*** Printing parsed config values from \"" + jsonFilename +"\":");
        System.out.println("-- numFloors: " + config.getNumFloors());
        System.out.println("-- numElevators: " + config.getNumElevators());
        System.out.println("-- travelTime: " + config.getTravelTime());
        System.out.println("-- loadTime: " + config.getLoadTime());
        System.out.println();

        return;
    }

    /**
     * Static inner class of Configurator. Container for parsed JSON data.
     */
    public static class Config {

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

}
