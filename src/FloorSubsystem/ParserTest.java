/**
 * Test class for Parser class.
 *
 * @author Michael De Santis
 * @version 20240202
 */
package FloorSubsystem;

import Networking.Direction;
import Networking.Events.FloorInputEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    private String filename = "input-file.txt";
    private Parser parser;
    private ArrayList<FloorInputEvent> floorInputEvents;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        parser = new Parser();
        floorInputEvents = new ArrayList<FloorInputEvent>();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testTimeStringToInt() {

        System.out.println("\n****** START: testTimeStringToInt() ******");

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
        System.out.println("\n*** All Tests Passed ***");

        System.out.println("\n****** END: testTimeStringToInt() ******");

    }

    @Test
    void testStringToFloorInputEvent() {

        System.out.println("\n****** START: testStringToFloorInputEvent() ******");

        String inputString;

        // Test: First FloorInputEvent, who determines relativeStartTime
        FloorInputEvent p0Actual;
        FloorInputEvent p0Expected;
        inputString = "10:20:30.123 1 Up 7";
        p0Actual = parser.stringToFloorInputEvent(inputString);
        // Check our relativeStartTime
        //assertEquals(p0Actual.time(), 37230123 - parser.getStartTime());
        p0Expected = new FloorInputEvent(0, 1, Direction.UP, 7);
        // Compare fields
        assertEquals(p0Actual.time(), p0Expected.time());
        assertEquals(p0Actual.sourceFloor(), p0Expected.sourceFloor());
        assertEquals(p0Actual.direction(), p0Expected.direction());
        assertEquals(p0Actual.destinationFloor(), p0Expected.destinationFloor());

        // Test: Subsequent FloorInputEvent (relative arrival times from t0)
        // Arrival at startTime + 1 min (60,000 ms)
        FloorInputEvent p1Actual;
        FloorInputEvent p1Expected;
        inputString = "10:21:30.123 6 Down 2";
        p1Actual = parser.stringToFloorInputEvent(inputString);
        // Check our relativeStartTime
        //assertEquals(p1Actual.time(), 37290123 - parser.getStartTime());
        p1Expected = new FloorInputEvent(60000, 6, Direction.DOWN, 2);
        // Compare fields
        assertEquals(p1Actual.time(), p1Expected.time());
        assertEquals(p1Actual.sourceFloor(), p1Expected.sourceFloor());
        assertEquals(p1Actual.direction(), p1Expected.direction());
        assertEquals(p1Actual.destinationFloor(), p1Expected.destinationFloor());

        // Test: Subsequent FloorInputEvent (relative arrival times from t0)
        // Arrival at startTime + 100s (100,000 ms)
        FloorInputEvent p2Actual;
        FloorInputEvent p2Expected;
        inputString = "10:22:10.123 3 Up 5";
        p2Actual = parser.stringToFloorInputEvent(inputString);
        System.out.println("p2Actual.toString():");
        System.out.println(p2Actual.toString());
        // Check our relativeStartTime
        //assertEquals(p2Actual.time() - 100000, 0);
        p2Expected = new FloorInputEvent(100000, 3, Direction.UP, 5);
        // Compare fields
        assertEquals(p2Actual.time(), p2Expected.time());
        assertEquals(p2Actual.sourceFloor(), p2Expected.sourceFloor());
        assertEquals(p2Actual.direction(), p2Expected.direction());
        assertEquals(p2Actual.destinationFloor(), p2Expected.destinationFloor());

        // Phew!
        System.out.println("\n*** All Tests Passed ***");

        System.out.println("\n****** END: testStringToFloorInputEvent() ******");

    }

    @Test
    void testParse() {

        System.out.println("\n****** START: testParse() ******");

        String filename = "input-file.txt";
        int expectedFloorInputEvents = 20;
        floorInputEvents = parser.parse(filename);
        // Assert our ArrayList now has expected number of FloorInputEvents
        assertEquals(expectedFloorInputEvents, floorInputEvents.size());

        // Phew!
        System.out.println("\n*** All Tests Passed ***");

        System.out.println("\n****** END: testParse() ******");
    }

}
