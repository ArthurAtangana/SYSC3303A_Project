package unit.ElevatorSubsystem;

import ElevatorSubsytem.Elevator;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.*;

public class ElevatorTest {

    @Test
    void sampleTest() {
        Elevator e = new Elevator(new Scheduler());
        assert true;
    }
}
