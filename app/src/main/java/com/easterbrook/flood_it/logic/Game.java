package com.easterbrook.flood_it.logic;

/**
 *A class to manage the logic behind the game
 */

public class Game extends AbstractGame {
    private Grid grid ;
    private int round = 1;
    private int roundLimit;

    private boolean isLost = false;
    public Game(int width, int height, int colourCount) {
        super(width, height, colourCount);
        roundLimit = (int) (width*1.5)+2;
        grid = new Grid(width,height,colourCount);

    }
    public boolean isLost() {
        return isLost;
    }
    public int getRoundLimit() {
        return roundLimit;
    }

    @Override
    public int getRound() {
        return round;
    }

    @Override
    protected void setColor(int x, int y, SquareColour colour) {
       grid.setValue(x,y,colour);
    }

    @Override
    public SquareColour getColor(int x, int y) {
      return grid.getValue(x,y);
    }

    @Override
    void notifyMove(int round) {
        for (GamePlayListener gamePlayListener:getGamePlayListeners()){
            gamePlayListener.onGameChanged(this,round);
        }
    }

    @Override
    void notifyWin(int round) {
        for (GameWinListener gameWinListener:getGameWinListeners()){
            gameWinListener.onWon(this,round);
        }

    }

    @Override
    public void playColour(SquareColour clr) {
        if ((grid.getValue(0,0)!= clr) && (round<roundLimit)) {
            grid.floodFill(0,0,grid.getValue(0,0),clr);
            round++;
            if (isWon()){
                notifyWin(getRound());
            }else {
                if (getRound()==roundLimit){
                    isLost = true;
                }
                notifyMove(getRound());
            }



        }

    }

    @Override
    public boolean isWon() {
        return grid.isGridSolidColour();
    }

    public Grid getGrid(){
        return grid;
    }


}
