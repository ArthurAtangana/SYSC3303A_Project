/**
 * Parser class which parses an input text file to simulate input events to
 * the system. As per the project requirements, each well-formed line of
 * the input file will be parsed into a discrete data object containing the
 * necessary information to model a system input at its boundaries. These 
 * data objects are accumulated to an array, and are made available to a
 * relevant dispatcher class that will be responsible for coordinating their
 * issuance in real-time.
 *
 * Assumptions:
 * -----------
 * 1. The input file contains a sequence of simulated events, with each event
 *    occurring as a string adherent to project specifications on a newline,
 *    and arranged chronologically.
 * 2. All input file event strings are assumed to contain information correct
 *    to the system (ie. no floors out of range) as no validation is performed
 *    by this parser.
 *
 * Notes:
 * -----
 * 1. Although time measurements are codified in a 24-hour clock in the input
 *    file, as per specification, the parsed objects maintain only a relative
 *    count of time, in milliseconds, from the start of a day.
 * 2. The input file should be placed adjacently in the Parser's directory, and
 *    only the file name, and not path, shall be argued to this Parser.
 * 3. Input line format:
 *      hh:mm:ss.mmm n [ Up | Down ] n
 *    Example:
 *      09:05:22.123 1 Up 7
 *
 * Public Usage:
 * ------------
 * 1. Instantiate a Parser object.
 *
 *      Parser parser = new Parser();
 *      
 * 2. Parse an input file and catch the returned ArrayList of objects.
 *
 *      ArrayList<FloorInputEvent> floorInputEvents = parser.parse("input-file.txt");
 *
 * @author Michael De Santis
 * @version 20240202
 */

package FloorSubsystem;

import Networking.Direction;
import Networking.Messages.FloorInputEvent;

import java.io.*;
import java.util.*;


public class Parser {

    /* Constants */

    // Delimiter RegEx constants
    private final String WS_DELIMITER = " ";
    private final String TIME_DELIMITER_ONE = ":";
    private final String TIME_DELIMITER_TWO= "\\.";
    // Input file comment char
    private final char COMMENT_CHAR = '#';

    /* Instance Variables */

    /* Constructors */

    /**
     * Default constructor for this Parser.
     *
     */
    public Parser() {}

    /* Methods */

    /**
     * Convert a time string, as per the project specification, to a long
     * representing the total milliseconds elapsed since the start of the
     * day in a 24-hour clock (00:00:00.000).
     *
     * @param timeString The input time string, as per spec.
     * @return The total milliseconds from 00:00:00.000.
     */
    private long timeStringToLong(String timeString) {

        /* Conversion factors */
        // Hours to milliseconds: 1h * 60m/h * 60s/m * 1000ms/s
        final int hoursToMilliseconds = (1 * 60 * 60 * 1000);
        // Minutes to milliseconds: 1m * 60s/m * 1000ms/s
        final int minutesToMilliseconds = (1 * 60 * 1000);
        // Seconds to milliseconds: 1s * 1000ms/s
        final int secondsToMilliseconds = (1 * 1000);

        // Result for return
        long totalMilliseconds = 0;

        // First split on ':' to yield: [hh, mm, ss.mmm]
        String[] timeStringArr = timeString.split(TIME_DELIMITER_ONE);

        // Calculate milliseconds from hours [hh]
        long hourMilliseconds = Long.parseLong(timeStringArr[0]) * hoursToMilliseconds;

        // Calculate milliseconds from minutes [mm]
        long minuteMilliseconds = Long.parseLong(timeStringArr[1]) * minutesToMilliseconds;

        // Second split on '.' to yield: [ss, mmm]
        String[] secondsMillisecondsStringArr = timeStringArr[2].split(TIME_DELIMITER_TWO);

        // Calculate milliseconds from seconds [ss]
        long secondsMilliseconds = Long.parseLong(secondsMillisecondsStringArr[0]) * secondsToMilliseconds;

        // Get milliseconds from milliseconds [mmm]
        long millisecondsMilliseconds = Long.parseLong(secondsMillisecondsStringArr[1]);

        // Sum milliseconds
        totalMilliseconds = hourMilliseconds + minuteMilliseconds + secondsMilliseconds + millisecondsMilliseconds;

        // Return total milliseconds
        return totalMilliseconds;
    }

    /**
     * Parse a single string to FloorInputEvent record.
     *
     * @param string The string to Parse.
     * @return The created FloorInputEvent record.
     */
    private FloorInputEvent stringToFloorInputEvent(String string) {
        
        // FloorInputEvent for return
        FloorInputEvent floorInputEvent;

        // Splits on space
        String[] splits = string.split(WS_DELIMITER);

        /* Prepare the FloorInputEvent record fields before construction */

        // arrivalTime
        long arrivalTime = timeStringToLong(splits[0]);

        // sourceFloor
        int sourceFloor = Integer.parseInt(splits[1]);

        // direction
        Direction direction = Direction.UP;
        if (splits[2].equals("Down")) {
            direction = Direction.DOWN;
        }

        // destinationFloor
        int destinationFloor = Integer.parseInt(splits[3]);

        // Create and return FloorInputEvent record with the above values as fields
        floorInputEvent = new FloorInputEvent(arrivalTime, sourceFloor, direction, destinationFloor);

        return floorInputEvent;
    }

    /**
     * Parse an input file, converting each line to a new FloorInputEvent record,
     * and return an ArrayList<FloorInputEvent> of the instantiated records.
     *
     * @param filename The file name of the file to parse.
     * @return The ArrayList of instantiated FloorInputEvent records.
     */
    public ArrayList<FloorInputEvent> parse (String filename) {

        // Local variables
        String line;
        BufferedReader bufferedReader;
        ArrayList<FloorInputEvent> floorInputEvents = new ArrayList<FloorInputEvent>();

        // File pathing
        System.out.println("Parser: Reading input file: " + filename);

        // Try: to read and parse input file
        try {

            // Create reader for file
            bufferedReader = new BufferedReader(new FileReader(filename));

            // While: there are lines in file
            while ((line = bufferedReader.readLine()) != null) {

                // Case: Ignore empty lines
                if (line.length() > 0) {
                    // Case: Non-comment line
                    if (!(line.charAt(0) == COMMENT_CHAR)) {
                        // Ingest this line as a FloorInputEvent
                        FloorInputEvent floorInputEvent = stringToFloorInputEvent(line);
                        // Add to queue
                        floorInputEvents.add(floorInputEvent);
                    }
                }
            }
        }
        // Catch: IOException, print trace, and exit abnormally
        catch (IOException e) {
            // File not found
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Parser: Created " + floorInputEvents.size() + " FloorInputEvents from input file " + filename + ".");

        return floorInputEvents;
    }
}
