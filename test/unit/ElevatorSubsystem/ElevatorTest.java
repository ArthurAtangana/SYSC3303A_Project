package unit.ElevatorSubsystem;

import ElevatorSubsytem.Elevator;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.*;

/**
 * Unit tests for class Elevator.
 */
public class ElevatorTest {

    @Test
    void sampleTest() {
        Elevator e = new Elevator(new Scheduler());
        assert true;
    }
}
