/**
 *
 *  Parser class which parses an input text file adherent to system
 * specifications.
 *
 * @author Michael De Santis
 * @version 20240126
 */

import java.io.*;
import java.nio.Buffer;
import java.util.*;

public class Parser {

    /* Instance Variables */
    //private File file;
    private String filename;
    private final String DELIMITER = " ";

    //private FloorSubsystem floorSubsystem;

    /* Constructors */

    /**
     * Parametric constructor for this Parser.
     *
     * @param file Input text file.
     * @param floorSubsystem This Parser's invoking FloorSubsystem
     */
    //public Parser(String filename, FloorSubsystem floorSubsystem) {
    public Parser(String filename) {
        // Initialize fields
        this.filename = filename;
        //this.FloorSubsystem = floorSubsystem;
    }

    /* Methods */

    private void parseStringToObject() {
        String line;
        BufferedReader bufferedReader;

        // Check file
        try {

            // Create reader
            bufferedReader = new BufferedReader(new FileReader(this.filename));

            // Loop through lines
            while ((line = bufferedReader.readLine()) != null) {

                // Split this line to object
                // line contains this line, if not null

                // Split the string into whitespace delimited fields, each as an element in a String[]
                String[] stringArray = line.split(DELIMITER);
                assert(stringArray.length == 4);

                // Create new ServiceObject
                ServiceObject serviceObject = new ServiceObject();

                // Populate ServiceObject with stringArr elements
                serviceObject.setTime(stringArr[0]);
                serviceObject.setRequestOriginFloor(stringArr[1]);
                serviceObject.setRequestDirection(stringArr[2]);
                serviceObject.setRequestDestinationFloor(stringArr[3]);

                // Add populated service Object to FloorSubsystem Queue
                floorSubsystem.addServiceObject(serviceObject);
            }

            // All lines processed, all object added to queue
        }
        catch (IOException e) {
            // File not found
            e.printStackTrace();
            System.exit(1);
        }

        //
    }


}
