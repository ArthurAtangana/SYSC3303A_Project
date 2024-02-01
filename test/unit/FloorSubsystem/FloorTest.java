package unit.FloorSubsystem;

import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.*;

public class FloorTest {

    @Test
    void sampleTest() {
        Floor f = new Floor(1, new Scheduler());
        assert true;
    }

}