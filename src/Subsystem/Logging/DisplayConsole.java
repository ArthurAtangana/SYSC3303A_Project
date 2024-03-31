package Logging;

import java.io.*;
import java.net.*;

/**
 * Console class which receives Logging messages and centralizes display.
 *
 * @author M. Desantis
 * @version Iteration-5
 */
public class DisplayConsole {
    
    /* Enums */
    public enum LEVEL {
        INFO,  // INFO level messages for general prints
        DEBUG  // DEBUG level messages for additional debug prints
    }

    /* Constants */
    public static final int RX_PORT = 55555;
    public static final int MAX_MESSAGE_BYTES = 200;
    public final String CONSOLE_LOG_ID = "CONSOLE";

    /* Instance Variables */

    private DatagramSocket rxSocket;

    /* Constructors */

    /**
     * Default constructor for class Console. Set up a socket.
     */
    public DisplayConsole() {
        try {
            // Rx from Logger
            rxSocket = new DatagramSocket(RX_PORT);
            System.out.println(CONSOLE_LOG_ID + " established Rx Socket on: " + RX_PORT);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /* Methods */

    /**
     * Print a received Logging message to console.
     */
    public void listen() {

        System.out.println(CONSOLE_LOG_ID + " listening on: " + RX_PORT);

        // Loop forever
        while (true) {

            // Local vars
            String msg;
            int len;
            byte[] data;
            data = new byte[MAX_MESSAGE_BYTES];
            // UDP packets and sockets
            DatagramPacket rxPacket = new DatagramPacket(data, data.length);

            // Block on RX socket until RX packet arrives from Client
            try {
                rxSocket.receive(rxPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            // Convert data to string
            len = rxPacket.getLength();
            data = rxPacket.getData();
            msg = new String(data, 0, len);

            // Print the thing
            System.out.println(msg);
        }

    }

    /**
     * Main method for this class.
     */
    public static void main(String[] args) {
        Logging.DisplayConsole displayConsole = new Logging.DisplayConsole();
        displayConsole.listen();
    }

}
