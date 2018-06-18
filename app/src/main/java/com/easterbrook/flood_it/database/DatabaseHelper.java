package com.easterbrook.flood_it.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.easterbrook.flood_it.database.tables.LeaderboardEntryTable;
import com.easterbrook.flood_it.database.tables.LeaderboardTable;

/**
 * A class to set up the basic database Schema and keep track of its version
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "flood_it_leaderboard.db";
    private static final int DATABASE_VERSION = 4;
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LeaderboardTable.SQL_CREATE);
        CreateLeaderboards(db);
        db.execSQL(LeaderboardEntryTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LeaderboardEntryTable.SQL_DROP);
        db.execSQL(LeaderboardTable.SQL_DROP);
        onCreate(db);
    }
   //Helper method to create the SQL statements for the required leaderboard tables
    private void  CreateLeaderboards(SQLiteDatabase database){

        int id = 1;
        for (int i = 6; i<=26; i+=4){
            for (int k = 3; k<=8; k++){
                database.execSQL("INSERT INTO " + LeaderboardTable.TABLE_NAME + " VALUES (" + id + "," + i + "," + k +");");
                id++;
            }
        }
    }
}
