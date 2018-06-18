package com.easterbrook.flood_it.database.tables;

/**
 * Helper class containing Constance which can be used when referencing the leaderboard entry table
 */

public class LeaderboardEntryTable {
    public static final String  TABLE_NAME = "LeaderboardEntry";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PLAYER_NAME = "player_name";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_LEADERBOARD = "leaderboard";
    public static final String[] ALL_COLUMNS = {COLUMN_ID,COLUMN_PLAYER_NAME,COLUMN_SCORE,COLUMN_LEADERBOARD};
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_PLAYER_NAME + " TEXT,"+
                    COLUMN_SCORE + " INTEGER,"+
                    COLUMN_LEADERBOARD + " INTEGER,"+
                    "FOREIGN KEY("+COLUMN_LEADERBOARD+") REFERENCES "+ LeaderboardTable.TABLE_NAME+"("+LeaderboardTable.COLUMN_ID+")"+
                    ");";
    public static final String SQL_DROP =
            "DROP TABLE " + TABLE_NAME;


}

