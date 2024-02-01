/**
 * Parser class which parses an input text file adherent to system
 * specifications. As per the specifications, this Parser, as a component
 * of the FloorSubsystem, will pass its parsed objects (Passenger objects)
 * to awaiting queues in the Scheduler thread, and also (as a somewhat
 * redundant exercise, but required by the spec) directly to a Floor thread.
 *
 * How, you might ask? I don't know yet!
 *
 * @author Michael De Santis
 * @version 20240126
 */

package FloorSubsystem;

import Networking.Direction;
import Networking.Passenger;
import SchedulerSubsystem.Scheduler;

import java.io.*;
import java.nio.Buffer;
import java.util.*;

import static java.lang.Float.min;
import static java.lang.Float.parseFloat;


public class Parser {

    /* Constants */

    // Delimiter RegEx
    private final String DELIMITER = " ";
    private final String COMMENT = "#";

    /* Instance Variables */

    // ArrayList of Passenger Objects from input file
    private ArrayList<Passenger> passengers;

    // The filename of the input file to parse
    private String filename;

    // This Parser's Scheduler
    private Scheduler scheduler;

    // Reference time that will be denoted as t=0 (t nought)
    private long startTime;

    // Hacky --verbose flag
    boolean verbose;

    /* Constructors */

    /**
     * Parametric constructor for this Parser.
     *
     * @param filename The filename of the input file to operate on.
     * @param scheduler The Scheduler this Parser must dispatch to.
     */
    public Parser(String filename, Scheduler scheduler) {
        // Initialize fields
        this.filename = filename;
        this.scheduler = scheduler;
        this.passengers = new ArrayList<Passenger>();
        this.startTime = 0;
        this.verbose = false;
    }

    /* Methods */

    /* Getters and Setters */

    public long getStartTime() {
        return startTime;
    }

    public int getNumPassengers() {
        return passengers.size();
    }

    /* Involved Methods */

    /**
     * Convert a time string, as per the project specification,
     * to a long representing the total milliseconds elapsed since
     * the start of the day (00:00:00.000).
     *
     * @param timeString The input time string, as per spec.
     * @return The total milliseconds from 00:00:00.000.
     *
     * TODO: unfuglify before anyone else sees shame shame shame
     */
    public long timeStringToLong(String timeString) {

        // Conversions
        int hoursToMilliseconds = (1 * 60 * 60 * 1000);
        int minutesToMilliseconds = (1 * 60 * 1000);
        int secondsToMilliseconds = (1 * 1000);

        // Result
        long totalMilliseconds = 0;

        // First split on ':'
        String[] timeStringArr = timeString.split(":");
        if (verbose) {
            int i = 0;
            for (String s : timeStringArr) {
                i++;
            }
        }

        // Calculate milliseconds from hours hh
        // 1h * 60m/h * 60s/m * 1000ms/s
        long hourMilliseconds = Long.parseLong(timeStringArr[0]) * hoursToMilliseconds;

        // Calculate milliseconds from minutes mm
        // 1m * 60s/m * 1000ms/s
        long minuteMilliseconds = Long.parseLong(timeStringArr[1]) * minutesToMilliseconds;

        // Need to split on '.' now for seconds and milliseconds, since we have a new delimiter
        String[] secondsMillisecondsStringArr = timeStringArr[2].split("\\.");

        // Calculate milliseconds from seconds ss
        // 1s * 1000ms/s
        long secondsMilliseconds = Long.parseLong(secondsMillisecondsStringArr[0]) * secondsToMilliseconds;

        // Calculate milliseconds from milliseconds mmm
        // 1ms
        long millisecondsMilliseconds = Long.parseLong(secondsMillisecondsStringArr[1]);

        // Sum
        totalMilliseconds = hourMilliseconds + minuteMilliseconds + secondsMilliseconds + millisecondsMilliseconds;

        if (verbose) System.out.println("Input Time String:      " + timeString);
        if (verbose) System.out.println("Output Time Long [ms]:  " + totalMilliseconds);

        // Return total milliseconds
        return totalMilliseconds;

    }

    /**
     * Parse a single string to Passenger thing.
     *
     * @param string The string to Parse.
     * @return The created Passenger record.
     */
    public Passenger stringToPassenger(String string) {

        // Passenger for return
        Passenger passenger;

        // Splits on space
        String[] splits = string.split(DELIMITER);

        // Prepare the Passenger record fields
        long arrivalTime = timeStringToLong(splits[0]);
        // startTime is only gonna be 0 until first line is parsed (hacky)
        if (startTime == 0) {
            startTime = arrivalTime;
        }
        // Cast it down to int to save on bits
        long relativeArrivalTime = arrivalTime - startTime;
        if (verbose) System.out.println("relativeArrivalTime: " + relativeArrivalTime);
        int sourceFloor = Integer.parseInt(splits[1]);
        // Assume valid input, can validate later if needed
        Direction direction = Direction.UP;
        if (splits[2].equals("Down")) {
            direction = Direction.DOWN;
        }
        int destinationFloor = Integer.parseInt(splits[3]);

        // Create record
        passenger = new Passenger(relativeArrivalTime, sourceFloor, direction, destinationFloor);

        if (verbose) {
            System.out.println("Initial String:\n" + string);
            System.out.println("Passenger Record:\n" + passenger.toString());
        }
        return passenger;
    }

    /**
     * Parse an input file, converting each line to a new Passenger record,
     * and accumulate all Passenger's in this Parser's queue.
     *
     * NB: This program does NO validation of strings in the input file.
     */
    public void fileToPassengers() {

        String line;
        BufferedReader bufferedReader;
        System.out.println("Reading file: " + filename);
        // TODO: Investigate pathing - might be an IntelliJ thing
        String relativeFilename = System.getProperty("user.dir") + "/src/FloorSubsystem/" + filename;

        // Try to read input file
        try {

            // Create reader
            bufferedReader = new BufferedReader(new FileReader(relativeFilename));

            // Loop through lines
            while ((line = bufferedReader.readLine()) != null) {

                // Avoid empty and/or commented lines
                if (line.length() > 0) {
                    // Scan past comment lines
                    if (line.charAt(0) == '#') {
                        if (verbose) System.out.println("* Ignored comment. *");
                        //line = bufferedReader.readLine();
                    }
                    else {
                        // Print it
                        if (verbose) System.out.println("line: " + line);

                        // Ingest this line as a Passenger
                        Passenger passenger = stringToPassenger(line);
                        if (verbose) System.out.println("Passenger created:\n" + passenger.toString() +
                                "\n");

                        // Add to queue
                        passengers.add(passenger);
                        if (verbose) System.out.println("Passenger added to list!");
                    }
                }
            }

        }
        catch (IOException e) {
            // File not found
            e.printStackTrace();
            System.exit(1);
        }

        if (verbose) {
            System.out.println("Created " + passengers.size() + " Passengers from input file " + filename + ".");
        }
    }


}
