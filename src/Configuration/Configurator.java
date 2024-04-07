package Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 *  Configurator class which parses a given JSON file and makes its data
 *  publicly available in a constructed Config Java object.
 *
 * @author M. Desantis
 * @version Iteration 2
 */
public class Configurator {
    private static final String DEFAULT_CONFIG_PATH = "res/system-config-00.json";

    /* Instance Variables */

    // Container for ingested JSON configuration data
    private final Config config;

    /**
     * Parametric constructor for Configurator class.
     *
     * @param jsonFilename The JSON file to read config from. Should be pathed
     *                     from the top-level project directory.
     */
    public Configurator (String jsonFilename) {
        System.out.println("Reading system configuration from \"" + jsonFilename + "\"...");

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
    }

    /**
     * Default constructor for Configurator class, uses hardcoded default config file.
     */
    public Configurator() {
        this(DEFAULT_CONFIG_PATH);
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
        config.printConfig();
    }
}