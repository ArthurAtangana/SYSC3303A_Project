package Logging;

/**
 *  Logger class which handles system logging via console print to `stdout`.
 *
 * @author M. Desantis
 * @version Iteration-3
 */
public class Logger {

    /* Enums */
    public enum LEVEL {
        INFO,  // INFO level messages for general prints
        DEBUG  // DEBUG level messages for additional debug prints
    }

    /* Instance Variables */
    private final int verbosity;

    /* Constructors */

    /**
     * Default constructor for class Logger.
     *
     * @param verbosity The verbosity level, which should be set in 
     * the system configuration JSON and supplied here. 
     *   0: indicates no messages should be logged (suppress output)
     *   1: indicates only INFO level messages should be logged
     *   2: indicates INFO and DEBUG messages should be logged
     */
    public Logger(int verbosity) {
        this.verbosity = verbosity;
    }

    /* Methods */

    /**
     * Log a message by printing to console, specifying the log level, the 
     * identifier of the message source, and the message.
     *
     * @param level Log level: [Logger.INFO | Logger.DEBUG]
     * @param logId Log ID for message source (eg. "Elevator 1")
     * @param message Message to log
     *
     */
    public void log(Logger.LEVEL level, String logId, String message) {

        // The prefix for this message
        String prefix = "[" + level + "::" + logId + "] ";

        // Case: Print INFO level messages only
        //if ((verbosity == 1) && (level == Logger.LEVEL.INFO)) {
        if (verbosity > 0) {
            // Case: Print INFO level messages only
            if (level == Logger.LEVEL.INFO) {
                System.out.println(prefix + message);
            }
            if ((verbosity == 2) && (level == Logger.LEVEL.DEBUG)) {
                System.out.println(prefix + message);
            }
        }
    }

}
