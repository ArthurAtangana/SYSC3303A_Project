package Subsystem.Logging;

import Configuration.Config;
import Configuration.Configurator;
import Subsystem.Logging.CellButton;

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
    private static final int FRAME_HEIGHT = 1000;
    private static final int FRAME_WIDTH = 1200;
    private int cellWidth;
    private int cellHeight;
    private ArrayList<CellButton> cellButtons = new ArrayList<CellButton>();
    private int floorRows;
    private int elevatorCols;
    private JPanel gridPanel;
    private JPanel legendPanel;
    private JPanel floorPanel;
    private int msgCol;
    private int msgRow;
    private int msgType;

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
        frame = new JFrame("Elfevator");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = frame.getContentPane();
        contentPane.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        initializeGridPanel();
        contentPane.add(gridPanel);
        initializeLegendPanel();
        contentPane.add(legendPanel);
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
        gridPanel.setBackground(Color.BLACK);
        gridPanel.setLayout(new GridLayout(this.floorRows, this.elevatorCols, 10, 10));
        gridPanel.setOpaque(true);

        // Stick it with buttons
        //System.out.println("Initializing cellButtons: " + this.cellWidth+ " by " + this.cellHeight);
        for (int i = this.floorRows; i > 0; --i) {
            for (int j = this.elevatorCols; j >= 0; --j) {
                // Create the cell button
                CellButton btn = new CellButton(i, j);
                if (j == 0){
                    btn.setText("Floor " + i);
                }
                // Track it in ArrayList for access
                cellButtons.add(btn);
                // GUI things
                btn.setPreferredSize(new Dimension(this.cellWidth, this.cellHeight));
                this.gridPanel.add(btn);
                // Oh I suppose we better light them up to start.
                if (i == 1) {
                   btn.setBackground(Color.YELLOW);
                }
                if (j == 0){
                    btn.setBackground(Color.WHITE);
                }
            }
        }

    }
    private void initializeLegendPanel(){
        legendPanel = new JPanel();
        legendPanel.setSize(FRAME_WIDTH,40);
        legendPanel.setBackground(Color.BLACK);
        legendPanel.setLayout(new GridLayout(1,7));
        legendPanel.setOpaque(true);

        JButton legend = new JButton("Legend: ");
        JButton moving = new JButton("Moving");
        JButton idle = new JButton("Idle");
        JButton loading = new JButton("Loading");
        JButton unloading = new JButton("Unloading");
        JButton transientFault = new JButton("Transient");
        JButton hardFault = new JButton("Hard fault");

        legend.setBackground(Color.WHITE);
        moving.setBackground(Color.GREEN);
        idle.setBackground(Color.YELLOW);
        loading.setBackground(Color.MAGENTA);
        unloading.setBackground(Color.PINK);
        transientFault.setBackground(Color.ORANGE);
        hardFault.setBackground(Color.RED);
        this.legendPanel.add(legend,0);
        this.legendPanel.add(moving,1);
        this.legendPanel.add(idle,2);
        this.legendPanel.add(loading,3);
        this.legendPanel.add(unloading,4);
        this.legendPanel.add(transientFault,5);
        this.legendPanel.add(hardFault,6);

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
            if (msg.length() == 5) {
               
               // Catch our message params as integers in the most verbose Java way possible.
               msgCol = Character.getNumericValue(msg.charAt(0)) * 10 +
                        Character.getNumericValue(msg.charAt(1));

               msgRow = Character.getNumericValue(msg.charAt(2)) * 10 +
                        Character.getNumericValue(msg.charAt(3));

               msgType = Character.getNumericValue(msg.charAt(4));

               // Check our floor/elevator cells for a match.
               for (CellButton cb : cellButtons) {
                    // Case: This is the column of elevators we are looking for.
                    //       Paint them black. Paint them red if fault.
                    if (msgCol == cb.getCol()) {
                        if (msgType == 1) { // transient fault
                            if (cb.getBackground() != Color.GRAY){
                                cb.setBackground(Color.ORANGE);
                            }
                        }
                        else if (msgType == 2) { // hard fault
                            if (cb.getBackground() != Color.GRAY) {
                                cb.setBackground(Color.RED);
                            }
                        }
                        else if (msgType == 3) { // idle
                            if (cb.getBackground() != Color.GRAY){
                                cb.setBackground(Color.YELLOW);
                            }
                        }
                        else if (msgType == 4) { // loading
                            if (cb.getBackground() != Color.GRAY){
                                cb.setBackground(Color.MAGENTA);
                            }
                        }
                        else if (msgType == 5) { // unloading
                            if (cb.getBackground() != Color.GRAY){
                                cb.setBackground(Color.PINK);
                            }
                        }
                        else {
                            cb.setBackground(Color.GRAY);
                            // Case: This is the elevator you are looking for.
                            //       Paint it green (its moving).
                            if (msgRow == cb.getRow()) {
                                cb.setBackground(Color.GREEN);
                            }
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
        DisplayConsole displayConsole = new DisplayConsole();
        displayConsole.listen();
    }

}
