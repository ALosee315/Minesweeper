/*--------------------------------------------------------------

-SUB-TO-WARRIORPUG-ON-YT-

    File: MineSweeperApp.java
    Date: 2/17/23
    Purpose: A tile class to be put into a 2D array for my minesweeper game
    Author: ALosee315
    Sauce Code: 8

----------------------------------------------------------------*/
public class Tile{
    
    private boolean isFlagged; //Is tile flagged
    private boolean isRevealed; //Is tile revealed
    private boolean isFirst; //Is the tile the first clicked, or around the first tile clicked
    private int numMines; //-1 if is mine
    
    //Constructor------------------------------------------------------
    public Tile(){
        numMines = 0;
        isRevealed = false;
        isFlagged = false;
        isFirst = false;
    }
    
    //Accessors-----------------------------------------------------------------------------
    public boolean getMineStatus(){ //Returns true if the tile is a mine, false if it isn't
        if(numMines == -1){
            return true;
        }else{
            return false;
        }
    }
    
    public int getMineNum(){ //Returns true if the tile is a mine, false if it isn't
        return numMines;
    }
    
    //Returns true if these booleans are true
    public boolean getRevealed(){ 
        return isRevealed;
    }
    
    public boolean getFlagged(){
        return isFlagged;
    }
    
    public boolean getFirst(){
        return isFirst;
    }
    //Mutators---------------------------------------------------------------------------
    public void setFirst(){ //First Tile Clicked
       isFirst = true;
    }
    public void setRevealed(){ //Tile Revealed
       isRevealed = true;
    }
    
    public void setFlagged(){ //Tile flagged
       isFlagged = true;
    }
    
    public void toggleFlagged(){ //Flagged = !Flagged
        isFlagged = !isFlagged;
    }
    
    public void reset(){ //Set tile to starting state
        numMines = 0;
        isRevealed = false;
        isFlagged = false;
        isFirst = false;
    }   
    
    public void incMineNum(){ //Increment number of mines around the tile
        numMines++;
    }
    public void setMine(){ //sets the tile to a mine tile
        numMines = -1; 
    }
    
    public void setMineNum(int num){ //Set the number of mines around a tile
        numMines = num;
    }
    
    
}
