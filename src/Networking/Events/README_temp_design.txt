- Messages (Msg) == information communicated (passed) through two subsystems
    - Event == Used to hold/pass simple information GLOBALLY, to an entire subsystem.
               Simple actions get triggered (store info in fields, show views).
    - Command (Cmd) == Used to refer to a SPECIFIC instance (key), and special information needed to accomplish action.
                       Complex actions triggered, usually with processing time (reply, trigger wait time).
    - Main difference: COMMANDS have a "key" to identify their recipients.
                       EVENTS are sent to all instances of a subsystem.
