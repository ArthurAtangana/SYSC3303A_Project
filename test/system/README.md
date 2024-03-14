# README
* Last Edited: 2024/02/23

## Description
System tests to verify overall system functionality.

## Contents

## System Test Method

The current method for testing the overall functionality of the system involves running the `main()` method of the
Mains.MainCombinedDMA class, and verifying its runtime output against expected results.
A sample of the program's expected output during a single execution is contained in this directory.

### Expected output

The generation section should be identical to sample-output.txt
The real time system operation section:

- Observe that floor receives events.
- Observe that Elevator will receive event and attempt to serve it (going in x direction, going to floor y)
- Observe that Elevator reaches floor after some delay
- Observe that the Elevator stops at floors it is serving.
- Observe that the Elevator is Unloading passengers if required
- Observe thta the Floor and Elevator are loading passengers if required
- (loops)
- Note: Floor may send events to the scheduler which get buffered for later if the elevator is busy while it happens.

### `sample-output.txt`
A sample of the program's output during a single execution. This is not equivalent to the expected output!

This is the sample output explained more coherently:
- Request1: Floor 1 going UP
- The elevator goes to floor 1. stops pick up R1(going to floor 7)
- The elevator starts going up floors
- Request2: Floor 1 going UP
- Request3: Floor 6 going DOWN
- Request4: Floor 5 going UP
- The elevator is still going up floors
- Reaches floor 5. stops, picks up R4 (going to floor 6)
- Elevator goes up again, reaches floor 6, stops, unloads R4
- Elevator goes up again, reaches floor 7, stops, unloads R1
- Request5: Floor 3 going DOWN
- Request6: Floor 1 going UP
- Elevator goes down to service R2 on floor 1
- Elevator doesn't pickup anyone on the way down
- Request7: Floor 2 going UP
- Elevator reaches floor 1. picks up R2 (going to Floor 3) and R6 (going to floor 6)
- Elevator goes up
- Elevator stops on floor 2. pickups R7 (going to floor 6)
- elevator goes up .
- Elevator stops on floor 3, unloads R2
- Elevator goes up
- Elevator stops on floor 6, unloads R6 and R7
- Elevator loads R3 (going to floor 2)
- Elevator goes down
- Elevator stops on floor 3, pickups R5 (going to floor 1)
- Elevator goes down
- Elevator stops on floor 2, unloads R3
- Elevator goes down
- Elevator stops on floor 1, unloads R5
- Elevator Idles.

NOTE: We do not expect an exact match in the output, due to the concurrent nature of the system,
some threads will be scheduled at different times leading to slight ordering variations.
