package com.easterbrook.flood_it.database.models;

/**
 * Class to represent an individual leaderboard entry
 */

public class LeaderboardModel {
    private int id;
    private int gridSize;
    private int colourCount;
    public LeaderboardModel(){

    }

    public LeaderboardModel(int id, int gridSize, int colourCount) {
        this.id = id;
        this.gridSize = gridSize;
        this.colourCount = colourCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getColourCount() {
        return colourCount;
    }

    public void setColourCount(int colourCount) {
        this.colourCount = colourCount;
    }

    @Override
    public String toString() {
        return "LeaderboardModel{" +
                "id=" + id +
                ", gridSize=" + gridSize +
                ", colourCount=" + colourCount +
                '}';
    }
    public String getCaption(){
        return "Size: " + gridSize + "x" + gridSize + " Colours: " + colourCount;
    }
}
