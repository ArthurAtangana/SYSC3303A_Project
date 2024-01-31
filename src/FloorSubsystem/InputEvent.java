/**
 * InputEvent record stores all information par sed directly from input files.
 *
 * @param time The time delta in milliseconds from the initial event in the system.
 * @param dir The direction of the request in the input.
 * @param callFloor The floor number where the request was called.
 * @param destFloor The floor number where the passenger wants to go once in the elevator.
 *
 * @author Alexandre Marques
 * @version 2024-01-30
 */
package FloorSubsystem;

import CommonModels.Direction;

public record InputEvent(int time, Direction dir, int callFloor, int destFloor) {}
