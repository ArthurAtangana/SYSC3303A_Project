/**
 * Test class for Parser class.
 *
 * @author Michael De Santis
 * @version 20240202
 */
package unit.FloorSubsystem;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.FloorInputEvent;
import Subsystem.FloorSubsystem.Parser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    private final String filename = "test/resources/parser-input-file.txt";
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
    void testParse() {

        System.out.println("\n****** START: testParse() ******");

        floorInputEvents = parser.parse(filename);

        // Assert our ArrayList now has expected number of FloorInputEvents
        int expectedFloorInputEvents = 20;
        assertEquals(expectedFloorInputEvents, floorInputEvents.size());

        /* Check each instantiated FloorInputEvent member against expected value */

        //FloorInputEvent: 0
        FloorInputEvent a0 = new FloorInputEvent(32722123, 1, Direction.UP, 7);
        assertEquals(a0.time(), floorInputEvents.get(0).time());
        assertEquals(a0.sourceFloor(), floorInputEvents.get(0).sourceFloor());
        assertEquals(a0.direction(), floorInputEvents.get(0).direction());
        assertEquals(a0.destinationFloor(), floorInputEvents.get(0).destinationFloor());

        // FloorInputEvent: 1
        FloorInputEvent a1= new FloorInputEvent(32732994, 1, Direction.UP, 3);
        assertEquals(a1.time(), floorInputEvents.get(1).time());
        assertEquals(a1.sourceFloor(), floorInputEvents.get(1).sourceFloor());
        assertEquals(a1.direction(), floorInputEvents.get(1).direction());
        assertEquals(a1.destinationFloor(), floorInputEvents.get(1).destinationFloor());

        // FloorInputEvent: 2
        FloorInputEvent a2= new FloorInputEvent(32735061, 1, Direction.UP, 6);
        assertEquals(a2.time(), floorInputEvents.get(2).time());
        assertEquals(a2.sourceFloor(), floorInputEvents.get(2).sourceFloor());
        assertEquals(a2.direction(), floorInputEvents.get(2).direction());
        assertEquals(a2.destinationFloor(), floorInputEvents.get(2).destinationFloor());

        // FloorInputEvent: 3
        FloorInputEvent a3= new FloorInputEvent(32752485, 7, Direction.DOWN, 4);
        assertEquals(a3.time(), floorInputEvents.get(3).time());
        assertEquals(a3.sourceFloor(), floorInputEvents.get(3).sourceFloor());
        assertEquals(a3.direction(), floorInputEvents.get(3).direction());
        assertEquals(a3.destinationFloor(), floorInputEvents.get(3).destinationFloor());

        // FloorInputEvent: 4
        FloorInputEvent a4= new FloorInputEvent(32763669, 2, Direction.UP, 5);
        assertEquals(a4.time(), floorInputEvents.get(4).time());
        assertEquals(a4.sourceFloor(), floorInputEvents.get(4).sourceFloor());
        assertEquals(a4.direction(), floorInputEvents.get(4).direction());
        assertEquals(a4.destinationFloor(), floorInputEvents.get(4).destinationFloor());

        // FloorInputEvent: 5
        FloorInputEvent a5= new FloorInputEvent(32774334, 5, Direction.DOWN, 2);
        assertEquals(a5.time(), floorInputEvents.get(5).time());
        assertEquals(a5.sourceFloor(), floorInputEvents.get(5).sourceFloor());
        assertEquals(a5.direction(), floorInputEvents.get(5).direction());
        assertEquals(a5.destinationFloor(), floorInputEvents.get(5).destinationFloor());

        // FloorInputEvent: 6
        FloorInputEvent a6= new FloorInputEvent(32776301, 5, Direction.DOWN, 1);
        assertEquals(a6.time(), floorInputEvents.get(6).time());
        assertEquals(a6.sourceFloor(), floorInputEvents.get(6).sourceFloor());
        assertEquals(a6.direction(), floorInputEvents.get(6).direction());
        assertEquals(a6.destinationFloor(), floorInputEvents.get(6).destinationFloor());

        // FloorInputEvent: 7
        FloorInputEvent a7= new FloorInputEvent(32779449, 4, Direction.DOWN, 1);
        assertEquals(a7.time(), floorInputEvents.get(7).time());
        assertEquals(a7.sourceFloor(), floorInputEvents.get(7).sourceFloor());
        assertEquals(a7.direction(), floorInputEvents.get(7).direction());
        assertEquals(a7.destinationFloor(), floorInputEvents.get(7).destinationFloor());

        // FloorInputEvent: 8
        FloorInputEvent a8= new FloorInputEvent(32787678, 2, Direction.UP, 7);
        assertEquals(a8.time(), floorInputEvents.get(8).time());
        assertEquals(a8.sourceFloor(), floorInputEvents.get(8).sourceFloor());
        assertEquals(a8.direction(), floorInputEvents.get(8).direction());
        assertEquals(a8.destinationFloor(), floorInputEvents.get(8).destinationFloor());

        // FloorInputEvent: 9
        FloorInputEvent a9= new FloorInputEvent(32793994, 3, Direction.DOWN, 1);
        assertEquals(a9.time(), floorInputEvents.get(9).time());
        assertEquals(a9.sourceFloor(), floorInputEvents.get(9).sourceFloor());
        assertEquals(a9.direction(), floorInputEvents.get(9).direction());
        assertEquals(a9.destinationFloor(), floorInputEvents.get(9).destinationFloor());

        // FloorInputEvent: 10
        FloorInputEvent a10= new FloorInputEvent(32798165, 1, Direction.UP, 2);
        assertEquals(a10.time(), floorInputEvents.get(10).time());
        assertEquals(a10.sourceFloor(), floorInputEvents.get(10).sourceFloor());
        assertEquals(a10.direction(), floorInputEvents.get(10).direction());
        assertEquals(a10.destinationFloor(), floorInputEvents.get(10).destinationFloor());

        // FloorInputEvent: 11
        FloorInputEvent a11= new FloorInputEvent(32810778, 1, Direction.UP, 7);
        assertEquals(a11.time(), floorInputEvents.get(11).time());
        assertEquals(a11.sourceFloor(), floorInputEvents.get(11).sourceFloor());
        assertEquals(a11.direction(), floorInputEvents.get(11).direction());
        assertEquals(a11.destinationFloor(), floorInputEvents.get(11).destinationFloor());

        // FloorInputEvent: 12
        FloorInputEvent a12= new FloorInputEvent(32812393, 7, Direction.DOWN, 1);
        assertEquals(a12.time(), floorInputEvents.get(12).time());
        assertEquals(a12.sourceFloor(), floorInputEvents.get(12).sourceFloor());
        assertEquals(a12.direction(), floorInputEvents.get(12).direction());
        assertEquals(a12.destinationFloor(), floorInputEvents.get(12).destinationFloor());

        // FloorInputEvent: 13
        FloorInputEvent a13= new FloorInputEvent(32814651, 7, Direction.DOWN, 2);
        assertEquals(a13.time(), floorInputEvents.get(13).time());
        assertEquals(a13.sourceFloor(), floorInputEvents.get(13).sourceFloor());
        assertEquals(a13.direction(), floorInputEvents.get(13).direction());
        assertEquals(a13.destinationFloor(), floorInputEvents.get(13).destinationFloor());

        // FloorInputEvent: 14
        FloorInputEvent a14= new FloorInputEvent(32819912, 4, Direction.DOWN, 1);
        assertEquals(a14.time(), floorInputEvents.get(14).time());
        assertEquals(a14.sourceFloor(), floorInputEvents.get(14).sourceFloor());
        assertEquals(a14.direction(), floorInputEvents.get(14).direction());
        assertEquals(a14.destinationFloor(), floorInputEvents.get(14).destinationFloor());

        // FloorInputEvent: 15
        FloorInputEvent a15= new FloorInputEvent(32828870, 5, Direction.UP, 6);
        assertEquals(a15.time(), floorInputEvents.get(15).time());
        assertEquals(a15.sourceFloor(), floorInputEvents.get(15).sourceFloor());
        assertEquals(a15.direction(), floorInputEvents.get(15).direction());
        assertEquals(a15.destinationFloor(), floorInputEvents.get(15).destinationFloor());

        // FloorInputEvent: 16
        FloorInputEvent a16= new FloorInputEvent(32829113, 4, Direction.UP, 6);
        assertEquals(a16.time(), floorInputEvents.get(16).time());
        assertEquals(a16.sourceFloor(), floorInputEvents.get(16).sourceFloor());
        assertEquals(a16.direction(), floorInputEvents.get(16).direction());
        assertEquals(a16.destinationFloor(), floorInputEvents.get(16).destinationFloor());

        // FloorInputEvent: 17
        FloorInputEvent a17= new FloorInputEvent(32841833, 7, Direction.DOWN, 1);
        assertEquals(a17.time(), floorInputEvents.get(17).time());
        assertEquals(a17.sourceFloor(), floorInputEvents.get(17).sourceFloor());
        assertEquals(a17.direction(), floorInputEvents.get(17).direction());
        assertEquals(a17.destinationFloor(), floorInputEvents.get(17).destinationFloor());

        // FloorInputEvent: 18
        FloorInputEvent a18= new FloorInputEvent(32844113, 7, Direction.DOWN, 4);
        assertEquals(a18.time(), floorInputEvents.get(18).time());
        assertEquals(a18.sourceFloor(), floorInputEvents.get(18).sourceFloor());
        assertEquals(a18.direction(), floorInputEvents.get(18).direction());
        assertEquals(a18.destinationFloor(), floorInputEvents.get(18).destinationFloor());

        // FloorInputEvent: 19
        FloorInputEvent a19= new FloorInputEvent(32851113, 2, Direction.DOWN, 1);
        assertEquals(a19.time(), floorInputEvents.get(19).time());
        assertEquals(a19.sourceFloor(), floorInputEvents.get(19).sourceFloor());
        assertEquals(a19.direction(), floorInputEvents.get(19).direction());
        assertEquals(a19.destinationFloor(), floorInputEvents.get(19).destinationFloor());

        // Phew!
        System.out.println("\n*** All Tests Passed ***");

        System.out.println("\n****** END: testParse() ******");
    }

}
