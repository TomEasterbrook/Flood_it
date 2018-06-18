package com.easterbrook.flood_it.database.tables;
//A class which contains constant values relating to the leaderboard table
public class LeaderboardTable {
    public static final String  TABLE_NAME = "Leaderboard";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_GRID_SIZE = "grid_size";
    public static final String COLUMN_COLOUR_COUNT = "colour_count";
    public static final String[] ALL_COLUMNS = {COLUMN_ID,COLUMN_GRID_SIZE,COLUMN_COLOUR_COUNT};
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_GRID_SIZE + " INTEGER,"+
                    COLUMN_COLOUR_COUNT + " INTEGER"+
                    ");";
    public static final String SQL_DROP =
            "DROP TABLE " + TABLE_NAME;

}