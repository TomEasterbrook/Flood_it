package com.easterbrook.flood_it.logic;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for the Flood It game. You can either extend this class (and keep everything clean) or
 * adapt it to no longer be abstract. It provides a framework for your implementation of the
 * assignment. You are able to change this class as you see fit, but remember that it is intended to
 * just contain game logic, not any Android related code.
 */

public abstract class AbstractGame {
  /**
   * The default amount of columns in the game (horizontal "pixel" amount).
   */
  public static int DEFAULT_WIDTH = 14;

  /**
   * The default amount of rows in the game (vertical "pixel" amount)
   */
  public static int DEFAULT_HEIGHT = 14;

  /**
   * The default amount of colours in the game. More colours is more difficult.
   */
  public static int DEFAULT_COLOUR_COUNT = 6;

  /**
   * The actual amount of columns in the game. This means there are mWidth*mHeight cells.
   */
  private final int mWidth;

  /**
   * The actual amount of rows in the game. This means there are mWidth*mHeight cells.
   */
  private final int mHeight;

  /**
   * The actual amount of colours in a specific game.
   */
  private final int mColourCount;

  /**
   * A set of objects that have registered to be informed when the game state changes (after a play
   * has been made).
   */
  private Set<GamePlayListener> mGamePlayListeners = new HashSet<>();

  /**
   * A set of objects that have registered to be informed when the game has been won.
   */
  private Set<GameWinListener> mGameWinListeners = new HashSet<>();


  /**
   * Constructor for a new game. Note that this does not actually initialize any cells. You will
   * have to write your own cell state logic. There are some abstract functions you should implement
   * using that data.
   *
   * The subclass should implement an algorithm to actually generate a maze and do this in the
   * subclass constructor.
   *
   * @param width The horizontal size of the game.
   * @param height The vertical size of the game.
   * @param colourCount The amount of colours in the game.
   */
  public AbstractGame(final int width, final int height, final int colourCount) {
    mWidth = width;
    mHeight = height;
    mColourCount = colourCount;
  }

  /**
   * The amount of columns in the game.
   * @return the column count.
   */
  public int getWidth() {
    return mWidth;
  }

  /**
   * The amount of rows in the game.
   * @return The row count.
   */
  public int getHeight() {
    return mHeight;
  }

  /**
   * The amount of colours in the game.
   * @return The colour count.
   */
  public int getColourCount() {
    return mColourCount;
  }

  /**
   * Implement this function to return the current game round (starting with 1, every flood
   * operation updates the round.
   * @return The current round
   */
  public abstract int getRound();

  /**
   * Set the colour at position (x,y) to the colour identified by the colour parameter
   * @param x The column to change
   * @param y The row to change
   * @param colour The new colour.
   */
  protected abstract void setColor(int x, int y, SquareColour colour);

  /**
   * Get the colour at position (x,y)
   * @param x The column to change
   * @param y The row to change
   * @return The colour at the coordinates.
   */
  public abstract SquareColour getColor(int x, int y);

  /**
   * Get the set of objects that listen to game changes. You need this function to implement the
   * code that informs the listeners (loop...)
   * @return The set of listeners.
   */
  protected Set<GamePlayListener> getGamePlayListeners() {
    return mGamePlayListeners;
  }

  /**
   * Remove the given listener from the game play listener set.
   * @param listener Listener to remove
   */
  public void removeGamePlayListener(final GamePlayListener listener) {
    mGamePlayListeners.remove(listener);
  }

  /**
   * Add the given listener to the set of listeners that want to listen to game updates.
   * @param listener The listener to add (if it is not there yet).
   */
  public void addGamePlayListener(final GamePlayListener listener) {
    mGamePlayListeners.add(listener);
  }

  /**
   * Get the set of objects that listen to game wins. You need this function to implement the
   * code that informs the listeners (loop...). The listener you register can be where you trigger a
   * dialogue to inform the user.
   *
   * @return The set of listeners.
   */
  protected Set<GameWinListener> getGameWinListeners() {
    return mGameWinListeners;
  }

  /**
   * Remove the given listener from the game win listener set.
   * @param gameWinListener Listener to remove
   */
  public void removeGameWinListener(final GameWinListener gameWinListener) {
    mGameWinListeners.remove(gameWinListener);
  }

  /**
   * Add the given listener to the set of listeners that want to listen to game wins.
   * @param gameWinListener The listener to add (if it is not there yet).
   */
  public void addGameWinListener(final GameWinListener gameWinListener) {
    mGameWinListeners.add(gameWinListener);
  }

  /**
   * You should implement this function to call the gamePlayListeners with the given round.
   * @param round The round that the game is in.
   */
  abstract void notifyMove(final int round);

  /**
   * You should implement this function to call the gameWinListeners with the given round.
   * @param round The round that the game is in / the amount of rounds used.
   */
  abstract void notifyWin(final int round);

  /**
   * Function to use to actually implement a move (in the game the flow point is static (top left).
   * Implement this function to do a flood fill from the location with the given colour.
   * @param clr The colour to fill with.
   */
  abstract void playColour(final SquareColour clr);

  /**
   * Determine whether the game has been won.
   * @return <code>true</code> if won, <code>false</code> if the game has not yet been won.
   */
  public abstract boolean isWon();

  /**
   * Listener interface for games handling color changes
   */

  public static interface GamePlayListener {
    /**
     * SAM function invoked when the game state has changed.
     * @param game The game that changed.
     * @param round The round the game is in.
     */
    void onGameChanged(final AbstractGame game, int round);
  }

  /**
   * Listener interface for games handling win conditions
   */

  public static interface GameWinListener {
    /**
     * SAM function invoked when the game has been won.
     * @param game The game that changed.
     * @param rounds The rounds taken to win.
     */
    void onWon(final AbstractGame game, int rounds);
  }
}
