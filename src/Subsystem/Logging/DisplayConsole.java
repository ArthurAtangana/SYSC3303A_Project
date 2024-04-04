package Logging;

import Configuration.Config;
import Configuration.Configurator;
import Logging.CellButton;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Console class which receives Logging messages and centralizes display.
 * Also implements a rickety GUI.
 *
 * @author M. Desantis
 * @version Iteration-5
 */
public class DisplayConsole {

    private String banner = "************************************************************\n"
                          + "**************** ELEVATOR SIMULATOR 3303 *******************\n"
                          + "************************************************************\n"
                          + "******* A real-cool, real-time elevator simulator. *********\n"
                          + "************************************************************\n"
                          + "*                                            |  |  |       *\n"
                          + "* Authors: SYSC 3303A Group 1             ___|__|__|___    *\n"
                          + "* ----------------------------           | ||       || |   *\n"
                          + "* Arthur Atangana:   101005197           | ||   0   || |   *\n"
                          + "* Victoria Malouf:   101179986           | || <`W'> || |   *\n"
                          + "* Michael De Santis: 101213450           | ||   |   || |   *\n"
                          + "* Braeden Kloke:     100895984           | ||  / \\  || |   *\n"
                          + "* Alexandre Marques: 101189743           |_||_______||_|   *\n"
                          + "*                                            |  |  |       *\n"
                          + "* Thanks:                                    |  |  |       *\n"
                          + "* ----------------------------               |  |  |       *\n"
                          + "* TA Maede Davoudzade                        |  |  |       *\n"
                          + "* Dr. Rami Sabouni                           |  |  |       *\n"
                          + "*                                            |  |  |       *\n"
                          + "************************************************************\n"
                          + "* (づ｡◕‿‿◕｡)づ Ride safe!!                                  *\n"
                          + "************************************************************\n";
    
    /* Enums */
    public enum LEVEL {
        INFO,  // INFO level messages for general prints
        DEBUG  // DEBUG level messages for additional debug prints
    }

    /* Constants */
    public static final int RX_PORT = 55555;
    public static final int MAX_MESSAGE_BYTES = 255;
    public final String CONSOLE_LOG_ID = "CONSOLE";

    /* Instance Variables */

    private DatagramSocket rxSocket;
    private Config config;

    /* GUI Things */

    private JFrame frame;
    private Container contentPane;
    private static final int FRAME_HEIGHT = 800;
    private static final int FRAME_WIDTH = 1200;
    private int cellWidth;
    private int cellHeight;
    private ArrayList<CellButton> cellButtons = new ArrayList<CellButton>();
    private int floorRows;
    private int elevatorCols;
    private JPanel gridPanel;
    private int msgCol;
    private int msgRow;

    /* Constructors */

    /**
     * Default constructor for class Console. Set up a socket. GUI things. TUI things.
     *
     * @author M. Desantis
     * @version Iteration-5
     */
    public DisplayConsole() {

        /* Socket */
        try {
            // Rx from Logger
            rxSocket = new DatagramSocket(RX_PORT);
            System.out.println(CONSOLE_LOG_ID + " established Rx Socket on: " + RX_PORT);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }

        /* Config */
        Config config = (new Configurator().getConfig());
        this.floorRows = config.getNumFloors();
        this.elevatorCols = config.getNumElevators();
        this.msgRow = 0; // Floor
        this.msgCol = 0; // Elevator
        this.cellWidth = FRAME_WIDTH / this.floorRows;
        this.cellHeight = FRAME_HEIGHT / this.elevatorCols;

        /* TUI */
        System.out.println(banner);

        /* GUI */
        frame = new JFrame("Get Elevated");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = frame.getContentPane();
        contentPane.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        initializeGridPanel();
        contentPane.add(gridPanel);
        frame.pack();
        frame.setVisible(true);

    }

    /**
     * Initializes the gridPanel.
     */
    private void initializeGridPanel() 
    {   
        // Initialize the gridPanel
        //System.out.println("Initializing grid: " + this.floorRows + " by " + this.elevatorCols);
        gridPanel = new JPanel();
        gridPanel.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        gridPanel.setBackground(Color.BLUE);
        gridPanel.setLayout(new GridLayout(this.floorRows, this.elevatorCols, 10, 10));
        gridPanel.setOpaque(true);

        // Stick it with buttons
        //System.out.println("Initializing cellButtons: " + this.cellWidth+ " by " + this.cellHeight);
        for (int i = this.floorRows; i > 0; --i) {
            for (int j = this.elevatorCols; j > 0; --j) {
                // Create the cell button
                CellButton btn = new CellButton(i, j);
                // Track it in ArrayList for access
                cellButtons.add(btn);
                // GUI things
                btn.setPreferredSize(new Dimension(this.cellWidth, this.cellHeight));
                this.gridPanel.add(btn);
                // Oh I suppose we better light them up to start.
                if (i == 1) {
                   btn.setBackground(Color.YELLOW);
                }
            }
        }

    }

    /* Methods */

    /**
     * Print a received Logging message to console TUI, and update the GUI.
     *
     * @author M. Desantis
     * @version Iteration-5
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

            // Reminder:
            // msgRow == FLOOR NUM
            // msgCol == ELEVATOR NUM

            // Case: GUI
            // If message is only 2 char, it is a GUI update.
            if (msg.length() == 2) {
               
               // Catch our message params as integers in the most verbose Java way possible.
               msgCol = Character.getNumericValue(msg.charAt(0));
               msgRow = Character.getNumericValue(msg.charAt(1));

               // Check our floor/elevator cells for a match.
               for (CellButton cb : cellButtons) {
                    // Case: This is the column of elevators we are looking for.
                    //       Paint them black.
                    if (msgCol == cb.getCol()) {
                        cb.setBackground(Color.BLACK);
                        // Case: This is the elevator you are looking for.
                        //       Paint it yellow.
                        if (msgRow == cb.getRow()) {
                            cb.setBackground(Color.YELLOW);
                        }
                    }
               }
            }
            // CASE: TUI
            // Print the thing. Wow, how nice and simple!
            else {
                System.out.println(msg);
            }
        }

    }

    /**
     * Main method for this class.
     *
     * @author M. Desantis
     * @version Iteration-5
     */
    public static void main(String[] args) {
        Logging.DisplayConsole displayConsole = new Logging.DisplayConsole();
        displayConsole.listen();
    }

}
