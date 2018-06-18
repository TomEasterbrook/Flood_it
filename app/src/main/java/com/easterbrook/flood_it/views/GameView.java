package com.easterbrook.flood_it.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easterbrook.flood_it.R;
import com.easterbrook.flood_it.activities.LeaderboardDisplayActivity;
import com.easterbrook.flood_it.activities.MainMenuActivity;
import com.easterbrook.flood_it.database.LeaderboardDataSource;
import com.easterbrook.flood_it.database.models.LeaderboardEntryModel;
import com.easterbrook.flood_it.database.models.LeaderboardModel;
import com.easterbrook.flood_it.logic.AbstractGame;
import com.easterbrook.flood_it.logic.Game;
import com.easterbrook.flood_it.logic.SquareColour;

/**
 * A class which renders the game to the screen
 */

public class GameView extends View implements AbstractGame.GamePlayListener,AbstractGame.GameWinListener {
    private Paint
                borderPaint,
                squarePaint,
                textPaint;

    private Game _game;
    private GestureDetector touchDetector;
    private RectF grid;
    private float cellWidth,cellHeight,headerX,headerY;

    private Bitmap headerBitmap;
    private boolean raisedAlert = false;
    private LeaderboardDataSource leaderboardDataSource;
    private LeaderboardModel leaderboard;
    private EditText scoreInput;
    private Context mContext;
    private MediaPlayer moveSoundPlayer;


    public GameView(Context context,int numberOfRows, int numberOfColumns, int numberOfColours) {
        super(context);
        mContext = context;
        _game = new Game(numberOfColumns,numberOfRows,numberOfColours);
        init();

    }

//Initialises required variables for the view to be drawn
    private void init(){
        moveSoundPlayer = MediaPlayer.create(mContext,R.raw.move);
        leaderboardDataSource = new LeaderboardDataSource(mContext);
        leaderboardDataSource.open();
        leaderboard = leaderboardDataSource.selectLeaderboard(_game.getWidth(),_game.getColourCount());
        registerListeners();
        initialisePaintbrushes();
        cellWidth = 800/ _game.getWidth();
        cellHeight = 800/ _game.getHeight();
        touchDetector = new GestureDetector(getContext(),new TouchListener());
        headerBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.flood_it_headermdpi);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean r = touchDetector.onTouchEvent(event);
        return super.onTouchEvent(event) || r;
    }
    //Adds the relevant game listeners
    private void registerListeners(){
        _game.addGamePlayListener(this);
        _game.addGameWinListener(this);
    }
    //Configures required paintbrushes
    private void initialisePaintbrushes(){
        borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(50);
        borderPaint.setAntiAlias(true);

        squarePaint = new Paint();
        squarePaint.setStyle(Paint.Style.FILL);
        squarePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(140);
        textPaint.setFakeBoldText(true);
    }
    //Prepare the outer rectangle to be drawn to the screen
    private RectF prepareRectangle() {
        float boxWidth = 800;
        float boxHeight = 800;
        float startX = ((getWidth() - boxWidth) / 2);
        float finishX = (startX + boxWidth);
        float startY =  ((getHeight() - boxHeight) / 2);
        float finishY = startY + boxHeight;

        return new RectF(startX, startY, finishX, finishY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
         grid=prepareRectangle();
        canvas.drawRect(grid, borderPaint);
        float currentX = grid.left ;
        float currentY = grid.top ;

        for (int row = 0; row< _game.getHeight(); row++){
            for (int col = 0; col< _game.getWidth(); col++){
                squarePaint.setColor(_game.getColor(col,row).getPaintValue());
                canvas.drawRect(currentX,currentY,currentX+cellWidth,currentY+cellHeight,squarePaint);
                currentX += cellWidth;
            }
            currentY+=cellHeight;
            currentX = grid.left;
        }
        headerX = (getWidth()-headerBitmap.getWidth())/2;
        headerY = (grid.top-headerBitmap.getHeight())/3;
        canvas.drawBitmap(headerBitmap,headerX,headerY,null);

        canvas.drawText(_game.getRound()+"/"+Integer.toString(_game.getRoundLimit()),(float)(getWidth()*0.5),(getBottom()+grid.bottom)/2,textPaint);
        super.onDraw(canvas);
    }
//Determines the colour of the pressed tile
    private SquareColour getPressedColour(MotionEvent event){

        float gridX = event.getX()-grid.left;
        float gridY = event.getY()-grid.top;
        int pressedCol = (int)(gridX / cellWidth);
        int pressedRow =(int) (gridY/cellHeight);

        return _game.getColor(pressedCol,pressedRow);
    }
//Handles when the user makes a successful move
    @Override
    public void onGameChanged(final AbstractGame game, int round) {
        moveSoundPlayer.start();
        invalidate();
        if (_game.isLost()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.game_over_dialog_title);
            builder.setMessage(R.string.game_over_dialog_message);
            builder.setPositiveButton(R.string.play_again_option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    _game = new Game(_game.getWidth(),_game.getHeight(),_game.getColourCount());
                    registerListeners();
                    dialog.dismiss();
                    invalidate();
                }
            });

            builder.setNegativeButton(R.string.return_to_main_menu_option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, MainMenuActivity.class);
                    mContext.startActivity(intent);
                }
            });
            builder.create().show();

        }
    }

    @Override
    //Called when the user wins. Determines whether a high score has been achieved.
    public void onWon(final AbstractGame game, final int rounds) {
        invalidate();
        if (leaderboardDataSource.isScoreHighEnough(leaderboard,rounds)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.game_win_dialog_title);
            builder.setMessage("You have successfully achieved a high score.Please enter your name to add it to the leaderboard");
           scoreInput = new EditText(mContext);
            builder.setView(scoreInput);
            builder.setPositiveButton("Submit Score", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   String name = scoreInput.getText().toString();
                    leaderboardDataSource.insertScore(new LeaderboardEntryModel(name,rounds,leaderboard));
                    leaderboardDataSource.close();
                    Intent intent = new Intent(mContext,LeaderboardDisplayActivity.class);
                    intent.putExtra("grid_size",game.getWidth());
                    intent.putExtra("colour_count",game.getColourCount());
                    mContext.startActivity(intent);


                }
            });

            builder.setNegativeButton(R.string.return_to_main_menu_option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, MainMenuActivity.class);
                    mContext.startActivity(intent);

                }
            });
            builder.create().show();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.game_win_dialog_title);
            builder.setMessage(R.string.game_with_no_high_score_message);
            builder.setPositiveButton(R.string.play_again_option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    _game = new Game(_game.getWidth(),_game.getHeight(),_game.getColourCount());
                    registerListeners();
                    dialog.dismiss();
                    invalidate();
                }
            });

            builder.setNegativeButton(R.string.return_to_main_menu_option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();

        }

    }
    private void selectLeaderboard(){

    }
    class TouchListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (e.getX()>=grid.left && e.getX()<=grid.right){
                if (e.getY()>=grid.top && e.getY()<=grid.bottom){
                 _game.playColour(getPressedColour(e));
                }
            }
            return super.onSingleTapUp(e);
        }
    }
}
