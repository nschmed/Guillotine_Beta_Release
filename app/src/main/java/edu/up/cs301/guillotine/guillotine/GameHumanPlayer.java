package edu.up.cs301.guillotine.guillotine;

import android.os.Handler;
import android.util.Log;


/**
 * class GameHumanPlayer
 * 
 * Is an abstract base class for a player that is controlled by a human. For any
 * particular game, a subclass should be created that can display the current
 * game state and responds to user commands. May be used, may not be depending on
 * implementation.
 *
 * @author Nathan Schmedake
 * @author Muhammed Acar
 * @author Melanie Martinell
 * @author Linnea Bair
 * @version November 2015
 * 
 */
public abstract class GameHumanPlayer implements GamePlayer{
	/**
	 * instance variables
	 */
	protected GuillotineState gameState; // the game
	protected int playerNum = 0; // my player ID
	protected String name; // my player's name

	/**
	 * constructor
	 * 
	 * @param name
	 */
	public GameHumanPlayer(String name) {
		// set the name via the argument
		this.name = name;

	}

	//This allows for a card to be played.
	public GuillotineState playCard(GuillotineState state){
		return null;
	}


}// class GameHumanPlayer

