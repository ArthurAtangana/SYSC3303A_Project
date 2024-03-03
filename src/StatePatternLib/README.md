# State Pattern

## Classic State Pattern

The state pattern is a behavioral pattern used to model a state machine (specifically a moore machine).
The behavior of a class switches based on its "state".

### State pattern classes

- Context: The "memory" of the machine, current state and other persistent information.
  It also holds application logic (processing).
- State (interface/abstract): A state in the state machine.
  It contains the logic to switch states.
- Concrete States: An implemented state.

### Limitations

Note: #7 is not really a limitation, more of an issue that isn't clearly
explained when people typically discuss the pattern, the "mitigation" in the modified pattern
section can likely be applied to the classic pattern already without modifications.

1. The states are tightly coupled to their context class (Makes some sense, but it limits reuse)
   States cannot use any of the context's application logic without type casting or hardcoding in abstract State.
2. OnEntry, OnExit, and doActivity are not inherently part of pattern -> not straightforward to model the state machine.
3. Event responsibility is unclear: Context calls specific methods on state based on events... which occur from where?
   Could be as a result of execution in state, or some external "interrupt". How to organize the code?
4. Each event requires a new method in the context, which the abstract state needs to implement (again high coupling)
5. The state pattern restricts what can be passed to (moore, no input) and returned from activities and actions.
   This can lead to extra states and a need for more persistence between states (extra fields).
6. Persistence between states needs to be saved in the context, requires extra setters,
   together with application logic which states use, exposes a lot of methods.
7. Although it is intended to work for state hierarchy, it's not typically explained very clearly how that would be
   done.
   Extension of the class would be the first instinct, but that inherits all characteristics which might not be ideal.

## Modified state pattern

The modified state pattern I've come up with is an attempt to address these limitations (see mitigations section),
while trying to keep the core ideas of the pattern. It's still using the same classes,
but their purposes may have been modified a bit to clearly separate responsibilities.
Most importantly, context has no control over state events, until the state delegates control
back to the context's application logic. For the most part, it's just an extension of the classic state pattern.

### Author

Alexandre Marques - 101189743
Version: V1.0
Date: 2024-02-18

### Mitigations

- #1: Abstract State class can make use of java generic type to define context type.
  Concrete State extends State with a concrete context type.
  The concrete context is what contains the application logic and data specific to the state machine implemented.
- #2: Can make a single entry point into state, which calls in this order: OnEntry, doActivity, selectState, OnExit,
  context.setState, and context calls this entry point after switching states.
- #3: selectState to generate events (either by making a call to receive one, or deriving them from context).
- #4: With the mitigation for 3, the state knows which events are called, and can choose methods private
  to the state as a response. This may lead to context application logic calls,
  but at least the abstract "State" class is not coupled to the context.
- #5: Can be mitigated by moving **activity code required for selection** in select state function.
  Actual implementation to be determined by a private method in the concrete state.
- #6**: Set up packages such that context and states live in the same package, and minimize other classes in this
  package
  create package-private context setters for the state.
  The added verbosity is still there, but at least it is properly encapsulated.
    - **Note: Java cannot access nested packages so this method does limit organization options, unfortunately.
      This especially limits superstate based organization.
- #7: Avoid super states if possible... It will likely add more complexity than simplification.
  If you were to implement it: use inner-contexts in super states to pass to inner states.
  This may lead to many levels of delegation calls, similarly to a decorator pattern...
  Honestly, I'm not too sure still what the best way to fix this is,
  not a big fan of the duplication it currently leads to.
  It's necessary to have multiple contexts to be able to be in two states at once.
  TODO: Could make an abstract SuperState class now if needed to make this more streamlined.

### How to implement?

This section can be expanded as questions come, but for now here are some guidelines:

- Follow the state and context javadocs.
- Keep in mind the mitigation principles while implementing the concrete states.
- Where do application logic methods live?
    - Processing code shared between states: In context.
    - Processing which requires "getters": Implement method in context instead.
    - Otherwise, (no need for context fields, not common code): implement as private state method.
