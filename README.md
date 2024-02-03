# README
* Last Edited: 2024/02/03

## Group A1:1
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

## Description

This project is being produced for SYSC 3303 - Real Time Concurrent Systems. 

Over five iterations, the team will be designing and implementing an elevator control 
system and simulator written in Java. 

The project model is designed using the "ELEV - 1" elevator in Carleton's Canal building.

Raw data was collected and analyzed during iteration 0 to model elevator 
travel time and passenger (de)boarding time.

## Design

The file structure is organized using Java packages. The src and test folder file structures are shown below:
```
src
    ElevatorSubsystem
        Elevator
    FloorSubsystem
        Floor
        Parser
        input-file.txt
        DestinationDispatcher
    SchedulerSubsystem
        Scheduler
    Networking
        Events
            ElevatorSystemEvent (interface)
            DestinationEvent (record)
            ElevatorStateEvent (record)
            FloorInputEvent (record)
        Receivers
            Receiver (interface)
            DMA_Receiver
        Transmitters
            Transmitter (interface)
            DMA_Transmitter 
        Direction (enum)
    Main
test
    integration
        SendEventFromFloorToSchedulerTest
    resources
        input-file.txt
    system
        OnePassengerTest
    unit
        ElevatorSubsystem
            ElevatorTest
        FloorSubsystem
            FloorTest
            ParserTest   
        SchedulerSubsystem
            SchedulerTest
 ```         

The three main subsystems are the FloorSubsystem, ElevatorSubsystem, and SchedulerSubsystem. 

The subsystems communicate with each other using classes from the Networking package.

The following is a typical workflow from the floor to the scheduler to the elevator and then back.

1. The DestinationDispatcher class (FloorSubsystem) is responsible for sending FloorInputEvent records 
to the Scheduler via a DMA_Transmitter. 

2. The Scheduler receives the FloorInputEvent records (via it's DMA_Receiver). 

3. The Scheduler sends DestinationEvent records (via it's DMA_Transmitter) to the Elevator's DMA_Receiver. 

4. The Elevator sends ElevatorStateEvent records (via it's DMA_Transmitter) to the Scheduler's DMA_Receiver. 

5. The Scheduler receives DestinationEvent records (via it's DMA_Receiver).

6. The Scheduler sends ElevatorStateEvent records (via it's DMA_Transmitter) to the Floor's DMA_Receiver.

In summary:
The floor receives data from the scheduler.
The scheduler receives data from the elevator and dispatcher and sends data to the floor and elevator.
The elevator receives data from the scheduler and sends data to the scheduler. 

The scheduler acts as a communication channel between the floor and elevator subsystems. 

## Scope

### Iteration 1
- Assumption 1: "Each line of input is to be sent" + "You should
  develop a data structure which only passes then necessary information" = 
  Only the relevant part of the input need to be sent to the scheduler. 
  No timing is specified either, we send it when appropriate according to input or model time.
- Assumption 2: "It is only necessary to create a test case showing that your program can read the input
  file and pass the data back and forth." = The MVP for full marks. 
  If these properties can be tested and demonstrated to work the system is at least fully functional for iteration 1.
- Assumption 3: "The elevators will make calls to the Scheduler which will then reply when there is work to be
  done." Is the only statement which describes how the communication occurs. 
  We can implement this behavior with thread blocking on a monitor but all other communications 
  can be implemented with the method we deem most fitting. 

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
* `integration`: Tests interoperability of components. (eg. Floor sending events to Scheduler)
* `system`: Tests all components working together. (eg. Does the simulation correctly get all passengers to their desired floors?)

All resources for tests (eg. input files) should be put in `test/resources`.

### Troubleshooting

**IntelliJ can't find your tests?**
Ensure you have directory `test` labelled as your Test Source Folder in Project Structure > Modules.

## Known Issues
See GitHub project issues for most up-to-date issues