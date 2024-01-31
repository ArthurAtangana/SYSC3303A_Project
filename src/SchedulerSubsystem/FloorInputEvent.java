package SchedulerSubsystem;

import CommonModels.Direction;

public record FloorInputEvent(Direction dir, int floorReq, int floorDest, int time) {

}
