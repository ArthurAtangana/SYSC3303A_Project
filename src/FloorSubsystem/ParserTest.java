package FloorSubsystem;

import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private Scheduler scheduler;
    private String filename = "input-file.txt";
    private Parser parser;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        scheduler = new Scheduler();
        parser = new Parser(filename, scheduler);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testTimeStringToInt() {

        String timeString;
        long timeLong;

        // Text min value
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

    }

}