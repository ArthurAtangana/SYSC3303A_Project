# README
* Last Edited: 2024/02/03

## Table of Contents

1. [Authors](#authors)
2. [Usage](#usage)
3. [Description](#description)
4. [Design Considerations](#design-considerations)
5. [Scope](#scope)
6. [Tasks](#tasks)
7. [Test](#test)
8. [Resources](#resources)
9. [Known Issues](#known-issues)

## Authors
Group A1:1
- Arthur Atangana: 101005197
- Victoria Malouf: 101179986
- Michael Desantis: 101213450
- Braeden Kloke: 100895984
- Alexandre Marques: 101189743

## Usage
Usage written for use in IntelliJ 2023 IDE.

Note: Compiled using JDK - 21 Oracle OpenJDK version 21.0.1
1. Retrieve source code (https://github.com/ArthurAtangana/SYSC3303A_Project) in IntelliJ
   - Clone using HTTPS:
     - `https://github.com/ArthurAtangana/SYSC3303A_Project.git` 
   - Clone using SSH:
     - `git clone git@github.com:ArthurAtangana/SYSC3303A_Project.git`
   - Clone using GitHub CLI:
     - `gh repo clone ArthurAtangana/SYSC3303A_Project`
   - For TA's milestone grading: use the submission zip file (it's a snapshot of the repo at submission time).
2. Add lib folder as a library
   1. Right click lib folder in root directory
   2. Select add as library
   3. Leave settings as default, select ok
3. Run the files in the "Mains" package in this order:
   MainSchedulerUDP, MainElevatorUDP, MainFloorUDP

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

| File                    | Package                | Description                                                                                   |
|-------------------------|------------------------|-----------------------------------------------------------------------------------------------|
| Config                  | Configuration          | Contains system configuration data.                                                           |
| Configurator            | Configuration          | Parses a JSON file and makes its data publicly available in a constructed Config Java object. |
| Elevator                | ElevatorSubsystem      | Models an elevator in the simulation.                                                         |
| Elevator Utilities      | ElevatorSubsystem      | Contains static methods used by the Elevator.                                                 |
| DestinationDispatcher   | FloorSubsystem         | Simulate events external to the system occurring in real time.                                |
| Floor                   | FloorSubsystem         | Models a floor in the simulation.                                                             |
| Parser                  | FloorSubsystem         | Parses an input text file to simulate input events to the system.                             |
| MoveElevatorCommand     | Messaging/Commands     | Command to elevator to move until the next floor is reached.                                  |
| MovePassengersCommand   | Messaging/Commands     | Command to elevator to load/offload passengers onto/into the elevator (when applicable).      |
| PassengerArrivedCommand | Messaging/Commands     | Command to floor that a passenger has arrived with an intended destination.                   |
| SendPassengersCommand   | Messaging/Commands     | Command to floor to send passengers back through the provided transmitter.                    |
| SystemCommand           | Messaging/Commands     | Interface definition for all commands passed in the system to inherit.                        |
| DestinationEvent        | Messaging/Events       | Holds data modelling a destination.                                                           |
| ElevatorStateEvent      | Messaging/Events       | Holds all data related to the state of an elevator during operation.                          |
| FloorInputEvent         | Messaging/Events       | Models an input event to the system.                                                          |
| FloorRequestEvent       | Messaging/Events       | Holds a destination event and the time the system received the request.                       |
| PassengerLoadEvent      | Messaging/Events       | Holds a list of passengers to load onto an elevator.                                          |
| SystemEvent             | Messaging/Events       | Interface definition for all events passed in the system to inherit.                          |
| DMA_Receiver            | Messaging/Receivers    | Provides a way to receive messages from DMA_Transmitters.                                     |
| Receiver                | Messaging/Receivers    | Defines methods required to receive Events from transmitters.                                 |
| DMA_Transmitter         | Messaging/Transmitters | Provides a way to send messages to DMA_Receivers.                                             |
| Transmitter             | Messaging/Transmitters | Defines methods required to transmit Events to receivers.                                     |
| Direction               | Messaging              | Holds elevator state for directionality of travel.                                            |
| SystemMessage           | Messaging              | Interface definition for all messages passed in the system to inherit.                        |
| Loader                  | SchedulerSubsystem     | Loads appropriate passengers into an elevator.                                                |
| Scheduler               | SchedulerSubsystem     | Models a scheduler in the simulation.                                                         |
| Mains.MainCombinedDMA   |                        | Models a scheduler in the simulation.                                                         |

## Design Considerations

- The scheduler algorithm prioritizes the prevention of starvation over achieving
the highest throughput possible.If the design can lead to starvation, it is an invalid design.

  - Throughout: A measure of efficiency. The rate at which the elevator control system can process
  passenger destination requests within a specified amount of time. A system that favours throughput 
  may only focus on servicing passengers that are close together to increase the amount of requests that can be serviced. 

  - Starvation: A passenger can experience starvation if there is a possibility that they could never
  be serviced. This would happen if they are on a floor that is rarely used and far away. 


### Documentation
- All public methods and classes require javadoc, private methods are optional.

## Tasks

### Iteration 1

| Task | Assignee|
|------|---------|
|Project Structure|Victoria Malouf|
|Floor Model| Victoria Malouf|
|Elevator Model|Arthur Atangana|
|Scheduler Model|Arthur Atangana|
|Parser|Michael De Santis|
|Dispatch|Alexandre Marques|
|Event Handling|Alexandre Marques|
|Diagrams|Braeden Kloke|
|Testing|Braeden Kloke|

### Iteration 2

| Task                    | Assignee|
|-------------------------|---------|
| Config file integration |Michael De Santis|
| UML class diagrams      |Michael De Santis|
| UML sequence diagrams   |Michael De Santis|
| State diagrams          |Braeden Kloke|
| Scheduler logic         |Braeden Kloke|
| Networking diagrams     |Victoria Malouf|
| Elevator utlitlity test |Victoria Malouf|
| Floor logic             |Victoria Malouf|
| State diagrams          |Alexandre Marques|
| Command/Event classes   |Alexandre Marques|
| Elevator logic          |Alexandre Marques|
| Scheduler logic         |Alexandre Marques|
| Loader class            |Alexandre Marques|
| Elevator logic          |Arthur Atangana|
| Scheduler logic         |Arthur Atangana|
| System test             |Arthur Atangana|

## Test

All tests are located in directory `test`.

Tests are categorized in subdirectories labelled:

* `unit`: Tests isolated components.
* `system`: Tests all components working together. (eg. Does the simulation correctly get all passengers to their desired floors?)

All resources for tests (eg. input files) are in `test/resources`.

Only classes that expose public methods are unit tested.

The testing framework used for unit tests is JUnit 5.8.1.

For detailed test layout, see test README.


### Troubleshooting

**IntelliJ can't find your tests?**
Ensure you have directory `test` labelled as your Test Source Folder in Project Structure > Modules.

**Test classes don't compile due to missing imports (red squiggles)?**
Alt-enter on the invalid imports, and select add JUnit 5.8.1 to classpath, select OK.

**Missing libraries?**
To add the included libraries in the project's `lib/` directory for build, use the following procedure.
1. In IntelliJ, open the `Project Structure` window (`File > Project Structure`), and navigate to the `Libraries` section (`Project Settings > Libraries`).
2. Click the `+` icon to add a new library, and select `Java` from the dropdown menu.
3. In the `Select Library Files` browser that opens, select this project's `lib/` directory and click `OK`.
4. In the `Choose Modules` dialog box that opens, ensure that this project's module, `SYSC3303A_Project`, is selected (this should be the default), and click `OK`.
5. In the `Project Structure` window, confirm the changes by clicking `OK`.
All JAR files in the `lib/` directory should now be included in the project for import and build.

## Resources
Resources for this project, such as system configuration and input files, are included in the `res/` directory. 

### System Input
System input files are text files used to simulate input into the system, and contain newline separated input strings adherent to the form specified in the project requirements.

### System Configuration
System configuration files are stored as JSONs, and specify values for the system components that configure its runtime behaviour.

## Known Issues
See GitHub project issues for most up-to-date issues
