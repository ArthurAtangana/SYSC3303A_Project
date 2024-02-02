package FloorSubsystem;

import Networking.Direction;
import Networking.Passenger;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private String filename = "input-file.txt";
    private Parser parser;
    private ArrayList<Passenger> passengers;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        parser = new Parser();
        passengers = new ArrayList<Passenger>();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testTimeStringToInt() {

        System.out.println("\n****** START: testTimeStringToInt() ******\n");

        String timeString;
        long timeLong;

        // Test min value
        timeString = "00:00:00.000";
        timeLong = 0;
        assertEquals(timeLong, parser.timeStringToLong(timeString));

        // Test AM value
        timeString = "10:20:30.123";
        timeLong = 37230123;

        assertEquals(timeLong, parser.timeStringToLong(timeString));

        // Test PM value
        timeString = "17:45:19.543";
        timeLong = 63919543;
        assertEquals(timeLong, parser.timeStringToLong(timeString));

        // Phew!
        System.out.println("*** All Tests Passed ***");

        System.out.println("\n****** END: testTimeStringToInt() ******\n");

    }

    @Test
    void testStringToPassenger() {

        System.out.println("\n****** START: testStringToPassenger() ******\n");

        String inputString;

        // Test: First Passenger, who determines relativeStartTime
        Passenger p0Actual;
        Passenger p0Expected;
        inputString = "10:20:30.123 1 Up 7";
        p0Actual = parser.stringToPassenger(inputString);
        System.out.println("p0Actual.toString():");
        System.out.println(p0Actual.toString());
        // Check our relativeStartTime for this one too!
        assertEquals(p0Actual.arrivalTime(), 37230123 - parser.getStartTime());
        p0Expected = new Passenger(0, 1, Direction.UP, 7);
        // Compare fields
        assertEquals(p0Actual.arrivalTime(), p0Expected.arrivalTime());
        assertEquals(p0Actual.sourceFloor(), p0Expected.sourceFloor());
        assertEquals(p0Actual.direction(), p0Expected.direction());
        assertEquals(p0Actual.destinationFloor(), p0Expected.destinationFloor());

        // Test: Subsequent Passengers (relative arrival times)
        // Arrival at startTime + 1 min (60,000 ms)
        Passenger p1Actual;
        Passenger p1Expected;
        inputString = "10:21:30.123 6 Down 2";
        p1Actual = parser.stringToPassenger(inputString);
        System.out.println("p1Actual.toString():");
        System.out.println(p1Actual.toString());
        // Check our relativeStartTime for this one too!
        assertEquals(p1Actual.arrivalTime(), 37290123 - parser.getStartTime());
        p1Expected = new Passenger(60000, 6, Direction.DOWN, 2);
        // Compare fields
        assertEquals(p1Actual.arrivalTime(), p1Expected.arrivalTime());
        assertEquals(p1Actual.sourceFloor(), p1Expected.sourceFloor());
        assertEquals(p1Actual.direction(), p1Expected.direction());
        assertEquals(p1Actual.destinationFloor(), p1Expected.destinationFloor());

        // Test: Subsequent Passengers (relative arrival times)
        // Arrival at startTime + 100s (100,000 ms)
        Passenger p2Actual;
        Passenger p2Expected;
        inputString = "10:22:10.123 3 Up 5";
        p2Actual = parser.stringToPassenger(inputString);
        System.out.println("p2Actual.toString():");
        System.out.println(p2Actual.toString());
        // Check our relativeStartTime for this one too!
        assertEquals(p2Actual.arrivalTime() - 100000, 0);
        p2Expected = new Passenger(100000, 3, Direction.UP, 5);
        // Compare fields
        assertEquals(p2Actual.arrivalTime(), p2Expected.arrivalTime());
        assertEquals(p2Actual.sourceFloor(), p2Expected.sourceFloor());
        assertEquals(p2Actual.direction(), p2Expected.direction());
        assertEquals(p2Actual.destinationFloor(), p2Expected.destinationFloor());

        // Phew!
        System.out.println("*** All Tests Passed ***");

        System.out.println("\n****** END: testStringToPassenger() ******\n");

    }

    @Test
    void testFileToPassengers() {

        System.out.println("\n****** START: testFileToPassengers() ******\n");

        String filename = "input-file.txt";

        // The number of input strings in text file
        int numInputStrings = 20;
        passengers = parser.parse(filename);

        // Assert our Parser now has 20 Passengers in its queue
        //assertEquals(numInputStrings, parser.getNumPassengers());

        // Phew!
        System.out.println("*** All Tests Passed ***");

        System.out.println("\n****** END: testFileToPassengers() ******\n");
    }

}