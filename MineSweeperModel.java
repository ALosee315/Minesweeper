
/*--------------------------------------------------------------

-S-H-E-N---H-I-G-H-S-C-H-O-O-L

    File: MineSweeperModel.java
    Date: 2/17/23
    Purpose: Logic for my minesweeper 2D array
    Author: Arcangelo Losee
    Sauce Code: 8

----------------------------------------------------------------*/
import java.util.Random;

public class MineSweeperModel {

    //Instance variables
    Random r = new Random();
    private int totalMines = 10;
    private int flags = totalMines;
    private boolean gameActive = true;
    private static Tile[][] mineArray = new Tile[8][8];

    public MineSweeperModel() {
        //Initialize a 2D array of tile objects
        for (int r = 0; r < mineArray.length; r++) {
            for (int c = 0; c < mineArray.length; c++) {
                mineArray[r][c] = new Tile();
            }
        }
    }
    public MineSweeperModel(int lvl) {
       //Changes the size of the mines array and amount of mines depending on the level
        if(lvl == 1){
            mineArray = new Tile[8][8];
            totalMines = 10;
        }else if(lvl == 2){
            mineArray = new Tile[14][14];
            totalMines = 30;
        }else if(lvl == 3){
            mineArray = new Tile[15][20];
            totalMines = 50;
        }
        flags = totalMines;
        for (int r = 0; r < mineArray.length; r++) {
            for (int c = 0; c < mineArray[0].length; c++) {
                mineArray[r][c] = new Tile();
            }
        }
    }
    

    public void fillMines() { //After the player clicks, this method will place the amount of mines based on the level in the grid. Makes sure mines are not where the player has already revealed numbers.
        int x;
        int y;
        int minesToPlace = totalMines;
        while (minesToPlace > 0) {
            x = r.nextInt(0, mineArray.length);
            y = r.nextInt(0, mineArray[0].length);

            if (mineArray[x][y].getMineStatus() == false && mineArray[x][y].getRevealed() == false && mineArray[x][y].getFirst() == false) {
                mineArray[x][y].setMine();

                minesToPlace--;
            }
        }
    }

    public void clear() { //Reset each tile to be empty
        gameActive = true;
        for (int r = 0; r < mineArray.length; r++) {
            for (int c = 0; c < mineArray[0].length; c++) {
                mineArray[r][c].reset();

            }
        }
    }

    
    public void flag(int r, int c) { //Sets the specified tile to flagged or not
        if (mineArray[r][c].getRevealed() == false && countFlags()<flags) {
            mineArray[r][c].toggleFlagged();
        }else if(mineArray[r][c].getFlagged()==true){
            mineArray[r][c].toggleFlagged();
        }

    }
    public int countFlags(){ //For counting how many total tiles are flagged
        int flags = 0;
        for (int r = 0; r < mineArray.length; r++) {
            for (int c = 0; c < mineArray[0].length; c++) {
                if(mineArray[r][c].getFlagged() == true){
                    flags++;
                }

            }
        }
        return flags;
    }
    
    public void firstClick(int r, int c) {//Sets tiles around the first one clicked to have a true first condition, useful so that mines aren't placed in these tiles

      
        mineArray[r][c].setRevealed();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                try {
                    mineArray[r + i][c + j].setFirst();
                } catch (Exception e) {
                    
                }

            }
        }
        this.fillMines();//Fill in mines
        this.fillMineNumbers();//Give each tile a number of mines around it
        this.clicked(r, c);//Reveal the first clicked tile


    }

    public void clicked(int r, int c) { //Tile Clicked
        
        mineArray[r][c].setRevealed();
        if (mineArray[r][c].getMineNum() == -1) {
            //Loss
            gameActive = false;
        }
        if (mineArray[r][c].getMineNum() == 0) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    try {

                        if (mineArray[r + i][c + j].getMineNum() == 0) {
                            int newR = r + i;
                            int newC = c + j;
                            if ((newR != r || newC != c) && mineArray[newR][newC].getRevealed() == false) {
                                this.clicked(newR, newC); //Recursion for revealing tiles without any mines around them
                            }
                        } else {
                            mineArray[r + i][c + j].setRevealed();
                        }
                    

                    } catch (Exception e) {
                        //Tile is off the board
                    }

                }
            }
        }

    }

    public boolean getGameActive() { //A game is being played
        return gameActive;
    }

    public boolean checkWin() { //Check for win condition: all the mine tiles are flagged
        int minesFlagged = 0;
        for (int r = 0; r < mineArray.length; r++) {
            for (int c = 0; c < mineArray[0].length; c++) {
                if (mineArray[r][c].getMineNum() == -1 && mineArray[r][c].getFlagged() == true) {
                    minesFlagged++;
                }
            }
        }
        if (minesFlagged == totalMines) {
            return true;
        }
        return false;
    }

    public void fillMineNumbers() { //For each tile in the array, fill in the number of mines in tiles surrounding it
        

        for (int r = 0; r < mineArray.length; r++) {
            for (int c = 0; c < mineArray[0].length; c++) {
                int mineNum = 0;
                //For each tile, check the 3x3 grid around it for mines; handle error if the tile is on an edge
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        try {
                            if (mineArray[r + i][c + j].getMineStatus() == true) {
                                mineNum++;
                            }
                        } catch (Exception e) {
                            //Tile is off the board
                        }

                    }
                }
                if (mineArray[r][c].getMineStatus() == false) {
                    mineArray[r][c].setMineNum(mineNum);
                }
            }
        }
       

    }

    public static Tile[][] getMineArray() { //For accessing the array of tiles from the frame
        return mineArray;
    }
}
