package com.easterbrook.flood_it.logic;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple enumeration which sets out the various colours a tile tile can take
 */

public enum SquareColour {


    GREEN(1, Color.GREEN),
    BLUE(2,Color.BLUE),
    RED(3,Color.RED),
    ORANGE(4,Color.rgb(255,204,102)),
    LIGHT_BLUE(5,Color.CYAN),
    YELLOW(6,Color.YELLOW),
    PURPLE(7,Color.rgb(128,0,128)),
    PINK(8,Color.MAGENTA);


    private int gridValue;
    private int paintValue;
    private static Map map = new HashMap<>();

     SquareColour(int value,int paintValue) {
        this.gridValue = value;
        this.paintValue = paintValue;
    }

    static {
        for (SquareColour squareColour : SquareColour.values()) {
            map.put(squareColour.gridValue, squareColour);
        }
    }

    public static SquareColour valueOf(int squareColour) {
        return (SquareColour) map.get(squareColour);
    }

    public int getGridValue() {
        return gridValue;
    }

    public int getPaintValue(){
        return paintValue;
    }
}
