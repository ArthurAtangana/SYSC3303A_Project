# README
* Last Edited: 2024/02/03

## Description
System tests to verify overall system functionality.

## Contents

## System Test Method
The current method for testing the overall functionality of the system involves running the `main()` method of the Main class, and verifying its runtime output against expected results. 
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

NOTE: We do not expect an exact match in the output, due to the concurrent nature of the system,
some threads will be scheduled at different times leading to slight ordering variations.
