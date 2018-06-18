package com.easterbrook.flood_it.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.easterbrook.flood_it.logic.Game;
import com.easterbrook.flood_it.views.GameView;


public class GameActivity extends Activity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int gridWidth = intent.getIntExtra("grid_width", Game.DEFAULT_WIDTH);
        int gridHeight = intent.getIntExtra("grid_height", Game.DEFAULT_HEIGHT);
        int gridColourCount = intent.getIntExtra("grid_colour_count", Game.DEFAULT_COLOUR_COUNT);
        gameView = new GameView(this,gridHeight,gridWidth,gridColourCount);
        setContentView(gameView);

    }
}
