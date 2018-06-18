package com.easterbrook.flood_it.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.easterbrook.flood_it.R;

import com.easterbrook.flood_it.logic.Game;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void playClassicButtonClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void playCustomButtonClick(View view){
        Intent intent = new Intent(this, CustomGameSettingsActivity.class);
        startActivity(intent);
    }
    public void leaderboardButtonClick(View view){
        Intent intent = new Intent(this,LeaderboardDisplayActivity.class);
        startActivity(intent);
    }
    public void howToPlayButtonClick(View view){
        Intent intent = new Intent(this,HowToPlayActivity.class);
        startActivity(intent);
    }
    public void aboutTheAppButtonClick(View view){
        Intent intent = new Intent(this,AboutTheAppActivity.class);
        startActivity(intent);
    }
}
