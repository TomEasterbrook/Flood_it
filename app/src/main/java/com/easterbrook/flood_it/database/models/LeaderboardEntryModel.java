package com.easterbrook.flood_it.database.models;

import android.content.ContentValues;

import com.easterbrook.flood_it.database.tables.LeaderboardEntryTable;
import com.easterbrook.flood_it.database.tables.LeaderboardTable;

import java.util.ArrayList;
import java.util.List;
/*
    *A class to represent An individual leaderboard
 */

public class LeaderboardEntryModel {
    private String playerName;
    private int score;
    private LeaderboardModel leaderboard;

    public LeaderboardEntryModel(){

    }

    public LeaderboardEntryModel(String playerName, int score, LeaderboardModel leaderboard) {
        this.playerName = playerName;
        this.score = score;
        this.leaderboard = leaderboard;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
    public LeaderboardModel getLeaderboard (){
        return leaderboard;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLeaderboard(LeaderboardModel leaderboard) {
        this.leaderboard = leaderboard;
    }

    public static List<LeaderboardEntryModel> generateTestData(){
        ArrayList<LeaderboardEntryModel> testData = new ArrayList<>(10);
        testData.add(new LeaderboardEntryModel("Dan",15,null));
        testData.add(new LeaderboardEntryModel("Cara",17 ,null));
        testData.add(new LeaderboardEntryModel("Samuel",20,null));
        testData.add(new LeaderboardEntryModel("Carrie",14,null));
        testData.add(new LeaderboardEntryModel("Georgina",12,null));
        return testData;
    }
    public ContentValues toValues(){
        ContentValues values = new ContentValues(3);
        values.put(LeaderboardEntryTable.COLUMN_PLAYER_NAME,getPlayerName());
        values.put(LeaderboardEntryTable.COLUMN_SCORE,getScore());
        values.put(LeaderboardEntryTable.COLUMN_LEADERBOARD,leaderboard.getId());
        return values;
    }
}
