package SchedulerSubsystem;

/**
 * SchedulerEvent has
 */
public class SchedulerEvent {
    public enum SchedulerEventType {
        FLOOR_BUTTON_PRESSED,
        ELEVATOR_ARRIVED_AT_FLOOR,
        ELEVATOR_BUTTON_PRESSED
    }
    public enum Direction {
        UP,
        DOWN
    }

    private SchedulerEventType eventType;
    private Record data;

    public SchedulerEvent(SchedulerEventType eventType, Record data) {
        this.eventType = eventType;
        this.data = data;
    }

}
