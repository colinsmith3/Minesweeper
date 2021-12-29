import javax.swing.*;
import java.awt.*;

public class Tile extends JButton { // Tile class definition

    //ImageIcons
    ImageIcon trapIcon = new ImageIcon("Images/thermalTrap.png"); //holds trap icon, every tile has it
    ImageIcon numIcon; //holds icon for number of traps that are adjacent

    int row; //holds which row this tile is in
    int column; //holds which column this tile is in

    int numAdjacentTraps; //holds number of traps adjacent to this tile

    //Index for all adjacent Tiles:
    //Will be set to -1 if the neighbor does not exist
    int N, S, E, W, NE, NW, SE, SW;

    int[] neighbors = new int[8]; //array containing all of a tile's neighbor indexes

    boolean isTrap; //stores if this tile is a trap
    boolean isCleared; //stores if this tile has been cleared

    Color initColor = new Color(68, 218, 245);

    public Tile() { //Tile constructor

        this.setOpaque(true);
        this.setBackground(initColor);
        this.setEnabled(false);

    }

    //Set Methods
    public void setRow(int r){ //method to set this tile's row
        row = r;
    }

    public void setColumn(int c){ //method to set this tile's column
        column = c;
    }


    public void setNumIcon(ImageIcon n){ //method to set the numIcon
        numIcon = n;
    }


    public void setIsTrap(boolean t){ //method to set isTrap for this tile
        isTrap = t;
    }

    public void setIsCleared(boolean c){
        isCleared = c;
    }

    public void setN(int n){ //method to set north neighbor index for this tile ( N )
        N = n;
    }

    public void setS(int n){ //method to set south neighbor index for this tile ( S )
        S = n;
    }

    public void setE(int n){ //method to set east neighbor index for this tile ( E )
        E = n;
    }

    public void setW(int n){ //method to set west neighbor index for this tile ( W )
        W = n;
    }

    public void setNE(int n){ //method to set northeast neighbor index for this tile ( NE )
        NE = n;
    }

    public void setNW(int n){ //method to set northwest neighbor index for this tile ( NW )
        NW = n;
    }

    public void setSE(int n){ //method to set southeast neighbor index for this tile ( SE )
        SE = n;
    }

    public void setSW(int n){ //method to set southwest neighbor index for this tile ( SW )
        SW = n;
    }

    public void setNeighborsArray(){
        neighbors[0] = N;
        neighbors[1] = S;
        neighbors[2] = E;
        neighbors[3] = W;
        neighbors[4] = NE;
        neighbors[5] = NW;
        neighbors[6] = SE;
        neighbors[7] = SW;
    }

    //Get methods
    public ImageIcon getNumIcon(){ //method to return the numIcon for this tile
        return numIcon;
    }

    public int getRow(){ //method to return this tile's row
        return row;
    }

    public int getColumn(){ //method to return this tile's column
        return column;
    }

    public int getNumAdjacentTraps(){ //method to return the number of traps that are adjacent to this tile
        return numAdjacentTraps;
    }

    public boolean getIsTrap(){ //method to return value of isTrap
        return isTrap;
    }

    public boolean getIsCleared(){
        return isCleared;
    }

    public int getN(){
        return N;
    }

    public int getS(){
        return S;
    }

    public int[] getNeighbors(){
        return neighbors;
    }

    //Other Methods
    public void incrementNumAdjacentTraps(){ //method that increments the number of traps that are adjacent to this tile
        this.numAdjacentTraps++;
    }


} //end Tile CLass definition
