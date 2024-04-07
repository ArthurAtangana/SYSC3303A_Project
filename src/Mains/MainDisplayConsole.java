package Mains;

import Subsystem.Logging.DisplayConsole;

/**
 * Class MainDisplayConsole displays a GUI for the elevator system.
 *
 * This class must be started before other subsystem main methods
 * or else proper display of elevators is not guaranteed.
 *
 * @author Will Ferrell
 * @version Iteration-5
 */
public class MainDisplayConsole {

    public static void main(String[] args) {
        DisplayConsole displayConsole = new DisplayConsole();
        displayConsole.listen();
    }
}