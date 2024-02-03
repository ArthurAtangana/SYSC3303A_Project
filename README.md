# README
* Last Edited: 2024/01/30

## Group A1:1
- Arthur Atangna: 101005197
- Victoria Malouf:
- Michael Desantis: 101213450
- Braeden Kloke: 100895984
- Alexandre Marques: 101189743

## Description
Elevator Simulator, written in Java.

## Contents

## Notes

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

### Iteration 0
* task
* task

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


## UML Diagrams

### System Class Diagram

The following diagram shows the three main subsystems (Floor, Scheduler, and Elevator) implemented as Runnables.
Each subsystem is composed of transmitters and receivers to facilitate communication.

<img src="./diagrams/system-class-diagram.png" alt="System Class Diagram" width="500" height="500" />

### Subsystem Communication Sequence Diagram

The following sequence diagram shows how the Floor subsystem communicates (eg. sends events) to the Scheduler subsystem via transmitters and receivers.
There are transmitters and receivers between each subsystem to facilitate communication.

<img src="./diagrams/subsystem-communication-sequence-diagram.png" alt="Subsystem Communication Sequence Diagram" width="500" height="500" />

