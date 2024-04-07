package Subsystem.Logging;

import javax.swing.*;
import java.awt.*;

/**
 * GUI cell. Just a funny button. Represents an elevator at a floor.
 * 
 * @author M. Desantis
 * @version Iteration-5
 */

public class CellButton extends JButton {

    private int row;
    private int col;

    /**
     * Parametric Constructor.
     * 
     * @param row row corresponding to floor
     * @param col column corresponding to elevator
     *
     * @author M. Desantis
     * @version Iteration-5
     */
    public CellButton(int row, int col)
    {
        super("[F:" + row + ", E:" + col + "]");
        this.setForeground(Color.BLACK);
        this.row = row;
        this.col = col;
        this.setBackground(Color.GRAY);
        this.setOpaque(true);
        this.setContentAreaFilled(true);
        this.setBorderPainted(false);
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public void toggleColour() {
        if (this.getBackground() == Color.BLACK) {
            this.setBackground(Color.YELLOW);
        }
        else {
            this.setBackground(Color.BLACK);
        }
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
    }
}