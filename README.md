# README
* Last Edited: 2024/02/03

## Table of Contents

1. [Authors](#authors)
2. [Usage](#usage)
3. [Description](#description)
4. [Source Files](#source-files)
5. [Scope](#scope)
6. [Tasks](#tasks)
7. [Test](#test)
8. [Known Issues](#known-issues)

## Authors 
Group A1:1
- Arthur Atangana: 101005197
- Victoria Malouf: 101179986
- Michael Desantis: 101213450
- Braeden Kloke: TODO
- Alexandre Marques: 101189743

## Usage
Note: Compiled using JDK - 21 Oracle OpenJDK version 21.0.1
1. Retrieve source code (https://github.com/ArthurAtangana/SYSC3303A_Project)
   - Clone using HTTPS:
     - `https://github.com/ArthurAtangana/SYSC3303A_Project.git` 
   - Clone using SSH:
     - `git clone git@github.com:ArthurAtangana/SYSC3303A_Project.git`
   - Clone using GitHub CLI:
     - `gh repo clone ArthurAtangana/SYSC3303A_Project`
2. Open Main.java
3. Run the main function

For test execution, see [test section](#test).

## Description

This project is being produced for SYSC 3303 - Real Time Concurrent Systems. 

Over five iterations, the team will be designing and implementing an elevator control 
system and simulator written in Java. 

The project model is designed using the "ELEV - 1" elevator in Carleton's Canal building.

Raw data was collected and analyzed during iteration 0 to model elevator 
travel time and passenger (de)boarding time.

## Source Files
The source file structure is organized using Java packages under the src/ folder. 
See test README for test file structure.

### Main.java
- Initializes and maintains track of threads

### <u>ElevatorSubsystem (Package)</u>

**Elevator.java**
- Receives DestinationEvent records from Scheduler
- Travels to floors
- Sends ElevatorStateEvent records to Scheduler

### <u>FloorSubsystem (Package)</u>

**Floor.java**
- Receives ElevatorStateEvent records from Scheduler
- Sets lamp

**Parser.java**
- Parses input-file.txt to simulate input events to the system

**input-file.txt**
- Input file used to provide inputs to the system

**DestinationDispatcher.java**
- Sends DestinationEvent records to the scheduler

### <u>SchedulerSubsystem (Package)</u>

**Scheduler.java**
- Receives ElevatorStateEvent records from the Elevator
- Receives DestinationEvent records from the DestinationDispatcher
- Sends DestinationEvent records to the Elevator
- Sends ElevatorStateEvent records to the Floor

### <u>Networking (Package)</u>

#### <u>Events</u>

**ElevatorSystemEvent.java** 
- Interface for elevator system events

**DestinationEvent.java**
- Holds destination floor and direction

**ElevatorStateEvent.java**
- Holds currentFloor, direction, and FloorInputEvents

**FloorInputEvent.java**
- Holds arrival time, source floor, direction, and destination floor

#### <u>Receivers</u>

**Receiver.java**
- Interface for receivers

**DMA_Receiver.java**
- Receives ElevatorSystemEvent records and stores them in a buffer

#### <u>Transmitters</u>

**Transmitter.java**
- Interface for transmitters

**DMA_Transmitter.java**
- Sends ElevatorSystemEvent records to DMA_Receiver objects

**Direction.java**
- Enum of directions (UP, DOWN, STOPPED)

## Scope

### Iteration 1
- Assumption 1: "Each line of input is to be sent" + "You should
  develop a data structure which only passes then necessary information" = 
  Only the relevant part of the input need to be sent to the scheduler. 
  No timing is specified either, we send it when appropriate according to input or model time.
- Assumption 2: "It is only necessary to create a test case showing that your program can read the input
  file and pass the data back and forth." = The MVP for full marks. 
  If these properties can be tested and demonstrated to work the system is at least fully functional for iteration 1.
  For our testing strategy, this is covered by "system" tests.
- Assumption 3: "The elevators will make calls to the Scheduler which will then reply when there is work to be
  done." Is the only statement which describes how the communication occurs. 
  We can implement this behavior with thread blocking on a monitor but all other communications 
  can be implemented with the method we deem most fitting. 
- Assumption 4: "Unit tests for all classes" only refers to classes which can be unit tested. 
  More specifically, unit tests covering all public methods defined (excluding constructors, and run). 
  Run methods being tested through system tests only because they depend on, and affect the overall system.
  Constructors cannot be tested without getters which most classes don't implement.

## Tasks

### Iteration 1

| Task | Assignee|
|------|---------|
| Project Structure|Victoria Malouf|
|Floor Model| Victoria Malouf|
|Elevator Model|Arthur Atangana|
|Scheduler Model|Arthur Atangana|
|Parser|Michael De Santis|
|Dispatch|Alexandre Marques|
|Event Handling|Alexandre Marques|
|Diagrams|Braeden Kloke|
|Testing|Braeden Kloke|

## Test

All tests are located in directory `test`.

Tests are categorized in subdirectories labelled:

* `unit`: Tests isolated components.
* `system`: Tests all components working together. (eg. Does the simulation correctly get all passengers to their desired floors?)

All resources for tests (eg. input files) are in `test/resources`.
For detailed test layout, see test README.

### Troubleshooting

**IntelliJ can't find your tests?**
Ensure you have directory `test` labelled as your Test Source Folder in Project Structure > Modules.

## Known Issues
See GitHub project issues for most up-to-date issues