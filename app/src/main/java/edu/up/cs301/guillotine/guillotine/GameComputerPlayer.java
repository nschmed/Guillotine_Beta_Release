package edu.up.cs301.guillotine.guillotine;

import android.os.Handler;
import android.os.Looper;

/**
 * An abstract computerized game player player. This is an abstract class, that
 * should be sub-classed to implement different AIs. The subclass must implement
 * the method.
 *
 * @author Nathan Schmedake
 * @author Muhammed Acar
 * @author Melanie Martinell
 * @author Linnea Bair
 * @version December 2015
 */
public abstract class GameComputerPlayer implements GamePlayer{
	/**
	 * the current game state
	 */
	protected GuillotineState curState; // the gameState object
	private boolean gameOver = false; // whether the game is over

	/*
	 * Basic constructor for a computer player
	 */
	public GameComputerPlayer() {

	}

	/*
	 * Basic method to return the game state. This is just the framework for it.
	 */
	@Override
	public GuillotineState playCard(GuillotineState state) {
		return null;
	}
}// class GameComputerPlayer
