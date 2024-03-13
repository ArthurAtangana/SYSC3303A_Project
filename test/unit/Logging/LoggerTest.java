/**
 * Test class for Logger class.
 *
 * Since this class only tests system prints, requires visual inspection of
 * output.
 *
 * @author Michael De Santis
 * @version Iteration-3
 */
package unit.Logging;

import Logging.Logger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoggerTest {

    private final String logId = "LoggerTest";

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testLog() {

        System.out.println("\n****** START: testLog() ******");

        // System verbosity
        int verbosity;

        // Message storage
        String message;

        // Message level storage
        Logger.LEVEL level;

        /* Test verbosity == 0 */
        // Expect nothing (ie. no prints)
        System.out.println("\n*** Test: verbosity == 0 ***");
        System.out.println("Expect no messages to print below this one.");
        verbosity = 0;
        Logger silentLogger = new Logger(verbosity);
        level = Logger.LEVEL.DEBUG;
        message = "testing silent mode";
        silentLogger.log(level, logId, message);

        /* Test verbosity == 1 */
        // Expect only INFO level messages to print
        System.out.println("\n*** Test: verbosity == 1 ***");
        System.out.println("Expect only INFO level messages to print below this one.");
        verbosity = 1;
        Logger infoLogger = new Logger(verbosity);
        level = Logger.LEVEL.DEBUG;
        message = "verbosity==1: testing DEBUG message";
        infoLogger.log(level, logId, message);
        level = Logger.LEVEL.INFO;
        message = "verbosity==1: testing INFO message";
        infoLogger.log(level, logId, message);

        /* Test verbosity == 2 */
        // Expect both INFO and DEBUG level messages to print
        System.out.println("\n*** Test: verbosity == 2 ***");
        System.out.println("Expect both INFO and DEBUG level messages to print below this one.");
        verbosity = 2;
        Logger debugLogger = new Logger(verbosity);
        level = Logger.LEVEL.DEBUG;
        message = "verbosity==2: testing DEBUG message";
        debugLogger.log(level, logId, message);
        level = Logger.LEVEL.INFO;
        message = "verbosity==2: testing INFO message";
        debugLogger.log(level, logId, message);

        System.out.println("\n****** END: testLog() ******");
    }

}
