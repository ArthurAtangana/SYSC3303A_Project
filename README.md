# README
* Last Edited: 2024/02/01

## Group A1:1
- Arthur Atangana: 101005197
- Victoria Malouf:
- Michael Desantis: 101213450
- Braeden Kloke:
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
