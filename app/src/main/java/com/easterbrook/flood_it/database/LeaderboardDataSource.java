package com.easterbrook.flood_it.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.easterbrook.flood_it.database.models.LeaderboardEntryModel;
import com.easterbrook.flood_it.database.models.LeaderboardModel;
import com.easterbrook.flood_it.database.tables.LeaderboardEntryTable;
import com.easterbrook.flood_it.database.tables.LeaderboardTable;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to provide helper methods in order to facilitate the necessary database manipulation
 */
//Errors in SQL statements being falsely triggered by IDE detection as it thinks I'm using a different kind of database which uses alternative parameter mining
public class LeaderboardDataSource {
    private Context mContext;
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;

    public LeaderboardDataSource(Context context) {
        mContext = context;
        dbHelper = new DatabaseHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public LeaderboardModel selectLeaderboard(int gridSize, int colourCount) {
        LeaderboardModel leaderboard = new LeaderboardModel();
        String[] parameters = new String[] {Integer.toString(gridSize),Integer.toString(colourCount)};
        Cursor cursor = database.rawQuery("SELECT * FROM Leaderboard WHERE grid_size = ? AND colour_count = ?" ,parameters);
        cursor.moveToFirst();
        leaderboard.setId(cursor.getInt(cursor.getColumnIndex(LeaderboardTable.COLUMN_ID)));
        leaderboard.setGridSize(cursor.getInt(cursor.getColumnIndex(LeaderboardTable.COLUMN_GRID_SIZE)));
        leaderboard.setColourCount(cursor.getInt(cursor.getColumnIndex(LeaderboardTable.COLUMN_COLOUR_COUNT)));
        cursor.close();
        return leaderboard;

    }
    //A method to get the scores held in the specific leaderboard from the database
    public List<LeaderboardEntryModel>getScoresFromLeaderboard(LeaderboardModel leaderboard){
        LeaderboardEntryModel score;
        List<LeaderboardEntryModel> scores = new ArrayList<>(10);
        Cursor cursor = getLeaderboardRecords(leaderboard);
        while (cursor.moveToNext()){
           score = new LeaderboardEntryModel();
           score.setPlayerName(cursor.getString(cursor.getColumnIndex(LeaderboardEntryTable.COLUMN_PLAYER_NAME)));
           score.setScore(cursor.getInt(cursor.getColumnIndex(LeaderboardEntryTable.COLUMN_SCORE)));
           score.setLeaderboard(leaderboard);
           scores.add(score);
    }
        cursor.close();
        return scores;

    }
    //Checks whether the score A user has achieved is high enough to be entered into leaderboard
    public boolean isScoreHighEnough(LeaderboardModel leaderboard, int score) {
       Cursor cursor = getLeaderboardRecords(leaderboard);
        if (cursor.getCount()<10) {
            cursor.close();
            return true;
        }
        else {
            cursor.moveToFirst();
            int firstScore = cursor.getInt(cursor.getColumnIndex(LeaderboardEntryTable.COLUMN_SCORE));
            cursor.moveToLast();
            int lastScore = cursor.getInt(cursor.getColumnIndex(LeaderboardEntryTable.COLUMN_SCORE));
            if (score > firstScore && score< lastScore ){
                cursor.close();
                return true;
            }else {
                cursor.close();
                return false;
            }


        }

    }
//A method to insert score into a database
    public void insertScore(LeaderboardEntryModel score) {
        Cursor cursor = getLeaderboardRecords(score.getLeaderboard());
        if (cursor.getCount()<10){
            database.insert(LeaderboardEntryTable.TABLE_NAME,null,score.toValues());

        }else {
            cursor.moveToLast();
            removeScore(cursor.getInt(cursor.getColumnIndex(LeaderboardEntryTable.COLUMN_SCORE)));
            database.insert(LeaderboardEntryTable.TABLE_NAME,null,score.toValues());
        }



    }

    private void removeScore(int scoreId) {
        database.delete(LeaderboardEntryTable.TABLE_NAME,"id = ?",new String[]{Integer.toString(scoreId)});


    }
    private Cursor getLeaderboardRecords(LeaderboardModel leaderboard){
         return database.rawQuery("SELECT * FROM "+ LeaderboardEntryTable.TABLE_NAME +" WHERE " + LeaderboardEntryTable.COLUMN_LEADERBOARD + " = ? ORDER BY "+LeaderboardEntryTable.COLUMN_SCORE + " ASC",new String[]{Integer.toString(leaderboard.getId())});
    }
    public ArrayList<LeaderboardModel> getLeaderboardsWithValues(){

        Cursor cursor = database.rawQuery("SELECT DISTINCT " + LeaderboardEntryTable.COLUMN_LEADERBOARD +" FROM " + LeaderboardEntryTable.TABLE_NAME,null);
        if (cursor.getCount()>0){
            ArrayList<LeaderboardModel>leaderboards = new ArrayList<>(cursor.getCount());
            int leaderboardId;
            while (cursor.moveToNext()){
                leaderboardId = cursor.getInt(cursor.getColumnIndex(LeaderboardEntryTable.COLUMN_LEADERBOARD));
                leaderboards.add(resolveLeaderboard(leaderboardId));

            }
            cursor.close();
            return leaderboards;
        }else {
            return  null;
        }
    }
    private LeaderboardModel resolveLeaderboard(int id){
        Cursor cursor = database.rawQuery("SELECT * FROM "+ LeaderboardTable.TABLE_NAME +" WHERE " + LeaderboardTable.COLUMN_ID + " = ?",new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        LeaderboardModel leaderboard = new LeaderboardModel ();
        leaderboard.setId(cursor.getInt(cursor.getColumnIndex(LeaderboardTable.COLUMN_ID)));
        leaderboard.setGridSize(cursor.getInt(cursor.getColumnIndex(LeaderboardTable.COLUMN_GRID_SIZE)));
        leaderboard.setColourCount(cursor.getInt(cursor.getColumnIndex(LeaderboardTable.COLUMN_COLOUR_COUNT)));
        cursor.close();
        return leaderboard;


    }
}

