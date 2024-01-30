package SchedulerSubsystem;

public record FloorInputEvent(Scheduler.Direction dir, int floorReq, int floorDest, int time) {

}
