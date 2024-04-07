package Logging;

import java.io.IOException;
import java.net.*;
import Subsystem.Logging.DisplayConsole;

/**
 * Logger class which handles subsystem logging via console print per process, 
 * and also sends log messages to a centralized DisplayConsole via UDP. Also
 * updates our GUI.
 *
 * @author M. Desantis
 * @version Iteration-5
 */
public class Logger {

    /* Enums */
    public enum LEVEL {
        INFO,  // INFO level messages for general prints
        DEBUG  // DEBUG level messages for additional debug prints
    }

    /* Instance Variables */
    private final int verbosity;

    private DatagramSocket txSocket;

    /* Constructors */

    /**
     * Default constructor for class Logger.
     *
     * @param verbosity The verbosity level, which should be set in 
     * the system configuration JSON and supplied here. 
     *   0: indicates no messages should be logged (suppress output)
     *   1: indicates only INFO level messages should be logged
     *   2: indicates INFO and DEBUG messages should be logged
     *
     * @author M. Desantis
     * @version Iteration-5
     */
    public Logger(int verbosity) {
        this.verbosity = verbosity;

        try {
            // For Tx to DisplayConsole
            txSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }

    }

    /* Methods */

    /**
     * Message for GUI update. 
     *
     * @param elevator The elevator number.
     * @param floor The floor number.
     * @param type types: 1  for transient, 2 for hard, 3 for idle, 4 for loading, 5 for unloading.
     *
     * @author M. Desantis
     * @version Iteration-5
     */
    public void updateGui(int elevator, int floor, int type) {

        // Packet for DisplayConsole
        byte[] data;
        //String msg = "" + elevator + floor;
        String msg = String.format("%02d", elevator) + String.format("%02d", floor) + type;
        data = msg.getBytes();
        DatagramPacket txPacket;

        // Create packet
        try {
            txPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), DisplayConsole.RX_PORT);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // Send to DisplayConsole via UDP
        try {
            txSocket.send(txPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Log a message by printing to console, specifying the log level, the 
     * identifier of the message source, and the message. This function 
     * formats the message for print, and prepends a System clock timestamp
     * (in UNIX milliseconds).
     *
     * @param level Log level: [Logger.INFO | Logger.DEBUG]
     * @param logId Log ID for message source (eg. "Elevator 1")
     * @param message Message to log
     *
     */
    public void log(Logger.LEVEL level, String logId, String message) {

        // The timestamp for this message
        //String timestamp = LocalDateTime.now().toString();
        long timestamp = System.currentTimeMillis();

        // The prefix for this message
        String prefix = "[" + timestamp + "::" + level + "::" + logId + "] ";

        // The whole thing
        String prefixedMessage = prefix + message;

        // Packet for DisplayConsole
        byte[] data;
        data = prefixedMessage.getBytes();
        // UDP packets and sockets
        DatagramPacket txPacket;
        try {
            txPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), DisplayConsole.RX_PORT);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // Case: Print INFO level messages only
        //if ((verbosity == 1) && (level == Logger.LEVEL.INFO)) {
        if (verbosity > 0) {
            // Case: Print INFO level messages only
            if (level == Logger.LEVEL.INFO) {
                // Local print
                System.out.println(prefixedMessage);
                // Send to DisplayConsole via UDP
                try {
                    txSocket.send(txPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            if ((verbosity == 2) && (level == Logger.LEVEL.DEBUG)) {
                // Local print
                System.out.println(prefixedMessage);
                // Send to DisplayConsole via UDP
                try {
                    txSocket.send(txPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Main method for this class. Just a quick test.
     *
     * @author M. Desantis
     * @version Iteration-5
     */
    public static void main(String[] args) {
        // Quick test
        int verbosity = 2;
        int loops = 20;
        int delay = 2000;
        Logger logger = new Logger(verbosity);
        String logId = "TEST-LOGGER";
        String debugMsg, infoMsg;
        debugMsg = "This is DEBUG message ";
        infoMsg = "This is INFO message ";

        for (int i = 0; i < loops; ++i) {
            if ((i % 2) == 0) {
                logger.log(Logger.LEVEL.DEBUG, logId, debugMsg + i);
            }
            else {
                logger.log(Logger.LEVEL.INFO, logId, infoMsg + i);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("All messages logged.");

    }

}
