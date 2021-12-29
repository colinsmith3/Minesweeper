import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Grid extends JPanel {

    ////Number of numIcon Image Icons
    final int MAX_NUM_ICONS = 8; //8 is the most amount of traps a tile can have adjacent to it
    final int MAX_NUM_ADJS = 8; //8 is also the max amount of adjacent tiles or "neighbors" a tile can have

    ///Arrays
    Tile[] tileArray; //array that holds all tiles on grid
    int[] trapField; //array that holds all tiles that are traps
    ImageIcon[] numIcons; //array that holds the number icons

    ///Ints
    int numTiles; //stores total number of tiles for current game
    int numTraps; //stores the number of traps for current grid
    int tilesShown; //holds number total number of tile button clicks
    int gameWonCount; //holds the number of tiles needed to win the game ( num tiles - numTraps )

    boolean isGameLost; //stores if game has been lost and a trap was selected
    boolean isGameWon; //stores if game has been won

    //Colors for tileHandler changes
    Color trapColor = (Color.red);
    Color notTrap = Color.yellow;

    //Handler
    tileHandler tHandler = new tileHandler();

    public Grid(int gridSize, int traps){ //Grid constructor: Needs int gridSize

        int rowAndColSize = (int) Math.sqrt(gridSize);

        this.setLayout(new GridLayout(rowAndColSize,rowAndColSize)); //set layout to GridLayout

        setNumTiles(gridSize); //set numTiles for this Grid to passed in gridSize
        setNumTraps(traps); //set numTraps for this Grid  to passed in traps
        setGameWonCountAndTilesShown();

        tileArray = new Tile[numTiles]; //build tileArray for number of tiles based on gridSize
        trapField = new int[numTraps]; //build trapField array to store all the tiles that are traps in the tileArray

        // Set up tileArray and trapField arrays
        setTileArray(tileArray);

        //Set up numIcons array
        setNumIconsArray();

        for (int i = 0; i < numTiles; i++) { //add all tiles in tileArray to Grid
            this.add(tileArray[i]);
        }

        setRowsAndColumns(tileArray); //call this to set rows and column vars for each tile
        setNeighbors(tileArray); //call this method to set up neighbors for each tile in tileArray ( rows and columns must be set prior )
        setTraps(); //call setTraps to use tileArray to set up trapField array with traps
        setNumAdjacentTraps(tileArray); //call this to find the correct number of traps that are adjacent to each tile and set their numAdjacentTraps variable
        setNumIconsForTiles(tileArray); //call this to set each tile's numIcon based on the function called above

    }//end Grid constructor

    ////Set Methods
    public void setTileArray(Tile[] t){ //method to set up tile array
        for (int i = 0; i < t.length; i ++){
            t[i] = new Tile(); //instantiate tiles in tileArray
            t[i].addActionListener(tHandler); //add tileHandler to each Tile
        }
    }

    public void setTraps(){ //method to set the traps for the game and place them into trapField array
        //Reset all tiles' isTrap to false in case this function is called more than once
        for (int i = 0; i < tileArray.length; i++){
            tileArray[i].setIsTrap(false);
        }

        //Generate a random index for the trap set
        for (int j = 0; j < numTraps; j++){ //go through an amount of times equal to numTraps
            int ranIndex = genRanTileIndex(numTiles); //generate random index in grid

            while(tileArray[ranIndex].getIsTrap()){ //while this index is already a trap, generate a new random index
                ranIndex = genRanTileIndex(numTiles);
            }
            tileArray[ranIndex].setIsTrap(true); //set this tile's isTrap to true
            trapField[j] = ranIndex; //make this tile the jth tile in trapField
        }
    }

    public void setRowsAndColumns(Tile[] t){ //method to call functions that set rows and columns in constructor
        setRows(t);
        setColumns(t);
    }

    public void setRows(Tile[] t){ //method to set the row for every tile in the tileArray

        //row and column size both always equal sqrt of grid size (numTiles) because Grid is always square
        int rowSize = (int) Math.sqrt(numTiles);

        int rowCountForRestOfFirstCol = 2; //starts at two because index 0 will be set in first row
        int rowMultiply = 2;

        for (int i = 0; i < t.length; i++){ //go through the entire tileArray

            if ( i < rowSize ) { //set first row
                t[i].setRow(1);
            }
            else if ( i % rowSize == 0 ){ //set row for rest of first column
                t[i].setRow(rowCountForRestOfFirstCol);
                rowCountForRestOfFirstCol++;
            }
            else { //if current tile is not in first row or first column
                if ((i+1) % rowSize == 0){ //see if current tile is last in row
                    t[i].setRow(rowMultiply); //set to rowMultiply, initially two
                    rowMultiply++; //increment for next time end of row is caught
                }
                else { //current tile is not first row, first column, or last column
                    t[i].setRow(rowMultiply); //should be set first at t index 5, incremented at the end of row in if statement above
                }
            }
        }
    }

    public void setColumns(Tile[] t){ //method to set the columns of every tile in the tileArray

        //row and column size both always equal sqrt of grid size (numTiles) because Grid is always square
        int colSize = (int) Math.sqrt(numTiles);

        for (int i = 0; i < t.length; i++) { //go through the entire tileArray

            if (i < colSize){ //set first row's columns
                t[i].setColumn(i+1);
            }
            else if ( i % colSize == 0){ //set first column
                t[i].setColumn(1);
            }
            else { //set rest of columns
                t[i].setColumn((i % colSize) + 1);
            }
        }
    }

    public void setNeighbors(Tile[] t){ //method to set the adjacent ( N,S,E,W,NE,NW,SE,SW vars ) for each tile as an index into tileArray
        //This method can ONLY be used AFTER the rows and columns have be set

        int rowSize = (int) Math.sqrt(numTiles);

        for (int i = 0; i < t.length; i ++){ //for each tile on grid

            //set North neighbor
            int north = ( i - rowSize); //north neighbor is exactly one row behind current tile
            if (north < 0){ //if in first row, north will be negative and should be set to 0
                t[i].setN(-1);
            }
            else { //otherwise, set the tile's N var as north
                t[i].setN(north);
            }

            //set South neighbor
            int south = ( i + rowSize ); //south will be exactly one row ahead of current tile
            if (south >= numTiles){ //if in last row, south will be greater than the total number of tiles and should be set to -1
                t[i].setS(-1);
            }
            else { //otherwise, set the tile's S var as south
                t[i].setS(south);
            }

            //set East neighbor
            int east = ( i + 1 ); //east will be the tile exactly one past the current tile
            if ( east % rowSize == 0 ){ //if the current tile is the last tile in the row, it will not have an east neighbor
                t[i].setE(-1);
            }
            else { //otherwise, set the tile's E var as east
                t[i].setE(east);
            }

            //set West neighbor
            int west = ( i - 1 ); //west will be exactly 1 place behind the current tile\
            if (t[i].getColumn() == 1){ //if current tile is in the first column, it's W should be set to -1
                t[i].setW(-1);
            }
            else { //Otherwise, set the tile's W var as west
                t[i].setW(west);
            }

            //set Northeast neighbor
            int northeast = ( (t[i].getN()) + 1 ); //northeast will be one to the right of the current tile's north
            if (t[i].getRow() == 1 || t[i].getColumn() == rowSize){ //if current tile is in first row or last column, northwest should be set to -1
                t[i].setNE(-1);
            }
            else { //otherwise, set current tile's NE to northeast
                t[i].setNE(northeast);
            }

            //set Northwest neighbor
            int northwest = ( t[i].getN() - 1 ); //northwest will be 1 behind the current tile's north
            if (t[i].getRow() == 1 || t[i].getColumn() == 1){ //if current tile is in first row or first column, northwest should be set to -1
                t[i].setNW(-1);
            }
            else { //otherwise, set current's NW to northwest
                t[i].setNW(northwest);
            }

            //set Southeast neighbor
            int southeast = ( t[i].getS() + 1 ); //southeast will be one to the right of current's south
            if (t[i].getRow() == rowSize || t[i].getColumn() == rowSize){ //if current tile is in last row or last column, southeast should be set to -1
                t[i].setSE(-1);
            }
            else { //otherwise, set current's SE as southeast
                t[i].setSE(southeast);
            }

            //set Southwest neighbor
            int southwest = ( t[i].getS() - 1); //southwest will be 1 to the left of current tile's south
            if (t[i].getRow() == rowSize || t[i].getColumn() == 1){ //if in last row or first column, SW should be set to -1
                t[i].setSW(-1);
            }
            else { //otherwise, set current's SW var to southwest
                t[i].setSW(southwest);
            }

            t[i].setNeighborsArray(); //once neighbors are all set, set up current tile's neighborsArray

        }
    }

    public void setTileNumAdjacentTraps(int[] tf, Tile t){ //method to find the number of adjacent traps to a tile passed in using trapField array

        int[] tempNeighbors = t.getNeighbors(); //make a temporary array for the neighbors of the passed tile

        for (int i = 0; i < MAX_NUM_ADJS; i++){ //go through all the neighbors of the passed tile

            if (!(tempNeighbors[i] == -1)) { //if neighbor is -1 and off grid, exit loop, continue otherwise

                for (int j = 0; j < numTraps; j++) { //go through every trap for the current neighbor

                    if ((tempNeighbors[i] == tf[j])) { // if the current neighbor is equal to the trap (both indexes into tileArray) then increment the tile's numAdjacentTraps
                        t.incrementNumAdjacentTraps();
                    }
                }
            }
        }
    }

    public void setNumAdjacentTraps(Tile[] t){ //method to set numAdjacentTraps for every tile in tileArray using setTileNumAdjacentTraps function
        for (int i = 0; i < numTiles; i++) { //go through every tile in trapField array
            setTileNumAdjacentTraps(trapField, t[i]);
        }
    }

    public void setNumIconsForTiles(Tile[] t){ //method to set the correct numIcon for each tile based on its numAdjacentTraps var set in the function above
        for (int i = 0; i < t.length; i++){ //go through each tile in tileArray which will be passed in
            if (t[i].getNumAdjacentTraps() > 0) { //check that it does have at least one trap adjacent to it
                t[i].setNumIcon(numIcons[t[i].getNumAdjacentTraps() - 1 ]); //set the numIcon of the current tile to the index in the numIcons array which matches its number of adjacent traps ( subtract 1 because numIcons is indexed at 0 )
            }
        }
    }

    public void setNumIconsArray() { //method to set up numIcons array INDEX OF NUMBER IMAGE WILL BE ONE LESS THAN NUMBER
        numIcons = new ImageIcon[MAX_NUM_ICONS];

        for (int i = 0; i < MAX_NUM_ICONS; i++){
            numIcons[i] = new ImageIcon("Images/num"+(i+1)+".png");
        }

    }

    public void setNumTiles(int t){ //method to set numTiles for current grid
        numTiles = t;
    }

    public void setNumTraps(int t){ //method to set numTraps
        numTraps = t;
    }

    public void setGameWonCountAndTilesShown(){
        tilesShown = 0;
        gameWonCount = numTiles - numTraps;
    }

    public void setIsGameLost(boolean l){
        isGameLost = l;
    }

    ////Get Methods
    public int getNumTiles(){ //method to return number of Tiles for this Grid
        return numTiles;
    }

    public int getNumTraps(){ //method to return numTraps
        return numTraps;
    }

    ////Other Methods

    public int genRanTileIndex(int gridSize){ //method to generate a random index in tileArray
        int min = 0;
        int max = gridSize;

        return (int) ((Math.random() * (max - min)) + min);
    }

    public void enableTileArray(){ //method to enable all tiles in tile Array (called in cweasel grid by start button)
        for (int i = 0; i < numTiles; i++){
            tileArray[i].setEnabled(true);
        }
    }


    public void clear(Tile t){ //recursive method used on a tile if it has no adjacent traps to clear
        tilesShown++; //increment tiles that are shown
        t.setBackground(notTrap); //set the backGround of this tile to the notTrap color
        t.setEnabled(false); //disable the tile ( out of play )
        t.setIsCleared(true);

        int[] myNeighbors = t.getNeighbors(); //store this tile's neighbors

        for (int i = 0; i < myNeighbors.length; i++){ //go through all neighbors of the passed Tile
            if (myNeighbors[i] != -1 && !tileArray[myNeighbors[i]].getIsCleared()){ //check if it is on grid and not cleared
                if (tileArray[myNeighbors[i]].getNumAdjacentTraps() > 0 && !tileArray[myNeighbors[i]].getIsTrap()){ //if the neighbor to the cleared tile has traps adjacent and is not a trap
                    tileArray[myNeighbors[i]].setIcon(tileArray[myNeighbors[i]].getNumIcon());
                    tileArray[myNeighbors[i]].setBackground(notTrap);
                    tileArray[myNeighbors[i]].setIsCleared(true);
                    tilesShown++;
                }
                if (tileArray[myNeighbors[i]].getNumAdjacentTraps() == 0 && !tileArray[myNeighbors[i]].getIsTrap()){ //if that neighbor also has no adjacent traps and is not a trap, run clear on it too
                    clear(tileArray[myNeighbors[i]]);
                }
            }
        }
    }

    public void trapWasFirstClick(){ //method to reset the grid if the first tile clicked was a trap

            for (int i = 0; i < numTiles; i++) { //remove all tiles in tileArray
                this.remove(tileArray[i]);
            }
            //reconstruct arrays
            tileArray = new Tile[numTiles];
            trapField = new int[numTraps];

            // Set up new arrays
            setTileArray(tileArray);
            setRowsAndColumns(tileArray); //call this to set rows and column vars for each tile
            setNeighbors(tileArray); //call this method to set up neighbors for each tile in tileArray ( rows and columns must be set prior )
            setTraps(); //call setTraps to use tileArray to set up trapField array with traps
            setNumAdjacentTraps(tileArray); //call this to find the correct number of traps that are adjacent to each tile and set their numAdjacentTraps variable
            setNumIconsForTiles(tileArray); //call this to set each tile's numIcon based on the function called above

            for (int j = 0; j < numTiles; j++) { //add new tileArray to Grid
                this.add(tileArray[j]);
            }
            this.validate(); //validate the new grid

            for (int k = 0; k < numTiles; k++){
                tileArray[k].setEnabled(true);
            }

    }


    public void trapFirstClick(Tile t){
        int trapIndex = -2;

        for (int i = 0; i < numTiles; i++){
            if (tileArray[i] == t){
                trapIndex = i;
            }
        }

        if (trapIndex >= 0) {
            while (tileArray[trapIndex].getIsTrap()) {
                trapWasFirstClick();
            }

            if (tileArray[trapIndex].getNumAdjacentTraps() > 0){
                tileArray[trapIndex].setIcon(tileArray[trapIndex].getNumIcon());
                tilesShown++;
                tileArray[trapIndex].setIsCleared(true);
                tileArray[trapIndex].setBackground(notTrap);
            }

            else if (tileArray[trapIndex].getNumAdjacentTraps() == 0){
                clear(tileArray[trapIndex]);
            }
        }

    }

    public void gridGameLost(){ //method for when trap was clicked, and it was not the first click of game
        for (int i = 0; i < numTiles; i++){
            tileArray[i].setBackground(trapColor);
            tileArray[i].setEnabled(false);
        }
        setIsGameLost(true);
    }

    public void wonGridGame(){
        for (int i = 0; i < numTiles; i++){
            tileArray[i].setBackground(notTrap);
            tileArray[i].setEnabled(false);
        }
        isGameWon = true;
    }


    public boolean isGridGameWon(){ //method for checking when the gameWonCount is equal to tilesShown
        return tilesShown == gameWonCount;
    }

    /////PRINT TESTS
    public void printTerminalFullTest(Tile[] t){
        for (int i = 0; i < numTiles; i++){

            System.out.println("Tile "+i+":");
            System.out.println("numAdjacentTiles: ");
            System.out.println(t[i].getNumAdjacentTraps());
            System.out.println("Neighbors: ");

            int[] printNeigh = t[i].getNeighbors();

            for (int j = 0; j < MAX_NUM_ADJS; j++){
                System.out.println(printNeigh[j]);
            }

            System.out.println("Row: ");
            System.out.println(t[i].getRow());
            System.out.println("Column:");
            System.out.println(t[i].getColumn());
        }
    }

    private class tileHandler implements ActionListener{ //handler for tile JButtons
        @Override
        public void actionPerformed(ActionEvent e) {

            Tile whichTile = (Tile) e.getSource(); //store which tile was pressed

            if (whichTile.getIcon() == null) { //if the tile has a null Icon it can be selected, otherwise it cannot (cleared tiles are disabled)

                if (tilesShown == 0 && whichTile.getIsTrap()){ //if first tile clicked is a trap, reset grid
                    trapFirstClick(whichTile);

                }
                else { //first was not a trap
                    if (whichTile.getIsTrap()) { //if tile clicked is a trap, show trapIcon and run gameLost method and cweasel gameState method
                        whichTile.setIcon(whichTile.trapIcon);
                        gridGameLost();
                    } else if (whichTile.getNumAdjacentTraps() == 0) { //if the tile has no traps adjacent, run recursive clear method
                            clear(whichTile); //this method increments tilesShown itself
                    } else if (!(whichTile.getNumIcon() == null)) { //if tile clicked has traps adjacent to it, show it's numIcon and set background to not trap
                        whichTile.setIcon(whichTile.getNumIcon());
                        whichTile.setBackground(notTrap);
                        tilesShown++;
                        whichTile.setIsCleared(true);
                    }
                }
            }
            if (isGridGameWon()){ //check if game is won after every click, if so, call gameState method in cweasel
                wonGridGame();
            }
        }
    }//end tileHandler class definition

}//end Grid class definition
