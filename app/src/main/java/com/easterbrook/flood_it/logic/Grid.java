package com.easterbrook.flood_it.logic;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class which logically represents the grid of tiles
 */

 class Grid {
    private SquareColour [] [] grid;
    private final int numberOfRows;
    private final int numberOfColumns;
    private final int numberOfColours;
    private Random random;
    private ArrayList<Integer> neighbourPossibilities;
     Grid(int rows,int columns,int colourCount){
        grid = new SquareColour [rows][columns];
        numberOfRows = rows;
        numberOfColumns = columns;
        numberOfColours = colourCount;
        random = new Random();
        prepareGrid(colourCount);

    }
     SquareColour getValue(int x,int y){
        return grid[x][y];
    }
     void setValue(int x,int y,SquareColour value){
        grid[x][y] = value;
    }
    //Configure is a grid of the right size and number of colours
    private void prepareGrid(int numberOfColours){
        for (int x = 0; x<numberOfRows; x++){
            for (int y = 0; y<numberOfColumns; y++){
            setValue(x,y,determineColourOfGridCell(x,y));
        }
     }
    }

    //Determines what colour a tile should take. Implements clustering bias in order to  make it easier for the player
    private  SquareColour determineColourOfGridCell(int x, int y){

        if (((random.nextInt(10)+1)>2)||(y==0)){
            return SquareColour.valueOf(random.nextInt(numberOfColours)+1);
        }else {
          return getValue(x,y-1);
        }
    }
    //An implementation of a recursive flood fill algorithm
     void floodFill(int x, int y,SquareColour oldColour,SquareColour newColour){
        if (x<0){
            return;
        }
        if (y<0){
            return;
        }
        if (x>=grid.length){
            return;
        }
        if (y>=grid[x].length){
            return;
        }
       SquareColour cellColour = getValue(x,y);
       if (cellColour == newColour){
           return;
       }
       if (oldColour!=cellColour){
           return;
       }
       setValue(x,y,newColour);

       floodFill(x+1,y,oldColour,newColour);
       floodFill(x-1,y,oldColour,newColour);
       floodFill(x,y+1,oldColour,newColour);
       floodFill(x,y-1,oldColour,newColour);

    }
    //Checks to see whether the grade is a solid colour
     boolean isGridSolidColour(){
        boolean solid = true;
        SquareColour startColour = getValue(0,0);
        for (int row = 0; row<numberOfRows;row++ ){
            for (int col = 0; col<numberOfColumns; col++){
                if (getValue(row,col)!= startColour){
                    solid = false;
                    break;

                }
            }
            if (!solid){
                break;
            }
        }
        return solid;
    }


}
